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

import java.util.List;

@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
@Builder
public class ActiveSubstationInputFlexibilityServiceScheduleDTO extends BaseDTO {

    private List<Integer> inertiaEmulation;
    private List<Integer> frequencyRegulation;
    private List<Integer> modalFrequencies;
    private List<Integer> voltageRegulation;
    private List<Integer> resourceVariability;
    private List<Integer> powerShifting;
    private List<Integer> rampRate;
    private List<Integer> powerCurtailment;
    private List<Integer> powerBalance;
    private List<Integer> congestionRelief;
    private List<Integer> powerBalanceReserveUp;
    private List<Integer> powerBalanceReserveDown;


}
