package crm.cloudApp.backend.dto.data.modules.weather;

import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.AllArgsConstructor;
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
public class WeatherResultDTO extends BaseDTO {

  private Instant weatherDateTime;

  private Double windDigree;

  private Double windSpeed;

  private Double temperature;

  private Double humidity;

  private Double cloudPercentage;

  private int weatherConditionId;

  private String weatherConditionMain;

  private String weatherConditionDescription;

  private String weatherConditionIcon;

  private Double radiation;

  private Instant radiationDate;

  private Instant radiationTime;

  private String skyCode;

  private String marketCode;

  private String type;
}
