package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.BasketDTOProjection;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import crm.cloudApp.backend.models.users.User;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:15+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class SingleOrderMapperImpl extends SingleOrderMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public SingleOrder map(SingleOrderDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SingleOrder singleOrder = new SingleOrder();

        if ( dto.getCreatedOn() != null ) {
            singleOrder.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            singleOrder.setId( dto.getId() );
        }
        if ( dto.getProduct() != null ) {
            singleOrder.setProduct( productMapper.map( dto.getProduct() ) );
        }
        if ( dto.getBidTime() != null ) {
            singleOrder.setBidTime( dto.getBidTime() );
        }
        if ( dto.getUser() != null ) {
            singleOrder.setUser( userMapper.map( dto.getUser() ) );
        }
        singleOrder.setPrice( dto.getPrice() );
        singleOrder.setQuantity( dto.getQuantity() );
        singleOrder.setCurrent_price( dto.getCurrent_price() );
        singleOrder.setCurrent_quantity( dto.getCurrent_quantity() );
        if ( dto.getOrderStatus() != null ) {
            singleOrder.setOrderStatus( dto.getOrderStatus() );
        }
        if ( dto.getOrderDirection() != null ) {
            singleOrder.setOrderDirection( dto.getOrderDirection() );
        }
        if ( dto.getOrderType() != null ) {
            singleOrder.setOrderType( dto.getOrderType() );
        }
        if ( dto.getMetadata() != null ) {
            singleOrder.setMetadata( dto.getMetadata() );
        }
        if ( dto.getRemovedOn() != null ) {
            singleOrder.setRemovedOn( dto.getRemovedOn() );
        }
        if ( dto.getBasket() != null ) {
            singleOrder.setBasket( basketDTOProjectionToBasket( dto.getBasket() ) );
        }

        return singleOrder;
    }

    @Override
    public void map(SingleOrderDTO dto, SingleOrder entity) {
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
        if ( dto.getProduct() != null ) {
            if ( entity.getProduct() == null ) {
                entity.setProduct( new Product() );
            }
            productMapper.map( dto.getProduct(), entity.getProduct() );
        }
        else {
            entity.setProduct( null );
        }
        if ( dto.getBidTime() != null ) {
            entity.setBidTime( dto.getBidTime() );
        }
        else {
            entity.setBidTime( null );
        }
        if ( dto.getUser() != null ) {
            if ( entity.getUser() == null ) {
                entity.setUser( new User() );
            }
            userMapper.map( dto.getUser(), entity.getUser() );
        }
        else {
            entity.setUser( null );
        }
        entity.setPrice( dto.getPrice() );
        entity.setQuantity( dto.getQuantity() );
        entity.setCurrent_price( dto.getCurrent_price() );
        entity.setCurrent_quantity( dto.getCurrent_quantity() );
        if ( dto.getOrderStatus() != null ) {
            entity.setOrderStatus( dto.getOrderStatus() );
        }
        else {
            entity.setOrderStatus( null );
        }
        if ( dto.getOrderDirection() != null ) {
            entity.setOrderDirection( dto.getOrderDirection() );
        }
        else {
            entity.setOrderDirection( null );
        }
        if ( dto.getOrderType() != null ) {
            entity.setOrderType( dto.getOrderType() );
        }
        else {
            entity.setOrderType( null );
        }
        if ( dto.getMetadata() != null ) {
            entity.setMetadata( dto.getMetadata() );
        }
        else {
            entity.setMetadata( null );
        }
        if ( dto.getRemovedOn() != null ) {
            entity.setRemovedOn( dto.getRemovedOn() );
        }
        else {
            entity.setRemovedOn( null );
        }
        if ( dto.getBasket() != null ) {
            if ( entity.getBasket() == null ) {
                entity.setBasket( new Basket() );
            }
            basketDTOProjectionToBasket1( dto.getBasket(), entity.getBasket() );
        }
        else {
            entity.setBasket( null );
        }
    }

    @Override
    public SingleOrder mapOrder(SingleOrderDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SingleOrder singleOrder = new SingleOrder();

        singleOrder.setQuantity( dto.getQuantity() );
        singleOrder.setPrice( dto.getPrice() );
        singleOrder.setCurrent_price( dto.getPrice() );
        singleOrder.setCurrent_quantity( dto.getQuantity() );
        if ( dto.getCreatedOn() != null ) {
            singleOrder.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getProduct() != null ) {
            singleOrder.setProduct( productMapper.map( dto.getProduct() ) );
        }
        if ( dto.getBidTime() != null ) {
            singleOrder.setBidTime( dto.getBidTime() );
        }
        if ( dto.getUser() != null ) {
            singleOrder.setUser( userMapper.map( dto.getUser() ) );
        }
        if ( dto.getOrderStatus() != null ) {
            singleOrder.setOrderStatus( dto.getOrderStatus() );
        }
        if ( dto.getOrderDirection() != null ) {
            singleOrder.setOrderDirection( dto.getOrderDirection() );
        }
        if ( dto.getOrderType() != null ) {
            singleOrder.setOrderType( dto.getOrderType() );
        }
        if ( dto.getMetadata() != null ) {
            singleOrder.setMetadata( dto.getMetadata() );
        }
        if ( dto.getRemovedOn() != null ) {
            singleOrder.setRemovedOn( dto.getRemovedOn() );
        }
        if ( dto.getBasket() != null ) {
            singleOrder.setBasket( basketDTOProjectionToBasket( dto.getBasket() ) );
        }

        return singleOrder;
    }

    @Override
    public SingleOrderDTO mapOrder(SingleOrder entity) {
        if ( entity == null ) {
            return null;
        }

        SingleOrderDTO singleOrderDTO = new SingleOrderDTO();

        if ( entity.getId() != null ) {
            singleOrderDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            singleOrderDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            singleOrderDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getProduct() != null ) {
            singleOrderDTO.setProduct( productMapper.map( entity.getProduct() ) );
        }
        if ( entity.getBidTime() != null ) {
            singleOrderDTO.setBidTime( entity.getBidTime() );
        }
        if ( entity.getUser() != null ) {
            singleOrderDTO.setUser( userMapper.map( entity.getUser() ) );
        }
        singleOrderDTO.setPrice( entity.getPrice() );
        singleOrderDTO.setQuantity( entity.getQuantity() );
        singleOrderDTO.setCurrent_price( entity.getCurrent_price() );
        singleOrderDTO.setCurrent_quantity( entity.getCurrent_quantity() );
        if ( entity.getOrderStatus() != null ) {
            singleOrderDTO.setOrderStatus( entity.getOrderStatus() );
        }
        if ( entity.getOrderDirection() != null ) {
            singleOrderDTO.setOrderDirection( entity.getOrderDirection() );
        }
        if ( entity.getOrderType() != null ) {
            singleOrderDTO.setOrderType( entity.getOrderType() );
        }
        if ( entity.getMetadata() != null ) {
            singleOrderDTO.setMetadata( entity.getMetadata() );
        }
        if ( entity.getBasket() != null ) {
            singleOrderDTO.setBasket( basketToBasketDTOProjection( entity.getBasket() ) );
        }
        if ( entity.getRemovedOn() != null ) {
            singleOrderDTO.setRemovedOn( entity.getRemovedOn() );
        }

        return singleOrderDTO;
    }

    @Override
    public SingleOrderDTO map(SingleOrder entity) {
        if ( entity == null ) {
            return null;
        }

        SingleOrderDTO singleOrderDTO = new SingleOrderDTO();

        if ( entity.getId() != null ) {
            singleOrderDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            singleOrderDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            singleOrderDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getProduct() != null ) {
            singleOrderDTO.setProduct( productMapper.map( entity.getProduct() ) );
        }
        if ( entity.getBidTime() != null ) {
            singleOrderDTO.setBidTime( entity.getBidTime() );
        }
        if ( entity.getUser() != null ) {
            singleOrderDTO.setUser( userMapper.mapBasic( entity.getUser() ) );
        }
        singleOrderDTO.setPrice( entity.getPrice() );
        singleOrderDTO.setQuantity( entity.getQuantity() );
        singleOrderDTO.setCurrent_price( entity.getCurrent_price() );
        singleOrderDTO.setCurrent_quantity( entity.getCurrent_quantity() );
        if ( entity.getOrderStatus() != null ) {
            singleOrderDTO.setOrderStatus( entity.getOrderStatus() );
        }
        if ( entity.getOrderDirection() != null ) {
            singleOrderDTO.setOrderDirection( entity.getOrderDirection() );
        }
        if ( entity.getOrderType() != null ) {
            singleOrderDTO.setOrderType( entity.getOrderType() );
        }
        if ( entity.getMetadata() != null ) {
            singleOrderDTO.setMetadata( entity.getMetadata() );
        }
        if ( entity.getBasket() != null ) {
            singleOrderDTO.setBasket( basketToBasketDTOProjection( entity.getBasket() ) );
        }
        if ( entity.getRemovedOn() != null ) {
            singleOrderDTO.setRemovedOn( entity.getRemovedOn() );
        }

        return singleOrderDTO;
    }

    protected Basket basketDTOProjectionToBasket(BasketDTOProjection basketDTOProjection) {
        if ( basketDTOProjection == null ) {
            return null;
        }

        Basket basket = new Basket();

        if ( basketDTOProjection.getCreatedOn() != null ) {
            basket.setCreatedOn( basketDTOProjection.getCreatedOn() );
        }
        if ( basketDTOProjection.getCreatedBy() != null ) {
            basket.setCreatedBy( basketDTOProjection.getCreatedBy() );
        }
        if ( basketDTOProjection.getId() != null ) {
            basket.setId( basketDTOProjection.getId() );
        }
        if ( basketDTOProjection.getBasketType() != null ) {
            basket.setBasketType( basketDTOProjection.getBasketType() );
        }
        if ( basketDTOProjection.getMetadata() != null ) {
            basket.setMetadata( basketDTOProjection.getMetadata() );
        }

        return basket;
    }

    protected void basketDTOProjectionToBasket1(BasketDTOProjection basketDTOProjection, Basket mappingTarget) {
        if ( basketDTOProjection == null ) {
            return;
        }

        if ( basketDTOProjection.getCreatedOn() != null ) {
            mappingTarget.setCreatedOn( basketDTOProjection.getCreatedOn() );
        }
        else {
            mappingTarget.setCreatedOn( null );
        }
        if ( basketDTOProjection.getCreatedBy() != null ) {
            mappingTarget.setCreatedBy( basketDTOProjection.getCreatedBy() );
        }
        else {
            mappingTarget.setCreatedBy( null );
        }
        if ( basketDTOProjection.getId() != null ) {
            mappingTarget.setId( basketDTOProjection.getId() );
        }
        else {
            mappingTarget.setId( null );
        }
        if ( basketDTOProjection.getBasketType() != null ) {
            mappingTarget.setBasketType( basketDTOProjection.getBasketType() );
        }
        else {
            mappingTarget.setBasketType( null );
        }
        if ( basketDTOProjection.getMetadata() != null ) {
            mappingTarget.setMetadata( basketDTOProjection.getMetadata() );
        }
        else {
            mappingTarget.setMetadata( null );
        }
    }

    protected BasketDTOProjection basketToBasketDTOProjection(Basket basket) {
        if ( basket == null ) {
            return null;
        }

        BasketDTOProjection basketDTOProjection = new BasketDTOProjection();

        if ( basket.getId() != null ) {
            basketDTOProjection.setId( basket.getId() );
        }
        if ( basket.getCreatedOn() != null ) {
            basketDTOProjection.setCreatedOn( basket.getCreatedOn() );
        }
        if ( basket.getCreatedBy() != null ) {
            basketDTOProjection.setCreatedBy( basket.getCreatedBy() );
        }
        if ( basket.getBasketType() != null ) {
            basketDTOProjection.setBasketType( basket.getBasketType() );
        }
        if ( basket.getMetadata() != null ) {
            basketDTOProjection.setMetadata( basket.getMetadata() );
        }

        return basketDTOProjection;
    }
}
