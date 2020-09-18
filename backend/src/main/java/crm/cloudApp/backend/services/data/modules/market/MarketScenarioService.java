package crm.cloudApp.backend.services.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.MarketScenarioDTO;
import crm.cloudApp.backend.mappers.data.modules.market.MarketScenarioMapper;
import crm.cloudApp.backend.models.data.modules.market.MarketScenario;
import crm.cloudApp.backend.repositories.data.modules.market.MarketScenarioRepository;
import crm.cloudApp.backend.services.users.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@Validated
public class MarketScenarioService {

    private final MarketScenarioRepository marketScenarioRepository;
    private final MarketScenarioMapper marketScenarioMapper;
    private final UserService userService;

    public MarketScenarioService(MarketScenarioRepository marketScenarioRepository,
                                 MarketScenarioMapper marketScenarioMapper, UserService userService) {
        this.marketScenarioRepository = marketScenarioRepository;
        this.marketScenarioMapper = marketScenarioMapper;
        this.userService = userService;
    }

    public MarketScenarioDTO get() {
        Page<MarketScenario> settings = marketScenarioRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdOn")));
        if (settings.getContent().size() != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to retrieve scenario");
        }
        return marketScenarioMapper.map(settings.getContent().get(0));
    }

    @Transactional
    public void create(MarketScenarioDTO marketScenarioDTO) {
        MarketScenario setting = marketScenarioMapper.map(marketScenarioDTO);

        setting.setCreatedBy(userService.getLoggedInUserName());
        setting.setCreatedOn(Instant.now());
        setting.setModifiedBy(userService.getLoggedInUserName());
        setting.setModifiedOn(Instant.now());

        marketScenarioRepository.save(setting);
    }

    public Page<MarketScenarioDTO> getPage(Pageable pageable) {
        Page<MarketScenario> settings = marketScenarioRepository.findPage(pageable);
        return marketScenarioMapper.map(settings);
    }


}
