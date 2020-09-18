package crm.cloudApp.backend.controllers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.services.data.modules.market.MatchedOrderService;
import crm.cloudApp.backend.services.data.modules.market.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/market/trades")
public class MatchedOrderController {

  private final MatchedOrderService matchedOrderService;
  private final OrderService orderService;

  public MatchedOrderController(MatchedOrderService matchedOrderService,
      OrderService orderService) {
    this.matchedOrderService = matchedOrderService;
    this.orderService = orderService;
  }

  @GetMapping(path = "/new")
  public List<TradeDTO> getNewTrades(
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
    return matchedOrderService.getNewTrades(marketCode);
  }

  @GetMapping
  public @ResponseBody
  List<TradeDTO> getUserTrades(
      @RequestParam("createdFrom") Instant createdFrom,
      @RequestParam("createdTo") Instant createdTo,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
    return this.orderService.getMatchedOrdersByCurrentUser("", createdFrom, createdTo, marketCode);
  }

  @GetMapping(path = "/all")
  public List<TradeDTO> getAllTrades(@RequestParam("createdFrom") Instant createdFrom,
      @RequestParam("createdTo") Instant createdTo,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
    return matchedOrderService.getTradesOnPeriod(createdFrom, createdTo, marketCode);
  }
}
