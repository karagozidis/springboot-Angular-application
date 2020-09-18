package crm.cloudApp.backend.dto.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class RandomOrderGenerationSettingsDTO extends BaseDTO {

  private Integer totalOrders;

  private double minPrice;

  private double maxPrice;

  private double minQuantity;

  private double maxQuantity;

  private List<AppConstants.Types.OrderDirection> orderDirections;

  private List<AppConstants.Types.SingleOrderType> orderTypes;

  private double minIcebergQuantity;

  private double maxIcebergQuantity;

  private double minIcebergPrice;

  private double maxIcebergPrice;

  private String marketCode;
}
