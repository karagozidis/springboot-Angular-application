package crm.cloudApp.backend.services.data;

import crm.cloudApp.backend.dto.data.MessageInfoDTO;
import crm.cloudApp.backend.mappers.data.MessageInfoMapper;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.repositories.data.MessageInfoRepository;
import crm.cloudApp.backend.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class MessageInfoService {

    @Autowired
    private final MessageInfoRepository messageInfoRepository;
    private final MessageInfoMapper messageInfoMapper;
    private final UserService userService;
    private static final String UNABLE_TO_DELETE_MESSAGE = "Unable to delete!!";

    @Autowired
    public MessageInfoService(MessageInfoRepository messageInfoRepository,
                              UserService userService,
                              MessageInfoMapper messageInfoMapper
    ) {
        this.messageInfoRepository = messageInfoRepository;
        this.userService = userService;
        this.messageInfoMapper = messageInfoMapper;
    }

    public Collection<MessageInfoDTO> getAllMessageInfos() {
        User user = userService.getLoggedInUser();
        Collection<MessageInfo> messageInfos = messageInfoRepository
                .findMessageInfosByUserId(user.getId());
        return messageInfoMapper.mapWithUserGroupName(messageInfos);
    }

    public Page<MessageInfoDTO> getMessageInfosPage(
            String name,
            String tag,
            Instant dateFrom,
            Instant dateTo,
            Pageable pageable) {

        User user = userService.getLoggedInUser();

        name = "%" + name + "%";
        tag = "%" + tag + "%";

        Page<MessageInfo> messageInfos = messageInfoRepository
                .findMessageInfosPageByUserId(
                        name,
                        tag,
                        dateFrom,
                        dateTo,
                        user.getId(),
                        pageable);
        return messageInfoMapper.mapWithUserGroupName(messageInfos);
    }


    public MessageInfoDTO createUniqueMessageInfoForTagAndUserGroup(MessageInfoDTO messageInfoDTO) {
        List<MessageInfoDTO> dtos = this.getMessageInfosByUserGroupIdTagAndUniqueId(messageInfoDTO.getUserGroupId(), messageInfoDTO.getTag(), "");
        if (dtos.size() == 0) {
            return this.create(messageInfoDTO);
        } else if (dtos.size() == 1) {
            this.delete(dtos.get(0).getId());
            return this.create(messageInfoDTO);
        }
        return null;
    }

    public MessageInfoDTO create(MessageInfoDTO messageInfoDTO) {

        MessageInfo messageInfo = messageInfoMapper.map(messageInfoDTO);
        messageInfo.getMessage().setCreatedBy(userService.getLoggedInUserName());
        messageInfo.getMessage().setCreatedOn(Instant.now());
        messageInfo.getMessage().setModifiedBy(userService.getLoggedInUserName());
        messageInfo.getMessage().setModifiedOn(Instant.now());

        messageInfo.setCreatedBy(userService.getLoggedInUserName());
        messageInfo.setCreatedOn(Instant.now());
        messageInfo.setModifiedBy(userService.getLoggedInUserName());
        messageInfo.setModifiedOn(Instant.now());
        messageInfo.setSenderId(userService.getLoggedInUser().getId());

        MessageInfo createdMessageInfo = messageInfoRepository.save(messageInfo);
        return messageInfoMapper.map(createdMessageInfo);
    }


    public MessageInfoDTO getMessageInfo(Long id) {
        Optional<MessageInfo> messageInfoOptional = messageInfoRepository.findById(id);
        if (messageInfoOptional.isPresent()) {
            return messageInfoMapper.map(messageInfoOptional.get());
        } else {
            return null;
        }
    }

    public MessageInfoDTO update(MessageInfoDTO messageInfoDTO) {

        Optional<MessageInfo> messageInfoOptional = messageInfoRepository
                .findById(messageInfoDTO.getId());
        if (messageInfoOptional.isPresent()) {
            MessageInfo messageInfo = messageInfoOptional.get();
            messageInfo.setName(messageInfoDTO.getName());
            messageInfo.setSize(messageInfoDTO.getSize());
            messageInfo.setTag(messageInfoDTO.getTag());
            messageInfo.setSenderId(messageInfoDTO.getSenderId());
            messageInfo.setUserGroupId(messageInfoDTO.getUserGroupId());
            messageInfo.setModifiedBy(userService.getLoggedInUserName());
            messageInfo.setModifiedOn(Instant.now());

            MessageInfo savedMessageInfo = messageInfoRepository.save(messageInfo);
            return messageInfoMapper.map(savedMessageInfo);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No MessageInfo with this id!!");
        }
    }

    public MessageInfoDTO getMessageInfosByUniqueId(String uniqueId) {
        MessageInfo messageInfo = messageInfoRepository.findByUniqueId(uniqueId);
        return messageInfoMapper.map(messageInfo);
    }

    public MessageInfoDTO getMessageInfosByMetadata(String metadata) {
        MessageInfo messageInfo = messageInfoRepository.findByMetadata(metadata);
        return messageInfoMapper.map(messageInfo);
    }

    public MessageInfoDTO getMessageInfosByMetadataAndTag(String metadata,String tag) {
        MessageInfo messageInfo = messageInfoRepository.findByMetadataAndTag(metadata, tag);
        return messageInfoMapper.map(messageInfo);
    }


    public List<MessageInfoDTO> getMessageInfosByUserGroupId(Long userGroupId) {
        List<MessageInfo> messageInfos = messageInfoRepository.findByUserGroupIdLike(userGroupId);
        return messageInfoMapper.map(messageInfos);
    }

    public List<MessageInfoDTO> getMessageInfosByUserGroupIdTagAndUniqueId(Long userGroupId,
                                                                           String tag, String uniqueId) {
        List<MessageInfo> messageInfos;
        if (uniqueId.equals("")) {
            messageInfos = messageInfoRepository.findByUserGroupIdAndTagLike(userGroupId, tag);
        } else {
            messageInfos = messageInfoRepository
                    .findByUserGroupIdAndTagAndAndUniqueIdLike(userGroupId, tag, uniqueId);
        }

        return messageInfoMapper.map(messageInfos);
    }

    public List<MessageInfoDTO> getMessageInfos(Long userGroupId, String tag, String uniqueId,
                                                String metadata) {

        if (tag.equals("")) {
            tag = "%";
        }
        if (uniqueId.equals("")) {
            uniqueId = "%";
        }
        if (metadata.equals("")) {
            metadata = "%";
        }

        List<MessageInfo> messageInfos = messageInfoRepository
                .findByFieldsLike(userGroupId, tag, uniqueId, metadata);
        return messageInfoMapper.map(messageInfos);
    }

    public boolean delete(Long id) {
        User user = userService.getLoggedInUser();
        List<MessageInfo> messageInfos = messageInfoRepository
                .findMessageInfosByIdAndUserId(id, user.getId());
        if (messageInfos.size() != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    UNABLE_TO_DELETE_MESSAGE);
        }

        try {
            messageInfoRepository.deleteById(messageInfos.get(0).getId());
            return true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    UNABLE_TO_DELETE_MESSAGE);
        }
    }

    public boolean deleteByUniqueId(String uniqueId) {
        User user = userService.getLoggedInUser();
        List<MessageInfo> messageInfos = messageInfoRepository
                .findMessageInfosByUniqueIdAndUserId(uniqueId, user.getId());

        if (messageInfos.size() > 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    UNABLE_TO_DELETE_MESSAGE);
        }
        if (messageInfos.isEmpty()) {
            return false;
        }

        try {
            messageInfoRepository.deleteById(messageInfos.get(0).getId());
            return true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    UNABLE_TO_DELETE_MESSAGE);
        }
    }


}
