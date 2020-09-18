package crm.cloudApp.backend.dto.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.common.BaseDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class ImportOrderDTO extends BaseDTO {

  private Long order_ID;

  private String action;

  private String product_name;

  @Enumerated(EnumType.STRING)
  private AppConstants.Types.SingleOrderType orderType;

  private double quantity;

  private double price;

  @Enumerated(EnumType.STRING)
  private AppConstants.Types.OrderDirection orderDirection;

  private Instant timeStamp;

  private String order_meta;

  private String basket_ID;

  private String basket_Type;

  private String basket_meta;

  private Long userId;

  @Enumerated(EnumType.STRING)
  private AppConstants.Types.ImportOrderStatus status;

  private Long messageInfoId;

  private String messageInfoName;

  private Long singleOrderId;

  private String marketCode;
}
