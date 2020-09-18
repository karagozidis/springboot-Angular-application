package crm.cloudApp.backend.repositories.data.modules.market;

import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BasketRepository extends BaseRepository<Basket> {

  @Query("SELECT b FROM Basket b " +
      " JOIN FETCH b.orders o " +
      " WHERE o.product.deliveryTimeStart >= :expectedDayDeliveryTimeStart " +
      " AND o.product.deliveryTimeStart < :expectedDayDeliveryTimeEnd " +
      " AND o.product.productStatus = 'OPEN' ")
  List<Basket> findAllTimeActiveBaskets(
      @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
      @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd);

  @Query("SELECT DISTINCT b FROM Basket b " +
      " JOIN FETCH b.orders o " +
      " JOIN FETCH o.product p " +
      " WHERE o.product.id in (:productIds)")
  List<Basket> findBasketsByProductIds(@Param("productIds") List<Long> productIds);

  List<Basket> findAll();

  @Query(
      " SELECT DISTINCT b " +
      " FROM Basket b " +
      " JOIN FETCH b.orders o " +
      " JOIN FETCH o.product p " +
      " WHERE b.id IN (SELECT DISTINCT s.basket.id FROM SingleOrder s WHERE s.id = :orderId) " +
      " AND o.orderStatus IN ( 'ACTIVE', 'PARTLY_MATCHED') " +
      " AND p.deliveryTimeStart >= :expectedDayDeliveryTimeStart " +
      " AND p.deliveryTimeStart < :expectedDayDeliveryTimeEnd "
  )
  Basket findByOrderId(
      @Param("orderId") Long orderId,
      @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
      @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd);

  @Query(
      " SELECT DISTINCT b " +
          " FROM Basket b " +
          " JOIN FETCH b.orders o " +
          " JOIN FETCH o.product p " +
          " WHERE b.id IN (SELECT DISTINCT s.basket.id FROM SingleOrder s WHERE s.id = :orderId) " +
          " AND o.orderStatus IN ( 'ACTIVE', 'PARTLY_MATCHED') "
  )
  Basket findByOrderId(
      @Param("orderId") Long orderId);


  @Query("SELECT DISTINCT b " +
      " FROM Basket b " +
      " JOIN FETCH b.orders o " +
      " JOIN FETCH o.product p " +
      " WHERE b.id = :basketId " +
      " AND p.deliveryTimeStart >= :expectedDayDeliveryTimeStart " +
      " AND p.deliveryTimeStart < :expectedDayDeliveryTimeEnd "
  )
  Basket findBybasketId(
      @Param("basketId") Long basketId,
      @Param("expectedDayDeliveryTimeStart") Instant expectedDayDeliveryTimeStart,
      @Param("expectedDayDeliveryTimeEnd") Instant expectedDayDeliveryTimeEnd
      );

}
