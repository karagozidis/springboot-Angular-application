package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.data.modules.market.MarketScenarioDTO;
import crm.cloudApp.backend.models.data.modules.market.MarketScenario;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class MarketScenarioMapper extends BaseMapper<MarketScenarioDTO, MarketScenario> {

}
