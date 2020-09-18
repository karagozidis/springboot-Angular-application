package crm.cloudApp.backend.dto.data.modules.active_substation;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
@Builder
public class ActiveSubstationMetadataDTO extends BaseDTO {

    private String uuid;
    private String name;
    private String description;
    private long usergroup;
    private AppConstants.Types.ActiveSubstationExecutionStatus status;
    private String location = "ONCRMCLOUDAPP";
    private Instant creationDate;
    private Boolean editable;
}
