package crm.cloudApp.backend.dto.data.modules.active_substation.output;

import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.*;
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
public class ActiveSubstationOutputHeaderDTO extends BaseDTO {

    private Instant date;
    private Integer clientId;
    private Integer clientInterface;
    private Integer service;
    private Integer resolution;
    private Instant realeseTime;

    private Double profit;
    private Double chargeCost;
    private Double dischargeRevenue;
    private Double revenuePBP;
    private Double revenueCMR;

}
