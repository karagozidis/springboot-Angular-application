package crm.cloudApp.backend.dto.data.modules.active_substation.input;

import crm.cloudApp.backend.dto.common.BaseDTO;
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
public class ActiveSubstationInputCommandsDTO extends BaseDTO {

    private Boolean clientEnquiry;
    private Boolean serverAcknowledge;
    private Boolean drm;
    private Boolean bmd;
    private Boolean ems;
    private Boolean gso;
    private Boolean pms;

}
