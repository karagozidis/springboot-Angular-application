package crm.cloudApp.backend.dto.data.modules.active_substation.output;


import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.*;
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
public class ActiveSubstationOutputCommandsDTO extends BaseDTO {

    private Boolean optimizerStatus;
    private Boolean serverDone;
    private Boolean clientAcknowledge;
    private Boolean warningLog;

}
