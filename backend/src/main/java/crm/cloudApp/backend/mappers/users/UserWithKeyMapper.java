package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.users.UserWithKeyDTO;
import crm.cloudApp.backend.models.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class UserWithKeyMapper extends BaseMapper<UserWithKeyDTO, User> {

  @Override
  @Mapping(expression = "java(entity.rsaPublicKeys != null && !entity.rsaPublicKeys.isEmpty() ? true : false)", target = "hasEnabledKey")
  public abstract UserWithKeyDTO map(User entity);

}
