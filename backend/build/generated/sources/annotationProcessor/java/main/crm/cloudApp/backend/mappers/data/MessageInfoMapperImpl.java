package crm.cloudApp.backend.mappers.data;

import crm.cloudApp.backend.dto.data.MessageInfoDTO;
import crm.cloudApp.backend.models.data.Message;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.users.UserGroup;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:11+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class MessageInfoMapperImpl extends MessageInfoMapper {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public MessageInfo map(MessageInfoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MessageInfo messageInfo = new MessageInfo();

        if ( dto.getCreatedOn() != null ) {
            messageInfo.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            messageInfo.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            messageInfo.setName( dto.getName() );
        }
        if ( dto.getUniqueId() != null ) {
            messageInfo.setUniqueId( dto.getUniqueId() );
        }
        if ( dto.getSize() != null ) {
            messageInfo.setSize( dto.getSize() );
        }
        if ( dto.getTag() != null ) {
            messageInfo.setTag( dto.getTag() );
        }
        if ( dto.getTimeSent() != null ) {
            messageInfo.setTimeSent( dto.getTimeSent() );
        }
        if ( dto.getUserGroupId() != null ) {
            messageInfo.setUserGroupId( dto.getUserGroupId() );
        }
        messageInfo.setEncryptionVersion( dto.getEncryptionVersion() );
        if ( dto.getSenderId() != null ) {
            messageInfo.setSenderId( dto.getSenderId() );
        }
        if ( dto.getMessage() != null ) {
            messageInfo.setMessage( messageMapper.map( dto.getMessage() ) );
        }
        if ( dto.getMetadata() != null ) {
            messageInfo.setMetadata( dto.getMetadata() );
        }

        return messageInfo;
    }

    @Override
    public MessageInfoDTO map(MessageInfo entity) {
        if ( entity == null ) {
            return null;
        }

        MessageInfoDTO messageInfoDTO = new MessageInfoDTO();

        if ( entity.getId() != null ) {
            messageInfoDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            messageInfoDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            messageInfoDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getName() != null ) {
            messageInfoDTO.setName( entity.getName() );
        }
        if ( entity.getUniqueId() != null ) {
            messageInfoDTO.setUniqueId( entity.getUniqueId() );
        }
        if ( entity.getSize() != null ) {
            messageInfoDTO.setSize( entity.getSize() );
        }
        if ( entity.getTag() != null ) {
            messageInfoDTO.setTag( entity.getTag() );
        }
        if ( entity.getTimeSent() != null ) {
            messageInfoDTO.setTimeSent( entity.getTimeSent() );
        }
        messageInfoDTO.setEncryptionVersion( entity.getEncryptionVersion() );
        if ( entity.getSenderId() != null ) {
            messageInfoDTO.setSenderId( entity.getSenderId() );
        }
        if ( entity.getUserGroupId() != null ) {
            messageInfoDTO.setUserGroupId( entity.getUserGroupId() );
        }
        if ( entity.getMessage() != null ) {
            messageInfoDTO.setMessage( messageMapper.map( entity.getMessage() ) );
        }
        if ( entity.getMetadata() != null ) {
            messageInfoDTO.setMetadata( entity.getMetadata() );
        }

        return messageInfoDTO;
    }

    @Override
    public void map(MessageInfoDTO dto, MessageInfo entity) {
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
        if ( dto.getUniqueId() != null ) {
            entity.setUniqueId( dto.getUniqueId() );
        }
        else {
            entity.setUniqueId( null );
        }
        if ( dto.getSize() != null ) {
            entity.setSize( dto.getSize() );
        }
        else {
            entity.setSize( null );
        }
        if ( dto.getTag() != null ) {
            entity.setTag( dto.getTag() );
        }
        else {
            entity.setTag( null );
        }
        if ( dto.getTimeSent() != null ) {
            entity.setTimeSent( dto.getTimeSent() );
        }
        else {
            entity.setTimeSent( null );
        }
        if ( dto.getUserGroupId() != null ) {
            entity.setUserGroupId( dto.getUserGroupId() );
        }
        else {
            entity.setUserGroupId( null );
        }
        entity.setEncryptionVersion( dto.getEncryptionVersion() );
        if ( dto.getSenderId() != null ) {
            entity.setSenderId( dto.getSenderId() );
        }
        else {
            entity.setSenderId( null );
        }
        if ( dto.getMessage() != null ) {
            if ( entity.getMessage() == null ) {
                entity.setMessage( new Message() );
            }
            messageMapper.map( dto.getMessage(), entity.getMessage() );
        }
        else {
            entity.setMessage( null );
        }
        if ( dto.getMetadata() != null ) {
            entity.setMetadata( dto.getMetadata() );
        }
        else {
            entity.setMetadata( null );
        }
    }

    @Override
    public MessageInfoDTO mapIngoringMessage(MessageInfo entity) {
        if ( entity == null ) {
            return null;
        }

        MessageInfoDTO messageInfoDTO = new MessageInfoDTO();

        if ( entity.getId() != null ) {
            messageInfoDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            messageInfoDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            messageInfoDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getName() != null ) {
            messageInfoDTO.setName( entity.getName() );
        }
        if ( entity.getUniqueId() != null ) {
            messageInfoDTO.setUniqueId( entity.getUniqueId() );
        }
        if ( entity.getSize() != null ) {
            messageInfoDTO.setSize( entity.getSize() );
        }
        if ( entity.getTag() != null ) {
            messageInfoDTO.setTag( entity.getTag() );
        }
        if ( entity.getTimeSent() != null ) {
            messageInfoDTO.setTimeSent( entity.getTimeSent() );
        }
        messageInfoDTO.setEncryptionVersion( entity.getEncryptionVersion() );
        if ( entity.getSenderId() != null ) {
            messageInfoDTO.setSenderId( entity.getSenderId() );
        }
        if ( entity.getUserGroupId() != null ) {
            messageInfoDTO.setUserGroupId( entity.getUserGroupId() );
        }
        if ( entity.getMetadata() != null ) {
            messageInfoDTO.setMetadata( entity.getMetadata() );
        }

        return messageInfoDTO;
    }

    @Override
    public MessageInfoDTO mapWithUserGroupName(MessageInfo entity) {
        if ( entity == null ) {
            return null;
        }

        MessageInfoDTO messageInfoDTO = new MessageInfoDTO();

        String name = entityUserGroupName( entity );
        if ( name != null ) {
            messageInfoDTO.setUserGroupName( name );
        }
        if ( entity.getId() != null ) {
            messageInfoDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            messageInfoDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            messageInfoDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getName() != null ) {
            messageInfoDTO.setName( entity.getName() );
        }
        if ( entity.getUniqueId() != null ) {
            messageInfoDTO.setUniqueId( entity.getUniqueId() );
        }
        if ( entity.getSize() != null ) {
            messageInfoDTO.setSize( entity.getSize() );
        }
        if ( entity.getTag() != null ) {
            messageInfoDTO.setTag( entity.getTag() );
        }
        if ( entity.getTimeSent() != null ) {
            messageInfoDTO.setTimeSent( entity.getTimeSent() );
        }
        messageInfoDTO.setEncryptionVersion( entity.getEncryptionVersion() );
        if ( entity.getSenderId() != null ) {
            messageInfoDTO.setSenderId( entity.getSenderId() );
        }
        if ( entity.getUserGroupId() != null ) {
            messageInfoDTO.setUserGroupId( entity.getUserGroupId() );
        }
        if ( entity.getMetadata() != null ) {
            messageInfoDTO.setMetadata( entity.getMetadata() );
        }

        return messageInfoDTO;
    }

    private String entityUserGroupName(MessageInfo messageInfo) {
        if ( messageInfo == null ) {
            return null;
        }
        UserGroup userGroup = messageInfo.getUserGroup();
        if ( userGroup == null ) {
            return null;
        }
        String name = userGroup.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
