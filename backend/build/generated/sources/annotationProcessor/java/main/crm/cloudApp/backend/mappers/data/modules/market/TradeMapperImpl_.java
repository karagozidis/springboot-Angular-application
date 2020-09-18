package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.BasketDTOProjection;
import crm.cloudApp.backend.dto.data.modules.market.ProductDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import crm.cloudApp.backend.models.users.Country;
import crm.cloudApp.backend.models.users.User;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:13+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
@Qualifier("delegate")
public class TradeMapperImpl_ extends TradeMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public MatchedOrder map(TradeDTO dto) {
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
        if ( dto.getSingleOrder1() != null ) {
            matchedOrder.setSingleOrder1( singleOrderDTOToSingleOrder( dto.getSingleOrder1() ) );
        }
        if ( dto.getSingleOrder2() != null ) {
            matchedOrder.setSingleOrder2( singleOrderDTOToSingleOrder( dto.getSingleOrder2() ) );
        }
        if ( dto.getPrice() != null ) {
            matchedOrder.setPrice( dto.getPrice() );
        }
        if ( dto.getQuantity() != null ) {
            matchedOrder.setQuantity( dto.getQuantity() );
        }
        if ( dto.getOrder1_price() != null ) {
            matchedOrder.setOrder1_price( dto.getOrder1_price() );
        }
        if ( dto.getOrder1_quantity() != null ) {
            matchedOrder.setOrder1_quantity( dto.getOrder1_quantity() );
        }
        if ( dto.getOrder2_price() != null ) {
            matchedOrder.setOrder2_price( dto.getOrder2_price() );
        }
        if ( dto.getOrder2_quantity() != null ) {
            matchedOrder.setOrder2_quantity( dto.getOrder2_quantity() );
        }
        if ( dto.getMetadata() != null ) {
            matchedOrder.setMetadata( dto.getMetadata() );
        }

