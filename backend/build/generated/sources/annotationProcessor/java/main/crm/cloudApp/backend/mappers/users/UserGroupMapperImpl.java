package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.dto.users.UserGroupDTO;
import crm.cloudApp.backend.models.users.Country;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class UserGroupMapperImpl extends UserGroupMapper {

    @Override
    public UserGroupDTO map(UserGroup entity) {
        if ( entity == null ) {
            return null;
        }

        UserGroupDTO userGroupDTO = new UserGroupDTO();

        if ( entity.getId() != null ) {
            userGroupDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            userGroupDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            userGroupDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getName() != null ) {
            userGroupDTO.setName( entity.getName() );
        }
        if ( entity.getDescription() != null ) {
            userGroupDTO.setDescription( entity.getDescription() );
        }
        userGroupDTO.setEncryptionVersion( entity.getEncryptionVersion() );
        List<UserDTO> list = userListToUserDTOList( entity.getUsers() );
        if ( list != null ) {
            userGroupDTO.setUsers( list );
        }

        return userGroupDTO;
    }

    @Override
    public void map(UserGroupDTO dto, UserGroup entity) {
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
        if ( dto.getDescription() != null ) {
            entity.setDescription( dto.getDescription() );
        }
        else {
            entity.setDescription( null );
        }
        entity.setEncryptionVersion( dto.getEncryptionVersion() );
        if ( entity.getUsers() != null ) {
            List<User> list = userDTOListToUserList( dto.getUsers() );
            if ( list != null ) {
                entity.getUsers().clear();
                entity.getUsers().addAll( list );
            }
        }
        else {
            List<User> list = userDTOListToUserList( dto.getUsers() );
            if ( list != null ) {
                entity.setUsers( list );
            }
        }
    }

    @Override
    public UserGroup map(UserGroupDTO entity) {
        if ( entity == null ) {
            return null;
        }

        UserGroup userGroup = new UserGroup();

        if ( entity.getCreatedOn() != null ) {
            userGroup.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            userGroup.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getId() != null ) {
            userGroup.setId( entity.getId() );
        }
        if ( entity.getName() != null ) {
            userGroup.setName( entity.getName() );
        }
        if ( entity.getDescription() != null ) {
            userGroup.setDescription( entity.getDescription() );
        }
        userGroup.setEncryptionVersion( entity.getEncryptionVersion() );
        List<User> list = userDTOListToUserList( entity.getUsers() );
        if ( list != null ) {
            userGroup.setUsers( list );
        }

        return userGroup;
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

    protected List<UserDTO> userListToUserDTOList(List<User> list) {
        if ( list == null ) {
            return null;
        }

        List<UserDTO> list1 = new ArrayList<UserDTO>( list.size() );
        for ( User user : list ) {
            list1.add( userToUserDTO( user ) );
        }

        return list1;
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

    protected List<User> userDTOListToUserList(List<UserDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<User> list1 = new ArrayList<User>( list.size() );
        for ( UserDTO userDTO : list ) {
            list1.add( userDTOToUser( userDTO ) );
        }

        return list1;
    }
}
