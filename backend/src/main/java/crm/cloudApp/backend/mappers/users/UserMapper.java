package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.models.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

//@Named("userMapper")
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class UserMapper extends BaseMapper<UserDTO, User> {

  @Mapping(ignore = true, target = "password")
  public abstract UserDTO map(User entity);

  @Named("mapBasic")
  @Mapping(ignore = true, target = "password")
  @Mapping(ignore = true, target = "createdOn")
  @Mapping(ignore = true, target = "createdBy")
  @Mapping(ignore = true, target = "status")
 // @Mapping(ignore = true, target = "country")
  @Mapping(ignore = true, target = "rolesCrm")
  @Mapping(ignore = true, target = "modulesCrm")
  @Mapping(ignore = true, target = "rolesMarket")
  @Mapping(ignore = true, target = "modulesMarket")
  public abstract UserDTO mapBasic(User entity);

}