        return matchedOrder;
    }

    @Override
    public TradeDTO map(MatchedOrder entity) {
        if ( entity == null ) {
            return null;
        }

        TradeDTO tradeDTO = new TradeDTO();

        if ( entity.getId() != null ) {
            tradeDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            tradeDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            tradeDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getTimestamp() != null ) {
            tradeDTO.setTimestamp( entity.getTimestamp() );
        }
        if ( entity.getPrice() != null ) {
            tradeDTO.setPrice( entity.getPrice() );
        }
        if ( entity.getQuantity() != null ) {
            tradeDTO.setQuantity( entity.getQuantity() );
        }
        if ( entity.getMetadata() != null ) {
            tradeDTO.setMetadata( entity.getMetadata() );
        }
        if ( entity.getSingleOrder1() != null ) {
            tradeDTO.setSingleOrder1( singleOrderToSingleOrderDTO( entity.getSingleOrder1() ) );
        }
        if ( entity.getSingleOrder2() != null ) {
            tradeDTO.setSingleOrder2( singleOrderToSingleOrderDTO( entity.getSingleOrder2() ) );
        }
        if ( entity.getOrder1_price() != null ) {
            tradeDTO.setOrder1_price( entity.getOrder1_price() );
        }
        if ( entity.getOrder1_quantity() != null ) {
            tradeDTO.setOrder1_quantity( entity.getOrder1_quantity() );
        }
        if ( entity.getOrder2_price() != null ) {
            tradeDTO.setOrder2_price( entity.getOrder2_price() );
        }
        if ( entity.getOrder2_quantity() != null ) {
            tradeDTO.setOrder2_quantity( entity.getOrder2_quantity() );
        }

        return tradeDTO;
    }

    @Override
    public void map(TradeDTO dto, MatchedOrder entity) {
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
        if ( dto.getSingleOrder1() != null ) {
            if ( entity.getSingleOrder1() == null ) {
                entity.setSingleOrder1( new SingleOrder() );
            }
            singleOrderDTOToSingleOrder1( dto.getSingleOrder1(), entity.getSingleOrder1() );
        }
        else {
            entity.setSingleOrder1( null );
        }
        if ( dto.getSingleOrder2() != null ) {
            if ( entity.getSingleOrder2() == null ) {
                entity.setSingleOrder2( new SingleOrder() );
            }
            singleOrderDTOToSingleOrder1( dto.getSingleOrder2(), entity.getSingleOrder2() );
        }
        else {
            entity.setSingleOrder2( null );
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
        if ( dto.getOrder1_price() != null ) {
            entity.setOrder1_price( dto.getOrder1_price() );
        }
        else {
            entity.setOrder1_price( null );
        }
        if ( dto.getOrder1_quantity() != null ) {
            entity.setOrder1_quantity( dto.getOrder1_quantity() );
        }
        else {
            entity.setOrder1_quantity( null );
        }
        if ( dto.getOrder2_price() != null ) {
            entity.setOrder2_price( dto.getOrder2_price() );
        }
        else {
            entity.setOrder2_price( null );
        }
        if ( dto.getOrder2_quantity() != null ) {
            entity.setOrder2_quantity( dto.getOrder2_quantity() );
        }
        else {
            entity.setOrder2_quantity( null );
        }
        if ( dto.getMetadata() != null ) {
            entity.setMetadata( dto.getMetadata() );
        }
        else {
            entity.setMetadata( null );
        }
    }

    @Override
    public TradeDTO mapWithBuyerSeller(MatchedOrder entity) {
        if ( entity == null ) {
            return null;
        }

        TradeDTO tradeDTO = new TradeDTO();

        String name = entitySingleOrder1ProductName( entity );
        if ( name != null ) {
            tradeDTO.setProductName( name );
        }
        if ( entity.getId() != null ) {
            tradeDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            tradeDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            tradeDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getTimestamp() != null ) {
            tradeDTO.setTimestamp( entity.getTimestamp() );
        }
        if ( entity.getPrice() != null ) {
            tradeDTO.setPrice( entity.getPrice() );
        }
        if ( entity.getQuantity() != null ) {
            tradeDTO.setQuantity( entity.getQuantity() );
        }
        if ( entity.getMetadata() != null ) {
            tradeDTO.setMetadata( entity.getMetadata() );
        }
        if ( entity.getSingleOrder1() != null ) {
            tradeDTO.setSingleOrder1( singleOrderToSingleOrderDTO1( entity.getSingleOrder1() ) );
        }
        if ( entity.getSingleOrder2() != null ) {
            tradeDTO.setSingleOrder2( singleOrderToSingleOrderDTO1( entity.getSingleOrder2() ) );
        }
        if ( entity.getOrder1_price() != null ) {
            tradeDTO.setOrder1_price( entity.getOrder1_price() );
        }
        if ( entity.getOrder1_quantity() != null ) {
            tradeDTO.setOrder1_quantity( entity.getOrder1_quantity() );
        }
        if ( entity.getOrder2_price() != null ) {
            tradeDTO.setOrder2_price( entity.getOrder2_price() );
        }
        if ( entity.getOrder2_quantity() != null ) {
            tradeDTO.setOrder2_quantity( entity.getOrder2_quantity() );
        }
        tradeDTO.setSeller( matchedOrderToUserDTO( entity ) );
        tradeDTO.setBuyer( matchedOrderToUserDTO1( entity ) );

        return tradeDTO;
    }

    @Override
    public TradeDTO mapWithBuyerSellerIgnoringOthers(MatchedOrder entity) {
        if ( entity == null ) {
            return null;
        }

        TradeDTO tradeDTO = new TradeDTO();

        if ( entity.getSingleOrder2() != null ) {
            tradeDTO.setSingleOrder2( singleOrderToSingleOrderDTO2( entity.getSingleOrder2() ) );
        }
        if ( entity.getSingleOrder1() != null ) {
            tradeDTO.setSingleOrder1( singleOrderToSingleOrderDTO3( entity.getSingleOrder1() ) );
        }
        String name = entitySingleOrder1ProductName( entity );
        if ( name != null ) {
            tradeDTO.setProductName( name );
        }
        if ( entity.getId() != null ) {
            tradeDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            tradeDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            tradeDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getTimestamp() != null ) {
            tradeDTO.setTimestamp( entity.getTimestamp() );
        }
        if ( entity.getPrice() != null ) {
            tradeDTO.setPrice( entity.getPrice() );
        }
        if ( entity.getQuantity() != null ) {
            tradeDTO.setQuantity( entity.getQuantity() );
        }
        if ( entity.getMetadata() != null ) {
            tradeDTO.setMetadata( entity.getMetadata() );
        }
        if ( entity.getOrder1_price() != null ) {
            tradeDTO.setOrder1_price( entity.getOrder1_price() );
        }
        if ( entity.getOrder1_quantity() != null ) {
            tradeDTO.setOrder1_quantity( entity.getOrder1_quantity() );
        }
        if ( entity.getOrder2_price() != null ) {
            tradeDTO.setOrder2_price( entity.getOrder2_price() );
        }
        if ( entity.getOrder2_quantity() != null ) {
            tradeDTO.setOrder2_quantity( entity.getOrder2_quantity() );
        }

        return tradeDTO;
    }

    @Override
    public TradeDTO mapWithBuyerSellerOwnOrder(MatchedOrder entity) {
        if ( entity == null ) {
            return null;
        }

        TradeDTO tradeDTO = new TradeDTO();

        if ( entity.getSingleOrder1() != null ) {
            tradeDTO.setSingleOrder1( singleOrderToSingleOrderDTO3( entity.getSingleOrder1() ) );
        }
        if ( entity.getSingleOrder2() != null ) {
            tradeDTO.setSingleOrder2( singleOrderToSingleOrderDTO3( entity.getSingleOrder2() ) );
        }
        String name = entitySingleOrder1ProductName( entity );
        if ( name != null ) {
            tradeDTO.setProductName( name );
        }
        if ( entity.getId() != null ) {
            tradeDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            tradeDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            tradeDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getTimestamp() != null ) {
            tradeDTO.setTimestamp( entity.getTimestamp() );
        }
        if ( entity.getPrice() != null ) {
            tradeDTO.setPrice( entity.getPrice() );
        }
        if ( entity.getQuantity() != null ) {
            tradeDTO.setQuantity( entity.getQuantity() );
        }
        if ( entity.getMetadata() != null ) {
            tradeDTO.setMetadata( entity.getMetadata() );
        }
        if ( entity.getOrder1_price() != null ) {
            tradeDTO.setOrder1_price( entity.getOrder1_price() );
        }
        if ( entity.getOrder1_quantity() != null ) {
            tradeDTO.setOrder1_quantity( entity.getOrder1_quantity() );
        }
        if ( entity.getOrder2_price() != null ) {
            tradeDTO.setOrder2_price( entity.getOrder2_price() );
        }
        if ( entity.getOrder2_quantity() != null ) {
            tradeDTO.setOrder2_quantity( entity.getOrder2_quantity() );
        }

        return tradeDTO;
    }

    protected Product productDTOToProduct(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product product = new Product();

        if ( productDTO.getCreatedOn() != null ) {
            product.setCreatedOn( productDTO.getCreatedOn() );
        }
        if ( productDTO.getCreatedBy() != null ) {
            product.setCreatedBy( productDTO.getCreatedBy() );
        }
        if ( productDTO.getId() != null ) {
            product.setId( productDTO.getId() );
        }
        if ( productDTO.getName() != null ) {
            product.setName( productDTO.getName() );
        }
        if ( productDTO.getDeliveryTimeStart() != null ) {
            product.setDeliveryTimeStart( productDTO.getDeliveryTimeStart() );
        }
        if ( productDTO.getDeliveryTimeEnd() != null ) {
            product.setDeliveryTimeEnd( productDTO.getDeliveryTimeEnd() );
        }
        if ( productDTO.getPeriod() != null ) {
            product.setPeriod( productDTO.getPeriod() );
        }
        if ( productDTO.getProductStatus() != null ) {
            product.setProductStatus( productDTO.getProductStatus() );
        }
        if ( productDTO.getUser() != null ) {
            product.setUser( userMapper.map( productDTO.getUser() ) );
        }
        if ( productDTO.getMarketCode() != null ) {
            product.setMarketCode( productDTO.getMarketCode() );
        }

        return product;
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

    protected SingleOrder singleOrderDTOToSingleOrder(SingleOrderDTO singleOrderDTO) {
        if ( singleOrderDTO == null ) {
            return null;
        }

        SingleOrder singleOrder = new SingleOrder();

        if ( singleOrderDTO.getCreatedOn() != null ) {
            singleOrder.setCreatedOn( singleOrderDTO.getCreatedOn() );
        }
        if ( singleOrderDTO.getCreatedBy() != null ) {
            singleOrder.setCreatedBy( singleOrderDTO.getCreatedBy() );
        }
        if ( singleOrderDTO.getId() != null ) {
            singleOrder.setId( singleOrderDTO.getId() );
        }
        if ( singleOrderDTO.getProduct() != null ) {
            singleOrder.setProduct( productDTOToProduct( singleOrderDTO.getProduct() ) );
        }
        if ( singleOrderDTO.getBidTime() != null ) {
            singleOrder.setBidTime( singleOrderDTO.getBidTime() );
        }
        if ( singleOrderDTO.getUser() != null ) {
            singleOrder.setUser( userMapper.map( singleOrderDTO.getUser() ) );
        }
        singleOrder.setPrice( singleOrderDTO.getPrice() );
        singleOrder.setQuantity( singleOrderDTO.getQuantity() );
        singleOrder.setCurrent_price( singleOrderDTO.getCurrent_price() );
        singleOrder.setCurrent_quantity( singleOrderDTO.getCurrent_quantity() );
        if ( singleOrderDTO.getOrderStatus() != null ) {
            singleOrder.setOrderStatus( singleOrderDTO.getOrderStatus() );
        }
        if ( singleOrderDTO.getOrderDirection() != null ) {
            singleOrder.setOrderDirection( singleOrderDTO.getOrderDirection() );
        }
        if ( singleOrderDTO.getOrderType() != null ) {
            singleOrder.setOrderType( singleOrderDTO.getOrderType() );
        }
        if ( singleOrderDTO.getMetadata() != null ) {
            singleOrder.setMetadata( singleOrderDTO.getMetadata() );
        }
        if ( singleOrderDTO.getRemovedOn() != null ) {
            singleOrder.setRemovedOn( singleOrderDTO.getRemovedOn() );
        }
        if ( singleOrderDTO.getBasket() != null ) {
            singleOrder.setBasket( basketDTOProjectionToBasket( singleOrderDTO.getBasket() ) );
        }

        return singleOrder;
    }

    protected ProductDTO productToProductDTO(Product product) {
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
        if ( product.getUser() != null ) {
            productDTO.setUser( userMapper.map( product.getUser() ) );
        }
        if ( product.getMarketCode() != null ) {
            productDTO.setMarketCode( product.getMarketCode() );
        }

        return productDTO;
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

    protected SingleOrderDTO singleOrderToSingleOrderDTO(SingleOrder singleOrder) {
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
        if ( singleOrder.getProduct() != null ) {
            singleOrderDTO.setProduct( productToProductDTO( singleOrder.getProduct() ) );
        }
        if ( singleOrder.getBidTime() != null ) {
            singleOrderDTO.setBidTime( singleOrder.getBidTime() );
        }
        if ( singleOrder.getUser() != null ) {
            singleOrderDTO.setUser( userMapper.map( singleOrder.getUser() ) );
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

    protected void productDTOToProduct1(ProductDTO productDTO, Product mappingTarget) {
        if ( productDTO == null ) {
            return;
        }

        if ( productDTO.getCreatedOn() != null ) {
            mappingTarget.setCreatedOn( productDTO.getCreatedOn() );
        }
        else {
            mappingTarget.setCreatedOn( null );
        }
        if ( productDTO.getCreatedBy() != null ) {
            mappingTarget.setCreatedBy( productDTO.getCreatedBy() );
        }
        else {
            mappingTarget.setCreatedBy( null );
        }
        if ( productDTO.getId() != null ) {
            mappingTarget.setId( productDTO.getId() );
        }
        else {
            mappingTarget.setId( null );
        }
        if ( productDTO.getName() != null ) {
            mappingTarget.setName( productDTO.getName() );
        }
        else {
            mappingTarget.setName( null );
        }
        if ( productDTO.getDeliveryTimeStart() != null ) {
            mappingTarget.setDeliveryTimeStart( productDTO.getDeliveryTimeStart() );
        }
        else {
            mappingTarget.setDeliveryTimeStart( null );
        }
        if ( productDTO.getDeliveryTimeEnd() != null ) {
            mappingTarget.setDeliveryTimeEnd( productDTO.getDeliveryTimeEnd() );
        }
        else {
            mappingTarget.setDeliveryTimeEnd( null );
        }
        if ( productDTO.getPeriod() != null ) {
            mappingTarget.setPeriod( productDTO.getPeriod() );
        }
        else {
            mappingTarget.setPeriod( null );
        }
        if ( productDTO.getProductStatus() != null ) {
            mappingTarget.setProductStatus( productDTO.getProductStatus() );
        }
        else {
            mappingTarget.setProductStatus( null );
        }
        if ( productDTO.getUser() != null ) {
            if ( mappingTarget.getUser() == null ) {
                mappingTarget.setUser( new User() );
            }
            userMapper.map( productDTO.getUser(), mappingTarget.getUser() );
        }
        else {
            mappingTarget.setUser( null );
        }
        if ( productDTO.getMarketCode() != null ) {
            mappingTarget.setMarketCode( productDTO.getMarketCode() );
        }
        else {
            mappingTarget.setMarketCode( null );
        }
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

    protected void singleOrderDTOToSingleOrder1(SingleOrderDTO singleOrderDTO, SingleOrder mappingTarget) {
        if ( singleOrderDTO == null ) {
            return;
        }

        if ( singleOrderDTO.getCreatedOn() != null ) {
            mappingTarget.setCreatedOn( singleOrderDTO.getCreatedOn() );
        }
        else {
            mappingTarget.setCreatedOn( null );
        }
        if ( singleOrderDTO.getCreatedBy() != null ) {
            mappingTarget.setCreatedBy( singleOrderDTO.getCreatedBy() );
        }
        else {
            mappingTarget.setCreatedBy( null );
        }
        if ( singleOrderDTO.getId() != null ) {
            mappingTarget.setId( singleOrderDTO.getId() );
        }
        else {
            mappingTarget.setId( null );
        }
        if ( singleOrderDTO.getProduct() != null ) {
            if ( mappingTarget.getProduct() == null ) {
                mappingTarget.setProduct( new Product() );
            }
            productDTOToProduct1( singleOrderDTO.getProduct(), mappingTarget.getProduct() );
        }
        else {
            mappingTarget.setProduct( null );
        }
        if ( singleOrderDTO.getBidTime() != null ) {
            mappingTarget.setBidTime( singleOrderDTO.getBidTime() );
        }
        else {
            mappingTarget.setBidTime( null );
        }
        if ( singleOrderDTO.getUser() != null ) {
            if ( mappingTarget.getUser() == null ) {
                mappingTarget.setUser( new User() );
            }
            userMapper.map( singleOrderDTO.getUser(), mappingTarget.getUser() );
        }
        else {
            mappingTarget.setUser( null );
        }
        mappingTarget.setPrice( singleOrderDTO.getPrice() );
        mappingTarget.setQuantity( singleOrderDTO.getQuantity() );
        mappingTarget.setCurrent_price( singleOrderDTO.getCurrent_price() );
        mappingTarget.setCurrent_quantity( singleOrderDTO.getCurrent_quantity() );
        if ( singleOrderDTO.getOrderStatus() != null ) {
            mappingTarget.setOrderStatus( singleOrderDTO.getOrderStatus() );
        }
        else {
            mappingTarget.setOrderStatus( null );
        }
        if ( singleOrderDTO.getOrderDirection() != null ) {
            mappingTarget.setOrderDirection( singleOrderDTO.getOrderDirection() );
        }
        else {
            mappingTarget.setOrderDirection( null );
        }
        if ( singleOrderDTO.getOrderType() != null ) {
            mappingTarget.setOrderType( singleOrderDTO.getOrderType() );
        }
        else {
            mappingTarget.setOrderType( null );
        }
        if ( singleOrderDTO.getMetadata() != null ) {
            mappingTarget.setMetadata( singleOrderDTO.getMetadata() );
        }
        else {
            mappingTarget.setMetadata( null );
        }
        if ( singleOrderDTO.getRemovedOn() != null ) {
            mappingTarget.setRemovedOn( singleOrderDTO.getRemovedOn() );
        }
        else {
            mappingTarget.setRemovedOn( null );
        }
        if ( singleOrderDTO.getBasket() != null ) {
            if ( mappingTarget.getBasket() == null ) {
                mappingTarget.setBasket( new Basket() );
            }
            basketDTOProjectionToBasket1( singleOrderDTO.getBasket(), mappingTarget.getBasket() );
        }
        else {
            mappingTarget.setBasket( null );
        }
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

    protected ProductDTO productToProductDTO1(Product product) {
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

        return productDTO;
    }

    protected SingleOrderDTO singleOrderToSingleOrderDTO1(SingleOrder singleOrder) {
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
        if ( singleOrder.getProduct() != null ) {
            singleOrderDTO.setProduct( productToProductDTO1( singleOrder.getProduct() ) );
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

    protected UserDTO matchedOrderToUserDTO(MatchedOrder matchedOrder) {
        if ( matchedOrder == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        return userDTO;
    }

    protected UserDTO matchedOrderToUserDTO1(MatchedOrder matchedOrder) {
        if ( matchedOrder == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        return userDTO;
    }

    protected CountryDTO countryToCountryDTO(Country country) {
        if ( country == null ) {
            return null;
        }

        CountryDTO countryDTO = new CountryDTO();

        if ( country.getId() != null ) {
            countryDTO.setId( country.getId() );
        }
        if ( country.getCreatedOn() != null ) {
            countryDTO.setCreatedOn( country.getCreatedOn() );
        }
        if ( country.getCreatedBy() != null ) {
            countryDTO.setCreatedBy( country.getCreatedBy() );
        }
        if ( country.getName() != null ) {
            countryDTO.setName( country.getName() );
        }

        return countryDTO;
    }

    protected UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        if ( user.getId() != null ) {
            userDTO.setId( user.getId() );
        }
        if ( user.getCreatedOn() != null ) {
            userDTO.setCreatedOn( user.getCreatedOn() );
        }
        if ( user.getCreatedBy() != null ) {
            userDTO.setCreatedBy( user.getCreatedBy() );
        }
        if ( user.getUsername() != null ) {
            userDTO.setUsername( user.getUsername() );
        }
        if ( user.getEmail() != null ) {
            userDTO.setEmail( user.getEmail() );
        }
        if ( user.getPassword() != null ) {
            userDTO.setPassword( user.getPassword() );
        }
        if ( user.getStatus() != null ) {
            userDTO.setStatus( user.getStatus() );
        }
        if ( user.getCountry() != null ) {
            userDTO.setCountry( countryToCountryDTO( user.getCountry() ) );
        }
        if ( user.getRolesCrm() != null ) {
            userDTO.setRolesCrm( user.getRolesCrm() );
        }
        if ( user.getModulesCrm() != null ) {
            userDTO.setModulesCrm( user.getModulesCrm() );
        }
        if ( user.getRolesMarket() != null ) {
            userDTO.setRolesMarket( user.getRolesMarket() );
        }
        if ( user.getModulesMarket() != null ) {
            userDTO.setModulesMarket( user.getModulesMarket() );
        }
        if ( user.getDefaultMarket() != null ) {
            userDTO.setDefaultMarket( user.getDefaultMarket() );
        }
        if ( user.getMarket() != null ) {
            userDTO.setMarket( user.getMarket() );
        }
        if ( user.getLandingPage() != null ) {
            userDTO.setLandingPage( user.getLandingPage() );
        }
        if ( user.getActiveSubstationUserGroupId() != null ) {
            userDTO.setActiveSubstationUserGroupId( user.getActiveSubstationUserGroupId() );
        }

        return userDTO;
    }

    protected SingleOrderDTO singleOrderToSingleOrderDTO2(SingleOrder singleOrder) {
        if ( singleOrder == null ) {
            return null;
        }

        SingleOrderDTO singleOrderDTO = new SingleOrderDTO();

        if ( singleOrder.getUser() != null ) {
            singleOrderDTO.setUser( userToUserDTO( singleOrder.getUser() ) );
        }
        if ( singleOrder.getId() != null ) {
            singleOrderDTO.setId( singleOrder.getId() );
        }
        if ( singleOrder.getCreatedOn() != null ) {
            singleOrderDTO.setCreatedOn( singleOrder.getCreatedOn() );
        }
        if ( singleOrder.getCreatedBy() != null ) {
            singleOrderDTO.setCreatedBy( singleOrder.getCreatedBy() );
        }
        if ( singleOrder.getProduct() != null ) {
            singleOrderDTO.setProduct( productToProductDTO1( singleOrder.getProduct() ) );
        }
        if ( singleOrder.getBidTime() != null ) {
            singleOrderDTO.setBidTime( singleOrder.getBidTime() );
        }
        if ( singleOrder.getOrderDirection() != null ) {
            singleOrderDTO.setOrderDirection( singleOrder.getOrderDirection() );
        }
        if ( singleOrder.getBasket() != null ) {
            singleOrderDTO.setBasket( basketToBasketDTOProjection( singleOrder.getBasket() ) );
        }
        if ( singleOrder.getRemovedOn() != null ) {
            singleOrderDTO.setRemovedOn( singleOrder.getRemovedOn() );
        }

        return singleOrderDTO;
    }

    protected SingleOrderDTO singleOrderToSingleOrderDTO3(SingleOrder singleOrder) {
        if ( singleOrder == null ) {
            return null;
        }

        SingleOrderDTO singleOrderDTO = new SingleOrderDTO();

        if ( singleOrder.getUser() != null ) {
            singleOrderDTO.setUser( userToUserDTO( singleOrder.getUser() ) );
        }
        if ( singleOrder.getId() != null ) {
            singleOrderDTO.setId( singleOrder.getId() );
        }
        if ( singleOrder.getCreatedOn() != null ) {
            singleOrderDTO.setCreatedOn( singleOrder.getCreatedOn() );
        }
        if ( singleOrder.getCreatedBy() != null ) {
            singleOrderDTO.setCreatedBy( singleOrder.getCreatedBy() );
        }
        if ( singleOrder.getProduct() != null ) {
            singleOrderDTO.setProduct( productToProductDTO1( singleOrder.getProduct() ) );
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
}
