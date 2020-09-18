package crm.cloudApp.backend.controllers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.MarketScenarioDTO;
import crm.cloudApp.backend.services.data.modules.market.MarketScenarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/market-scenario")
public class MarketScenarioController {

    private final MarketScenarioService marketScenarioService;

    public MarketScenarioController(MarketScenarioService marketScenarioService) {
        this.marketScenarioService = marketScenarioService;
    }

    @GetMapping
    public MarketScenarioDTO get() {
        return marketScenarioService.get();
    }

    @PostMapping
    public void create(
            @RequestBody MarketScenarioDTO marketScenarioDTO) {
        marketScenarioService.create(marketScenarioDTO);
    }

    @GetMapping(path = "/pages")
    public Page<MarketScenarioDTO> getPage(Pageable pageable) {
        return marketScenarioService.getPage(pageable);
    }

}
