package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.dto.users.UserGroupDTO;
import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.models.users.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class UserGroupMapper extends BaseMapper<UserGroupDTO, UserGroup> {

  public abstract UserGroup map(UserGroupDTO entity);

}
