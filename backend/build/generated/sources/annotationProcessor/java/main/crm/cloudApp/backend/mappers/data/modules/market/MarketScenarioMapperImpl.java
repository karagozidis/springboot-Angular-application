package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.MarketScenarioDTO;
import crm.cloudApp.backend.models.data.modules.market.MarketScenario;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class MarketScenarioMapperImpl extends MarketScenarioMapper {

    @Override
    public MarketScenario map(MarketScenarioDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MarketScenario marketScenario = new MarketScenario();

        if ( dto.getCreatedOn() != null ) {
            marketScenario.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            marketScenario.setId( dto.getId() );
        }
        if ( dto.getScenario() != null ) {
            marketScenario.setScenario( dto.getScenario() );
        }

        return marketScenario;
    }

    @Override
    public MarketScenarioDTO map(MarketScenario entity) {
        if ( entity == null ) {
            return null;
        }

        MarketScenarioDTO marketScenarioDTO = new MarketScenarioDTO();

        if ( entity.getId() != null ) {
            marketScenarioDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            marketScenarioDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            marketScenarioDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getScenario() != null ) {
            marketScenarioDTO.setScenario( entity.getScenario() );
        }

        return marketScenarioDTO;
    }

    @Override
    public void map(MarketScenarioDTO dto, MarketScenario entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreatedOn() != null ) {
            entity.setCreatedOn( dto.getCreatedOn() );
        }
        else {
            entity.setCreatedOn( null );
        }
        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        else {
            entity.setId( null );
        }
        if ( dto.getScenario() != null ) {
            entity.setScenario( dto.getScenario() );
        }
        else {
            entity.setScenario( null );
        }
    }
}
