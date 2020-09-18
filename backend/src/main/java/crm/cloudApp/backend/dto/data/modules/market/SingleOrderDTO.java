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
public class SingleOrderDTO extends BaseDTO implements Serializable {

  private ProductDTO product;

  private Instant bidTime;

  private UserDTO user;

  private double price;

  private double quantity;

  private double current_price;

  private double current_quantity;

  private AppConstants.Types.SingleOrderStatus orderStatus;

  private AppConstants.Types.OrderDirection orderDirection;

  private AppConstants.Types.SingleOrderType orderType;

  private String metadata;

  private BasketDTOProjection basket;

  private Instant removedOn;

}
