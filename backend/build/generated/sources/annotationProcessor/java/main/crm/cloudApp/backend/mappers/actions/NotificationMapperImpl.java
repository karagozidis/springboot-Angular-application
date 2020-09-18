package crm.cloudApp.backend.mappers.actions;

import crm.cloudApp.backend.dto.action.NotificationDTO;
import crm.cloudApp.backend.models.actions.Notification;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class NotificationMapperImpl extends NotificationMapper {

    @Override
    public Notification map(NotificationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Notification notification = new Notification();

        if ( dto.getCreatedOn() != null ) {
            notification.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            notification.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            notification.setName( dto.getName() );
        }
        if ( dto.getDescription() != null ) {
            notification.setDescription( dto.getDescription() );
        }
        if ( dto.getTimeSent() != null ) {
            notification.setTimeSent( dto.getTimeSent() );
        }
        if ( dto.getStatus() != null ) {
            notification.setStatus( dto.getStatus() );
        }
        if ( dto.getSenderId() != null ) {
            notification.setSenderId( dto.getSenderId() );
        }
        if ( dto.getReceiverId() != null ) {
            notification.setReceiverId( dto.getReceiverId() );
        }

        return notification;
    }

    @Override
    public NotificationDTO map(Notification entity) {
        if ( entity == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        if ( entity.getId() != null ) {
            notificationDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            notificationDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            notificationDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getName() != null ) {
            notificationDTO.setName( entity.getName() );
        }
        if ( entity.getDescription() != null ) {
            notificationDTO.setDescription( entity.getDescription() );
        }
        if ( entity.getTimeSent() != null ) {
            notificationDTO.setTimeSent( entity.getTimeSent() );
        }
        if ( entity.getStatus() != null ) {
            notificationDTO.setStatus( entity.getStatus() );
        }
        if ( entity.getSenderId() != null ) {
            notificationDTO.setSenderId( entity.getSenderId() );
        }
        if ( entity.getReceiverId() != null ) {
            notificationDTO.setReceiverId( entity.getReceiverId() );
        }

        return notificationDTO;
    }

    @Override
    public void map(NotificationDTO dto, Notification entity) {
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
        if ( dto.getTimeSent() != null ) {
            entity.setTimeSent( dto.getTimeSent() );
        }
        else {
            entity.setTimeSent( null );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        else {
            entity.setStatus( null );
        }
        if ( dto.getSenderId() != null ) {
            entity.setSenderId( dto.getSenderId() );
        }
        else {
            entity.setSenderId( null );
        }
        if ( dto.getReceiverId() != null ) {
            entity.setReceiverId( dto.getReceiverId() );
        }
        else {
            entity.setReceiverId( null );
        }
    }
}
