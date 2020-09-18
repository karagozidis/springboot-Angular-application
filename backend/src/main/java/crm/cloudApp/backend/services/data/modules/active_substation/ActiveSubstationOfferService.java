package crm.cloudApp.backend.services.data.modules.active_substation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.active_substation.ActiveSubstationMetadataDTO;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.repositories.data.MessageInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActiveSubstationOfferService {
    private final MessageInfoRepository messageInfoRepository;

    public ActiveSubstationOfferService(MessageInfoRepository messageInfoRepository) {
        this.messageInfoRepository = messageInfoRepository;
    }

    public ActiveSubstationMetadataDTO getOfferMetadata(String uuid) throws IOException {

        MessageInfo messageInfo = messageInfoRepository.findByUniqueId(uuid);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enableDefaultTyping();

        ActiveSubstationMetadataDTO activeSubstationMetadataDTO = mapper
                .readValue(messageInfo.getMetadata(), ActiveSubstationMetadataDTO.class);

        return activeSubstationMetadataDTO;
    }

    public ActiveSubstationMetadataDTO getOffersMetadataToday(Long userGroup) throws IOException {
        List<ActiveSubstationMetadataDTO> activeSubstationMetadataDTOs = this.getMetadata(userGroup);

        activeSubstationMetadataDTOs = activeSubstationMetadataDTOs.stream()
                .sorted(Comparator.comparing(ActiveSubstationMetadataDTO::getCreationDate).reversed())
                .collect(Collectors.toList());

        ActiveSubstationMetadataDTO activeSubstationMetadataDTO = activeSubstationMetadataDTOs.stream()
                  .filter(x -> x.getCreationDate().compareTo(Instant.now().minusSeconds(86400)) > 0 && x.getCreationDate().compareTo(Instant.now().plusSeconds(86400)) < 0 )
                .findFirst().orElse(null);

          return activeSubstationMetadataDTO;
    }

    public List<ActiveSubstationMetadataDTO> getOffersMetadata(Long userGroup) throws IOException {
        List<ActiveSubstationMetadataDTO> activeSubstationMetadataDTOs = this.getMetadata(userGroup);
        //  activeSubstationMetadataDTOs = activeSubstationMetadataDTOs.stream().filter(x -> x.getStatus().equals(AppConstants.Types.ActiveSubstationExecutionStatus.NOT_EXECUTED)).collect(Collectors.toList());
        return activeSubstationMetadataDTOs;
    }

    public List<ActiveSubstationMetadataDTO> getMetadata(Long userGroup) throws IOException {
        List<ActiveSubstationMetadataDTO> activeSubstationMetadataDTOs = new ArrayList<>();
        List<MessageInfo> messageInfos = messageInfoRepository
                .findByUserGroupIdsAndTagLike(Arrays.asList(userGroup), "active-substation-output-offer");

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

    @Transactional
    public void updateStatus(String uuid, String status) throws IOException {

        MessageInfo messageInfo = messageInfoRepository.findByUniqueId(uuid);
        if (messageInfo != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ActiveSubstationMetadataDTO activeSubstationMetadataDTO = mapper
                    .readValue(messageInfo.getMetadata(), ActiveSubstationMetadataDTO.class);

            activeSubstationMetadataDTO.setStatus(AppConstants.Types.ActiveSubstationExecutionStatus.valueOf(status));

            String activeSubstationMetadataDTOString = mapper.writeValueAsString(activeSubstationMetadataDTO);
            messageInfoRepository.updateMetadataByUuid(uuid,activeSubstationMetadataDTOString);
        }

    }



}
