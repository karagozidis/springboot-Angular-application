package crm.cloudApp.backend.controllers.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.config.data.modules.market.MarketInitialization;
import crm.cloudApp.backend.dto.data.modules.market.RandomOrderGenerationSettingsDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.models.data.modules.market.BMEOrderBookEntry;
import crm.cloudApp.backend.services.data.modules.market.BasketService;
import crm.cloudApp.backend.services.data.modules.market.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/market/orders")
public class OrderController {

  private final OrderService orderService;
  private final BasketService basketService;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final MarketInitialization marketInitialization;

  public OrderController(OrderService orderService,
      BasketService basketService,
      SimpMessagingTemplate simpMessagingTemplate,
      MarketInitialization marketInitialization) {
    this.orderService = orderService;
    this.basketService = basketService;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.marketInitialization = marketInitialization;
  }

  @PostMapping
  public SingleOrderDTO create(@RequestBody SingleOrderDTO singleOrderDTO) throws Exception {

    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }

    orderService.checkNumericValues(singleOrderDTO);
    marketInitialization.verifyAndRestoreTradingProcesses();

    SingleOrderDTO createdSingleOrderDTO = orderService.create(singleOrderDTO);
    this.simpMessagingTemplate.convertAndSend("/topic/market/order", "orders-update");
    return createdSingleOrderDTO;
  }

  @DeleteMapping
  public Boolean remove(@RequestParam("order-id") Long orderId) throws Exception {
    marketInitialization.verifyAndRestoreTradingProcesses();
    return orderService.remove(orderId);
  }

  @PutMapping(path = "/{order-id}")
  public SingleOrderDTO update(@PathVariable("order-id") Long orderId,
      @RequestBody SingleOrderDTO singleOrderDTO) throws Exception {
    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }

    orderService.checkNumericValues(singleOrderDTO);
    marketInitialization.verifyAndRestoreTradingProcesses();
    if (singleOrderDTO.getOrderType().equals(AppConstants.Types.SingleOrderType.BASKET)) {
      return this.basketService.updateOrder(singleOrderDTO, orderId);
    } else {
      orderService.remove(orderId);
      return orderService.create(singleOrderDTO);
    }
  }

  @GetMapping(path = "/get_order_book")
  public List<BMEOrderBookEntry> get_order_book(
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode)
      throws IOException {
    return orderService.getOrderBook(marketCode);
  }

  @GetMapping(path = "/active")
  public List<SingleOrderDTO> getActiveOrders(
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
    return orderService.getActiveOrders(marketCode);
  }

  @GetMapping
  public List<SingleOrderDTO> getOrders(@RequestParam("orderType") String orderType,
      @RequestParam("bidTimeFrom") Instant bidTimeFrom,
      @RequestParam("bidTimeTo") Instant bidTimeTo,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
    return orderService.getOrders(
        orderType,
        bidTimeFrom,
        bidTimeTo,
        marketCode
    );
  }

  @PostMapping(path = "/generate-random")
  public List<SingleOrderDTO> generateRandomOrders(
      @RequestBody RandomOrderGenerationSettingsDTO randomOrderGenerationSettingsDTO
  ) throws Exception {
    marketInitialization.verifyAndRestoreTradingProcesses();
    return orderService.generateRandomOrders(randomOrderGenerationSettingsDTO);
  }

}
