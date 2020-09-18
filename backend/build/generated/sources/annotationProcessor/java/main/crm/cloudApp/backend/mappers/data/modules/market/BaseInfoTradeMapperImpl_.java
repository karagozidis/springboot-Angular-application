package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.trades.BaseInfoTradeDTO;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
@Qualifier("delegate")
public class BaseInfoTradeMapperImpl_ extends BaseInfoTradeMapper {

    @Override
    public MatchedOrder map(BaseInfoTradeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MatchedOrder matchedOrder = new MatchedOrder();

        if ( dto.getCreatedOn() != null ) {
            matchedOrder.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            matchedOrder.setId( dto.getId() );
        }
        if ( dto.getTimestamp() != null ) {
            matchedOrder.setTimestamp( dto.getTimestamp() );
        }
        if ( dto.getPrice() != null ) {
            matchedOrder.setPrice( dto.getPrice() );
        }
        if ( dto.getQuantity() != null ) {
            matchedOrder.setQuantity( dto.getQuantity() );
        }
        if ( dto.getMetadata() != null ) {
            matchedOrder.setMetadata( dto.getMetadata() );
        }

        return matchedOrder;
    }

    @Override
    public BaseInfoTradeDTO map(MatchedOrder entity) {
        if ( entity == null ) {
            return null;
        }

        BaseInfoTradeDTO baseInfoTradeDTO = new BaseInfoTradeDTO();

        if ( entity.getId() != null ) {
            baseInfoTradeDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            baseInfoTradeDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            baseInfoTradeDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getTimestamp() != null ) {
            baseInfoTradeDTO.setTimestamp( entity.getTimestamp() );
        }
        if ( entity.getPrice() != null ) {
            baseInfoTradeDTO.setPrice( entity.getPrice() );
        }
        if ( entity.getQuantity() != null ) {
            baseInfoTradeDTO.setQuantity( entity.getQuantity() );
        }
        if ( entity.getMetadata() != null ) {
            baseInfoTradeDTO.setMetadata( entity.getMetadata() );
        }

        return baseInfoTradeDTO;
    }

    @Override
    public void map(BaseInfoTradeDTO dto, MatchedOrder entity) {
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
        if ( dto.getTimestamp() != null ) {
            entity.setTimestamp( dto.getTimestamp() );
        }
        else {
            entity.setTimestamp( null );
        }
        if ( dto.getPrice() != null ) {
            entity.setPrice( dto.getPrice() );
        }
        else {
            entity.setPrice( null );
        }
        if ( dto.getQuantity() != null ) {
            entity.setQuantity( dto.getQuantity() );
        }
        else {
            entity.setQuantity( null );
        }
        if ( dto.getMetadata() != null ) {
            entity.setMetadata( dto.getMetadata() );
        }
        else {
            entity.setMetadata( null );
        }
    }

    @Override
    public BaseInfoTradeDTO mapBaseTrades(MatchedOrder entity) {
        if ( entity == null ) {
            return null;
        }

        BaseInfoTradeDTO baseInfoTradeDTO = new BaseInfoTradeDTO();

        String name = entitySingleOrder1ProductName( entity );
        if ( name != null ) {
            baseInfoTradeDTO.setProductName( name );
        }
        if ( entity.getId() != null ) {
            baseInfoTradeDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            baseInfoTradeDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            baseInfoTradeDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getTimestamp() != null ) {
            baseInfoTradeDTO.setTimestamp( entity.getTimestamp() );
        }
        if ( entity.getPrice() != null ) {
            baseInfoTradeDTO.setPrice( entity.getPrice() );
        }
        if ( entity.getQuantity() != null ) {
            baseInfoTradeDTO.setQuantity( entity.getQuantity() );
        }
        if ( entity.getMetadata() != null ) {
            baseInfoTradeDTO.setMetadata( entity.getMetadata() );
        }

        return baseInfoTradeDTO;
    }

    private String entitySingleOrder1ProductName(MatchedOrder matchedOrder) {
        if ( matchedOrder == null ) {
            return null;
        }
        SingleOrder singleOrder1 = matchedOrder.getSingleOrder1();
        if ( singleOrder1 == null ) {
            return null;
        }
        Product product = singleOrder1.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
