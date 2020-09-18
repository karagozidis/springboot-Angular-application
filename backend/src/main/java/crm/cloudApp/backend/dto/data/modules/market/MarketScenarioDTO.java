package crm.cloudApp.backend.dto.data.modules.market;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class MarketScenarioDTO extends BaseDTO {

    private String scenario;

}
