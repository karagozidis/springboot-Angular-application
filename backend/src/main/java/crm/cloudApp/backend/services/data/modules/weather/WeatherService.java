package crm.cloudApp.backend.services.data.modules.weather;

import crm.cloudApp.backend.dto.data.modules.weather.WeatherResultDTO;
import crm.cloudApp.backend.mappers.data.modules.weather.WeatherResultMapper;
import crm.cloudApp.backend.models.data.MessageInfo;
import crm.cloudApp.backend.models.data.modules.weather.WeatherResult;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.repositories.data.modules.weather.WeatherResultRepository;
import crm.cloudApp.backend.services.users.UserService;
import crm.cloudApp.backend.utils.files.OpenCsvUtil;
import net.aksingh.owmjapis.api.APIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${crm.cloudApp.openweathermaps.apikey}")
    private String apikey;

    @Value("${crm.cloudApp.openweathermaps.lat}")
    private Double lat;

    @Value("${onCloudApp.crm-central.openweathermaps.lon}")
    private Double lon;

    private final WeatherResultRepository weatherResultRepository;
    private final WeatherResultMapper weatherResultMapper;
    private final UserService userService;

    public WeatherService(WeatherResultRepository weatherResultRepository,
                          WeatherResultMapper weatherResultMapper,
                          UserService userService) {
        this.weatherResultRepository = weatherResultRepository;
        this.weatherResultMapper = weatherResultMapper;
        this.userService = userService;
    }

    public void saveWeatherData() throws APIException {
//    WeatherResult weatherResult = getWeatherResultsFromAPI();
//    this.weatherResultRepository.save(weatherResult);
    }

    private WeatherResult getWeatherResultsFromAPI() throws APIException {

        WeatherResult weatherResult = new WeatherResult();
        return weatherResult;
    }

    public List<WeatherResultDTO> get(Instant timeFrom, Instant timeTo, String marketCode) {

        List<WeatherResult> weatherResults = new ArrayList<>();
        weatherResults = this.weatherResultRepository.findAllPeriod(timeFrom, timeTo, marketCode);

        return this.weatherResultMapper.map(weatherResults);
    }

    public List<WeatherResultDTO> getPreview(Instant timeFrom, Instant timeTo, String marketCode) {

        List<WeatherResult> weatherResults = new ArrayList<>();

        List<WeatherResult> actualWeatherResults = this.weatherResultRepository
                .findOnPeriod(timeFrom, timeTo, marketCode, "Actual");

        if (!actualWeatherResults.isEmpty()) {
            weatherResults.add(actualWeatherResults.get(0));
        }

        List<WeatherResult> predictionWeatherResults = this.weatherResultRepository
                .findOnPeriod(timeFrom, timeTo, marketCode, "Prediction");

        if (!predictionWeatherResults.isEmpty()) {
            predictionWeatherResults.remove(0);
            weatherResults.addAll(predictionWeatherResults);
        }

        return this.weatherResultMapper.map(weatherResults);
    }

    public List<WeatherResult> parseCsvAndGenerateImportOrders(MessageInfo messageinfo,
                                                               String marketCode)
            throws IOException {

        List<WeatherResult> weatherResults = new ArrayList<>();
        List<String[]> csvData = OpenCsvUtil.csvInList(messageinfo.getMessage().getEncryptedMessage());

        User user = userService.getLoggedInUser();
        Boolean firstRow = true;
        int csvLineCounter = 1;

        for (String[] csvLine : csvData) {

            if (csvLineCounter > 1) {
                WeatherResult weatherResult = new WeatherResult();

                try {

//                    DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
//                            .appendPattern("yyyy-MM-dd H:mm")
//                            .toFormatter()
//                            .withZone(ZoneOffset.UTC);
//                    Instant deliveryTime = dateTimeFormatter.parse(csvLine[0], Instant::from);
                    OffsetDateTime deliveryTime = OffsetDateTime.parse(csvLine[0]);
                    weatherResult.setWeatherDateTime(deliveryTime.toInstant());

                } catch (Exception ex) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error ar line " + csvLineCounter + ". Weather datetime could not be read.");
                }

                if (!csvLine[1].matches("[0-9]+") &&
                        !csvLine[1].matches("^[0-9]+.[0-9]+$")) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error ar line " + csvLineCounter + ". Sun radiation should be an Double value.");
                }
                weatherResult.setRadiation(Double.parseDouble(csvLine[1]));

                if (!csvLine[2].matches("[0-9]+") &&
                        !csvLine[2].matches("^[0-9]+.[0-9]+$")) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error ar line " + csvLineCounter + ". Wind speed should be an Double value.");
                }
                weatherResult.setWindSpeed(Double.parseDouble(csvLine[2]));

                if (!csvLine[3].matches("[0-9]+") &&
                        !csvLine[3].matches("^[0-9]+.[0-9]+$")) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error ar line " + csvLineCounter + ". Temperature should be an Double value");
                }
                weatherResult.setTemperature(Double.parseDouble(csvLine[3]));

                if (csvLine[4].toLowerCase().isBlank() || csvLine[4].toLowerCase().isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error ar line " + csvLineCounter + ". Sky Code name must not be empty.");
                }
                weatherResult.setSkyCode(csvLine[4].toString());

                if (!csvLine[5].toLowerCase().equals("actual") && !csvLine[5].toLowerCase().equals("prediction")
                ) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error ar line " + csvLineCounter + ". Type value must be 'Actual' or 'Prediction'.");
                }
                weatherResult.setType(csvLine[5].toString());

                weatherResult.setMarketCode(marketCode);

                weatherResult.setCreatedBy(user.getUsername());
                weatherResult.setCreatedOn(Instant.now());
                weatherResult.setModifiedBy(user.getUsername());
                weatherResult.setModifiedOn(Instant.now());
                weatherResults.add(weatherResult);
            }

            csvLineCounter++;
        }

        return weatherResults;
    }

    public void create(List<WeatherResult> weatherResults) {
        this.weatherResultRepository.saveAll(weatherResults);
    }


    public void deleteOnPeriod(Instant timeFrom, Instant timeTo, String marketCode) {
        this.weatherResultRepository
                .deleteOnPeriod(timeFrom, timeTo, marketCode);
    }

    public void deleteById(Long id) {
        this.weatherResultRepository.deleteById(id);
    }

}
