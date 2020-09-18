package crm.cloudApp.backend.repositories.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SingleOrderRepository extends BaseRepository<SingleOrder> {

    @Query(" SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " WHERE s.product.id =:productId " +
            " AND s.orderDirection <>:orderDirection " +
            " AND s.orderStatus = 'ACTIVE' ")
    List<SingleOrder> findRelatedOrders(@Param("productId") Long productId,
                                        @Param("orderDirection") AppConstants.Types.OrderDirection orderDirection);

    @Query(" SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " WHERE s.user.id =:userId " +
            " AND s.bidTime >=:bidTimeFrom " +
            " AND s.bidTime < :bidTimeTo " +
            " AND s.product.marketCode = :marketCode ")
    List<SingleOrder> findAllByUserIdAndBidTimeBetween(@Param("userId") Long userId,
                                                       @Param("bidTimeFrom") Instant bidTimeFrom,
                                                       @Param("bidTimeTo") Instant bidTimeTo,
                                                       @Param("marketCode") String marketCode);

    @Query(" SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " WHERE s.user.id =:userId " +
            " AND s.orderType =:orderType " +
            " AND s.bidTime >=:bidTimeFrom " +
            " AND s.bidTime < :bidTimeTo " +
            " AND s.product.marketCode = :marketCode ")
    List<SingleOrder> findAllByUserIdAndOrderTypeAndBidTimeBetween(@Param("userId") Long userId,
                                                                   @Param("orderType") AppConstants.Types.SingleOrderType orderType,
                                                                   @Param("bidTimeFrom") Instant bidTimeFrom,
                                                                   @Param("bidTimeTo") Instant bidTimeTo,
                                                                   @Param("marketCode") String marketCode);

    @Query(" SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " WHERE s.bidTime >=:bidTimeFrom " +
            " AND s.bidTime < :bidTimeTo " +
            " AND s.product.marketCode = :marketCode ")
    List<SingleOrder> findAllByBidTimeBetween(@Param("bidTimeFrom") Instant bidTimeFrom,
                                              @Param("bidTimeTo") Instant bidTimeTo,
                                              @Param("marketCode") String marketCode);

    @Query(" SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " WHERE s.orderType =:orderType " +
            " AND s.bidTime >=:bidTimeFrom " +
            " AND s.bidTime < :bidTimeTo " +
            " AND s.product.marketCode = :marketCode")
    List<SingleOrder> findAllByOrderTypeAndBidTimeBetween(
            @Param("orderType") AppConstants.Types.SingleOrderType orderType,
            @Param("bidTimeFrom") Instant bidTimeFrom,
            @Param("bidTimeTo") Instant bidTimeTo,
            @Param("marketCode") String marketCode);

    @Query(
            " SELECT s " +
                    " FROM SingleOrder s " +
                    " LEFT JOIN FETCH s.basket " +
                    " WHERE s.product.id =:productId " +
                    " AND s.orderStatus IN ('ACTIVE', 'PARTLY_MATCHED') " +
                    " ORDER BY s.orderDirection,s.current_price DESC, s.bidTime DESC")
    List<SingleOrder> findNotMatchedByProductId(@Param("productId") Long productId);

    @Query(
            " SELECT s " +
                    " FROM SingleOrder s " +
                    " LEFT JOIN FETCH s.basket " +
                    " WHERE s.product.id =:productId " +
                    " AND s.orderStatus IN ('ACTIVE', 'PARTLY_MATCHED') " +
                    " AND s.orderDirection = 'BUY' " +
                    " ORDER BY s.current_price DESC, s.bidTime ASC")
    List<SingleOrder> findNotMatchedBuysByProductId(@Param("productId") Long productId);


    @Query(
            " SELECT s " +
                    " FROM SingleOrder s " +
                    " LEFT JOIN FETCH s.basket " +
                    " WHERE s.product.id =:productId " +
                    " AND s.orderStatus IN ('ACTIVE', 'PARTLY_MATCHED') " +
                    " AND s.orderDirection = 'SELL' " +
                    " ORDER BY s.current_price ASC, s.bidTime ASC")
    List<SingleOrder> findNotMatchedSellsByProductId(@Param("productId") Long productId);


    @Query(" SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " WHERE s.product.id =:productId " +
            " AND s.orderDirection =:orderDirection ")
    List<SingleOrder> findAllByProductIdAndOrderDirection(@Param("productId") Long productId,
                                                          @Param("orderDirection") AppConstants.Types.OrderDirection orderDirection);

    @Transactional
    @Modifying
    @Query("UPDATE SingleOrder s SET  s.current_quantity =:quantity, s.current_price =:price where s.id =:id ")
    void setQuantityAndPrice(@Param("id") Long id,
                             @Param("quantity") Double quantity,
                             @Param("price") Double price);


    @Modifying
    @Query("UPDATE SingleOrder s SET  s.orderStatus = 'PARTLY_MATCHED'"
            + " WHERE s.id =:id "
            + " AND s.quantity > s.current_quantity")
    void updatePartlyMatched(@Param("id") Long id);


    @Modifying
    @Query("UPDATE SingleOrder s SET  s.orderStatus = 'MATCHED'"
            + " WHERE s.id =:id "
            + " AND s.current_quantity = 0")
    void updateFullyMatched(@Param("id") Long id);


    @Modifying
    @Query("UPDATE SingleOrder s SET s.orderStatus = 'MATCHED' where s.id not in(:productIds) and  s.orderStatus = 'ACTIVE' ")
    void setStatusInProducts(@Param("productIds") List<Long> productIds);

    List<SingleOrder> findAll();

    @Query("SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " WHERE s.product.deliveryTimeStart >= :expectedDayDeliveryTimeStart " +
            " AND s.product.deliveryTimeStart < :expectedDayDeliveryTimeEnd " +
            " AND s.product.marketCode = :marketCode " +
            " AND s.orderStatus = 'ACTIVE' " +
            " ORDER BY s.product.deliveryTimeStart")
    List<SingleOrder> findAllTimeActiveOrders(
            @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
            @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd,
            @Param("marketCode") String marketCode);

    @Query("SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " LEFT JOIN FETCH s.product " +
            " WHERE s.product.id IN (:productIds) " +
            " AND COALESCE(s.basket,0) <= 0 ")
    List<SingleOrder> findOrdersByProductIds(@Param("productIds") List<Long> productIds);

    @Query("SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " LEFT JOIN FETCH s.product " +
            " WHERE s.product.id = :productId ")
    List<SingleOrder> findOrdersByProductId(@Param("productId") Long productId);

    @Query("SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.basket " +
            " LEFT JOIN FETCH s.product " +
            " WHERE s.product.id = :productId " +
            " AND s.user.id = :userId")
    List<SingleOrder> findOrdersByProductIdAndUser(
            @Param("productId") Long productId,
            @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE SingleOrder s SET s.orderStatus = 'ACTIVE' where s.id in(:orderIds) and  s.orderStatus = 'DRAFT' ")
    void activateDraftOrders(@Param("orderIds") List<Long> orderIds);

    @Modifying
    @Query("UPDATE SingleOrder s SET s.orderStatus = 'DEACTIVATED' where s.id in(:orderIds) and  s.orderStatus = 'ACTIVE' ")
    void deactivateOrders(@Param("orderIds") List<Long> orderIds);

    @Modifying
    @Query("UPDATE SingleOrder s SET s.orderStatus = 'ACTIVE' where s.id = :orderId and s.orderStatus = 'DRAFT' ")
    void activateDraftOrder(@Param("orderId") Long orderId);

    @Modifying
    @Query(
            " UPDATE SingleOrder s SET s.orderStatus = 'EXPIRED' " +
                    " WHERE s.product.id in(:productsIds) " +
                    " AND s.orderStatus <> 'MATCHED' ")
    void expireOrdersByProductIds(@Param("productsIds") List<Long> productsIds);

    @Modifying
    @Query(
            " UPDATE SingleOrder s SET s.orderStatus = 'EXPIRED' " +
                    " WHERE s.id IN (:ids) " +
                    " AND s.orderStatus <> 'MATCHED' ")
    void expireOrdersByIds(@Param("ids") List<Long> ids);


    @Query(" SELECT s " +
            " FROM SingleOrder s " +
            " LEFT JOIN FETCH s.product " +
            " WHERE s.id IN (:orderId) ")
    Optional<SingleOrder> findByIdWithProduct(@Param("orderId") Long orderId);

}
