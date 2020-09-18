package crm.cloudApp.backend.services.data.modules.active_substation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import crm.cloudApp.backend.dto.data.modules.active_substation.ActiveSubstationForecastMetadataDTO;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.data.MessageInfoRepository;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActiveSubstationForecastsService {

    private final UserGroupRepository userGroupRepository;
    private final UserService userService;
    private final MessageInfoRepository messageInfoRepository;


    public ActiveSubstationForecastsService(UserGroupRepository userGroupRepository,
                                            UserService userService,
                                            MessageInfoRepository messageInfoRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userService = userService;
        this.messageInfoRepository = messageInfoRepository;
    }


    public List<ActiveSubstationForecastMetadataDTO> getByTags(List<String> tags) throws IOException {

        Collection<UserGroup> userGroups = this.userGroupRepository.findUserGroupsByUserId(userService.getLoggedInUser().getId());

        List<Long> userGroupIds = userGroups.stream().map(UserGroup::getId).collect(Collectors.toList());

        List<MessageInfo> messageInfos = this.messageInfoRepository
                .findMetaDataBySenderIdOrUserGroupsAndTagLike(userService.getLoggedInUser().getId(), tags, userGroupIds);

        List<ActiveSubstationForecastMetadataDTO> activeSubstationForecastMetadataDTOs = new ArrayList<>();

        for (MessageInfo messageInfo : messageInfos) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ActiveSubstationForecastMetadataDTO activeSubstationForecastMetadataDTO = mapper
                    .readValue(messageInfo.getMetadata(), ActiveSubstationForecastMetadataDTO.class);
            activeSubstationForecastMetadataDTOs.add(activeSubstationForecastMetadataDTO);
        }

        return activeSubstationForecastMetadataDTOs;
    }


    public List<ActiveSubstationForecastMetadataDTO> getSyncList(Long userGroup) throws IOException {

        List<MessageInfo> messageInfos = this.messageInfoRepository
                .findMetaDataBySenderIdOrUserGroupsAndTagAndMetadataLike(userService.getLoggedInUser().getId(), Arrays.asList("activeSubstationForecast"),Arrays.asList(userGroup), "%\"status\":\"TOCRM\"%");

        List<ActiveSubstationForecastMetadataDTO> activeSubstationForecastMetadataDTOs = new ArrayList<>();

        for (MessageInfo messageInfo : messageInfos) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ActiveSubstationForecastMetadataDTO activeSubstationForecastMetadataDTO = mapper
                    .readValue(messageInfo.getMetadata(), ActiveSubstationForecastMetadataDTO.class);
            activeSubstationForecastMetadataDTOs.add(activeSubstationForecastMetadataDTO);
        }

        return activeSubstationForecastMetadataDTOs;
    }

    public ActiveSubstationForecastMetadataDTO getSyncMetaByUniqueId(String uniqueId) throws IOException {

        MessageInfo messageInfo = this.messageInfoRepository
                .findByUniqueId(uniqueId);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ActiveSubstationForecastMetadataDTO activeSubstationForecastMetadataDTO = mapper
                    .readValue(messageInfo.getMetadata(), ActiveSubstationForecastMetadataDTO.class);

        return activeSubstationForecastMetadataDTO;
    }


    public List<ActiveSubstationForecastMetadataDTO> getAll() throws IOException {
        Collection<UserGroup> userGroups = this.userGroupRepository.findUserGroupsByUserId(userService.getLoggedInUser().getId());

        List<Long> userGroupIds = userGroups.stream().map(UserGroup::getId).collect(Collectors.toList());

        List<MessageInfo> messageInfos = this.messageInfoRepository
                .findMetaDataBySenderIdOrUserGroupsAndTagLike(userService.getLoggedInUser().getId(), Arrays.asList("activeSubstationForecast", "activeSubstationHistory", "activeSubstationEvent"), userGroupIds);

        List<ActiveSubstationForecastMetadataDTO> activeSubstationForecastMetadataDTOs = new ArrayList<>();

        for (MessageInfo messageInfo : messageInfos) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ActiveSubstationForecastMetadataDTO activeSubstationForecastMetadataDTO = mapper
                    .readValue(messageInfo.getMetadata(), ActiveSubstationForecastMetadataDTO.class);
            activeSubstationForecastMetadataDTOs.add(activeSubstationForecastMetadataDTO);
        }

        return activeSubstationForecastMetadataDTOs;
    }


    public void updateMetadataAll(List<ActiveSubstationForecastMetadataDTO> syncList) throws JsonProcessingException {
        for(ActiveSubstationForecastMetadataDTO dto : syncList){
            this.updateMetadata(dto.getUniqueId(),dto);
        }
    }

    public void updateMetadata(String uniqueId, ActiveSubstationForecastMetadataDTO activeSubstationForecastMetadataDTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        activeSubstationForecastMetadataDTO.setStatus("TOLOYOLA");
        this.messageInfoRepository
                .updateMetadataByUuid(uniqueId, mapper.writeValueAsString(activeSubstationForecastMetadataDTO));
    }


    public Map<String, Instant> getDates() throws IOException {

        Collection<UserGroup> userGroups = this.userGroupRepository.findUserGroupsByUserId(userService.getLoggedInUser().getId());

        List<Long> userGroupIds = userGroups.stream().map(UserGroup::getId).collect(Collectors.toList());

        List<MessageInfo> messageInfos = this.messageInfoRepository
                .findMetaDataBySenderIdOrUserGroupsAndTagLike(userService.getLoggedInUser().getId(), Arrays.asList("activeSubstationForecast"), userGroupIds);

        Map<String, Instant> datesForTags = new HashMap<>();
        for (MessageInfo messageInfo : messageInfos) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ActiveSubstationForecastMetadataDTO activeSubstationForecastMetadataDTO = mapper
                    .readValue(messageInfo.getMetadata(), ActiveSubstationForecastMetadataDTO.class);

            if (!datesForTags.containsKey(activeSubstationForecastMetadataDTO.getTag())) {
                datesForTags.put(activeSubstationForecastMetadataDTO.getTag(), activeSubstationForecastMetadataDTO.getFromDate());
            } else {

                if (
                        datesForTags.get(activeSubstationForecastMetadataDTO.getTag()).isBefore(Instant.now()) &&
                                activeSubstationForecastMetadataDTO.getFromDate().isAfter(
                                        datesForTags.get(activeSubstationForecastMetadataDTO.getTag())
                                )) {
                    datesForTags.replace(activeSubstationForecastMetadataDTO.getTag(), activeSubstationForecastMetadataDTO.getFromDate());
                } else if (
                        !datesForTags.get(activeSubstationForecastMetadataDTO.getTag()).isBefore(Instant.now()) &&
                                activeSubstationForecastMetadataDTO.getFromDate().isBefore(
                                        datesForTags.get(activeSubstationForecastMetadataDTO.getTag())) &&
                                !activeSubstationForecastMetadataDTO.getFromDate().isBefore(Instant.now())
                ) {
                    datesForTags.replace(activeSubstationForecastMetadataDTO.getTag(), activeSubstationForecastMetadataDTO.getFromDate());
                }

            }
        }

        for (Map.Entry<String, Instant> entry : datesForTags.entrySet()) {

            if (entry.getValue().isAfter(Instant.now().plus(1, ChronoUnit.DAYS))) {

            }
        }
        return datesForTags;
    }

    public void deleteByUniqueId(String uniqueId) {
        MessageInfo messageInfo = this.messageInfoRepository.findByUniqueId(uniqueId);
        if (messageInfo == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete!!");
        }

        if (!messageInfo.getSenderId().equals(this.userService.getLoggedInUser().getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete!!");
        }
        messageInfoRepository.deleteById(messageInfo.getId());
    }

}
