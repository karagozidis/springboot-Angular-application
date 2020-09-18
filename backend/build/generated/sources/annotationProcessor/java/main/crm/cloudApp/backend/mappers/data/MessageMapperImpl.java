package crm.cloudApp.backend.mappers.data;

import crm.cloudApp.backend.dto.data.MessageDTO;
import crm.cloudApp.backend.models.data.Message;

import java.util.Arrays;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class MessageMapperImpl extends MessageMapper {

    @Override
    public Message map(MessageDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Message message = new Message();

        if ( dto.getCreatedOn() != null ) {
            message.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            message.setId( dto.getId() );
        }
        byte[] encryptedMessage = dto.getEncryptedMessage();
        if ( encryptedMessage != null ) {
            message.setEncryptedMessage( Arrays.copyOf( encryptedMessage, encryptedMessage.length ) );
        }

        return message;
    }

    @Override
    public MessageDTO map(Message entity) {
        if ( entity == null ) {
            return null;
        }

        MessageDTO messageDTO = new MessageDTO();

        if ( entity.getId() != null ) {
            messageDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            messageDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            messageDTO.setCreatedBy( entity.getCreatedBy() );
        }
        byte[] encryptedMessage = entity.getEncryptedMessage();
        if ( encryptedMessage != null ) {
            messageDTO.setEncryptedMessage( Arrays.copyOf( encryptedMessage, encryptedMessage.length ) );
        }

        return messageDTO;
    }

    @Override
    public void map(MessageDTO dto, Message entity) {
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
        byte[] encryptedMessage = dto.getEncryptedMessage();
        if ( encryptedMessage != null ) {
            entity.setEncryptedMessage( Arrays.copyOf( encryptedMessage, encryptedMessage.length ) );
        }
        else {
            entity.setEncryptedMessage( null );
        }
    }
}
