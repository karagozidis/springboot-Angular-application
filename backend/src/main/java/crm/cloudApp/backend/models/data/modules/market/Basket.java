package crm.cloudApp.backend.models.data.modules.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.config.AppConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class Basket extends BaseEntity {

  @Column
  private Instant timestamp;

  @Column(name = "basketType")
  @Enumerated(EnumType.STRING)
  private AppConstants.Types.BasketType basketType;

  @Column(name = "basketStatus")
  @Enumerated(EnumType.STRING)
  private AppConstants.Types.BasketStatus status;

  @Column
  private String metadata;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "basket")
  private List<SingleOrder> orders;

  static Map<String, String> basketTypesBME = Map.of(
      "EXCLUSIVE", "exclusive_group",
      "VOLUME_CONSTRAINED", "volume_constrained_group",
      "CUMULATIVE_VOLUME_CONSTRAINED", "cumulative_volume_constrained_group",
      "LINKED", "linked_group"
  );

  public BMECommand generateBasketCreationCommand() throws IOException {

    String message = "";

    if (basketType == AppConstants.Types.BasketType.EXCLUSIVE) {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, String> metadataMap = mapper.readValue(metadata, Map.class);
      message = "create_" + basketTypesBME.get(basketType.toString()) + " " +  metadataMap
                      .get("maxMatchedOrders");
    } else if (basketType == AppConstants.Types.BasketType.VOLUME_CONSTRAINED) {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, String> metadataMap = mapper.readValue(metadata, Map.class);
      message = "create_" + basketTypesBME.get(basketType.toString()) + " " + metadataMap
          .get("totalQuantity");
    } else if (basketType == AppConstants.Types.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, String> metadataMap = mapper.readValue(metadata, Map.class);
      message = "create_" + basketTypesBME.get(basketType.toString()) + " " + metadataMap
          .get("maxCapacity") + " " + metadataMap.get("initialCharge") + " " + metadataMap
          .get("ratedPower");
    } else {
      message = "create_" + basketTypesBME.get(basketType.toString());
    }

    return new BMECommand(message, AppConstants.Types.CommandType.OUTPUT);
  }

  public BMECommand generateBasketSelectionCommand() {
    return new BMECommand("basket_set {basket_id}", AppConstants.Types.CommandType.OUTPUT);
  }

  public BMECommand generateBasketCommitCommand() {
    return new BMECommand("basket_commit ", AppConstants.Types.CommandType.OUTPUT);
  }

}
