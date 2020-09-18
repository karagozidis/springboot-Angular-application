package crm.cloudApp.backend.services.data.modules.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.action.NotificationDTO;
import crm.cloudApp.backend.dto.data.modules.market.ProductDTO;
import crm.cloudApp.backend.dto.data.modules.market.RandomOrderGenerationSettingsDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.mappers.data.modules.market.SingleOrderMapper;
import crm.cloudApp.backend.mappers.data.modules.market.TradeMapper;
import crm.cloudApp.backend.models.data.modules.market.*;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.repositories.data.modules.market.BMEOrderBookEntryRepository;
import crm.cloudApp.backend.repositories.data.modules.market.MatchedOrderRepository;
import crm.cloudApp.backend.repositories.data.modules.market.ProductRepository;
import crm.cloudApp.backend.repositories.data.modules.market.SingleOrderRepository;
import crm.cloudApp.backend.repositories.users.UserRepository;
import crm.cloudApp.backend.services.action.NotificationService;
import crm.cloudApp.backend.services.users.UserService;
import crm.cloudApp.backend.utils.data.modules.market.MarketTimeIntervalsUtil;
import onCloudApp.crmcentral.backend.models.data.modules.market.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@Validated
@Transactional
public class OrderService {

    private final SingleOrderMapper singleOrderMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final SingleOrderRepository singleOrderRepository;
    private final ProductRepository productRepository;
    private final MatchedOrderRepository matchedOrderRepository;
    private final TradingProcessInteractionService marketProcessor;
    private final TradeMapper tradeMapper;
    private final MatchedOrderService matchedOrderService;
    private final BMEOrderBookEntryRepository bMEOrderBookEntryRepository;
    private final NotificationService notificationService;
    private final Random random = new Random();

    public OrderService(
            SingleOrderMapper singleOrderMapper,
            UserService userService,
            UserRepository userRepository,
            SingleOrderRepository singleOrderRepository,
            ProductRepository productRepository,
            MatchedOrderRepository matchedOrderRepository,
            TradingProcessInteractionService marketProcessor,
            TradeMapper tradeMapper,
            MatchedOrderService matchedOrderService,
            BMEOrderBookEntryRepository bMEOrderBookEntryRepository,
            NotificationService notificationService) {
        this.singleOrderMapper = singleOrderMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.singleOrderRepository = singleOrderRepository;
        this.productRepository = productRepository;
        this.matchedOrderRepository = matchedOrderRepository;
        this.marketProcessor = marketProcessor;
        this.tradeMapper = tradeMapper;
        this.matchedOrderService = matchedOrderService;
        this.bMEOrderBookEntryRepository = bMEOrderBookEntryRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public SingleOrderDTO create(SingleOrderDTO singleOrderDTO) throws IOException {

        SingleOrder singleOrder = singleOrderMapper.mapOrder(singleOrderDTO);

        //Save singleOrder to crm as Draft
        SingleOrder createdSingleOrder = this.saveDraftSingleOrderToCrm(singleOrder);

        //Send to BME market
        BMECommand command = createdSingleOrder.generateCommand();
        List<String> messages = marketProcessor.marketInteraction(Arrays.asList(command), false,
                createdSingleOrder.getProduct().getMarketCode());

        //Check for Error in BME responces
        this.checkForErrorInBMEMarketResponces(singleOrder, messages);

        //Check for updated quantities in BME responces and Save to Crm
        List<SingleOrder> updatedValuesOnOrders =
                this.updateQuantitiesAndPricesByBMEMarketResponces(messages);

        //Check for Matchings in BME responces
        List<MatchedOrder> matchedOrders = this
                .checkForMatchingsInBMEMarketResponces(messages, updatedValuesOnOrders);

        if (!matchedOrders.isEmpty()) {

            //Save matches
            matchedOrderService.addMatches(matchedOrders);

        }

        //After all actions are done, turn state of Order from Draft to Active or Expired if type in IoC,FoK,AoN

        if (singleOrder.getOrderType().equals(AppConstants.Types.SingleOrderType.FILL_OR_KILL) ||
                singleOrder.getOrderType().equals(AppConstants.Types.SingleOrderType.IMMEDIATE_OR_CANCEL)) {
            singleOrderRepository.expireOrdersByIds(Arrays.asList(singleOrder.getId()));
        } else {
            singleOrderRepository.activateDraftOrder(singleOrder.getId());
        }

        // Create notification
        //this.createOrderNotification(createdSingleOrder);

        return singleOrderMapper.map(createdSingleOrder);
    }


    private void createOrderNotification(SingleOrder singleOrder) {

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setStatus(AppConstants.Types.NotificationStatus.pending);
        notificationDTO.setDescription(
                "<h4><b>Congratulations, you just placed a new Order to the Market</b></h4> " +
                        " Product : " + singleOrder.getProduct().getName() +
                        " <br> Order id : " + singleOrder.getId().toString() +

                        " <br> Bid time : " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.of("UTC")).format(singleOrder.getBidTime()) + " UTC" +
                        " <br> Order direction : " + singleOrder.getOrderDirection().toString() +
                        " <br> Order type : " + singleOrder.getOrderType().toString() +
                        " <br> Order details : " + Optional.ofNullable(singleOrder.getMetadata()).orElse("") +
                        " <br> <b>Quantity : " + singleOrder.getQuantity() + "</b>" +
                        " <br> <b>Price : " + singleOrder.getPrice() + "</b>"
        );
        notificationDTO.setName(
                "New Order! " + singleOrder.getProduct().getName() +
                        "/" + singleOrder.getId().toString() +
                        " Qty: " + singleOrder.getQuantity() +
                        " Price: " + singleOrder.getPrice()
        );
        notificationDTO.setSenderId(singleOrder.getUser().getId());
        notificationDTO.setReceiverId(singleOrder.getUser().getId());
        notificationDTO.setTimeSent(Instant.now());
        notificationDTO.setType(AppConstants.Types.NotificationType.marketTradeCreation);
        this.notificationService.create(notificationDTO);
    }


