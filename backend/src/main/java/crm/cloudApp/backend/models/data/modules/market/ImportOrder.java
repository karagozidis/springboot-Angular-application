package crm.cloudApp.backend.models.data.modules.market;

import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.config.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class ImportOrder extends BaseEntity {

    @Column
    private Long order_ID;

    @Column
    private String action;

    @Column
    private String product_name;

    @Column
    @Enumerated(EnumType.STRING)
    private AppConstants.Types.SingleOrderType orderType;

    @Column
    private double quantity;

    @Column
    private double price;

    @Column
    @Enumerated(EnumType.STRING)
    private AppConstants.Types.OrderDirection orderDirection;

    @Column
    private Instant timeStamp;

    @Column
    private String order_meta;

    @Column
    private String basket_ID;

    @Column
    private String basket_Type;

    @Column
    private String basket_meta;

    @Column
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private AppConstants.Types.ImportOrderStatus status;

    @Column
    private Long messageInfoId;

    @Column
    private String messageInfoName;

    @Column
    private Long singleOrderId;

    @Column
    private Long activeGroupId;

    @Column
    private String marketCode;

}

