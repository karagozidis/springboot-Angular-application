package crm.cloudApp.backend.services.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.market.*;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.mappers.data.modules.market.ProductMapper;
import crm.cloudApp.backend.models.data.modules.market.BMECommand;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.repositories.data.modules.market.ProductRepository;
import crm.cloudApp.backend.repositories.data.modules.market.SingleOrderRepository;
import crm.cloudApp.backend.services.users.UserService;
import crm.cloudApp.backend.utils.data.modules.market.MarketTimeIntervalsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class ProductService {

    private final ProductMapper productMapper;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final SingleOrderRepository singleOrderRepository;
    private final TradingProcessInteractionService marketProcessor;
    private final MarketScenarioService marketScenarioService;

    public ProductService(ProductMapper productMapper,
                          UserService userService,
                          ProductRepository productRepository,
                          SingleOrderRepository singleOrderRepository,
                          TradingProcessInteractionService marketProcessor, MarketScenarioService marketScenarioService) {
        this.productMapper = productMapper;
        this.userService = userService;
        this.productRepository = productRepository;
        this.singleOrderRepository = singleOrderRepository;
        this.marketProcessor = marketProcessor;
        this.marketScenarioService = marketScenarioService;
    }

    @Transactional
    public ProductDTO create(ProductDTO productDTO) throws IOException {

        Product product = productMapper.map(productDTO);
        product.setUser(userService.getLoggedInUser());
        product.setProductStatus(AppConstants.Types.ProductStatus.OPEN);

        product.setCreatedBy(userService.getLoggedInUserName());
        product.setCreatedOn(Instant.now());
        product.setModifiedBy(userService.getLoggedInUserName());
        product.setModifiedOn(Instant.now());

        if (product.getPeriod() == AppConstants.Types.ProductDeliveryPeriod.MINUTES15) {
            Instant delivaryTimeStart = product.getDeliveryTimeStart();
            Instant delivaryTimeEnd = delivaryTimeStart.plus(15, ChronoUnit.MINUTES);
            product.setDeliveryTimeEnd(delivaryTimeEnd);
        } else if (product.getPeriod() == AppConstants.Types.ProductDeliveryPeriod.MINUTES60) {
            Instant delivaryTimeStart = product.getDeliveryTimeStart();
            Instant delivaryTimeEnd = delivaryTimeStart.plus(60, ChronoUnit.MINUTES);
            product.setDeliveryTimeEnd(delivaryTimeEnd);
        }

        Product createdProduct = productRepository.save(product);

        BMECommand command = createdProduct.generateOpenProductCommand();
        List<String> messages = marketProcessor
                .marketInteraction(Arrays.asList(command), false, createdProduct.getMarketCode());

        this.checkForErrorInBMEMarketResponces(createdProduct, messages);

        return productMapper.map(createdProduct);
    }

    private void checkForErrorInBMEMarketResponces(Product product, List<String> messages) {
        for (String message : messages) {
            if (message.contains("Error")) {
                productRepository.deleteById(product.getId());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
            }
        }
    }

    public List<ProductDTO> getProductsByCurrentUser() {
        List<Product> products = productRepository
                .findAllByUserId(userService.getLoggedInUser().getId());
        return productMapper.map(products);
    }

    public List<ProductDTO> getProductsByUser(Long userId) {
        List<Product> products = productRepository.findAllByUserId(userId);
        return productMapper.map(products);
    }


    public List<ProductDTO> getActiveProducts(String marketCode) {
        MarketScenarioDTO marketScenarioDTO = marketScenarioService.get();
        String[] scenarios = marketScenarioDTO.getScenario().split(",");
        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();
        List<Product> products = new ArrayList<>();

        if (Arrays.asList(scenarios).contains("MINUTES60_PRODUCTS") && Arrays.asList(scenarios).contains("MINUTES15_PRODUCTS")) {
            products = productRepository
                    .findAllTimeActiveProducts(baseTimeInterva, upperTimeInterval, marketCode);
        } else if (Arrays.asList(scenarios).contains("MINUTES60_PRODUCTS")) {
            products = productRepository
                    .findAllTimeActiveProducts(baseTimeInterva, upperTimeInterval, marketCode, AppConstants.Types.ProductDeliveryPeriod.MINUTES60);
        } else if (Arrays.asList(scenarios).contains("MINUTES15_PRODUCTS")) {
            products = productRepository
                    .findAllTimeActiveProducts(baseTimeInterva, upperTimeInterval, marketCode, AppConstants.Types.ProductDeliveryPeriod.MINUTES15);
        }

        return this.productMapper.mapProductsForBook(products);
    }


    public List<ProductDTO> getProducts(Instant deliveryTimeStartFrom, Instant deliveryTimeStartTo,
                                        String marketCode) {
        List<Product> products = productRepository
                .findProducts(deliveryTimeStartFrom, deliveryTimeStartTo, marketCode);
        return this.productMapper.mapProductsForHistory(products);
    }

    public List<ProductDTO> getUserProducts(Instant deliveryTimeStartFrom, Instant deliveryTimeStartTo,
                                            String marketCode) {
        List<Product> products = productRepository
                .findUserProducts(deliveryTimeStartFrom, deliveryTimeStartTo, marketCode, this.userService.getLoggedInUser().getId());
        return this.productMapper.mapProductsForHistory(products);
    }

    public List<ProductMarketProjection> getActiveProductsNativeSql() {

        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();

        return productRepository
                .findAllTimeActiveProductsNativeSql(baseTimeInterva, upperTimeInterval);

    }

    @Transactional
    public void deactivateClosedOnBme(String marketCode) {

        Instant baseTimeInterva = MarketTimeIntervalsUtil.getBaseTimeInterval();
        List<Product> products = productRepository.findProductsThatMustBeClosed(baseTimeInterva, marketCode);

        List<Long> productsIds = products.stream()
                .map(Product::getId).collect(Collectors.toList());

        if (!productsIds.isEmpty()) {
            this.productRepository.setProductClosed(productsIds);
            this.singleOrderRepository.expireOrdersByProductIds(productsIds);
        }
    }

    public synchronized List<ProductDTO> generateHourlyProducts(Instant dateFrom,
                                                                Instant dateTo,
                                                                String marketCode) throws IOException {
        List<ProductDTO> productDTOs = new ArrayList<>();
        int athensTimeOffsetSeconds = ZoneId.of("Europe/Athens").getRules().getOffset(Instant.now()).getTotalSeconds();
        Instant deliveryTime = dateFrom.minus(athensTimeOffsetSeconds, ChronoUnit.SECONDS);

        while (dateTo.compareTo(deliveryTime) > 0) {

            String productName = DateTimeFormatter.ofPattern("1'H-'yyyyMMdd-HHmm").withZone(ZoneId.of("Europe/Athens")).format(deliveryTime);

            List<Product> existingProducts = this.productRepository.findByName(productName, marketCode);
            ProductDTO createdProduct;

            if (!existingProducts.isEmpty()) {
                BMECommand command = existingProducts.get(0).generateOpenProductCommand();
                marketProcessor.marketInteraction(Arrays.asList(command), false, marketCode);
                createdProduct = productMapper.map(existingProducts.get(0));
                productDTOs.add(createdProduct);
            } else {
                ProductDTO product = new ProductDTO();
                product.setName(productName);
                product.setDeliveryTimeStart(deliveryTime);
                product.setPeriod(AppConstants.Types.ProductDeliveryPeriod.MINUTES60);
                product.setMarketCode(marketCode);
                createdProduct = this.create(product);
                productDTOs.add(createdProduct);
            }

            deliveryTime = deliveryTime.plus(Duration.ofHours(1));
        }

        return productDTOs;
    }


    public synchronized List<ProductDTO> generateQuarterHourlyProducts(Instant dateFrom, Instant dateTo,
                                                                       String marketCode) throws IOException {

        List<ProductDTO> productDTOs = new ArrayList<>();
        int athensTimeOffsetSeconds = ZoneId.of("Europe/Athens").getRules().getOffset(Instant.now()).getTotalSeconds();
        Instant deliveryTime = dateFrom.minus(athensTimeOffsetSeconds, ChronoUnit.SECONDS);

        while (dateTo.compareTo(deliveryTime) > 0) {

            String productName = DateTimeFormatter.ofPattern("'QH-'yyyyMMdd-HHmm").withZone(ZoneId.of("Europe/Athens")).format(deliveryTime);

            List<Product> existingProducts = this.productRepository.findByName(productName, marketCode);
            ProductDTO createdProduct;

            if (!existingProducts.isEmpty()) {
                BMECommand command = existingProducts.get(0).generateOpenProductCommand();
                marketProcessor.marketInteraction(Arrays.asList(command), false, marketCode);
                createdProduct = productMapper.map(existingProducts.get(0));
                productDTOs.add(createdProduct);
            } else {
                ProductDTO product = new ProductDTO();
                product.setName(productName);
                product.setDeliveryTimeStart(deliveryTime);
                product.setPeriod(AppConstants.Types.ProductDeliveryPeriod.MINUTES15);
                product.setMarketCode(marketCode);
                createdProduct = this.create(product);
                productDTOs.add(createdProduct);
            }

            deliveryTime = deliveryTime.plus(Duration.ofMinutes(15));
        }

        return productDTOs;

    }

    public List<MarketObject> toTransactions(List<SingleOrderDTO> singleOrders,
                                             List<TradeDTO> matchedOrders) {
        List<MarketObject> marketObjects = new ArrayList<>();

        for (SingleOrderDTO singleOrder : singleOrders) {
            MarketObject marketObject = new MarketObject();
            marketObject.setType("ORDER");
            marketObject.setCreatedOn(singleOrder.getCreatedOn());
            marketObject.setSingleOrderDTO(singleOrder);
            marketObjects.add(marketObject);
        }

        for (SingleOrderDTO singleOrder : singleOrders) {
            if (!singleOrder.getOrderStatus().equals(AppConstants.Types.SingleOrderStatus.REMOVED)) continue;
            MarketObject marketObject = new MarketObject();
            marketObject.setType("ORDER_REMOVED");
            marketObject.setCreatedOn(singleOrder.getRemovedOn());
            marketObject.setSingleOrderDTO(singleOrder);
            marketObjects.add(marketObject);
        }

        for (TradeDTO matchedOrder : matchedOrders) {
            MarketObject marketObject = new MarketObject();
            marketObject.setType("TRADE");
            marketObject.setCreatedOn(matchedOrder.getCreatedOn());
            marketObject.setTradeDTO(matchedOrder);
            marketObjects.add(marketObject);
        }

        // Sort all marketObjects by creationDate
        return marketObjects.stream().sorted(Comparator.comparing(MarketObject::getCreatedOn)).collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return this.productMapper.map(product.get());
        } else {
            return null;
        }
    }

    public List<GraphDTO> toGraphData(List<SingleOrderDTO> singleOrders, List<TradeDTO> matchedOrders) {

        Double min = 0D;
        Double max = 0D;
        Double min2 = 0D;
        Double max2 = 0D;

        SingleOrderDTO singleOrderMin = singleOrders.stream().min(Comparator.comparing(SingleOrderDTO::getQuantity)).orElse(null);
        if (singleOrderMin != null) min = singleOrderMin.getQuantity();
        SingleOrderDTO singleOrderMax = singleOrders.stream().max(Comparator.comparing(SingleOrderDTO::getQuantity)).orElse(null);
        if (singleOrderMax != null) max = singleOrderMax.getQuantity();
        TradeDTO tradeDTOMin2 = matchedOrders.stream().min(Comparator.comparing(TradeDTO::getQuantity)).orElse(null);
        if (tradeDTOMin2 != null) {
            min2 = tradeDTOMin2.getQuantity();
            if (min > min2 || min.equals(0D)) min = min2;
        }
        TradeDTO tradeDTOMax2 = matchedOrders.stream().max(Comparator.comparing(TradeDTO::getQuantity)).orElse(null);
        if (tradeDTOMax2 != null) {
            max2 = tradeDTOMax2.getQuantity();
            if (max < max2) max = max2;
        }

        Double diff = max - min;

        List<GraphDTO> graphDTOs = new ArrayList<>();
        List<MarketObject> marketObjects = new ArrayList<>();

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setMode("lines+markers");
        graphDTO.setName("BUY");
        graphDTO.setType("scattergl");
        graphDTO.setUid(UUID.randomUUID().toString());
        graphDTO.setYaxis("y");
        graphDTO.setCsvIndex(0);
        graphDTO.getMarker().put("color", "red");
        List<Double> sizes = new ArrayList<>();
        for (SingleOrderDTO singleOrder : singleOrders) {
            if (!singleOrder.getOrderDirection().equals(AppConstants.Types.OrderDirection.BUY)) continue;
            graphDTO.getX().add(singleOrder.getBidTime().toString());
            graphDTO.getY().add(String.valueOf(singleOrder.getPrice()));
            graphDTO.getHovertext().add(
                    "<b>" + String.valueOf(singleOrder.getId()) + "</b>" +
                            "<br>Bid time: " + String.valueOf(singleOrder.getBidTime()) +
                            "<br>Qty: " + String.valueOf(singleOrder.getQuantity()) + " (now:" + String.valueOf(singleOrder.getCurrent_quantity()) + ")" +
                            "<br>Price: " + String.valueOf(singleOrder.getPrice()) + "(now: " + String.valueOf(singleOrder.getCurrent_price()) + ")" +
                            "<br>Type: " + String.valueOf(singleOrder.getOrderType().toString()) +
                            "<br>Options: " + String.valueOf(singleOrder.getMetadata()));

            Double qtyUnit = singleOrder.getQuantity() / diff;
            sizes.add(10 + (qtyUnit * 20));
        }
        graphDTO.getMarker().put("size", sizes);
        graphDTOs.add(graphDTO);

        graphDTO = new GraphDTO();
        graphDTO.setMode("lines+markers");
        graphDTO.setName("SELL");
        graphDTO.setType("scattergl");
        graphDTO.setUid(UUID.randomUUID().toString());
        graphDTO.setYaxis("y");
        graphDTO.setCsvIndex(0);
        graphDTO.getMarker().put("color", "green");
        sizes = new ArrayList<>();
        for (SingleOrderDTO singleOrder : singleOrders) {
            if (!singleOrder.getOrderDirection().equals(AppConstants.Types.OrderDirection.SELL)) continue;
            graphDTO.getX().add(singleOrder.getBidTime().toString());
            graphDTO.getY().add(String.valueOf(singleOrder.getPrice()));

            graphDTO.getHovertext().add(
                    "<b>" + String.valueOf(singleOrder.getId()) + "</b>" +
                            "<br>Bid time: " + String.valueOf(singleOrder.getBidTime()) +
                            "<br>Qty: " + String.valueOf(singleOrder.getQuantity()) + " (now:" + String.valueOf(singleOrder.getCurrent_quantity()) + ")" +
                            "<br>Price: " + String.valueOf(singleOrder.getPrice()) + " (now: " + String.valueOf(singleOrder.getCurrent_price()) + ")" +
                            "<br>Type: " + String.valueOf(singleOrder.getOrderType().toString()) +
                            "<br>Options: " + String.valueOf(singleOrder.getMetadata()));
            Double qtyUnit = singleOrder.getQuantity() / diff;
            sizes.add(10 + (qtyUnit * 20));
        }
        graphDTO.getMarker().put("size", sizes);
        graphDTOs.add(graphDTO);
        graphDTO = new GraphDTO();
        graphDTO.setMode("markers");
        graphDTO.setName("TRADE");
        graphDTO.setType("scattergl");
        graphDTO.setUid(UUID.randomUUID().toString());
        graphDTO.setYaxis("y");
        graphDTO.setCsvIndex(0);
        graphDTO.getMarker().put("color", "blue");
        sizes = new ArrayList<>();
        for (TradeDTO tradeDTO : matchedOrders) {
            graphDTO.getX().add(tradeDTO.getTimestamp().toString());
            graphDTO.getY().add(String.valueOf(tradeDTO.getPrice()));
            graphDTO.getHovertext().add(
                    "<b>" + String.valueOf(tradeDTO.getId()) + "</b>" +
                            "<br>Trade time: " + String.valueOf(tradeDTO.getTimestamp()) +
                            "<br>Qty: " + String.valueOf(tradeDTO.getQuantity()) +
                            "<br>Price: " + String.valueOf(tradeDTO.getPrice()) +
                            "<br><b> Order 1: " + String.valueOf(tradeDTO.getSingleOrder1().getId()) + "</b>" +
                            "<br>New Qty: " + String.valueOf(tradeDTO.getOrder1_quantity()) +
                            "<br>New Price: " + String.valueOf(tradeDTO.getOrder1_price()) +
                            "<br><b> Order 2: " + String.valueOf(tradeDTO.getSingleOrder2().getId()) + "</b>" +
                            "<br>New Qty: " + String.valueOf(tradeDTO.getOrder2_quantity()) +
                            "<br>New Price: " + String.valueOf(tradeDTO.getOrder2_price())
            );

            Double qtyUnit = tradeDTO.getQuantity() / diff;
            sizes.add(10 + (qtyUnit * 20));
            // sizes.add(tradeDTO.getQuantity());
        }
        graphDTO.getMarker().put("size", sizes);
        graphDTOs.add(graphDTO);

        return graphDTOs;
    }


}
