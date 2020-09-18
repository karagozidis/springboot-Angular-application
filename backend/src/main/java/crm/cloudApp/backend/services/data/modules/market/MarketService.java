package crm.cloudApp.backend.services.data.modules.market;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@Transactional
public class MarketService {

  private final TradingProcessInteractionService marketProcessor;

  public MarketService(
      TradingProcessInteractionService marketProcessor) {
    this.marketProcessor = marketProcessor;
  }

  public boolean isTradingExeAlive(String marketCode) {
    return marketProcessor.isProcessAlive(marketCode);
  }

}
