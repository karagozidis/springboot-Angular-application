package crm.cloudApp.backend.models.data.modules.market;

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

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class BMEOrderBookEntry extends BaseEntity {


  @Column
  Long orderId;

  @Column
  Long productId;

  @Column
  Double quantity;

  @Column
  Double price;

  @Column
  String entryGroupMessage;

}
