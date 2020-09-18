package crm.cloudApp.backend.controllers.data.modules.market;

import crm.cloudApp.backend.services.data.modules.market.TradingProcessInteractionService;
import crm.cloudApp.backend.services.data.modules.market.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/market/settings")
public class MarketController {

  private final MarketService marketService;
  private final TradingProcessInteractionService tradingProcessInteractionService;

  public MarketController(
      MarketService marketService,
      TradingProcessInteractionService tradingProcessInteractionService) {
    this.marketService = marketService;
    this.tradingProcessInteractionService = tradingProcessInteractionService;
  }

  @PutMapping(path = "/log-bme-commands-to-websockets")
  public void update(@RequestParam("connection-state") Boolean logBMECommandsToWebSockets) {
    this.tradingProcessInteractionService.setLogBMECommandsToWebSockets(logBMECommandsToWebSockets);
  }

  @GetMapping(path = "/log-bme-commands-to-websockets")
  public boolean get() {
    return this.tradingProcessInteractionService.getLogBMECommandsToWebSockets();
  }

  @GetMapping(path = "/process-alive")
  public boolean isProcessAlive(
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {
    return marketService.isTradingExeAlive(marketCode);
  }

}
