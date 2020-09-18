package crm.cloudApp.backend.dto.data.modules.market;

import crm.cloudApp.backend.dto.common.BaseDTO;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
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
public class MarketObject extends BaseDTO {

  private String type;

  private ProductDTO Product;

  private TradeDTO tradeDTO;

  private SingleOrderDTO singleOrderDTO;

}
