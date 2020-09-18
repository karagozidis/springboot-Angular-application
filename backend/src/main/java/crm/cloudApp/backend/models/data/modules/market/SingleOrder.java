package crm.cloudApp.backend.models.data.modules.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.config.AppConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class SingleOrder extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Column
  private Instant bidTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column
  private double price;

  @Column
  private double quantity;

  @Column
  private double current_price;

  @Column
  private double current_quantity;

  @Column
  @Enumerated(EnumType.STRING)
  private AppConstants.Types.SingleOrderStatus orderStatus;

  @Column
  @Enumerated(EnumType.STRING)
  private AppConstants.Types.OrderDirection orderDirection;

  @Column
  @Enumerated(EnumType.STRING)
  private AppConstants.Types.SingleOrderType orderType;

  @Column
  private String metadata;

  @Column
  private Instant removedOn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "basket_id")
  private Basket basket;

  static Map<String, String> orderTypesBME = Map.of(
      "ICEBERG", "iceberg_order",
      "IMMEDIATE_OR_CANCEL", "IoC_order",
      "FILL_OR_KILL", "FoK_order",
      "ALL_OR_NONE", "AoN_order",
      "LIMIT", "limit_order",
      "BASKET", "basket_order"
  );

  static Map<String, String> orderDirectionsBME = Map.of(
      "SELL", "sell",
      "BUY", "buy"
  );

  public BMECommand generateCommand() throws IOException {
    String bidTimeStr = DateTimeFormatter.ofPattern("%Y-%m-%d'T'%H:%M:%S.%F").ISO_INSTANT
        .format(bidTime);
    String message = "";
    if (orderType == AppConstants.Types.SingleOrderType.ICEBERG) {

      ObjectMapper mapper = new ObjectMapper();
      Map<String, String> metadataMap = mapper.readValue(metadata, Map.class);
      message =
          orderTypesBME.get(orderType.toString()) + " " +
              this.product.getId() + " " +
              orderDirectionsBME.get(this.getOrderDirection().toString()) + " " +
              metadataMap.get("icebergQuantity") + " " +
              this.price + " " +
              bidTimeStr + " " +
              this.getId() + " " +
              this.quantity + " " +
              metadataMap.get("icebergPriceDelta");

    } else {
      message = orderTypesBME.get(orderType.toString()) + " " +
          this.product.getId() + " " +
          orderDirectionsBME.get(this.getOrderDirection().toString()) + " " +
          this.quantity + " " +
          this.price + " " +
          bidTimeStr + " " +
          this.getId();
    }

    return new BMECommand(message, AppConstants.Types.CommandType.OUTPUT);
  }

  public BMECommand generateDeactivationCommand() {

    String message = "remove_order_by_custom_id " + this.getId();
    return new BMECommand(message, AppConstants.Types.CommandType.OUTPUT);
  }

  public List<BMECommand> generateUpdateCommands() throws IOException {

    BMECommand deactivationCommand = this.generateDeactivationCommand();
    BMECommand creationCommand = this.generateCommand();

    List<BMECommand> commands = new ArrayList<>();
    commands.add(deactivationCommand);
    commands.add(creationCommand);

    return commands;
  }

}
