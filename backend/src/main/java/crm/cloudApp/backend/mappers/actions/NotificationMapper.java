package crm.cloudApp.backend.mappers.actions;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.action.NotificationDTO;
import crm.cloudApp.backend.models.actions.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class NotificationMapper extends BaseMapper<NotificationDTO, Notification> {

}
