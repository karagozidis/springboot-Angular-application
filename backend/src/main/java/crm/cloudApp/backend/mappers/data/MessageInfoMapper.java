package crm.cloudApp.backend.mappers.data;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.mappers.users.UserGroupMapper;
import crm.cloudApp.backend.dto.data.MessageInfoDTO;
import crm.cloudApp.backend.models.data.MessageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {
    MessageMapper.class, UserGroupMapper.class})
public abstract class MessageInfoMapper extends BaseMapper<MessageInfoDTO, MessageInfo> {


  public List<MessageInfoDTO> mapIngoringMessage(Iterable<MessageInfo> all) {
    return StreamSupport.stream(all.spliterator(), true).map(this::mapWithUserGroupName)
        .collect(Collectors.toList());
  }

  @Mapping(ignore = true, target = "message")
  public abstract MessageInfoDTO mapIngoringMessage(MessageInfo entity);

  public Collection<MessageInfoDTO> mapWithUserGroupName(Iterable<MessageInfo> all) {
    return StreamSupport.stream(all.spliterator(), true).map(this::mapWithUserGroupName)
        .collect(Collectors.toList());
  }


  public Page<MessageInfoDTO> mapWithUserGroupName(Page<MessageInfo> messageInfosPage) {
    List<MessageInfoDTO> messageInfoDTOs = StreamSupport
        .stream(messageInfosPage.getContent().spliterator(),
            true).map(this::mapWithUserGroupName)
        .collect(Collectors.toList());

    return new PageImpl<MessageInfoDTO>(messageInfoDTOs,
        messageInfosPage.getPageable(), messageInfosPage.getTotalElements());
  }

  @Mapping(target = "userGroupName", source = "userGroup.name")
  @Mapping(ignore = true, target = "message")
  public abstract MessageInfoDTO mapWithUserGroupName(MessageInfo entity);

}



