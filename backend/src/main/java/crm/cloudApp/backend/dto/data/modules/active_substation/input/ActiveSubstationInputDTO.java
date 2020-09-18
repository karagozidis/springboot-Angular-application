package crm.cloudApp.backend.dto.data.modules.active_substation.input;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.common.BaseDTO;
import crm.cloudApp.backend.dto.data.modules.active_substation.ActiveSubstationMetadataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
@Builder
public class ActiveSubstationInputDTO extends BaseDTO {

    private ActiveSubstationInputHeaderDTO header;
    private ActiveSubstationInputCommandsDTO commands;
    private ActiveSubstationInputDataFilesDTO dataFiles;
    private ActiveSubstationInputOperationConstraintsDTO operationConstraints;
    private ActiveSubstationInputFlexibilityServiceScheduleDTO onCloudAppbilityServiceSchedule;
    private AppConstants.Types.ActiveSubstationExecutionStatus status;
    private ActiveSubstationMetadataDTO metadata;
}
