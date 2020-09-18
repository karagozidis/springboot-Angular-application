package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.dto.users.UserWithKeyDTO;
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
public class UserWithKeyMapperImpl extends UserWithKeyMapper {

    @Override
    public User map(UserWithKeyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        if ( dto.getCreatedOn() != null ) {
            user.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            user.setId( dto.getId() );
        }
        if ( dto.getUsername() != null ) {
            user.setUsername( dto.getUsername() );
        }
        if ( dto.getEmail() != null ) {
            user.setEmail( dto.getEmail() );
        }
        if ( dto.getPassword() != null ) {
            user.setPassword( dto.getPassword() );
        }
        if ( dto.getStatus() != null ) {
            user.setStatus( dto.getStatus() );
        }
        if ( dto.getCountry() != null ) {
            user.setCountry( countryDTOToCountry( dto.getCountry() ) );
        }
        if ( dto.getRolesCrm() != null ) {
            user.setRolesCrm( dto.getRolesCrm() );
        }
        if ( dto.getModulesCrm() != null ) {
            user.setModulesCrm( dto.getModulesCrm() );
        }
        if ( dto.getRolesMarket() != null ) {
            user.setRolesMarket( dto.getRolesMarket() );
        }
        if ( dto.getModulesMarket() != null ) {
            user.setModulesMarket( dto.getModulesMarket() );
        }
        if ( dto.getDefaultMarket() != null ) {
            user.setDefaultMarket( dto.getDefaultMarket() );
        }
        if ( dto.getMarket() != null ) {
            user.setMarket( dto.getMarket() );
        }
        if ( dto.getLandingPage() != null ) {
            user.setLandingPage( dto.getLandingPage() );
        }
        if ( dto.getActiveSubstationUserGroupId() != null ) {
            user.setActiveSubstationUserGroupId( dto.getActiveSubstationUserGroupId() );
        }

        return user;
    }

    @Override
    public void map(UserWithKeyDTO dto, User entity) {
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
        if ( dto.getUsername() != null ) {
            entity.setUsername( dto.getUsername() );
        }
        else {
            entity.setUsername( null );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        else {
            entity.setEmail( null );
        }
        if ( dto.getPassword() != null ) {
            entity.setPassword( dto.getPassword() );
        }
        else {
            entity.setPassword( null );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        else {
            entity.setStatus( null );
        }
        if ( dto.getCountry() != null ) {
            if ( entity.getCountry() == null ) {
                entity.setCountry( new Country() );
            }
            countryDTOToCountry1( dto.getCountry(), entity.getCountry() );
        }
        else {
            entity.setCountry( null );
        }
        if ( dto.getRolesCrm() != null ) {
            entity.setRolesCrm( dto.getRolesCrm() );
        }
        else {
            entity.setRolesCrm( null );
        }
        if ( dto.getModulesCrm() != null ) {
            entity.setModulesCrm( dto.getModulesCrm() );
        }
        else {
            entity.setModulesCrm( null );
        }
        if ( dto.getRolesMarket() != null ) {
            entity.setRolesMarket( dto.getRolesMarket() );
        }
        else {
            entity.setRolesMarket( null );
        }
        if ( dto.getModulesMarket() != null ) {
            entity.setModulesMarket( dto.getModulesMarket() );
        }
        else {
            entity.setModulesMarket( null );
        }
        if ( dto.getDefaultMarket() != null ) {
            entity.setDefaultMarket( dto.getDefaultMarket() );
        }
        else {
            entity.setDefaultMarket( null );
        }
        if ( dto.getMarket() != null ) {
            entity.setMarket( dto.getMarket() );
        }
        else {
            entity.setMarket( null );
        }
        if ( dto.getLandingPage() != null ) {
            entity.setLandingPage( dto.getLandingPage() );
        }
        else {
            entity.setLandingPage( null );
        }
        if ( dto.getActiveSubstationUserGroupId() != null ) {
            entity.setActiveSubstationUserGroupId( dto.getActiveSubstationUserGroupId() );
        }
        else {
            entity.setActiveSubstationUserGroupId( null );
        }
    }

    @Override
    public UserWithKeyDTO map(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserWithKeyDTO userWithKeyDTO = new UserWithKeyDTO();

        if ( entity.getId() != null ) {
            userWithKeyDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            userWithKeyDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            userWithKeyDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getUsername() != null ) {
            userWithKeyDTO.setUsername( entity.getUsername() );
        }
        if ( entity.getEmail() != null ) {
            userWithKeyDTO.setEmail( entity.getEmail() );
        }
        if ( entity.getPassword() != null ) {
            userWithKeyDTO.setPassword( entity.getPassword() );
        }
        if ( entity.getStatus() != null ) {
            userWithKeyDTO.setStatus( entity.getStatus() );
        }
        if ( entity.getCountry() != null ) {
            userWithKeyDTO.setCountry( countryToCountryDTO( entity.getCountry() ) );
        }
        if ( entity.getRolesCrm() != null ) {
            userWithKeyDTO.setRolesCrm( entity.getRolesCrm() );
        }
        if ( entity.getModulesCrm() != null ) {
            userWithKeyDTO.setModulesCrm( entity.getModulesCrm() );
        }
        if ( entity.getRolesMarket() != null ) {
            userWithKeyDTO.setRolesMarket( entity.getRolesMarket() );
        }
        if ( entity.getModulesMarket() != null ) {
            userWithKeyDTO.setModulesMarket( entity.getModulesMarket() );
        }
        if ( entity.getDefaultMarket() != null ) {
            userWithKeyDTO.setDefaultMarket( entity.getDefaultMarket() );
        }
        if ( entity.getMarket() != null ) {
            userWithKeyDTO.setMarket( entity.getMarket() );
        }
        if ( entity.getLandingPage() != null ) {
            userWithKeyDTO.setLandingPage( entity.getLandingPage() );
        }
        if ( entity.getActiveSubstationUserGroupId() != null ) {
            userWithKeyDTO.setActiveSubstationUserGroupId( entity.getActiveSubstationUserGroupId() );
        }

        userWithKeyDTO.setHasEnabledKey( entity.rsaPublicKeys != null && !entity.rsaPublicKeys.isEmpty() ? true : false );

        return userWithKeyDTO;
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
}
