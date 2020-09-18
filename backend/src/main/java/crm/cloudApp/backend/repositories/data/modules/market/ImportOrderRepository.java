package crm.cloudApp.backend.repositories.data.modules.market;

import crm.cloudApp.backend.models.data.modules.market.ImportOrder;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface ImportOrderRepository extends BaseRepository<ImportOrder> {

    List<ImportOrder> findAll();

    List<ImportOrder> findByMarketCodeIsLikeAndUserIdIsLike(String marketCode, Long userId);

    Page<ImportOrder> findByMarketCodeIsLikeAndUserIdIsLike(String marketCode,Long userId, Pageable pageable);

    @Query("SELECT o " +
            " FROM ImportOrder o " +
            " WHERE o.timeStamp >= :timestampFrom " +
            " AND o.timeStamp < :timestampTo " +
            " AND o.orderType <> 'BASKET' " +
            " ORDER BY o.id "
    )
    List<ImportOrder> findOrdersByTimestampOnPeriod(@Param("timestampFrom") Instant timestampFrom,
                                                    @Param("timestampTo") Instant timestampTo);

    @Query("SELECT o " +
            " FROM ImportOrder o " +
            " WHERE o.timeStamp >= :timestampFrom " +
            " AND o.timeStamp < :timestampTo " +
            " AND o.orderType = 'BASKET' " +
            " ORDER BY o.id ")
    List<ImportOrder> findBasketsByTimestampOnPeriod(@Param("timestampFrom") Instant timestampFrom,
                                                     @Param("timestampTo") Instant timestampTo);

    @Modifying
    @Transactional
    @Query(" UPDATE ImportOrder " +
            " SET singleOrderId = :singleOrderId, " +
            " status = 'EXECUTED' " +
            " WHERE id = :id")
    void updateSingleOrderData(@Param("id") Long id,
                               @Param("singleOrderId") Long singleOrderId);

    @Modifying
    @Transactional
    @Query(" UPDATE ImportOrder " +
            " SET activeGroupId = :activeGroupId " +
            " WHERE messageInfoId = :messageInfoId " +
            " AND order_ID = :order_id ")
    void updateSingleOrderActiveGroupId(
            @Param("messageInfoId") Long messageInfoId,
            @Param("order_id") Long order_id,
            @Param("activeGroupId") Long activeGroupId);


    @Modifying
    @Transactional
    @Query(" UPDATE ImportOrder " +
            " SET activeGroupId = :activeGroupId " +
            " WHERE messageInfoId = :messageInfoId " +
            " AND basket_ID = :basket_id ")
    void updateBasketOrderActiveGroupId(
            @Param("messageInfoId") Long messageInfoId,
            @Param("basket_id") String basket_id,
            @Param("activeGroupId") Long activeGroupId);

    @Modifying
    @Transactional
    @Query(" DELETE FROM ImportOrder " +
            " WHERE userId = :id " +
            " AND marketCode LIKE :marketCode ")
    void deleteByUserAndMarketCode(@Param("id") Long id, @Param("marketCode") String marketCode);

    @Modifying
    @Transactional
    @Query(" DELETE FROM ImportOrder " +
            " WHERE messageInfoId = :messageInfoId " +
            " AND order_ID = :order_id ")
    void deleteGroup(@Param("messageInfoId") Long messageInfoId, @Param("order_id") Long order_id);

    @Modifying
    @Transactional
    @Query(" DELETE FROM ImportOrder " +
            " WHERE messageInfoId = :messageInfoId " +
            " AND userId = :userId ")
    void deleteByMessageInfoId(@Param("messageInfoId") Long messageInfoId, @Param("userId") Long userId);

}
