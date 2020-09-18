package crm.cloudApp.backend.models.data.modules.market;

import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.config.AppConstants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Data
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class Product extends BaseEntity {

  @Column
  private String name;

  @Column
  private Instant deliveryTimeStart;

  @Column
  private Instant deliveryTimeEnd;

  @Column
  @Enumerated(EnumType.STRING)
  private AppConstants.Types.ProductDeliveryPeriod period;

  @Column
  @Enumerated(EnumType.STRING)
  private AppConstants.Types.ProductStatus productStatus;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
  private List<SingleOrder> orders;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "user_id")
  private User user;

  @Column
  String marketCode;

  public BMECommand generateOpenProductCommand() {

    String deliveryTimeStartStr = DateTimeFormatter.ofPattern("%Y-%m-%d'T'%H:%M:%S.%F").ISO_INSTANT
        .format(deliveryTimeStart);
    String deliveryTimeEndStr = DateTimeFormatter.ofPattern("%Y-%m-%d'T'%H:%M:%S.%F").ISO_INSTANT
        .format(deliveryTimeEnd);

    String message =
        "open_product " + deliveryTimeStartStr + " " + deliveryTimeEndStr + " " + this.getId();
    return new BMECommand(message, AppConstants.Types.CommandType.OUTPUT);

  }

  public BMECommand generateCloseProductCommand() {
    String message = "close_product " + " " + this.getId();
    return new BMECommand(message, AppConstants.Types.CommandType.OUTPUT);
  }

}
