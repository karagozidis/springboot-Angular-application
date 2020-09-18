package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.ImportOrderDTO;
import crm.cloudApp.backend.models.data.modules.market.ImportOrder;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:11+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class ImportOrderMapperImpl extends ImportOrderMapper {

    @Override
    public ImportOrder map(ImportOrderDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ImportOrder importOrder = new ImportOrder();

        if ( dto.getCreatedOn() != null ) {
            importOrder.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            importOrder.setId( dto.getId() );
        }
        if ( dto.getOrder_ID() != null ) {
            importOrder.setOrder_ID( dto.getOrder_ID() );
        }
        if ( dto.getAction() != null ) {
            importOrder.setAction( dto.getAction() );
        }
        if ( dto.getProduct_name() != null ) {
            importOrder.setProduct_name( dto.getProduct_name() );
        }
        if ( dto.getOrderType() != null ) {
            importOrder.setOrderType( dto.getOrderType() );
        }
        importOrder.setQuantity( dto.getQuantity() );
        importOrder.setPrice( dto.getPrice() );
        if ( dto.getOrderDirection() != null ) {
            importOrder.setOrderDirection( dto.getOrderDirection() );
        }
        if ( dto.getTimeStamp() != null ) {
            importOrder.setTimeStamp( dto.getTimeStamp() );
        }
        if ( dto.getOrder_meta() != null ) {
            importOrder.setOrder_meta( dto.getOrder_meta() );
        }
        if ( dto.getBasket_ID() != null ) {
            importOrder.setBasket_ID( dto.getBasket_ID() );
        }
        if ( dto.getBasket_Type() != null ) {
            importOrder.setBasket_Type( dto.getBasket_Type() );
        }
        if ( dto.getBasket_meta() != null ) {
            importOrder.setBasket_meta( dto.getBasket_meta() );
        }
        if ( dto.getUserId() != null ) {
            importOrder.setUserId( dto.getUserId() );
        }
        if ( dto.getStatus() != null ) {
            importOrder.setStatus( dto.getStatus() );
        }
        if ( dto.getMessageInfoId() != null ) {
            importOrder.setMessageInfoId( dto.getMessageInfoId() );
        }
        if ( dto.getMessageInfoName() != null ) {
            importOrder.setMessageInfoName( dto.getMessageInfoName() );
        }
        if ( dto.getSingleOrderId() != null ) {
            importOrder.setSingleOrderId( dto.getSingleOrderId() );
        }
        if ( dto.getMarketCode() != null ) {
            importOrder.setMarketCode( dto.getMarketCode() );
        }

        return importOrder;
    }

    @Override
    public ImportOrderDTO map(ImportOrder entity) {
        if ( entity == null ) {
            return null;
        }

        ImportOrderDTO importOrderDTO = new ImportOrderDTO();

        if ( entity.getId() != null ) {
            importOrderDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            importOrderDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            importOrderDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getOrder_ID() != null ) {
            importOrderDTO.setOrder_ID( entity.getOrder_ID() );
        }
        if ( entity.getAction() != null ) {
            importOrderDTO.setAction( entity.getAction() );
        }
        if ( entity.getProduct_name() != null ) {
            importOrderDTO.setProduct_name( entity.getProduct_name() );
        }
        if ( entity.getOrderType() != null ) {
            importOrderDTO.setOrderType( entity.getOrderType() );
        }
        importOrderDTO.setQuantity( entity.getQuantity() );
        importOrderDTO.setPrice( entity.getPrice() );
        if ( entity.getOrderDirection() != null ) {
            importOrderDTO.setOrderDirection( entity.getOrderDirection() );
        }
        if ( entity.getTimeStamp() != null ) {
            importOrderDTO.setTimeStamp( entity.getTimeStamp() );
        }
        if ( entity.getOrder_meta() != null ) {
            importOrderDTO.setOrder_meta( entity.getOrder_meta() );
        }
        if ( entity.getBasket_ID() != null ) {
            importOrderDTO.setBasket_ID( entity.getBasket_ID() );
        }
        if ( entity.getBasket_Type() != null ) {
            importOrderDTO.setBasket_Type( entity.getBasket_Type() );
        }
        if ( entity.getBasket_meta() != null ) {
            importOrderDTO.setBasket_meta( entity.getBasket_meta() );
        }
        if ( entity.getUserId() != null ) {
            importOrderDTO.setUserId( entity.getUserId() );
        }
        if ( entity.getStatus() != null ) {
            importOrderDTO.setStatus( entity.getStatus() );
        }
        if ( entity.getMessageInfoId() != null ) {
            importOrderDTO.setMessageInfoId( entity.getMessageInfoId() );
        }
        if ( entity.getMessageInfoName() != null ) {
            importOrderDTO.setMessageInfoName( entity.getMessageInfoName() );
        }
        if ( entity.getSingleOrderId() != null ) {
            importOrderDTO.setSingleOrderId( entity.getSingleOrderId() );
        }
        if ( entity.getMarketCode() != null ) {
            importOrderDTO.setMarketCode( entity.getMarketCode() );
        }

        return importOrderDTO;
    }

    @Override
    public void map(ImportOrderDTO dto, ImportOrder entity) {
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
        if ( dto.getOrder_ID() != null ) {
            entity.setOrder_ID( dto.getOrder_ID() );
        }
        else {
            entity.setOrder_ID( null );
        }
        if ( dto.getAction() != null ) {
            entity.setAction( dto.getAction() );
        }
        else {
            entity.setAction( null );
        }
        if ( dto.getProduct_name() != null ) {
            entity.setProduct_name( dto.getProduct_name() );
        }
        else {
            entity.setProduct_name( null );
        }
        if ( dto.getOrderType() != null ) {
            entity.setOrderType( dto.getOrderType() );
        }
        else {
            entity.setOrderType( null );
        }
        entity.setQuantity( dto.getQuantity() );
        entity.setPrice( dto.getPrice() );
        if ( dto.getOrderDirection() != null ) {
            entity.setOrderDirection( dto.getOrderDirection() );
        }
        else {
            entity.setOrderDirection( null );
        }
        if ( dto.getTimeStamp() != null ) {
            entity.setTimeStamp( dto.getTimeStamp() );
        }
        else {
            entity.setTimeStamp( null );
        }
        if ( dto.getOrder_meta() != null ) {
            entity.setOrder_meta( dto.getOrder_meta() );
        }
        else {
            entity.setOrder_meta( null );
        }
        if ( dto.getBasket_ID() != null ) {
            entity.setBasket_ID( dto.getBasket_ID() );
        }
        else {
            entity.setBasket_ID( null );
        }
        if ( dto.getBasket_Type() != null ) {
            entity.setBasket_Type( dto.getBasket_Type() );
        }
        else {
            entity.setBasket_Type( null );
        }
        if ( dto.getBasket_meta() != null ) {
            entity.setBasket_meta( dto.getBasket_meta() );
        }
        else {
            entity.setBasket_meta( null );
        }
        if ( dto.getUserId() != null ) {
            entity.setUserId( dto.getUserId() );
        }
        else {
            entity.setUserId( null );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        else {
            entity.setStatus( null );
        }
        if ( dto.getMessageInfoId() != null ) {
            entity.setMessageInfoId( dto.getMessageInfoId() );
        }
        else {
            entity.setMessageInfoId( null );
        }
        if ( dto.getMessageInfoName() != null ) {
            entity.setMessageInfoName( dto.getMessageInfoName() );
        }
        else {
            entity.setMessageInfoName( null );
        }
        if ( dto.getSingleOrderId() != null ) {
            entity.setSingleOrderId( dto.getSingleOrderId() );
        }
        else {
            entity.setSingleOrderId( null );
        }
        if ( dto.getMarketCode() != null ) {
            entity.setMarketCode( dto.getMarketCode() );
        }
        else {
            entity.setMarketCode( null );
        }
    }
}