    @Transactional
    public SingleOrder saveDraftSingleOrderToCrm(SingleOrder singleOrder) {

        // Check if product is valid
        Product product = productRepository.findProduct(singleOrder.getProduct().getId());
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Product not available!!");
        }
        if (product.getProductStatus() != AppConstants.Types.ProductStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Product must be active!!");
        }

        // Check if time interval is valid
        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();

        if (product.getDeliveryTimeStart().isBefore(baseTimeInterva) ||
                product.getDeliveryTimeStart().isAfter(upperTimeInterval)
        ) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Product is not active!!");
        }

        singleOrder.setProduct(product);

        // If user is null, then set loggedIn user
        // Else check if user is valid
        User user;
        if (singleOrder.getUser() == null) {
            singleOrder.setUser(userService.getLoggedInUser());
        } else {
            user = this.userRepository.findById(singleOrder.getUser().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "User does not exist!!"));
            singleOrder.setUser(user);
        }

        //If bidTime is null, insert now as bidTime
        if (singleOrder.getBidTime() == null) {
            singleOrder.setBidTime(Instant.now());
        }

        //Save order as draft
        singleOrder.setOrderStatus(AppConstants.Types.SingleOrderStatus.DRAFT);

        if (singleOrder.getId() == null) {
            singleOrder.setCreatedBy(userService.getLoggedInUserName());
            singleOrder.setCreatedOn(Instant.now());
        }
        singleOrder.setModifiedBy(userService.getLoggedInUserName());
        singleOrder.setModifiedOn(Instant.now());

        return singleOrderRepository.save(singleOrder);
    }

    private void checkForErrorInBMEMarketResponces(SingleOrder singleOrder, List<String> messages) {
        for (String message : messages) {
            if (message.contains("Error")) {
                singleOrderRepository.deleteById(singleOrder.getId());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
            }
        }
    }

    @Transactional
    public List<MatchedOrder> checkForMatchingsInBMEMarketResponces(List<String> messages,
                                                                    List<SingleOrder> updatedValuesOnOrders) {
        List<MatchedOrder> matchedOrders = new ArrayList<>();

        Boolean tradesStarted = false;
        for (String message : messages) {
            if (message.contains("UPDATED_ORDERS")) {
                tradesStarted = false;
            }

            if (tradesStarted) {
                String[] messageParts = message.split(";");
                Double quantity = Double.parseDouble(messageParts[2]);
                Double price = Double.parseDouble(messageParts[3]);
                Long id1 = Long.parseLong(messageParts[4]);
                Long id2 = Long.parseLong(messageParts[5]);

                Optional<SingleOrder> optionalSingleOrder1 = singleOrderRepository.findById(id1);
                Optional<SingleOrder> optionalSingleOrder2 = singleOrderRepository.findById(id2);

                if (!optionalSingleOrder1.isEmpty() && !optionalSingleOrder2.isEmpty()) {
                    MatchedOrder matchedOrder = new MatchedOrder();
                    matchedOrder.setQuantity(quantity);
                    matchedOrder.setPrice(price);

                    matchedOrder.setSingleOrder1(optionalSingleOrder1.get());
                    for (SingleOrder singleOrder : updatedValuesOnOrders) {
                        if (singleOrder.getId().equals(optionalSingleOrder1.get().getId())) {
                            matchedOrder.setOrder1_price(singleOrder.getPrice());
                            matchedOrder.setOrder1_quantity(singleOrder.getQuantity());
                        }
                    }

                    matchedOrder.setSingleOrder2(optionalSingleOrder2.get());
                    for (SingleOrder singleOrder : updatedValuesOnOrders) {
                        if (singleOrder.getId().equals(optionalSingleOrder2.get().getId())) {
                            matchedOrder.setOrder2_price(singleOrder.getPrice());
                            matchedOrder.setOrder2_quantity(singleOrder.getQuantity());
                        }
                    }

                    matchedOrder.setCreatedBy(userService.getLoggedInUserName());
                    matchedOrder.setCreatedOn(Instant.now());
                    matchedOrder.setModifiedBy(userService.getLoggedInUserName());
                    matchedOrder.setModifiedOn(Instant.now());
                    matchedOrder.setTimestamp(Instant.now());

                    matchedOrders.add(matchedOrder);
                }
            }

            if (message.contains("TRADES")) {
                tradesStarted = true;
            }
        }

        return matchedOrders;
    }


    public List<SingleOrderDTO> getOrders(String orderType,
                                          Instant bidTimeFrom,
                                          Instant bidTimeTo,
                                          String marketCode) {
        List<SingleOrder> singleOrders;
        if (orderType.contains("") || orderType.toLowerCase().equals("all")) {

            singleOrders = singleOrderRepository.findAllByOrderTypeAndBidTimeBetween(
                    AppConstants.Types.SingleOrderType.valueOf(orderType),
                    bidTimeFrom,
                    bidTimeTo,
                    marketCode);

        } else {
            singleOrders = singleOrderRepository.findAllByBidTimeBetween(
                    bidTimeFrom,
                    bidTimeTo,
                    marketCode);
        }

        return singleOrderMapper.map(singleOrders);
    }

    public List<SingleOrderDTO> getActiveOrders(String marketCode) {
        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();

        List<SingleOrder> singleOrders = singleOrderRepository
                .findAllTimeActiveOrders(baseTimeInterva, upperTimeInterval, marketCode);
        return singleOrderMapper.map(singleOrders);
    }

    public void activateOrdersOnBME(String marketCode) {
        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();
        singleOrderRepository
                .findAllTimeActiveOrders(baseTimeInterva, upperTimeInterval, marketCode);
    }


    @Transactional
    public List<SingleOrder> updateQuantitiesAndPricesByBMEMarketResponces(List<String> messages) {
        List<SingleOrder> updatedValuesOnOrders = new ArrayList<>();

        Boolean tradesStarted = false;
        for (String message : messages) {
            if (tradesStarted) {
                String[] messageParts = message.split(";");
                Double quantity = Double.parseDouble(messageParts[6]);
                Double price = Double.parseDouble(messageParts[7]);
                Long id = Long.parseLong(messageParts[10]);

                singleOrderRepository.setQuantityAndPrice(id, quantity, price);
                singleOrderRepository.updatePartlyMatched(id);
                singleOrderRepository.updateFullyMatched(id);

                SingleOrder singleOrder = new SingleOrder();
                singleOrder.setId(id);
                singleOrder.setQuantity(quantity);
                singleOrder.setPrice(price);
                updatedValuesOnOrders.add(singleOrder);

            }
            if (message.contains("UPDATED_ORDERS")) {
                tradesStarted = true;
            }
        }

        return updatedValuesOnOrders;

    }

    public List<SingleOrderDTO> getOrdersByCurrentUser(String orderType,
                                                       Instant bidTimeFrom,
                                                       Instant bidTimeTo,
                                                       String marketCode) {
        List<SingleOrder> singleOrders;

        if (orderType.equals("") || orderType.equals("all")) {
            singleOrders = singleOrderRepository.findAllByUserIdAndBidTimeBetween(
                    userService.getLoggedInUser().getId(),
                    bidTimeFrom,
                    bidTimeTo,
                    marketCode
            );
        } else {
            singleOrders = singleOrderRepository.findAllByUserIdAndOrderTypeAndBidTimeBetween(
                    userService.getLoggedInUser().getId(),
                    AppConstants.Types.SingleOrderType.valueOf(orderType),
                    bidTimeFrom,
                    bidTimeTo,
                    marketCode
            );
        }

        return singleOrderMapper.map(singleOrders);
    }

    public List<SingleOrderDTO> getOrdersByUser(Long userId,
                                                String orderType,
                                                Instant bidTimeFrom,
                                                Instant bidTimeTo,
                                                String marketCode) {
        List<SingleOrder> singleOrders;

        if (orderType.equals("") || orderType.equals("all")) {
            singleOrders = singleOrderRepository.findAllByUserIdAndBidTimeBetween(
                    userId,
                    bidTimeFrom,
                    bidTimeTo,
                    marketCode
            );
        } else {
            singleOrders = singleOrderRepository.findAllByUserIdAndOrderTypeAndBidTimeBetween(
                    userId,
                    AppConstants.Types.SingleOrderType.valueOf(orderType),
                    bidTimeFrom,
                    bidTimeTo,
                    marketCode
            );
        }

        return singleOrderMapper.map(singleOrders);
    }

    public List<SingleOrderDTO> getAllOrdersByProduct(Long productId) {
        List<SingleOrder> singleOrderBuys =
                singleOrderRepository.findNotMatchedBuysByProductId(productId);
        List<SingleOrder> singleOrderSells =
                singleOrderRepository.findNotMatchedSellsByProductId(productId);

        List<SingleOrder> singleOrders = new ArrayList<>();
        singleOrders.addAll(singleOrderBuys);
        singleOrders.addAll(singleOrderSells);
        return singleOrderMapper.map(singleOrders);
    }

    public List<SingleOrderDTO> getSellOrdersByProduct(Long productId) {
        List<SingleOrder> singleOrders = singleOrderRepository
                .findAllByProductIdAndOrderDirection(productId, AppConstants.Types.OrderDirection.SELL);
        return singleOrderMapper.map(singleOrders);
    }

    public List<SingleOrderDTO> getBuyOrdersByProduct(Long productId) {
        List<SingleOrder> singleOrders = singleOrderRepository
                .findAllByProductIdAndOrderDirection(productId, AppConstants.Types.OrderDirection.BUY);
        return singleOrderMapper.map(singleOrders);
    }

    public List<TradeDTO> getMatchedOrdersByProduct(Long productId) {
        List<MatchedOrder> matchedOrders = matchedOrderRepository.findByProduct(productId);

        Long loggedInUserId = 0L;
        if (userService.getLoggedInUser() != null) {
            loggedInUserId = userService.getLoggedInUser().getId();
        }

        return tradeMapper
                .mapForUserCloningOwnOrders(matchedOrders, loggedInUserId);
    }

    public List<TradeDTO> getMatchedOrdersByCurrentUser(String orderType,
                                                        Instant bidTimeFrom,
                                                        Instant bidTimeTo,
                                                        String marketCode) {

        List<MatchedOrder> matchedOrders;
        if (orderType.equals("") || orderType.equals("all")) {
            matchedOrders = matchedOrderRepository.findByUserAndBidDate(
                    userService.getLoggedInUser().getId(),
                    bidTimeFrom,
                    bidTimeTo,
                    marketCode);
        } else {
            matchedOrders = matchedOrderRepository.findByUserAndBidDateAndType(
                    userService.getLoggedInUser().getId(),
                    AppConstants.Types.SingleOrderType.valueOf(orderType),
                    bidTimeFrom,
                    bidTimeTo,
                    marketCode);
        }

        return tradeMapper
                .mapForUserCloningOwnOrders(matchedOrders, userService.getLoggedInUser().getId());
    }

    public List<BMEOrderBookEntry> getOrderBook(String marketCode) throws IOException {

        this.bMEOrderBookEntryRepository.deleteAll();

        BMECommand command = new BMECommand("get_order_book", AppConstants.Types.CommandType.OUTPUT);
        List<String> messages = marketProcessor
                .marketInteraction(Arrays.asList(command), false, marketCode);
        List<BMEOrderBookEntry> bMEOrderBookEntries = new ArrayList<>();

        int counter = 0;
        for (String message : messages) {
            if (counter > 1) {
                String[] messageParts = message.split(";");

                Double quantity = Double.parseDouble(messageParts[6]);
                Double price = Double.parseDouble(messageParts[7]);
                Long orderId = Long.parseLong(messageParts[10]);
                Long productId = Long.parseLong(messageParts[3]);

                BMEOrderBookEntry bmeOrderBookEntry = new BMEOrderBookEntry(orderId, productId, quantity,
                        price, "");
                bmeOrderBookEntry.setCreatedBy(userService.getLoggedInUserName());
                bmeOrderBookEntry.setCreatedOn(Instant.now());
                bmeOrderBookEntry.setModifiedBy(userService.getLoggedInUserName());
                bmeOrderBookEntry.setModifiedOn(Instant.now());
                bMEOrderBookEntries.add(bmeOrderBookEntry);

                this.bMEOrderBookEntryRepository.save(bmeOrderBookEntry);
            }
            counter++;
        }

        return bMEOrderBookEntries;
    }

    public SingleOrderDTO buildOrderDTO(Long id,
                                        Instant bidTime,
                                        double currentPrice,
                                        double currentQuantity,
                                        String metadata,
                                        String orderStatus,
                                        String orderDirection,
                                        String orderType
    ) {
        SingleOrderDTO order = new SingleOrderDTO();
        order.setId(id);
        order.setBidTime(bidTime);
        order.setPrice(currentPrice);
        order.setQuantity(currentQuantity);
        order.setMetadata(metadata);
        order.setOrderStatus(AppConstants.Types.SingleOrderStatus.valueOf(orderStatus));
        order.setOrderDirection(AppConstants.Types.OrderDirection.valueOf(orderDirection));
        order.setOrderType(AppConstants.Types.SingleOrderType.valueOf(orderType));

        return order;
    }


    public Boolean checkOrderIsActive(Long singleOrderId) {

        Optional<SingleOrder> opitonalSingleOrder = singleOrderRepository
                .findByIdWithProduct(singleOrderId);

        if (opitonalSingleOrder.isEmpty()) {
            return false;
        }

        SingleOrder singleOrder = opitonalSingleOrder.get();

        if (singleOrder.getOrderStatus()
                != AppConstants.Types.SingleOrderStatus.ACTIVE
                && singleOrder.getOrderStatus()
                != AppConstants.Types.SingleOrderStatus.PARTLY_MATCHED
        ) {
            return false;
        }

        Product product = singleOrder.getProduct();

        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();

        if (product.getDeliveryTimeStart().isBefore(baseTimeInterva) ||
                product.getDeliveryTimeStart().isAfter(upperTimeInterval)
        ) {
            return false;
        }

        return true;
    }

