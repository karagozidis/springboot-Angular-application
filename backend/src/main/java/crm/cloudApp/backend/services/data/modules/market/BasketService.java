package crm.cloudApp.backend.services.data.modules.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.market.BasketDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.mappers.data.modules.market.BasketMapper;
import crm.cloudApp.backend.models.data.modules.market.BMECommand;
import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import crm.cloudApp.backend.repositories.data.modules.market.BasketRepository;
import crm.cloudApp.backend.repositories.data.modules.market.SingleOrderRepository;
import crm.cloudApp.backend.services.users.UserService;
import crm.cloudApp.backend.utils.data.modules.market.MarketTimeIntervalsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Validated
@Transactional
public class BasketService {

    private final BasketMapper basketMapper;
    private final TradingProcessInteractionService marketProcessor;
    private final SingleOrderRepository singleOrderRepository;
    private final UserService userService;
    private final MatchedOrderService matchedOrderService;
    private final OrderService orderServiceBME;
    private final BasketRepository basketRepository;

    public BasketService(BasketMapper basketMapper,
                         TradingProcessInteractionService marketProcessor,
                         SingleOrderRepository singleOrderRepository,
                         UserService userService,
                         MatchedOrderService matchedOrderService,
                         OrderService orderServiceBME,
                         BasketRepository basketRepository
    ) {
        this.basketMapper = basketMapper;
        this.marketProcessor = marketProcessor;
        this.singleOrderRepository = singleOrderRepository;
        this.userService = userService;
        this.matchedOrderService = matchedOrderService;
        this.orderServiceBME = orderServiceBME;
        this.basketRepository = basketRepository;
    }

    @Transactional
    public BasketDTO create(BasketDTO basketDTO) throws IOException {
        List<BMECommand> commands = new ArrayList<>();

        Basket basket = basketMapper.mapBasket(basketDTO);

        //Save Basket to the Database
        basket.setCreatedBy(userService.getLoggedInUserName());
        basket.setCreatedOn(Instant.now());
        basket.setModifiedBy(userService.getLoggedInUserName());
        basket.setModifiedOn(Instant.now());
        basket.setStatus(AppConstants.Types.BasketStatus.ACTIVE);
        basketRepository.save(basket);

        // Create Basket creation command for BME
        commands.add(basket.generateBasketCreationCommand());

        // Create Basket selection command for BME
        commands.add(basket.generateBasketSelectionCommand());

        // Save Basket Orders as Drafts
        // Create Basket Order commands for BME
        String marketCode = "";
        List<SingleOrder> createdSingleOrders = new ArrayList<>();
        List<Long> createdSingleOrderIds = new ArrayList<>();
        for (SingleOrder singleOrder : basket.getOrders()) {
            singleOrder.setBasket(basket);
            SingleOrder createdSingleOrder = this.orderServiceBME.saveDraftSingleOrderToCrm(singleOrder);
            marketCode = createdSingleOrder.getProduct().getMarketCode();
            createdSingleOrders.add(createdSingleOrder);
            createdSingleOrderIds.add(createdSingleOrder.getId());
            commands.add(createdSingleOrder.generateCommand());
        }

        // Create Basket Commit command for BME
        commands.add(basket.generateBasketCommitCommand());

        // Send commands to BME
        List<String> messages = marketProcessor.marketInteraction(commands, true, marketCode);

        //7. Check for updated quantities in BME responces and save to Crm
        List<SingleOrder> udatedOrders =
                this.orderServiceBME.updateQuantitiesAndPricesByBMEMarketResponces(messages);

        // Check for matchings from BME
        List<MatchedOrder> matchedOrders = this.orderServiceBME
                .checkForMatchingsInBMEMarketResponces(messages, udatedOrders);

        //6. Save matchings to the Database
        if (matchedOrders.size() > 0) {

            //7. Check for updated quantities in BME responces and save to Crm
            this.orderServiceBME.updateQuantitiesAndPricesByBMEMarketResponces(messages);

            //8. Save matches
            matchedOrderService.addMatches(matchedOrders);
        }

        //10. Set DRAFT orders to ACTIVE
        for (SingleOrder createdSingleOrder : createdSingleOrders) {
            createdSingleOrder.setOrderStatus(AppConstants.Types.SingleOrderStatus.ACTIVE);
        }
        basket.setOrders(createdSingleOrders);
        singleOrderRepository.activateDraftOrders(createdSingleOrderIds);

        return basketMapper.map(basket);
    }

    //  private void checkForErrorInBMEMarketResponces(List<String> messages) {
    //    for (String message : messages) {
    //      if (message.contains("Error")) {
    //        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    //      }
    //    }
    //  }

