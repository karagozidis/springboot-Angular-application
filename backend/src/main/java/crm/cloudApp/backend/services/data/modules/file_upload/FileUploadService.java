package crm.cloudApp.backend.services.data.modules.file_upload;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.action.NotificationDTO;
import crm.cloudApp.backend.dto.data.MessageInfoDTO;
import crm.cloudApp.backend.mappers.data.MessageInfoMapper;
import crm.cloudApp.backend.models.data.Message;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.data.MessageInfoRepository;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.services.action.NotificationService;
import crm.cloudApp.backend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class FileUploadService {


    private final UserService userService;
    private final MessageInfoRepository messageInfoRepository;
    private final MessageInfoMapper messageInfoMapper;
    private final UserGroupRepository userGroupRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public FileUploadService(UserService userService,
                             MessageInfoRepository messageInfoRepository,
                             MessageInfoMapper messageInfoMapper, UserGroupRepository userGroupRepository, NotificationService notificationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.userService = userService;
        this.messageInfoRepository = messageInfoRepository;
        this.messageInfoMapper = messageInfoMapper;
        this.userGroupRepository = userGroupRepository;
        this.notificationService = notificationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public MessageInfo uploadFiles(MultipartFile multipartFile, String tag, Long userGroupId) throws IOException {

        byte[] data = multipartFile.getInputStream().readAllBytes();
        Message message = new Message();
        message.setEncryptedMessage(data);
        message.setCreatedBy(userService.getLoggedInUserName());
        message.setCreatedOn(Instant.now());
        message.setModifiedBy(userService.getLoggedInUserName());
        message.setModifiedOn(Instant.now());

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTag(tag);
        messageInfo.setName(multipartFile.getOriginalFilename());
        messageInfo.setSender(userService.getLoggedInUser());
        messageInfo.setSenderId(userService.getLoggedInUser().getId());
        messageInfo.setMetadata(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        messageInfo.setMessage(message);
        messageInfo.setCreatedBy(userService.getLoggedInUserName());
        messageInfo.setCreatedOn(Instant.now());
        messageInfo.setModifiedBy(userService.getLoggedInUserName());
        messageInfo.setModifiedOn(Instant.now());
        messageInfo.setUniqueId(UUID.randomUUID().toString());

        Optional<UserGroup> optionalUserGroup = this.userGroupRepository.findById(userGroupId);

        if (optionalUserGroup.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on User Group");
        }
        UserGroup userGroup = optionalUserGroup.get();
        List<Long> usersIds = userGroup.getUsers().stream().map(User::getId).collect(Collectors.toList());
        Boolean userOnUserGroup = usersIds.contains(userService.getLoggedInUser().getId());
        if (!userOnUserGroup) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on User Group");
        }
        messageInfo.setUserGroupId(userGroupId);
        MessageInfo createdMessageInfo = messageInfoRepository.save(messageInfo);


        this.createNotificationsToUserGroup(createdMessageInfo, userGroup);
        this.notifyUsersUsingWebSockets(usersIds);

        return createdMessageInfo;
    }


    private void notifyUsersUsingWebSockets(List<Long> usersIds) {
        for (Long id : usersIds) {
            this.simpMessagingTemplate
                    .convertAndSend("/topic/notifications/" + id + "", "new-notification");
        }
    }

    public void createNotifications(Long messageInfoId) {

        Optional<MessageInfo> optionalMessageInfo = this.messageInfoRepository.findDetailedtById(messageInfoId);
        if (optionalMessageInfo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on message");
        }
        MessageInfo messageInfo = optionalMessageInfo.get();
        if (!messageInfo.getSenderId().equals(this.userService.getLoggedInUser().getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on message");
        }

        Optional<UserGroup> optionalUserGroup = this.userGroupRepository.findById(messageInfo.getUserGroupId());

        if (optionalUserGroup.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on User Group");
        }
        UserGroup userGroup = optionalUserGroup.get();
        List<Long> usersIds = userGroup.getUsers().stream().map(User::getId).collect(Collectors.toList());
        Boolean userOnUserGroup = usersIds.contains(userService.getLoggedInUser().getId());
        if (!userOnUserGroup) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on User Group");
        }

        this.createNotificationsToUserGroup(messageInfo, userGroup);
        this.notifyUsersUsingWebSockets(usersIds);

    }


    private void createNotificationsToUserGroup(MessageInfo messageInfo, UserGroup userGroup) {
        for (User user : userGroup.getUsers()) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setStatus(AppConstants.Types.NotificationStatus.pending);

            notificationDTO.setName(
                    messageInfo.getName() + " File is uploaded!"
            );

            notificationDTO.setDescription(
                    "New File " + messageInfo.getName() + " has been uploaded! " +
                            "<br> File owner is user: " + messageInfo.getSender().getUsername() +
                            "<br> File is uploaded on UserGroup: " + userGroup.getName()
            );

            notificationDTO.setSenderId(messageInfo.getSender().getId());
            notificationDTO.setReceiverId(user.getId());
            notificationDTO.setTimeSent(Instant.now());
            notificationDTO.setType(AppConstants.Types.NotificationType.marketTradeCreation);
            this.notificationService.create(notificationDTO);
        }
    }

    public MessageInfo uploadFiles(MultipartFile multipartFile, String tag) throws IOException {

        byte[] data = multipartFile.getInputStream().readAllBytes();
        Message message = new Message();
        message.setEncryptedMessage(data);
        message.setCreatedBy(userService.getLoggedInUserName());
        message.setCreatedOn(Instant.now());
        message.setModifiedBy(userService.getLoggedInUserName());
        message.setModifiedOn(Instant.now());

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTag(tag);
        messageInfo.setName(multipartFile.getOriginalFilename());
        messageInfo.setSender(userService.getLoggedInUser());
        messageInfo.setSenderId(userService.getLoggedInUser().getId());
        messageInfo.setMetadata(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        messageInfo.setMessage(message);
        messageInfo.setCreatedBy(userService.getLoggedInUserName());
        messageInfo.setCreatedOn(Instant.now());
        messageInfo.setModifiedBy(userService.getLoggedInUserName());
        messageInfo.setModifiedOn(Instant.now());
        messageInfo.setUniqueId(UUID.randomUUID().toString());
        return messageInfoRepository.save(messageInfo);
    }

    public List<MessageInfoDTO> getAll() {
        Collection<UserGroup> userGroups = this.userGroupRepository.findUserGroupsByUserId(userService.getLoggedInUser().getId());
        if (userGroups.size() <= 0) return null;

        List<Long> userGroupIds = userGroups.stream().map(UserGroup::getId).collect(Collectors.toList());

        List<MessageInfo> messageInfos = this.messageInfoRepository
                .findMetaDataBySenderIdOrUserGroupsAndTagLike(userService.getLoggedInUser().getId(), Arrays.asList("file_upload", "encrypted_file_upload"), userGroupIds);
        return messageInfoMapper.mapIngoringMessage(messageInfos);
    }

    public void downloadFile(HttpServletResponse response, Long id) throws IOException {

        MessageInfo messageInfo = this.messageInfoRepository.findByIdWithMessage(id);
        if (messageInfo == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error!");
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + messageInfo.getMetadata());
        response.setStatus(HttpServletResponse.SC_OK);

        OutputStream outputStream = response.getOutputStream();
        outputStream.write(messageInfo.getMessage().getEncryptedMessage());
        outputStream.flush();
    }

    public void delete(Long id) {

        Optional<MessageInfo> optionalMessageInfo = this.messageInfoRepository.findById(id);
        if (optionalMessageInfo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete!!");
        }
        MessageInfo messageInfo = optionalMessageInfo.get();
        if (!messageInfo.getSenderId().equals(this.userService.getLoggedInUser().getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete!!");
        }
        messageInfoRepository.deleteById(id);
    }

    public List<MessageInfoDTO> getByTags(List<String> tags) {
        Collection<UserGroup> userGroups = this.userGroupRepository.findUserGroupsByUserId(userService.getLoggedInUser().getId());

        List<Long> userGroupIds = userGroups.stream().map(UserGroup::getId).collect(Collectors.toList());

        List<MessageInfo> messageInfos = this.messageInfoRepository
                .findMetaDataBySenderIdOrUserGroupsAndTagLike(userService.getLoggedInUser().getId(), tags, userGroupIds);
        return messageInfoMapper.mapIngoringMessage(messageInfos);
    }
}
