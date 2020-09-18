package crm.cloudApp.backend.dto.data.modules.market;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public interface ProductMarketProjection {

  @Value("#{target.id}")
  public Long getId();

  @Value("#{target.name}")
  public String getName();

  @Value("#{target.delivery_time_start}")
  public Instant getDeliveryTimeStart();

  @Value("#{target.delivery_time_end}")
  public Instant getDeliveryTimeEnd();

  @Value("#{target.period}")
  public String getPeriod();

  @Value("#{target.product_status}")
  public String getProductStatus();

  @Value("#{target.closes_at}")
  public Instant getClosesAt();

  @Value("#{@orderService.buildOrderDTO(" +
      "target.buy_id, " +
      "target.buy_bid_time, " +
      "target.buy_price, " +
      "target.buy_quantity, " +
      "target.buy_current_price, " +
      "target.buy_current_quantity, " +
      "target.buy_metadata, " +
      "target.buy_order_status, " +
      "target.buy_order_direction, " +
      "target.buy_order_type, " +
      ")}")
  public SingleOrderDTO getLatestBuyOrder();

  @Value("#{@orderService.buildOrderDTO(" +
      "target.sell_id, " +
      "target.sell_bid_time, " +
      "target.sell_price, " +
      "target.sell_quantity, " +
      "target.sell_current_price, " +
      "target.sell_current_quantity, " +
      "target.sell_metadata, " +
      "target.sell_order_status, " +
      "target.sell_order_direction, " +
      "target.sell_order_type, " +
      ")}")
  public SingleOrderDTO getLatestSellOrder();
}
