package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.BasketDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;

import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:15+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class BasketMapperImpl extends BasketMapper {

    @Autowired
    private SingleOrderMapper singleOrderMapper;

    @Override
    public Basket map(BasketDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Basket basket = new Basket();

        if ( dto.getCreatedOn() != null ) {
            basket.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            basket.setId( dto.getId() );
        }
        if ( dto.getBasketType() != null ) {
            basket.setBasketType( dto.getBasketType() );
        }
        if ( dto.getMetadata() != null ) {
            basket.setMetadata( dto.getMetadata() );
        }
        List<SingleOrder> list = singleOrderMapper.mapDTOs( dto.getOrders() );
        if ( list != null ) {
            basket.setOrders( list );
        }

        return basket;
    }

    @Override
    public BasketDTO map(Basket entity) {
        if ( entity == null ) {
            return null;
        }

        BasketDTO basketDTO = new BasketDTO();

        if ( entity.getId() != null ) {
            basketDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            basketDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            basketDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getBasketType() != null ) {
            basketDTO.setBasketType( entity.getBasketType() );
        }
        if ( entity.getMetadata() != null ) {
            basketDTO.setMetadata( entity.getMetadata() );
        }
        List<SingleOrderDTO> list = singleOrderMapper.map( entity.getOrders() );
        if ( list != null ) {
            basketDTO.setOrders( list );
        }

        return basketDTO;
    }

    @Override
    public void map(BasketDTO dto, Basket entity) {
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
        if ( dto.getBasketType() != null ) {
            entity.setBasketType( dto.getBasketType() );
        }
        else {
            entity.setBasketType( null );
        }
        if ( dto.getMetadata() != null ) {
            entity.setMetadata( dto.getMetadata() );
        }
        else {
            entity.setMetadata( null );
        }
        if ( entity.getOrders() != null ) {
            List<SingleOrder> list = singleOrderMapper.mapDTOs( dto.getOrders() );
            if ( list != null ) {
                entity.getOrders().clear();
                entity.getOrders().addAll( list );
            }
        }
        else {
            List<SingleOrder> list = singleOrderMapper.mapDTOs( dto.getOrders() );
            if ( list != null ) {
                entity.setOrders( list );
            }
        }
    }

    @Override
    protected Basket map(SingleOrderDTO basketDTO) {
        if ( basketDTO == null ) {
            return null;
        }

        Basket basket = new Basket();

        if ( basketDTO.getCreatedOn() != null ) {
            basket.setCreatedOn( basketDTO.getCreatedOn() );
        }
        if ( basketDTO.getId() != null ) {
            basket.setId( basketDTO.getId() );
        }
        if ( basketDTO.getMetadata() != null ) {
            basket.setMetadata( basketDTO.getMetadata() );
        }

        return basket;
    }
}
