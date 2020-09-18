package crm.cloudApp.backend.dto.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.common.BaseDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.Instant;

@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class ProductDTO extends BaseDTO implements Serializable {

  private String name;

  private Instant deliveryTimeStart;

  private Instant deliveryTimeEnd;

  private Instant closesAt;

  private AppConstants.Types.ProductDeliveryPeriod period;

  private AppConstants.Types.ProductStatus productStatus;

  private UserDTO user;

  private SingleOrderDTO latestSellOrder;

  private SingleOrderDTO latestBuyOrder;

  private String marketCode;

  private boolean hasOrders;

  private int totalOrders;
}
