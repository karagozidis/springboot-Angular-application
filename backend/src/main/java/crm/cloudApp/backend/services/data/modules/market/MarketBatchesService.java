package crm.cloudApp.backend.services.data.modules.market;

import crm.cloudApp.backend.models.data.modules.market.BMECommand;
import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import crm.cloudApp.backend.repositories.data.modules.market.BasketRepository;
import crm.cloudApp.backend.repositories.data.modules.market.ProductRepository;
import crm.cloudApp.backend.repositories.data.modules.market.SingleOrderRepository;
import crm.cloudApp.backend.utils.data.modules.market.MarketTimeIntervalsUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MarketBatchesService {

  private final ProductRepository productRepository;
  private final SingleOrderRepository singleOrderRepository;
  private final BasketRepository basketRepository;
  private final TradingProcessInteractionService marketProcessor;

  public MarketBatchesService(
      ProductRepository productRepository,
      SingleOrderRepository singleOrderRepository,
      BasketRepository basketRepository, TradingProcessInteractionService marketProcessor) {
    this.productRepository = productRepository;
    this.singleOrderRepository = singleOrderRepository;
    this.basketRepository = basketRepository;
    this.marketProcessor = marketProcessor;
  }


  public void restoreTimeActiveEntitesToTradingExe(String marketCode) throws IOException {
    marketProcessor.resetMarketProcess(marketCode);
    this.initializeTimeActiveEntitesToTradingExe(marketCode);
  }

  public void initializeTimeActiveEntitesToTradingExe() throws IOException {
    this.initializeTimeActiveEntitesToTradingExe("CY");
    this.initializeTimeActiveEntitesToTradingExe("BG");
  }

  public void initializeTimeActiveEntitesToTradingExe(String marketCode) throws IOException {
    Instant baseTimeInterval = MarketTimeIntervalsUtil.getBaseTimeInterval();
    Instant upperTimeInterval = MarketTimeIntervalsUtil.getUpperTimeInterval();
    this.marketEntitiesToTradingExeProcess(baseTimeInterval, upperTimeInterval, marketCode);
  }

  public void initializeFutureProductsToTradingExe() throws IOException {
    Instant baseTimeInterval = ZonedDateTime.now().plusDays(1).toLocalDate().atStartOfDay()
        .toInstant(ZoneOffset.UTC);
    Instant upperTimeInterval = ZonedDateTime.now().plusDays(2).toLocalDate().atStartOfDay()
        .toInstant(ZoneOffset.UTC);
    this.marketEntitiesToTradingExeProcess(baseTimeInterval, upperTimeInterval, "CY");
    this.marketEntitiesToTradingExeProcess(baseTimeInterval, upperTimeInterval, "BG");
  }


  public void marketEntitiesToTradingExeProcess(Instant baseTimeInterval, Instant upperTimeInterval,
      String marketCode) throws IOException {

    List<MarketBatchesService.MarketEntityContainer> marketEntityContainers = new ArrayList<>();

    //2. Get currently active products by date intervals
    List<Product> activeProducts = productRepository
        .findAllTimeActiveProducts(baseTimeInterval, upperTimeInterval, marketCode);

    for (Product product : activeProducts) {
      marketEntityContainers.add(
          new MarketBatchesService.MarketEntityContainer(product.getCreatedOn(), "product",
              product));
    }
    List<Long> productIds = activeProducts.stream()
        .map(Product::getId).collect(Collectors.toList());

    //3. Get single orders of selected products
    if (!productIds.isEmpty()) {
      List<SingleOrder> singleOrders = singleOrderRepository.findOrdersByProductIds(productIds);
      for (SingleOrder singleOrder : singleOrders) {
        marketEntityContainers.add(
            new MarketBatchesService.MarketEntityContainer(singleOrder.getCreatedOn(),
                "singleOrder", singleOrder));
      }
    }

    //4. Get baskets of selected products
    if (!productIds.isEmpty()) {
      List<Basket> baskets = basketRepository.findBasketsByProductIds(productIds);
      for (Basket basket : baskets) {
        marketEntityContainers.add(
            new MarketBatchesService.MarketEntityContainer(basket.getCreatedOn(), "basket",
                basket));
      }
    }

    //5. Sort all of those market entities (products, orders, baskets) by creationDate
    marketEntityContainers = marketEntityContainers.stream()
        .sorted(Comparator.comparing(MarketBatchesService.MarketEntityContainer::getCreatedOn))
        .collect(
            Collectors.toList());

    //6. Iterate all the market entities and send them to the BME trading.exe
    for (MarketBatchesService.MarketEntityContainer marketEntityContainer : marketEntityContainers) {
      this.addressMarketEntityContainer(marketEntityContainer);
    }

  }

  private void addressMarketEntityContainer(
      MarketBatchesService.MarketEntityContainer marketEntityContainer)
      throws IOException {

    if (marketEntityContainer.getEntityType().equals("product")) {
      Product product = (Product) marketEntityContainer.getMarketEntity();
      this.generateProductToBMEMarket(product);
    } else if (marketEntityContainer.getEntityType().equals("singleOrder")) {
      SingleOrder singleOrder = (SingleOrder) marketEntityContainer.getMarketEntity();
      singleOrder.setCurrent_quantity(
          singleOrder.getQuantity()); //Reset prices and quantities to initial for BME
      singleOrder.setCurrent_price(singleOrder.getPrice());
      this.generateOrderToBMEMarket(singleOrder);
    } else {
      Basket basket = (Basket) marketEntityContainer.getMarketEntity();
      for (SingleOrder order : basket.getOrders()) {
        order.setCurrent_quantity(
            order.getQuantity()); //Reset prices and quantities to initial for BME
        order.setCurrent_price(order.getPrice());
      }

      if (!basket.getOrders().isEmpty()) {
        this.generateBasketToBMEMarket(basket);
      }

    }


  }


  private void generateProductToBMEMarket(Product product) throws IOException {
    BMECommand command = product.generateOpenProductCommand();
    marketProcessor.marketInteraction(Arrays.asList(command), false, product.getMarketCode());
  }

  private void generateOrderToBMEMarket(SingleOrder singleOrder) throws IOException {
    BMECommand command = singleOrder.generateCommand();
    marketProcessor
        .marketInteraction(Arrays.asList(command), false, singleOrder.getProduct().getMarketCode());
  }

  private void generateBasketToBMEMarket(Basket basket) throws IOException {

    List<BMECommand> commands = new ArrayList<>();

    //1. Send Basket creation command to BME
    commands.add(basket.generateBasketCreationCommand());

    //2. Send Basket selection command to BME
    commands.add(basket.generateBasketSelectionCommand());

    //3. Send orders to BME
    List<SingleOrder> orders = basket.getOrders();
    for (SingleOrder order : orders) {
      commands.add(order.generateCommand());
    }

    //4. Send basket commit command to BME
    commands.add(basket.generateBasketCommitCommand());
    marketProcessor
        .marketInteraction(commands, true, basket.getOrders().get(0).getProduct().getMarketCode());
  }

  public boolean isProcessAlive(String marketCode) {
    return marketProcessor.isProcessAlive(marketCode);
  }


  @Data
  @AllArgsConstructor
  class MarketEntityContainer {

    private Instant createdOn;
    private String entityType;
    private Object marketEntity;
  }

}
