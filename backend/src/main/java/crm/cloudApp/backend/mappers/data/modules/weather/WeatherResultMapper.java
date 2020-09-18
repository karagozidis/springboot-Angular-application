package crm.cloudApp.backend.mappers.data.modules.weather;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.data.modules.weather.WeatherResultDTO;
import crm.cloudApp.backend.models.data.modules.weather.WeatherResult;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class WeatherResultMapper extends BaseMapper<WeatherResultDTO, WeatherResult> {


}
