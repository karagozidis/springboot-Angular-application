package crm.cloudApp.backend.dto.data.modules.active_substation.output;

import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.*;
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
public class ActiveSubstationOutputScheduleDTO extends BaseDTO {

    private List<Double> energyMarketAP;
    private List<Double> energyMarketRP;
    private List<Double> energyMarketP;

    private List<Double> powerBalanceProvisionAPU;
    private List<Double> powerBalanceProvisionAPD;
    private List<Double> powerBalanceProvisionP;

    private List<Double> congestionManagementReliefAP;
    private List<Double> congestionManagementReliefRP;
    private List<Double> congestionManagementReliefP;

    private List<Double> pricesSLCServices1RVM;
    private List<Double> pricesSLCServices1PSM;
    private List<Double> pricesSLCServices2RRE;
    private List<Double> pricesSLCServices2PC;

}
