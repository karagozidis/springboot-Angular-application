package crm.cloudApp.backend.models.data.modules.market;

import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.config.AppConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class MatchedOrder extends BaseEntity {

  @Column
  private Instant timestamp;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "singleOrder1_id")
  private SingleOrder singleOrder1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "singleOrder2_id")
  private SingleOrder singleOrder2;

  @Column
  private Double price;

  @Column
  private Double quantity;

  @Column
  private Double order1_price;

  @Column
  private Double order1_quantity;

  @Column
  private Double order2_price;

  @Column
  private Double order2_quantity;

  @Column
  private String metadata;

  @Column
  private AppConstants.Types.MatchedOrderStatus status;

}
