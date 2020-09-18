package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.dto.users.ContactDTO;
import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.models.users.Contact;
import crm.cloudApp.backend.models.users.Country;
import crm.cloudApp.backend.models.users.User;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:13+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class ContactMapperImpl extends ContactMapper {

    @Override
    public Contact map(ContactDTO dto) {
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
        if ( dto.getStatus() != null ) {
            contact.setStatus( dto.getStatus() );
        }
        if ( dto.getSender() != null ) {
            contact.setSender( userDTOToUser( dto.getSender() ) );
        }
        if ( dto.getReceiver() != null ) {
            contact.setReceiver( userDTOToUser( dto.getReceiver() ) );
        }

        return contact;
    }

    @Override
    public ContactDTO map(Contact entity) {
        if ( entity == null ) {
            return null;
        }

        ContactDTO contactDTO = new ContactDTO();

        if ( entity.getId() != null ) {
            contactDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            contactDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            contactDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getStatus() != null ) {
            contactDTO.setStatus( entity.getStatus() );
        }
        if ( entity.getSender() != null ) {
            contactDTO.setSender( userToUserDTO( entity.getSender() ) );
        }
        if ( entity.getReceiver() != null ) {
            contactDTO.setReceiver( userToUserDTO( entity.getReceiver() ) );
        }

        return contactDTO;
    }

    @Override
    public void map(ContactDTO dto, Contact entity) {
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
        if ( dto.getSender() != null ) {
            if ( entity.getSender() == null ) {
                entity.setSender( new User() );
            }
            userDTOToUser1( dto.getSender(), entity.getSender() );
        }
        else {
            entity.setSender( null );
        }
        if ( dto.getReceiver() != null ) {
            if ( entity.getReceiver() == null ) {
                entity.setReceiver( new User() );
            }
            userDTOToUser1( dto.getReceiver(), entity.getReceiver() );
        }
        else {
            entity.setReceiver( null );
        }
    }

    protected Country countryDTOToCountry(CountryDTO countryDTO) {
        if ( countryDTO == null ) {
            return null;
        }

        Country country = new Country();

        if ( countryDTO.getCreatedOn() != null ) {
            country.setCreatedOn( countryDTO.getCreatedOn() );
        }
        if ( countryDTO.getCreatedBy() != null ) {
            country.setCreatedBy( countryDTO.getCreatedBy() );
        }
        if ( countryDTO.getId() != null ) {
            country.setId( countryDTO.getId() );
        }
        if ( countryDTO.getName() != null ) {
            country.setName( countryDTO.getName() );
        }

        return country;
    }

    protected User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        if ( userDTO.getCreatedOn() != null ) {
            user.setCreatedOn( userDTO.getCreatedOn() );
        }
        if ( userDTO.getCreatedBy() != null ) {
            user.setCreatedBy( userDTO.getCreatedBy() );
        }
        if ( userDTO.getId() != null ) {
            user.setId( userDTO.getId() );
        }
        if ( userDTO.getUsername() != null ) {
            user.setUsername( userDTO.getUsername() );
        }
        if ( userDTO.getEmail() != null ) {
            user.setEmail( userDTO.getEmail() );
        }
        if ( userDTO.getPassword() != null ) {
            user.setPassword( userDTO.getPassword() );
        }
        if ( userDTO.getStatus() != null ) {
            user.setStatus( userDTO.getStatus() );
        }
        if ( userDTO.getCountry() != null ) {
            user.setCountry( countryDTOToCountry( userDTO.getCountry() ) );
        }
        if ( userDTO.getRolesCrm() != null ) {
            user.setRolesCrm( userDTO.getRolesCrm() );
        }
        if ( userDTO.getModulesCrm() != null ) {
            user.setModulesCrm( userDTO.getModulesCrm() );
        }
        if ( userDTO.getRolesMarket() != null ) {
            user.setRolesMarket( userDTO.getRolesMarket() );
        }
        if ( userDTO.getModulesMarket() != null ) {
            user.setModulesMarket( userDTO.getModulesMarket() );
        }
        if ( userDTO.getDefaultMarket() != null ) {
            user.setDefaultMarket( userDTO.getDefaultMarket() );
        }
        if ( userDTO.getMarket() != null ) {
            user.setMarket( userDTO.getMarket() );
        }
        if ( userDTO.getLandingPage() != null ) {
            user.setLandingPage( userDTO.getLandingPage() );
        }
        if ( userDTO.getActiveSubstationUserGroupId() != null ) {
            user.setActiveSubstationUserGroupId( userDTO.getActiveSubstationUserGroupId() );
        }

        return user;
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

    protected void countryDTOToCountry1(CountryDTO countryDTO, Country mappingTarget) {
        if ( countryDTO == null ) {
            return;
        }

        if ( countryDTO.getCreatedOn() != null ) {
            mappingTarget.setCreatedOn( countryDTO.getCreatedOn() );
        }
        else {
            mappingTarget.setCreatedOn( null );
        }
        if ( countryDTO.getCreatedBy() != null ) {
            mappingTarget.setCreatedBy( countryDTO.getCreatedBy() );
        }
        else {
            mappingTarget.setCreatedBy( null );
        }
        if ( countryDTO.getId() != null ) {
            mappingTarget.setId( countryDTO.getId() );
        }
        else {
            mappingTarget.setId( null );
        }
        if ( countryDTO.getName() != null ) {
            mappingTarget.setName( countryDTO.getName() );
        }
        else {
            mappingTarget.setName( null );
        }
    }

    protected void userDTOToUser1(UserDTO userDTO, User mappingTarget) {
        if ( userDTO == null ) {
            return;
        }

        if ( userDTO.getCreatedOn() != null ) {
            mappingTarget.setCreatedOn( userDTO.getCreatedOn() );
        }
        else {
            mappingTarget.setCreatedOn( null );
        }
        if ( userDTO.getCreatedBy() != null ) {
            mappingTarget.setCreatedBy( userDTO.getCreatedBy() );
        }
        else {
            mappingTarget.setCreatedBy( null );
        }
        if ( userDTO.getId() != null ) {
            mappingTarget.setId( userDTO.getId() );
        }
        else {
            mappingTarget.setId( null );
        }
        if ( userDTO.getUsername() != null ) {
            mappingTarget.setUsername( userDTO.getUsername() );
        }
        else {
            mappingTarget.setUsername( null );
        }
        if ( userDTO.getEmail() != null ) {
            mappingTarget.setEmail( userDTO.getEmail() );
        }
        else {
            mappingTarget.setEmail( null );
        }
        if ( userDTO.getPassword() != null ) {
            mappingTarget.setPassword( userDTO.getPassword() );
        }
        else {
            mappingTarget.setPassword( null );
        }
        if ( userDTO.getStatus() != null ) {
            mappingTarget.setStatus( userDTO.getStatus() );
        }
        else {
            mappingTarget.setStatus( null );
        }
        if ( userDTO.getCountry() != null ) {
            if ( mappingTarget.getCountry() == null ) {
                mappingTarget.setCountry( new Country() );
            }
            countryDTOToCountry1( userDTO.getCountry(), mappingTarget.getCountry() );
        }
        else {
            mappingTarget.setCountry( null );
        }
        if ( userDTO.getRolesCrm() != null ) {
            mappingTarget.setRolesCrm( userDTO.getRolesCrm() );
        }
        else {
            mappingTarget.setRolesCrm( null );
        }
        if ( userDTO.getModulesCrm() != null ) {
            mappingTarget.setModulesCrm( userDTO.getModulesCrm() );
        }
        else {
            mappingTarget.setModulesCrm( null );
        }
        if ( userDTO.getRolesMarket() != null ) {
            mappingTarget.setRolesMarket( userDTO.getRolesMarket() );
        }
        else {
            mappingTarget.setRolesMarket( null );
        }
        if ( userDTO.getModulesMarket() != null ) {
            mappingTarget.setModulesMarket( userDTO.getModulesMarket() );
        }
        else {
            mappingTarget.setModulesMarket( null );
        }
        if ( userDTO.getDefaultMarket() != null ) {
            mappingTarget.setDefaultMarket( userDTO.getDefaultMarket() );
        }
        else {
            mappingTarget.setDefaultMarket( null );
        }
        if ( userDTO.getMarket() != null ) {
            mappingTarget.setMarket( userDTO.getMarket() );
        }
        else {
            mappingTarget.setMarket( null );
        }
        if ( userDTO.getLandingPage() != null ) {
            mappingTarget.setLandingPage( userDTO.getLandingPage() );
        }
        else {
            mappingTarget.setLandingPage( null );
        }
        if ( userDTO.getActiveSubstationUserGroupId() != null ) {
            mappingTarget.setActiveSubstationUserGroupId( userDTO.getActiveSubstationUserGroupId() );
        }
        else {
            mappingTarget.setActiveSubstationUserGroupId( null );
        }
    }
}
