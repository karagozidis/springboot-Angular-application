package crm.cloudApp.backend.utils.data.modules.market;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@UtilityClass
public final class MarketTimeIntervalsUtil {

  /*
   *
   * Returns the minimum product.deliveryTimeStart (the delivery start time) that a product must have to be active on the Market.
   * It considers the current market time as the current UTC time
   *
   */
  public static Instant getBaseTimeInterval() {
    return MarketTimeIntervalsUtil.getBaseTimeInterval(Instant.now());
  }

  /*
   *
   * Returns the current maximum deliveryTimeStart that a product must have to be active on the Market
   * It considers the current market time as the current UTC time
   *
   */
  public static Instant getUpperTimeInterval() {
    return MarketTimeIntervalsUtil.getUpperTimeInterval(Instant.now());
  }

  /*
   *
   * Returns the minimum product.deliveryTimeStart (the delivery start time) that a product must have to be active on the Market.
   * It considers the current market time as the time that is set by the user
   *
   */
  public static Instant getBaseTimeInterval(Instant instant) {
   // ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Europe/Athens"));
    return zonedDateTime.now().plusHours(1).toInstant();
  }

  /*
   *
   * Returns the current maximum deliveryTimeStart that a product must have to be active on the Market
   * It considers the current market time as the current UTC time
   *
   */
  public static Instant getUpperTimeInterval(Instant instant) {
//    ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
    ZoneId zone = ZoneId.of("Europe/Athens");
    ZonedDateTime zonedDateTime = ZonedDateTime.now(zone);

    Instant upperTimeInterval = null;
    if (zonedDateTime.getHour() < 16) {
      upperTimeInterval = zonedDateTime.plusDays(1).toLocalDate().atStartOfDay()
          .toInstant(zonedDateTime.getOffset());
    } else {
      upperTimeInterval = zonedDateTime.now().plusDays(2).toLocalDate().atStartOfDay()
          .toInstant(zonedDateTime.getOffset());
    }

    return upperTimeInterval;
  }

}
