package crm.cloudApp.backend.mappers.encryption;

import crm.cloudApp.backend.dto.encryption.AesKeyDTO;
import crm.cloudApp.backend.models.encryption.AesKey;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class AesKeyMapperImpl extends AesKeyMapper {

    @Override
    public AesKey map(AesKeyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AesKey aesKey = new AesKey();

        if ( dto.getCreatedOn() != null ) {
            aesKey.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            aesKey.setId( dto.getId() );
        }
        if ( dto.getEncryptedKey() != null ) {
            aesKey.setEncryptedKey( dto.getEncryptedKey() );
        }
        aesKey.setSize( dto.getSize() );
        if ( dto.getUserGroupId() != null ) {
            aesKey.setUserGroupId( dto.getUserGroupId() );
        }
        aesKey.setEncryptionVersion( dto.getEncryptionVersion() );
        if ( dto.getRsaPublicKeyId() != null ) {
            aesKey.setRsaPublicKeyId( dto.getRsaPublicKeyId() );
        }
        if ( dto.getKeyUUID() != null ) {
            aesKey.setKeyUUID( dto.getKeyUUID() );
        }

        return aesKey;
    }

    @Override
    public AesKeyDTO map(AesKey entity) {
        if ( entity == null ) {
            return null;
        }

        AesKeyDTO aesKeyDTO = new AesKeyDTO();

        if ( entity.getId() != null ) {
            aesKeyDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            aesKeyDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            aesKeyDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getEncryptedKey() != null ) {
            aesKeyDTO.setEncryptedKey( entity.getEncryptedKey() );
        }
        aesKeyDTO.setSize( entity.getSize() );
        aesKeyDTO.setEncryptionVersion( entity.getEncryptionVersion() );
        if ( entity.getUserGroupId() != null ) {
            aesKeyDTO.setUserGroupId( entity.getUserGroupId() );
        }
        if ( entity.getRsaPublicKeyId() != null ) {
            aesKeyDTO.setRsaPublicKeyId( entity.getRsaPublicKeyId() );
        }
        if ( entity.getKeyUUID() != null ) {
            aesKeyDTO.setKeyUUID( entity.getKeyUUID() );
        }

        return aesKeyDTO;
    }

    @Override
    public void map(AesKeyDTO dto, AesKey entity) {
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
        if ( dto.getEncryptedKey() != null ) {
            entity.setEncryptedKey( dto.getEncryptedKey() );
        }
        else {
            entity.setEncryptedKey( null );
        }
        entity.setSize( dto.getSize() );
        if ( dto.getUserGroupId() != null ) {
            entity.setUserGroupId( dto.getUserGroupId() );
        }
        else {
            entity.setUserGroupId( null );
        }
        entity.setEncryptionVersion( dto.getEncryptionVersion() );
        if ( dto.getRsaPublicKeyId() != null ) {
            entity.setRsaPublicKeyId( dto.getRsaPublicKeyId() );
        }
        else {
            entity.setRsaPublicKeyId( null );
        }
        if ( dto.getKeyUUID() != null ) {
            entity.setKeyUUID( dto.getKeyUUID() );
        }
        else {
            entity.setKeyUUID( null );
        }
    }
}
