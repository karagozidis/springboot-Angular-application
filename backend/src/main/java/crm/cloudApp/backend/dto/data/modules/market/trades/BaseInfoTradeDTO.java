package crm.cloudApp.backend.dto.data.modules.market.trades;

import crm.cloudApp.backend.dto.common.BaseDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
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
public class BaseInfoTradeDTO extends BaseDTO {

  private Instant timestamp;

  private String productName;

  private Double price;

  private Double quantity;

  private String metadata;

  private UserDTO buyer;

  private UserDTO seller;
}
