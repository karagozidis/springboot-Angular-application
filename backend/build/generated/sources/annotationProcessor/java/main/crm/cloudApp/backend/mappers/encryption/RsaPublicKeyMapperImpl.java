package crm.cloudApp.backend.mappers.encryption;

import crm.cloudApp.backend.dto.encryption.RsaPublicKeyDTO;
import crm.cloudApp.backend.models.encryption.RsaPublicKey;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class RsaPublicKeyMapperImpl extends RsaPublicKeyMapper {

    @Override
    public RsaPublicKey map(RsaPublicKeyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        RsaPublicKey rsaPublicKey = new RsaPublicKey();

        if ( dto.getCreatedOn() != null ) {
            rsaPublicKey.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            rsaPublicKey.setId( dto.getId() );
        }
        if ( dto.getStatus() != null ) {
            rsaPublicKey.setStatus( dto.getStatus() );
        }
        if ( dto.getPublicKey() != null ) {
            rsaPublicKey.setPublicKey( dto.getPublicKey() );
        }
        rsaPublicKey.setSize( dto.getSize() );
        if ( dto.getUserId() != null ) {
            rsaPublicKey.setUserId( dto.getUserId() );
        }
        if ( dto.getKeyUUID() != null ) {
            rsaPublicKey.setKeyUUID( dto.getKeyUUID() );
        }

        return rsaPublicKey;
    }

    @Override
    public RsaPublicKeyDTO map(RsaPublicKey entity) {
        if ( entity == null ) {
            return null;
        }

        RsaPublicKeyDTO rsaPublicKeyDTO = new RsaPublicKeyDTO();

        if ( entity.getId() != null ) {
            rsaPublicKeyDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            rsaPublicKeyDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            rsaPublicKeyDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getPublicKey() != null ) {
            rsaPublicKeyDTO.setPublicKey( entity.getPublicKey() );
        }
        rsaPublicKeyDTO.setSize( entity.getSize() );
        if ( entity.getStatus() != null ) {
            rsaPublicKeyDTO.setStatus( entity.getStatus() );
        }
        if ( entity.getUserId() != null ) {
            rsaPublicKeyDTO.setUserId( entity.getUserId() );
        }
        if ( entity.getKeyUUID() != null ) {
            rsaPublicKeyDTO.setKeyUUID( entity.getKeyUUID() );
        }

        return rsaPublicKeyDTO;
    }

    @Override
    public void map(RsaPublicKeyDTO dto, RsaPublicKey entity) {
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
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        else {
            entity.setStatus( null );
        }
        if ( dto.getPublicKey() != null ) {
            entity.setPublicKey( dto.getPublicKey() );
        }
        else {
            entity.setPublicKey( null );
        }
        entity.setSize( dto.getSize() );
        if ( dto.getUserId() != null ) {
            entity.setUserId( dto.getUserId() );
        }
        else {
            entity.setUserId( null );
        }
        if ( dto.getKeyUUID() != null ) {
            entity.setKeyUUID( dto.getKeyUUID() );
        }
        else {
            entity.setKeyUUID( null );
        }
    }
}