//    public String checkOrderIsActiveMessage(Long singleOrderId) {
//
//        Optional<SingleOrder> opitonalSingleOrder = singleOrderRepository
//                .findByIdWithProduct(singleOrderId);
//
//        if (opitonalSingleOrder.isEmpty()) {
//            return "The order does not exist!";
//        }
//
//        SingleOrder singleOrder = opitonalSingleOrder.get();
//
//        if (singleOrder.getOrderStatus()
//                != AppConstants.Types.SingleOrderStatus.ACTIVE
//                && singleOrder.getOrderStatus()
//                != SingleOrderStatus.PARTLY_MATCHED
//        ) {
//            return "The order is not active!";
//        }
//
//        Product product = singleOrder.getProduct();
//
//        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
//        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();
//
//        if (product.getDeliveryTimeStart().isBefore(baseTimeInterva) ||
//                product.getDeliveryTimeStart().isAfter(upperTimeInterval)
//        ) {
//            return "Product of Order is not active!";
//        }
//
//        return "Order is active!";
//    }


    @Transactional
    @Modifying
    public Boolean remove(Long singleOrderId) throws IOException {
        Optional<SingleOrder> opitonalSingleOrder = singleOrderRepository
                .findByIdWithProduct(singleOrderId);

        if (opitonalSingleOrder.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "The order does not exist!");
        }

        SingleOrder singleOrder = opitonalSingleOrder.get();

        if (singleOrder.getOrderStatus()
                != AppConstants.Types.SingleOrderStatus.ACTIVE
                && singleOrder.getOrderStatus()
                != AppConstants.Types.SingleOrderStatus.PARTLY_MATCHED
        ) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "the order is not active!");
        }

        Product product = singleOrder.getProduct();

        // Check if time interval is valid
        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();

        if (product.getDeliveryTimeStart().isBefore(baseTimeInterva) ||
                product.getDeliveryTimeStart().isAfter(upperTimeInterval)
        ) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Product of Order is not active!!");
        }

        //Send to BME market
        BMECommand command = singleOrder.generateDeactivationCommand();
        List<String> messages = marketProcessor
                .marketInteraction(Arrays.asList(command), false, singleOrder.getProduct().getMarketCode());

        //Check for Error in BME responces
        this.checkForErrorInBMEMarketResponces(singleOrder, messages);

        singleOrder.setOrderStatus(AppConstants.Types.SingleOrderStatus.REMOVED);
        singleOrder.setRemovedOn(Instant.now());
        singleOrderRepository.save(singleOrder);

        return true;
    }

    public List<SingleOrderDTO> generateRandomOrders(
            RandomOrderGenerationSettingsDTO randomOrderSettings)
            throws IOException {

        List<SingleOrderDTO> orders = new ArrayList<>();

        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();
        List<Product> products = this.productRepository
                .findAllTimeActiveProducts(baseTimeInterva, upperTimeInterval, randomOrderSettings.getMarketCode());
        int position;
        List<User> users =
                new ArrayList(this.userRepository.findAllByStatusIsNotLike(AppConstants.Types.UserStatus.deleted));

        for (int i = 0; i < randomOrderSettings.getTotalOrders(); i++) {

            SingleOrderDTO singleOrderDTO = new SingleOrderDTO();
            singleOrderDTO.setProduct(new ProductDTO());
            singleOrderDTO.setMetadata("");

            if (randomOrderSettings.getOrderTypes().isEmpty()) {
                singleOrderDTO.setOrderType(AppConstants.Types.SingleOrderType.LIMIT);
            } else {
                position = random.nextInt(randomOrderSettings.getOrderTypes().size());
                singleOrderDTO.setOrderType(randomOrderSettings.getOrderTypes().get(position));
            }

            if (randomOrderSettings.getOrderDirections().isEmpty()) {
                if (random.nextBoolean()) {
                    singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.BUY);
                } else {
                    singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.SELL);
                }
            } else {
                position = random.nextInt(randomOrderSettings.getOrderDirections().size());
                singleOrderDTO.setOrderDirection(randomOrderSettings.getOrderDirections().get(position));
            }

            Double quantity =
                    randomOrderSettings.getMinQuantity() + (
                            (randomOrderSettings.getMaxQuantity() - randomOrderSettings.getMinQuantity()) * random.nextDouble());
            Double price =
                    randomOrderSettings.getMinPrice() + ((randomOrderSettings.getMaxPrice() - randomOrderSettings.getMinPrice()) * random.nextDouble());
            singleOrderDTO.setQuantity(round(quantity, 1)); //Random Quantity with range 100 - 600
            singleOrderDTO.setPrice(round(price, 1));   //Random Quantity with range 100 - 200

            position = random.nextInt(products.size());
            singleOrderDTO.getProduct().setId(products.get(position).getId());

            position = random.nextInt(users.size());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(users.get(position).getId());
            singleOrderDTO.setUser(userDTO);

            if (singleOrderDTO.getOrderType().equals(AppConstants.Types.SingleOrderType.ICEBERG)) {

                Double icebergQuantity =
                        randomOrderSettings.getMinIcebergQuantity() + (
                                (randomOrderSettings.getMaxIcebergQuantity() - randomOrderSettings.getMinIcebergQuantity()) * random.nextDouble());
                Double icebergPrice =
                        randomOrderSettings.getMinIcebergPrice() + ((randomOrderSettings.getMaxIcebergPrice() - randomOrderSettings.getMinIcebergPrice()) * random.nextDouble());

                singleOrderDTO.setMetadata("{\"icebergQuantity\":\"" + icebergQuantity + "\",\"icebergPriceDelta\":\"" + icebergPrice + "\"}");
            }


            SingleOrderDTO createdSingleOrder = this.create(singleOrderDTO);
            orders.add(createdSingleOrder);
        }

        return orders;
    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void checkNumericValues(SingleOrderDTO singleOrderDTO) throws IOException {

        String[] splitter = Double.toString(singleOrderDTO.getQuantity()).split("\\.");
        if (splitter[1].length() > 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Order quantity must "
                    + "contain maximum 1 decimal value!!");
        }

        splitter = Double.toString(singleOrderDTO.getPrice()).split("\\.");
        if (splitter[1].length() > 2) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Order price must "
                    + "contain maximum 2 decimal values!!");
        }

        if (singleOrderDTO.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Order Quantity must "
                    + "be greater then 0!!");
        }

        if (singleOrderDTO.getPrice() <= 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Order price must "
                    + "be greater then 0!!");
        }

        if (singleOrderDTO.getOrderType() == AppConstants.Types.SingleOrderType.ICEBERG) {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> metadataMap = mapper.readValue(singleOrderDTO.getMetadata(), Map.class);

            String icebergQuantityString = metadataMap.get("icebergQuantity");
           // String icebergPriceDeltaString = metadataMap.get("icebergPriceDelta");

            double icebergQuantity = Double.parseDouble(icebergQuantityString);
           // double icebergPriceDelta = Double.parseDouble(icebergPriceDeltaString);

            if (singleOrderDTO.getQuantity() > icebergQuantity) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The Iceberg quantity must be greater than the quantity of the order !!");
            }

        }


    }

    public List<SingleOrderDTO> findByProductId(Long productId) {
        List<SingleOrder> singleOrders = this.singleOrderRepository.findOrdersByProductId(productId);
        return singleOrderMapper.map(singleOrders);
    }

    public List<SingleOrderDTO> findCurrentUsersByProductId(Long productId) {
        List<SingleOrder> singleOrders =
                this.singleOrderRepository.findOrdersByProductIdAndUser(productId,
                        userService.getLoggedInUser().getId());
        return singleOrderMapper.map(singleOrders);
    }

}
