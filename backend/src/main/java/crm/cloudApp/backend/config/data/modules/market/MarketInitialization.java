package crm.cloudApp.backend.config.data.modules.market;

import crm.cloudApp.backend.services.data.modules.market.MarketBatchesService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MarketInitialization implements InitializingBean {


  private final MarketBatchesService marketBatchesService;
  public static boolean initialized = false;
  public static final String MARKET_INITIALIZATION_MESSAGE = "Market is on Initialization. This "
      + "service will be available soon";

  public MarketInitialization(MarketBatchesService marketBatchesService) {
    this.marketBatchesService = marketBatchesService;
  }

  public static void setInitialized(boolean initialized) {
    MarketInitialization.initialized = initialized;
  }

  @Override
  public void afterPropertiesSet() throws IOException {
    MarketInitialization.setInitialized(false);
    marketBatchesService.initializeTimeActiveEntitesToTradingExe();
    MarketInitialization.setInitialized(true);
  }


  public void verifyAndRestoreTradingProcesses() throws IOException {

    if (!marketBatchesService.isProcessAlive("CY")) {
      MarketInitialization.setInitialized(false);
      marketBatchesService.restoreTimeActiveEntitesToTradingExe("CY");
      MarketInitialization.setInitialized(true);
    }

    if (!marketBatchesService.isProcessAlive("BG")) {
      MarketInitialization.setInitialized(false);
      marketBatchesService.restoreTimeActiveEntitesToTradingExe("BG");
      MarketInitialization.setInitialized(true);
    }

  }


}
