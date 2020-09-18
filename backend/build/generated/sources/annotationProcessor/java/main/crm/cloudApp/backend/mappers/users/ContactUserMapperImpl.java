package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.dto.users.ContactUserDTO;
import crm.cloudApp.backend.models.users.Contact;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:13+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class ContactUserMapperImpl extends ContactUserMapper {

    @Override
    public Contact map(ContactUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Contact contact = new Contact();

        if ( dto.getCreatedOn() != null ) {
            contact.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            contact.setId( dto.getId() );
        }

        return contact;
    }

    @Override
    public ContactUserDTO map(Contact entity) {
        if ( entity == null ) {
            return null;
        }

        ContactUserDTO contactUserDTO = new ContactUserDTO();

        if ( entity.getId() != null ) {
            contactUserDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            contactUserDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            contactUserDTO.setCreatedBy( entity.getCreatedBy() );
        }

        return contactUserDTO;
    }

    @Override
    public void map(ContactUserDTO dto, Contact entity) {
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
    }
}
