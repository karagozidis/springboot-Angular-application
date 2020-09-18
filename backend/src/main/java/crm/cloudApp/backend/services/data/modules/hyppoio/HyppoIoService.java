package crm.cloudApp.backend.services.data.modules.hyppoio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import crm.cloudApp.backend.dto.data.modules.hyppoio.HyppoIoMetaDataDTO;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.data.MessageInfoRepository;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class HyppoIoService {

  private final MessageInfoRepository messageInfoRepository;
  private final UserGroupRepository userGroupRepository;

  private final UserService userService;

  public HyppoIoService(MessageInfoRepository messageInfoRepository,
      UserGroupRepository userGroupRepository,
      UserService userService) {
    this.messageInfoRepository = messageInfoRepository;
    this.userGroupRepository = userGroupRepository;
    this.userService = userService;
  }

  public List<HyppoIoMetaDataDTO> getHistory() throws IOException {
    Long senderId = userService.getLoggedInUser().getId();
    List<HyppoIoMetaDataDTO> hyppoIoMetaDataList = new ArrayList<>();

    Collection<UserGroup> userGroups = userGroupRepository.findUserGroupsByUserId(senderId);
    List<Long> ids = userGroups.stream().map(UserGroup::getId).collect(Collectors.toList());

    if (ids.isEmpty()) {
      return hyppoIoMetaDataList;
    }

    List<MessageInfo> messageInfos = messageInfoRepository
        .findByUserGroupIdsAndTagLike(ids, "hippoio-results");

    for (MessageInfo messageInfo : messageInfos) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());

      HyppoIoMetaDataDTO hyppoIoMetaData = mapper
          .readValue(messageInfo.getMetadata(), HyppoIoMetaDataDTO.class);
      hyppoIoMetaData.setUserGroupName(messageInfo.getUserGroup().getName());
      hyppoIoMetaData.setSenderUsername(messageInfo.getSender().getUsername());

      hyppoIoMetaDataList.add(hyppoIoMetaData);
    }

    return hyppoIoMetaDataList;

  }

}
