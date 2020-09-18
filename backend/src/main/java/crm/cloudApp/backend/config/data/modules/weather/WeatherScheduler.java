package crm.cloudApp.backend.config.data.modules.weather;

import crm.cloudApp.backend.services.data.modules.weather.WeatherService;
import net.aksingh.owmjapis.api.APIException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class WeatherScheduler {


  private final WeatherService weatherService;

  public WeatherScheduler(WeatherService weatherService) {
    this.weatherService = weatherService;
  }


  //  @Scheduled(fixedRate = 10000)
  //  @Scheduled(cron = "0 * * * * ?")
  public void weatherSchedulerTask() throws APIException {
    weatherService.saveWeatherData();
  }
}
