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
public class ActiveSubstationInputDataFilesDTO extends BaseDTO {

    private Integer sourceYear;
    private Integer cmrPriceYear;
    private Integer  demandYear;
    private Integer generationYear;
    private Integer pvPowerYear;
    private Integer pbpPriceYear;
    private Integer pvPriceYear;
    private Integer tieLinePowerFlowYear;
    private Integer wholesalePriceYear;
    private Integer windPowerYear;

    private Integer sourceMonth;
    private Integer cmrPriceMonth;
    private Integer  demandMonth;
    private Integer generationMonth;
    private Integer pvPowerMonth;
    private Integer pbpPriceMonth;
    private Integer pvPriceMonth;
    private Integer tieLinePowerFlowMonth;
    private Integer wholesalePriceMonth;
    private Integer windPowerMonth;

    private Integer sourceDay;
    private Integer cmrPriceDay;
    private Integer  demandDay;
    private Integer generationDay;
    private Integer pvPowerDay;
    private Integer pbpPriceDay;
    private Integer pvPriceDay;
    private Integer tieLinePowerFlowDay;
    private Integer wholesalePriceDay;
    private Integer windPowerDay;

}
