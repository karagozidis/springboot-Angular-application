package crm.cloudApp.backend.controllers.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.config.data.modules.market.MarketInitialization;
import crm.cloudApp.backend.dto.data.modules.market.*;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.services.data.modules.market.MatchedOrderService;
import crm.cloudApp.backend.services.data.modules.market.OrderService;
import crm.cloudApp.backend.services.data.modules.market.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/market/products")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final MatchedOrderService matcherOrderService;
    private final MarketInitialization marketInitialization;

    public ProductController(ProductService productService, OrderService orderService,
                             MatchedOrderService matcherOrderService,
                             MarketInitialization marketInitialization) {
        this.productService = productService;
        this.orderService = orderService;
        this.matcherOrderService = matcherOrderService;
        this.marketInitialization = marketInitialization;
    }

    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO productDTO) throws Exception {
        marketInitialization.verifyAndRestoreTradingProcesses();
        return productService.create(productDTO);
    }

    @PostMapping(path = "/generate-day")
    public List<ProductDTO> generateProductsAtDay(
            @RequestParam("date") Instant atDate,
            @RequestParam("delivery-period") AppConstants.Types.ProductDeliveryPeriod deliveryPeriod,
            @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
    ) throws Exception {

        if (!MarketInitialization.initialized) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Market is on Initialization. This endpoint will be available soon");
        }

        marketInitialization.verifyAndRestoreTradingProcesses();


        if (deliveryPeriod.equals(AppConstants.Types.ProductDeliveryPeriod.MINUTES60)) {
            return productService
                    .generateHourlyProducts(
                            atDate,
                            atDate.plus(Period.ofDays(1)),
                            marketCode);
        } else {
            return productService
                    .generateQuarterHourlyProducts(atDate, atDate.plus(Period.ofDays(1)), marketCode);
        }

    }

    @PostMapping(path = "/generate-period")
    public List<ProductDTO> generateProductsOnPeriod(
            @RequestParam("date-from") Instant dateFrom,
            @RequestParam("date-to") Instant dateTo,
            @RequestParam("delivery-period") AppConstants.Types.ProductDeliveryPeriod deliveryPeriod,
            @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
    ) throws Exception {

        marketInitialization.verifyAndRestoreTradingProcesses();

        if (deliveryPeriod.equals(AppConstants.Types.ProductDeliveryPeriod.MINUTES60)) {
            return productService.generateHourlyProducts(dateFrom, dateTo, marketCode);
        } else {
            return productService.generateQuarterHourlyProducts(dateFrom, dateTo, marketCode);
        }
    }

    @GetMapping(path = "/active/")
    public List<ProductDTO> getActiveProducts(
            @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
    ) {
        return productService.getActiveProducts(marketCode);
    }

    @GetMapping(path = "/active/fast")
    public List<ProductMarketProjection> getActiveProductsNativeSql(
            @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {

        //Start counting time
        Instant start = Instant.now();

        List<ProductMarketProjection> products = productService.getActiveProductsNativeSql();

        //End counting time
        Instant finish = Instant.now();

        //Print time
        long timeElapsed = Duration.between(start, finish).toMillis();

        log.info("******* Execution time: " + timeElapsed);
        return products;
    }


    @GetMapping
    public List<ProductDTO> getProducts(
            @RequestParam("delivery-time-start-from") Instant deliveryTimeStartFrom,
            @RequestParam("delivery-time-start-to") Instant deliveryTimeStartTo,
            @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
        return productService.getProducts(deliveryTimeStartFrom, deliveryTimeStartTo, marketCode);
    }

    @GetMapping(path = "/by-id")
    public ProductDTO getProductById(@RequestParam("id") Long id) {
        return productService.getProductById(id);
    }


    @GetMapping(path = "/user-relevant")
    public List<ProductDTO> getUserProducts(
            @RequestParam("delivery-time-start-from") Instant deliveryTimeStartFrom,
            @RequestParam("delivery-time-start-to") Instant deliveryTimeStartTo,
            @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
        return productService.getUserProducts(deliveryTimeStartFrom, deliveryTimeStartTo, marketCode);
    }

    @GetMapping(path = "/market-transactions")
    public List<MarketObject> getMarketTransactions(@RequestParam("product-id") Long productId) {

        List<SingleOrderDTO> singleOrdersDTO = orderService.findByProductId(productId);
        List<TradeDTO> matchedOrders = matcherOrderService.findByProductId(productId);

        return this.productService.toTransactions(singleOrdersDTO, matchedOrders);
    }

    @GetMapping(path = "/market-transactions/user-relevant")
    public List<MarketObject> getUserMarketTransactions(@RequestParam("product-id") Long productId) {

        List<SingleOrderDTO> singleOrdersDTO = orderService.findCurrentUsersByProductId(productId);
        List<TradeDTO> matchedOrders = matcherOrderService.findCurrentUsersByProductId(productId);

        return this.productService.toTransactions(singleOrdersDTO, matchedOrders);
    }

    @GetMapping(path = "/graph")
    public List<GraphDTO> getGraphData(@RequestParam("product-id") Long productId) {

        List<SingleOrderDTO> singleOrdersDTO = orderService.findByProductId(productId);
        List<TradeDTO> matchedOrders = matcherOrderService.findByProductId(productId);

        return this.productService.toGraphData(singleOrdersDTO, matchedOrders);

    }


    @GetMapping(path = "/orders")
    public @ResponseBody
    List<SingleOrderDTO> getAllOrdersByProduct(@RequestParam("product-id") Long productId) {
        return this.orderService.getAllOrdersByProduct(productId);
    }

    @GetMapping(path = "/orders/sell")
    public @ResponseBody
    List<SingleOrderDTO> getSellOrdersByProduct(@RequestParam("product-id") Long productId) {
        return this.orderService.getSellOrdersByProduct(productId);
    }

    @GetMapping(path = "/orders/buy")
    public @ResponseBody
    List<SingleOrderDTO> getBuyOrdersByProduct(@RequestParam("product-id") Long productId) {
        return this.orderService.getBuyOrdersByProduct(productId);
    }

    @GetMapping(path = "/matched-orders")
    public @ResponseBody
    List<TradeDTO> getMatchedOrders(@RequestParam("product-id") Long productId) {
        return this.orderService.getMatchedOrdersByProduct(productId);
    }

}
