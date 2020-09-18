package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.BasketDTOProjection;
import crm.cloudApp.backend.dto.data.modules.market.ProductDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import crm.cloudApp.backend.models.users.User;

import java.time.temporal.ChronoUnit;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class ProductMapperImpl extends ProductMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Product map(ProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        if ( dto.getCreatedOn() != null ) {
            product.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            product.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            product.setName( dto.getName() );
        }
        if ( dto.getDeliveryTimeStart() != null ) {
            product.setDeliveryTimeStart( dto.getDeliveryTimeStart() );
        }
        if ( dto.getDeliveryTimeEnd() != null ) {
            product.setDeliveryTimeEnd( dto.getDeliveryTimeEnd() );
        }
        if ( dto.getPeriod() != null ) {
            product.setPeriod( dto.getPeriod() );
        }
        if ( dto.getProductStatus() != null ) {
            product.setProductStatus( dto.getProductStatus() );
        }
        if ( dto.getUser() != null ) {
            product.setUser( userMapper.map( dto.getUser() ) );
        }
        if ( dto.getMarketCode() != null ) {
            product.setMarketCode( dto.getMarketCode() );
        }

        return product;
    }

    @Override
    public void map(ProductDTO dto, Product entity) {
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
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        else {
            entity.setName( null );
        }
        if ( dto.getDeliveryTimeStart() != null ) {
            entity.setDeliveryTimeStart( dto.getDeliveryTimeStart() );
        }
        else {
            entity.setDeliveryTimeStart( null );
        }
        if ( dto.getDeliveryTimeEnd() != null ) {
            entity.setDeliveryTimeEnd( dto.getDeliveryTimeEnd() );
        }
        else {
            entity.setDeliveryTimeEnd( null );
        }
        if ( dto.getPeriod() != null ) {
            entity.setPeriod( dto.getPeriod() );
        }
        else {
            entity.setPeriod( null );
        }
        if ( dto.getProductStatus() != null ) {
            entity.setProductStatus( dto.getProductStatus() );
        }
        else {
            entity.setProductStatus( null );
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
        if ( dto.getMarketCode() != null ) {
            entity.setMarketCode( dto.getMarketCode() );
        }
        else {
            entity.setMarketCode( null );
        }
    }

    @Override
    public ProductDTO map(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        if ( product.getId() != null ) {
            productDTO.setId( product.getId() );
        }
        if ( product.getCreatedOn() != null ) {
            productDTO.setCreatedOn( product.getCreatedOn() );
        }
        if ( product.getCreatedBy() != null ) {
            productDTO.setCreatedBy( product.getCreatedBy() );
        }
        if ( product.getName() != null ) {
            productDTO.setName( product.getName() );
        }
        if ( product.getDeliveryTimeStart() != null ) {
            productDTO.setDeliveryTimeStart( product.getDeliveryTimeStart() );
        }
        if ( product.getDeliveryTimeEnd() != null ) {
            productDTO.setDeliveryTimeEnd( product.getDeliveryTimeEnd() );
        }
        if ( product.getPeriod() != null ) {
            productDTO.setPeriod( product.getPeriod() );
        }
        if ( product.getProductStatus() != null ) {
            productDTO.setProductStatus( product.getProductStatus() );
        }
        if ( product.getMarketCode() != null ) {
            productDTO.setMarketCode( product.getMarketCode() );
        }

        productDTO.setClosesAt( product.getDeliveryTimeStart().minus(1, ChronoUnit.HOURS) );

        return productDTO;
    }

    @Override
    public SingleOrderDTO mapSelectedOrder(SingleOrder singleOrder) {
        if ( singleOrder == null ) {
            return null;
        }

        SingleOrderDTO singleOrderDTO = new SingleOrderDTO();

        if ( singleOrder.getId() != null ) {
            singleOrderDTO.setId( singleOrder.getId() );
        }
        if ( singleOrder.getCreatedOn() != null ) {
            singleOrderDTO.setCreatedOn( singleOrder.getCreatedOn() );
        }
        if ( singleOrder.getCreatedBy() != null ) {
            singleOrderDTO.setCreatedBy( singleOrder.getCreatedBy() );
        }
        if ( singleOrder.getBidTime() != null ) {
            singleOrderDTO.setBidTime( singleOrder.getBidTime() );
        }
        singleOrderDTO.setPrice( singleOrder.getPrice() );
        singleOrderDTO.setQuantity( singleOrder.getQuantity() );
        singleOrderDTO.setCurrent_price( singleOrder.getCurrent_price() );
        singleOrderDTO.setCurrent_quantity( singleOrder.getCurrent_quantity() );
        if ( singleOrder.getOrderStatus() != null ) {
            singleOrderDTO.setOrderStatus( singleOrder.getOrderStatus() );
        }
        if ( singleOrder.getOrderDirection() != null ) {
            singleOrderDTO.setOrderDirection( singleOrder.getOrderDirection() );
        }
        if ( singleOrder.getOrderType() != null ) {
            singleOrderDTO.setOrderType( singleOrder.getOrderType() );
        }
        if ( singleOrder.getMetadata() != null ) {
            singleOrderDTO.setMetadata( singleOrder.getMetadata() );
        }
        if ( singleOrder.getBasket() != null ) {
            singleOrderDTO.setBasket( basketToBasketDTOProjection( singleOrder.getBasket() ) );
        }
        if ( singleOrder.getRemovedOn() != null ) {
            singleOrderDTO.setRemovedOn( singleOrder.getRemovedOn() );
        }

        return singleOrderDTO;
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