    public BasketDTO update(BasketDTO basketDTO) throws IOException {
        basketDTO.setId(null);
        for (SingleOrderDTO singleOrder : basketDTO.getOrders()) {
            singleOrder.setId(null);
        }
        return this.create(basketDTO);
    }

    @Transactional
    @Modifying
    public Boolean remove(Long basketId) throws IOException {

        Optional<Basket> optionalBasket = this.basketRepository.findById(basketId);

        if (optionalBasket.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "This Basket does not exist!");
        }

        Basket basket = optionalBasket.get();

        for (SingleOrder order : basket.getOrders()) {
            Boolean isOrderActive = this.orderServiceBME.checkOrderIsActive(order.getId());
            if (isOrderActive) {
                this.orderServiceBME.remove(order.getId());
            }
        }

        basket.setStatus(AppConstants.Types.BasketStatus.DEACTIVATED);
        this.basketRepository.save(basket);

        return true;
    }

    public void checkNumericValues(BasketDTO basketDTO) throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> metadataMap;
        if (basketDTO.getBasketType() == AppConstants.Types.BasketType.EXCLUSIVE) {
            metadataMap = mapper.readValue(basketDTO.getMetadata(), Map.class);
            String maxMatchedOrdersString = metadataMap.get("maxMatchedOrders");
            double maxMatchedOrders = Double.parseDouble(maxMatchedOrdersString);
            if (maxMatchedOrders <= 0) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Μax matched orders must "
                        + "be greater then 0!!");
            }

        } else if (basketDTO.getBasketType() == AppConstants.Types.BasketType.VOLUME_CONSTRAINED) {
            metadataMap = mapper.readValue(basketDTO.getMetadata(), Map.class);
            String totalQuantityString = metadataMap.get("totalQuantity");
            double totalQuantity = Double.parseDouble(totalQuantityString);
            if (totalQuantity <= 0) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Total quantity orders must "
                        + "be greater then 0!!");
            }
        } else if (basketDTO.getBasketType() == AppConstants.Types.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
            metadataMap = mapper.readValue(basketDTO.getMetadata(), Map.class);
            String maxCapacityString = metadataMap.get("maxCapacity");
            String initialChargeString = metadataMap.get("initialCharge");
            String ratedPowerString = metadataMap.get("ratedPower");
            double maxCapacity = Double.parseDouble(maxCapacityString);
            double initialCharge = Double.parseDouble(initialChargeString);
            double ratedPower = Double.parseDouble(ratedPowerString);

            if (maxCapacity <= 0 || initialCharge < 0 || ratedPower <= 0) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Max Capacity, Initial Charge & Rated Power parameters must "
                        + "be greater then 0!!");
            }

        }


        for (SingleOrderDTO singleOrderDTO : basketDTO.getOrders()) {
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


                metadataMap = mapper.readValue(singleOrderDTO.getMetadata(), Map.class);

                String icebergQuantityString = metadataMap.get("icebergQuantity");
                // String icebergPriceDeltaString = metadataMap.get("icebergPriceDelta");

                double icebergQuantity = Double.parseDouble(icebergQuantityString);
                // double icebergPriceDelta = Double.parseDouble(icebergPriceDeltaString);

                if (singleOrderDTO.getQuantity() > icebergQuantity) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The Iceberg quantity must be greater than the quantity of the order !!");
                }

            }

        }
    }


    public SingleOrderDTO updateOrder(SingleOrderDTO singleOrderDTO, Long orderId)
            throws IOException {
        Basket basket = this.basketRepository.findByOrderId(singleOrderDTO.getId());
        BasketDTO basketDTO = this.basketMapper.map(basket);

        this.remove(basketDTO.getId());

        List<SingleOrderDTO> newOrders = new ArrayList<>();
        for (SingleOrderDTO currentOrderDTO : basketDTO.getOrders()) {
            if (currentOrderDTO.getId().equals(orderId)) {
                currentOrderDTO = singleOrderDTO;
                currentOrderDTO.setMetadata(orderId.toString());
            }
            newOrders.add(currentOrderDTO);
        }
        basketDTO.setOrders(newOrders);

        basketDTO = this.update(basketDTO);
        return basketDTO.getOrders().stream().filter(x -> x.getMetadata().equals(orderId.toString()))
                .findFirst().orElse(null);
    }

    public BasketDTO getByBasketId(Long basketId) {

        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();

        Basket basket = this.basketRepository.findBybasketId(basketId, baseTimeInterva,
                upperTimeInterval);
        return basketMapper.map(basket);
    }

    public BasketDTO getByOrderId(Long orderId) {

        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();

        Basket basket = this.basketRepository.findByOrderId(orderId, baseTimeInterva,
                upperTimeInterval);
        return basketMapper.map(basket);
    }
}
