package crm.cloudApp.backend.mappers.data.modules.weather;

import crm.cloudApp.backend.dto.data.modules.weather.WeatherResultDTO;
import crm.cloudApp.backend.models.data.modules.weather.WeatherResult;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class WeatherResultMapperImpl extends WeatherResultMapper {

    @Override
    public WeatherResult map(WeatherResultDTO dto) {
        if ( dto == null ) {
            return null;
        }

        WeatherResult weatherResult = new WeatherResult();

        if ( dto.getCreatedOn() != null ) {
            weatherResult.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            weatherResult.setId( dto.getId() );
        }
        if ( dto.getWeatherDateTime() != null ) {
            weatherResult.setWeatherDateTime( dto.getWeatherDateTime() );
        }
        if ( dto.getWindSpeed() != null ) {
            weatherResult.setWindSpeed( dto.getWindSpeed() );
        }
        if ( dto.getTemperature() != null ) {
            weatherResult.setTemperature( dto.getTemperature() );
        }
        if ( dto.getRadiation() != null ) {
            weatherResult.setRadiation( dto.getRadiation() );
        }
        if ( dto.getSkyCode() != null ) {
            weatherResult.setSkyCode( dto.getSkyCode() );
        }
        if ( dto.getMarketCode() != null ) {
            weatherResult.setMarketCode( dto.getMarketCode() );
        }
        if ( dto.getType() != null ) {
            weatherResult.setType( dto.getType() );
        }

        return weatherResult;
    }

    @Override
    public WeatherResultDTO map(WeatherResult entity) {
        if ( entity == null ) {
            return null;
        }

        WeatherResultDTO weatherResultDTO = new WeatherResultDTO();

        if ( entity.getId() != null ) {
            weatherResultDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            weatherResultDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            weatherResultDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getWeatherDateTime() != null ) {
            weatherResultDTO.setWeatherDateTime( entity.getWeatherDateTime() );
        }
        if ( entity.getWindSpeed() != null ) {
            weatherResultDTO.setWindSpeed( entity.getWindSpeed() );
        }
        if ( entity.getTemperature() != null ) {
            weatherResultDTO.setTemperature( entity.getTemperature() );
        }
        if ( entity.getRadiation() != null ) {
            weatherResultDTO.setRadiation( entity.getRadiation() );
        }
        if ( entity.getSkyCode() != null ) {
            weatherResultDTO.setSkyCode( entity.getSkyCode() );
        }
        if ( entity.getMarketCode() != null ) {
            weatherResultDTO.setMarketCode( entity.getMarketCode() );
        }
        if ( entity.getType() != null ) {
            weatherResultDTO.setType( entity.getType() );
        }

        return weatherResultDTO;
    }

    @Override
    public void map(WeatherResultDTO dto, WeatherResult entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreatedOn() != null ) {
            entity.setCreatedOn( dto.getCreatedOn() );
        }
        else {
            entity.setCreatedOn( null );
        }
        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        else {
            entity.setId( null );
        }
        if ( dto.getWeatherDateTime() != null ) {
            entity.setWeatherDateTime( dto.getWeatherDateTime() );
        }
        else {
            entity.setWeatherDateTime( null );
        }
        if ( dto.getWindSpeed() != null ) {
            entity.setWindSpeed( dto.getWindSpeed() );
        }
        else {
            entity.setWindSpeed( null );
        }
        if ( dto.getTemperature() != null ) {
            entity.setTemperature( dto.getTemperature() );
        }
        else {
            entity.setTemperature( null );
        }
        if ( dto.getRadiation() != null ) {
            entity.setRadiation( dto.getRadiation() );
        }
        else {
            entity.setRadiation( null );
        }
        if ( dto.getSkyCode() != null ) {
            entity.setSkyCode( dto.getSkyCode() );
        }
        else {
            entity.setSkyCode( null );
        }
        if ( dto.getMarketCode() != null ) {
            entity.setMarketCode( dto.getMarketCode() );
        }
        else {
            entity.setMarketCode( null );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
        }
        else {
            entity.setType( null );
        }
    }
}
