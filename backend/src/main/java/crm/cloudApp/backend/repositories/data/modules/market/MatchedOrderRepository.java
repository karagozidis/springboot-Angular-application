package crm.cloudApp.backend.repositories.data.modules.market;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MatchedOrderRepository extends BaseRepository<MatchedOrder> {

  @Query("SELECT m FROM MatchedOrder m " +
      "JOIN FETCH m.singleOrder1 o1 " +
      "JOIN FETCH m.singleOrder2 o2 " +
      " WHERE o1.product.id =:productId or o2.product.id =:productId ")
  List<MatchedOrder> findByProduct(@Param("productId") Long productId);

  @Query(
      " SELECT m FROM MatchedOrder m " +
          " JOIN FETCH m.singleOrder1 o1 " +
          " JOIN FETCH m.singleOrder2 o2 " +
          " WHERE ( o1.product.id =:productId or o2.product.id =:productId ) " +
          " AND ( o1.user.id =:userId or o2.user.id =:userId ) ")
  List<MatchedOrder> findByProductAndUser(
      @Param("productId") Long productId,
      @Param("userId") Long userId);


  @Query("SELECT m FROM MatchedOrder m " +
      "JOIN FETCH m.singleOrder1 o1 " +
      "JOIN FETCH m.singleOrder2 o2 " +
      " WHERE " +
      " ( o1.user.id =:userId AND o1.bidTime >=:bidTimeFrom " +
      " AND o1.bidTime <:bidTimeTo" +
      " AND o1.product.marketCode = :marketCode ) " +
      " OR " +
      " (o2.user.id =:userId " +
      " AND o2.bidTime >=:bidTimeFrom " +
      " AND o2.bidTime <:bidTimeTo " +
      " AND o2.product.marketCode = :marketCode ) "
  )
  List<MatchedOrder> findByUserAndBidDate(
      @Param("userId") Long userId,
      @Param("bidTimeFrom") Instant bidTimeFrom,
      @Param("bidTimeTo") Instant bidTimeTo,
      @Param("marketCode") String marketCode
  );

  @Query("SELECT m FROM MatchedOrder m " +
      "JOIN FETCH m.singleOrder1 o1 " +
      "JOIN FETCH m.singleOrder2 o2 " +
      " WHERE " +
      " ( o1.user.id =:userId" +
      " AND o1.bidTime >=:bidTimeFrom " +
      " AND o1.bidTime <:bidTimeTo " +
      " AND o1.orderType =:orderType " +
      " AND o1.product.marketCode = :marketCode ) " +
      " OR " +
      " (o2.user.id =:userId " +
      " AND o2.bidTime >=:bidTimeFrom " +
      " AND o2.bidTime <:bidTimeTo" +
      " AND o2.orderType =:orderType " +
      " AND o2.product.marketCode = :marketCode )"
  )
  List<MatchedOrder> findByUserAndBidDateAndType(
      @Param("userId") Long userId,
      @Param("orderType") AppConstants.Types.SingleOrderType orderType,
      @Param("bidTimeFrom") Instant bidTimeFrom,
      @Param("bidTimeTo") Instant bidTimeTo,
      @Param("marketCode") String marketCode

  );

  @Query("SELECT m FROM MatchedOrder m " +
      "JOIN FETCH m.singleOrder1 o1 " +
      "JOIN FETCH m.singleOrder2 o2 " +
      " WHERE ( o1.user.id =:userId  " +
      "  OR o2.user.id =:userId ) " +
      " AND m.status = 'NEW' " +
      " AND o1.product.marketCode = :marketCode ")
  List<MatchedOrder> findNewByUser(@Param("userId") Long userId,
      @Param("marketCode") String marketCode);

  @Modifying
  @Query("UPDATE MatchedOrder m SET  m.status ='VIEWED' WHERE m.id IN (:ids) ")
  void setStatusToViewed(@Param("ids") List<Long> ids);

  @Query(" SELECT m FROM MatchedOrder m " +
      " JOIN FETCH m.singleOrder1 o1 " +
      " JOIN FETCH m.singleOrder2 o2 " +
      " WHERE " +
      " m.createdOn >=:createdFrom " +
      "AND m.createdOn <:createdTo " +
      "AND o1.product.marketCode = :marketCode ")
  List<MatchedOrder> getTradesOnPeriod(@Param("createdFrom") Instant createdFrom,
      @Param("createdTo") Instant createdTo,
      @Param("marketCode") String marketCode);
}
