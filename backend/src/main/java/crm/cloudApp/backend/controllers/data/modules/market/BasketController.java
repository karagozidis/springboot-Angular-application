package crm.cloudApp.backend.controllers.data.modules.market;

import crm.cloudApp.backend.config.data.modules.market.MarketInitialization;
import crm.cloudApp.backend.dto.data.modules.market.BasketDTO;
import crm.cloudApp.backend.services.data.modules.market.BasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/market/baskets")
public class BasketController {

  private final BasketService basketService;
  private final MarketInitialization marketInitialization;

  public BasketController(BasketService basketService,
      MarketInitialization marketInitialization) {
    this.basketService = basketService;
    this.marketInitialization = marketInitialization;
  }


  @GetMapping(path = "/by-id")
  public BasketDTO getById(@RequestParam("basket-id") Long basketId) {
    return basketService.getByBasketId(basketId);
  }

  @GetMapping(path = "/by-order-id")
  public BasketDTO getByOrderId(@RequestParam("order-id") Long orderId) {
    return basketService.getByOrderId(orderId);
  }

  @PostMapping
  public BasketDTO create(@RequestBody BasketDTO basketDTO) throws Exception {

    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }

    basketService.checkNumericValues(basketDTO);
    marketInitialization.verifyAndRestoreTradingProcesses();
    return basketService.create(basketDTO);
  }

  @PutMapping(path = "/{basket-id}")
  public BasketDTO update(@PathVariable("basket-id") Long basketId,
      @RequestBody BasketDTO basketDTO) throws Exception {

    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }
    marketInitialization.verifyAndRestoreTradingProcesses();
    basketService.remove(basketId);
    return basketService.update(basketDTO);
  }

  @DeleteMapping
  public Boolean remove(@RequestParam("basket-id") Long basketId) throws Exception {
    if (!MarketInitialization.initialized) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          MarketInitialization.MARKET_INITIALIZATION_MESSAGE);
    }
    marketInitialization.verifyAndRestoreTradingProcesses();
    return basketService.remove(basketId);
  }

}
