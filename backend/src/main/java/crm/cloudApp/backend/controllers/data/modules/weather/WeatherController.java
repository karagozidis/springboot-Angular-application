package crm.cloudApp.backend.controllers.data.modules.weather;

import crm.cloudApp.backend.dto.data.modules.weather.WeatherDTO;
import crm.cloudApp.backend.dto.data.modules.weather.WeatherResultDTO;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.data.modules.weather.WeatherResult;
import crm.cloudApp.backend.services.data.modules.file_upload.FileUploadService;
import crm.cloudApp.backend.services.data.modules.weather.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/weather")
public class WeatherController {

  private final WeatherService weatherService;
  private final FileUploadService fileUploadService;

  public WeatherController(WeatherService weatherService,
      FileUploadService fileUploadService) {
    this.weatherService = weatherService;
    this.fileUploadService = fileUploadService;
  }


  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void uploadOrders(
      @RequestParam("file") MultipartFile multipartFile,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
  ) throws IOException {
    MessageInfo messageinfo = this.fileUploadService.uploadFiles(multipartFile, "weather_file");
    try {
      List<WeatherResult> weatherResults = this.weatherService
          .parseCsvAndGenerateImportOrders(messageinfo, marketCode);
      this.weatherService.create(weatherResults);
    } catch (ResponseStatusException ex) {
      log.error(ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Error parsing the file!");
    }
  }

  @GetMapping(path = "/period")
  public List<WeatherResultDTO> get(
      @RequestParam("time-from") Instant timeFrom,
      @RequestParam("time-to") Instant timeTo,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
  ) {
    return this.weatherService.get(timeFrom, timeTo, marketCode);
  }

  @GetMapping(path = "/current")
  public List<WeatherDTO> getCurrentWeaher() {

    ZonedDateTime.now().plusDays(2).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);

    return new ArrayList<>();
  }

  @GetMapping(path = "/today")
  public List<WeatherResultDTO> getToday(
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
  ) {
    Instant timeFrom = Instant.now().minus(1, ChronoUnit.HOURS);
    Instant timeTo =
        ZonedDateTime.now().plusDays(1).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);

    return this.weatherService.getPreview( timeFrom, timeTo, marketCode );
  }


  @DeleteMapping(path = "/period")
  public void deleteOnPeriod(
      @RequestParam("time-from") Instant timeFrom,
      @RequestParam("time-to") Instant timeTo,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode
  ) {
    this.weatherService.deleteOnPeriod(timeFrom, timeTo, marketCode);
  }

  @DeleteMapping(path = "/by-id")
  public void deleteById(
      @RequestParam("id") Long id
  ) {
    this.weatherService.deleteById(id);
  }


}
