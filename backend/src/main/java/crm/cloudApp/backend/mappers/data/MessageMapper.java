package crm.cloudApp.backend.mappers.data;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.data.MessageDTO;
import crm.cloudApp.backend.models.data.Message;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class MessageMapper extends BaseMapper<MessageDTO, Message> {

}


