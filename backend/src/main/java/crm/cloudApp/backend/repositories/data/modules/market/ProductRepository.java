package crm.cloudApp.backend.repositories.data.modules.market;


import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.market.ProductMarketProjection;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface ProductRepository extends BaseRepository<Product> {

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.user u " +
            " WHERE u.id =:userId ")
    List<Product> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.user " +
            " where p.id =:productId ")
    Product findProduct(@Param("productId") Long id);

    @Query("SELECT DISTINCT p FROM Product p " +
            " LEFT JOIN FETCH p.orders o" +
            " LEFT OUTER JOIN FETCH o.basket " +
            " WHERE p.deliveryTimeStart >= :expectedDayDeliveryTimeStart " +
            " AND p.deliveryTimeStart < :expectedDayDeliveryTimeEnd " +
            " AND p.productStatus = 'OPEN'" +
            " AND p.marketCode = :marketCode " +
            " ORDER BY p.name ASC, p.deliveryTimeStart ASC")
    List<Product> findAllTimeActiveProducts(
            @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
            @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd,
            @Param("marketCode") String marketCode);

    @Query("SELECT DISTINCT p FROM Product p " +
            " LEFT JOIN FETCH p.orders o" +
            " LEFT OUTER JOIN FETCH o.basket " +
            " WHERE p.deliveryTimeStart >= :expectedDayDeliveryTimeStart " +
            " AND p.deliveryTimeStart < :expectedDayDeliveryTimeEnd " +
            " AND p.productStatus = 'OPEN'" +
            " AND p.marketCode = :marketCode " +
            " AND p.period = :period " +
            " ORDER BY p.name ASC, p.deliveryTimeStart ASC")
    List<Product> findAllTimeActiveProducts(
            @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
            @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd,
            @Param("marketCode") String marketCode,
            @Param("period") AppConstants.Types.ProductDeliveryPeriod period);

    @Query("SELECT DISTINCT p FROM Product p " +
            " LEFT JOIN FETCH p.orders o" +
            " LEFT OUTER JOIN FETCH o.basket " +
            " WHERE p.deliveryTimeStart >= :expectedDayDeliveryTimeStart " +
            " AND p.deliveryTimeStart < :expectedDayDeliveryTimeEnd " +
            " AND p.marketCode = :marketCode " +
            " ORDER BY p.deliveryTimeStart")
    List<Product> findProducts(
            @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
            @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd,
            @Param("marketCode") String marketCode);

    @Query("SELECT DISTINCT p " +
            " FROM Product p " +
            " LEFT JOIN FETCH p.orders o" +
            " LEFT OUTER JOIN FETCH o.basket " +
            " WHERE p.deliveryTimeStart >= :expectedDayDeliveryTimeStart " +
            " AND p.deliveryTimeStart < :expectedDayDeliveryTimeEnd " +
            " AND p.marketCode = :marketCode " +
            " AND o.user.id = :userId " +
            " ORDER BY p.deliveryTimeStart")
    List<Product> findUserProducts(
            @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
            @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd,
            @Param("marketCode") String marketCode,
            @Param("userId") Long userId
    );

    @Query(
            value =
                    " SELECT                     " +
                            "  p.id,					" +
                            "  p.delivery_time_end,     " +
                            "  p.delivery_time_start,   " +
                            "  p.name,                  " +
                            "  p.period,                " +
                            "  p.product_status,        " +
                            "  DATE_SUB(p.delivery_time_start,INTERVAL 1 HOUR) AS closes_at ,        " +

                            "  p.market_code,           " +

                            "    latest_buy_order.id AS buy_id,                                     " +
                            "    latest_buy_order.bid_time AS buy_bid_time,                         " +
                            "    latest_buy_order.price AS buy_price,               " +
                            "    latest_buy_order.quantity AS buy_quantity,         " +
                            "    latest_buy_order.current_price AS buy_current_price,               " +
                            "    latest_buy_order.current_quantity AS buy_current_quantity,         " +
                            "    latest_buy_order.metadata AS buy_metadata,                         " +
                            "    latest_buy_order.order_status AS buy_order_status,         " +
                            "    latest_buy_order.order_direction AS buy_order_direction,   " +
                            "    latest_buy_order.order_type AS buy_order_type,            " +

                            "    latest_sell_order.id AS sell_id,                                       " +
                            "    latest_sell_order.bid_time AS sell_bid_time,                           " +
                            "    latest_sell_order.price AS sell_price,                                 " +
                            "    latest_sell_order.quantity AS sell_quantity,                           " +
                            "    latest_sell_order.current_price AS sell_current_price,                 " +
                            "    latest_sell_order.current_quantity AS sell_current_quantity,           " +
                            "    latest_sell_order.metadata AS sell_metadata,                           " +
                            "    latest_sell_order.order_status  AS sell_order_status,                  " +
                            "    latest_sell_order.order_direction AS sell_order_direction,             " +
                            "    latest_sell_order.order_type AS sell_order_type                        " +

                            " FROM product p     " +

                            " LEFT OUTER JOIN single_order latest_buy_order    " +
                            " ON latest_buy_order.id  =" +
                            " (SELECT buy_order2.id " +
                            " FROM single_order buy_order2 " +
                            " WHERE buy_order2.product_id = p.id " +
                            " AND buy_order2.order_direction = 'BUY' " +
                            " AND buy_order2.order_status IN ('PARTLY_MATCHED','ACTIVE')  LIMIT 1 )" +

                            " LEFT OUTER JOIN single_order latest_sell_order    " +
                            " ON latest_sell_order.id  = " +
                            " (SELECT sell_order2.id " +
                            " FROM single_order sell_order2 " +
                            " WHERE sell_order2.product_id = p.id " +
                            " AND sell_order2.order_direction = 'BUY' " +
                            " AND sell_order2.order_status IN ('PARTLY_MATCHED','ACTIVE')  LIMIT 1 )" +

                            " WHERE p.delivery_time_start >= :expectedDayDeliveryTimeStart   " +
                            " AND p.delivery_time_end < :expectedDayDeliveryTimeEnd          " +
                            " AND p.product_status = 'OPEN'                                  " +
                            " ORDER BY p.delivery_time_start                                 ",
            nativeQuery = true
    )
    List<ProductMarketProjection> findAllTimeActiveProductsNativeSql(
            @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
            @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd);


    @Query("SELECT DISTINCT p FROM Product p " +
            " WHERE p.deliveryTimeStart < :expectedDayDeliveryTimeStart " +
            " AND p.productStatus <> 'CLOSED' " +
            " AND p.marketCode = :marketCode ")
    List<Product> findProductsThatMustBeClosed(
            @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
            @Param("marketCode") String marketCode);


    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStatus = 'CLOSED' where p.id in (:productIds)")
    void setProductClosed(@Param("productIds") List<Long> productIds);


    @Query(" SELECT p FROM Product p " +
            " LEFT JOIN FETCH p.user u " +
            " WHERE p.name LIKE :name " +
            " AND p.marketCode = :marketCode ")
    List<Product> findByName(
            @Param("name") String name,
            @Param("marketCode") String marketCode
    );

}
