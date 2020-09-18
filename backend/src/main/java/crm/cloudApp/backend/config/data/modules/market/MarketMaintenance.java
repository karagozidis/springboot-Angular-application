package crm.cloudApp.backend.config.data.modules.market;

import crm.cloudApp.backend.services.data.modules.market.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Configuration
@EnableScheduling
@Slf4j
public class MarketMaintenance {

    private final ProductService productServiceBME;

    @Value("${modules.market.HourlyProducts}")
    private Boolean hourlyProducts;

    @Value("${modules.market.QuarterHourlyProducts}")
    private Boolean quarterHourlyProducts;

    //  private final MarketBatchesService marketBatchesService;

    public MarketMaintenance(ProductService productServiceBME) {
        this.productServiceBME = productServiceBME;
        // this.marketBatchesService = marketBatchesService;
    }

    // Deactivate expired products from trading.exe every 5 minutes.
    @Scheduled(cron = "0 0/5 * * * ?")
    public void deactivateExpiredProducts() {
        productServiceBME.deactivateClosedOnBme("CY");
        productServiceBME.deactivateClosedOnBme("BG");
    }

    // Open future products to the trading.exe every day at 15:55 for the next day.
    @Scheduled(cron = "0 50 15 ? * *")
    public void openFutureProducts() throws IOException {

        if (hourlyProducts) {
            productServiceBME.generateHourlyProducts(
                    ZonedDateTime.now().plusDays(1).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                    ZonedDateTime.now().plusDays(2).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                    "CY"
            );
            productServiceBME.generateHourlyProducts(
                    ZonedDateTime.now().plusDays(1).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                    ZonedDateTime.now().plusDays(2).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                    "BG"
            );
        }

        if (quarterHourlyProducts) {
            productServiceBME.generateQuarterHourlyProducts(
                    ZonedDateTime.now().plusDays(1).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                    ZonedDateTime.now().plusDays(2).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                    "CY"
            );
            productServiceBME.generateQuarterHourlyProducts(
                    ZonedDateTime.now().plusDays(1).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                    ZonedDateTime.now().plusDays(2).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                    "BG"
            );
        }

    }

}
