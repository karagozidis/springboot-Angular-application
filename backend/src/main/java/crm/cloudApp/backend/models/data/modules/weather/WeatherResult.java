package crm.cloudApp.backend.models.data.modules.weather;


import crm.cloudApp.backend.models.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class WeatherResult extends BaseEntity {

  @Column
  private Instant weatherDateTime;

  @Column
  private Double windSpeed;

  @Column
  private Double temperature;

  @Column
  private Double radiation;

  @Column
  private String skyCode;

  @Column
  private String marketCode;

  @Column
  private String type;

}
