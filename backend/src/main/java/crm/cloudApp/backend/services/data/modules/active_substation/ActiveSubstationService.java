package crm.cloudApp.backend.services.data.modules.active_substation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.active_substation.ActiveSubstationMetadataDTO;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.repositories.data.MessageInfoRepository;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActiveSubstationService {

    private final MessageInfoRepository messageInfoRepository;
    private final UserGroupRepository userGroupRepository;

    private final UserService userService;

    public ActiveSubstationService(MessageInfoRepository messageInfoRepository,
                                   UserGroupRepository userGroupRepository,
                                   UserService userService) {
        this.messageInfoRepository = messageInfoRepository;
        this.userGroupRepository = userGroupRepository;
        this.userService = userService;
    }


    public String getExecutionNextMetadata(Long userGroup) throws IOException {
        List<ActiveSubstationMetadataDTO> activeSubstationMetadataDTOs = this.getMetadata(userGroup);
        activeSubstationMetadataDTOs = activeSubstationMetadataDTOs.stream().filter(x -> x.getStatus().equals(AppConstants.Types.ActiveSubstationExecutionStatus.NOT_EXECUTED)).collect(Collectors.toList());
        if (activeSubstationMetadataDTOs.size() == 0) return "";
        else return activeSubstationMetadataDTOs.get(0).getUuid();
    }

    public List<ActiveSubstationMetadataDTO> getExecutionListMetadata(Long userGroup) throws IOException {
        List<ActiveSubstationMetadataDTO> activeSubstationMetadataDTOs = this.getMetadata(userGroup);
        activeSubstationMetadataDTOs = activeSubstationMetadataDTOs.stream().filter(x -> x.getStatus().equals(AppConstants.Types.ActiveSubstationExecutionStatus.NOT_EXECUTED)).collect(Collectors.toList());
        return activeSubstationMetadataDTOs;
    }

    public int getExecutionStatus(Long userGroup, Map<String, String> headers) throws IOException {
        List<ActiveSubstationMetadataDTO> dtos = this.getMetadata(userGroup);
        ActiveSubstationMetadataDTO todayDTO = null;

        for (ActiveSubstationMetadataDTO dto : dtos) {
            Instant instant1 = dto.getCreationDate()
                    .truncatedTo(ChronoUnit.DAYS);
            Instant instant2 = Instant.now().truncatedTo(ChronoUnit.DAYS);
            if (instant1.equals(instant2)) {
                todayDTO = dto;
                break;
            }
        }

        if (todayDTO != null) {
            if (todayDTO.getStatus().toString().equals("OFFER_ACCEPTED") ||
                    todayDTO.getStatus().toString().equals("OFFER_REJECTED")) {
                return 4;
            } else if (
                    todayDTO.getStatus().toString().equals("OFFER_SENT")) {
                return 3;
            }
        }

        if (checkCacheExistanceToday(userGroup)) {
            return 2;
        }

        return 1;
    }

    public List<ActiveSubstationMetadataDTO> getMetadata(Long userGroup) throws IOException {

        List<ActiveSubstationMetadataDTO> activeSubstationMetadataDTOs = new ArrayList<>();

        List<MessageInfo> messageInfos = messageInfoRepository
                .findByUserGroupIdsAndTagLike(Arrays.asList(userGroup), "active-substation");

        for (MessageInfo messageInfo : messageInfos) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.enableDefaultTyping();

            ActiveSubstationMetadataDTO activeSubstationMetadataDTO = mapper
                    .readValue(messageInfo.getMetadata(), ActiveSubstationMetadataDTO.class);
            activeSubstationMetadataDTOs.add(activeSubstationMetadataDTO);
        }

        return activeSubstationMetadataDTOs;
    }

    public Boolean checkCacheExistanceToday(Long userGroup) throws IOException {

        List<ActiveSubstationMetadataDTO> activeSubstationMetadataDTOs = new ArrayList<>();

        List<MessageInfo> messageInfos = messageInfoRepository
                .findByUserGroupIdsAndTagLike(Arrays.asList(userGroup), "active-substation-cache");

        for (MessageInfo messageInfo : messageInfos) {

            Instant instant1 = messageInfo.getCreatedOn().truncatedTo(ChronoUnit.DAYS);
            Instant instant2 = Instant.now().truncatedTo(ChronoUnit.DAYS);
            if (instant1.equals(instant2)) {
                return true;
            }


        }

        return false;
    }


    @Transactional
    public void updateStatus(String id, String status) throws IOException {

        MessageInfo messageInfo = messageInfoRepository.findByUniqueId(id);
        if (messageInfo != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ActiveSubstationMetadataDTO activeSubstationMetadataDTO = mapper
                    .readValue(messageInfo.getMetadata(), ActiveSubstationMetadataDTO.class);

            activeSubstationMetadataDTO.setStatus(AppConstants.Types.ActiveSubstationExecutionStatus.valueOf(status));

            String activeSubstationMetadataDTOString = mapper.writeValueAsString(activeSubstationMetadataDTO);
            messageInfoRepository.updateMetadataByUuid(id, activeSubstationMetadataDTOString);
        }

    }

    public Boolean isLoyolaIoAuthotized() {

        if (userService.getLoggedInUser().getModulesCrm() == null) return false;

        String[] modules = StringUtils
                .tokenizeToStringArray(userService.getLoggedInUser().getModulesCrm(), ",");

        if (Arrays.asList(modules).contains("substation-optimization")) return true;
        else return false;
    }


}
