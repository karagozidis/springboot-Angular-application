package crm.cloudApp.backend.dto.data.modules.market.trades;

import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import lombok.AllArgsConstructor;
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
public class TradeDTO extends BaseInfoTradeDTO {

  private SingleOrderDTO singleOrder1;

  private SingleOrderDTO singleOrder2;

  private Double order1_price;

  private Double order1_quantity;

  private Double order2_price;

  private Double order2_quantity;

}
