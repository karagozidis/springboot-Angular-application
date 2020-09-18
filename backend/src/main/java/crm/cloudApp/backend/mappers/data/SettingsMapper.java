package crm.cloudApp.backend.mappers.data;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.data.SettingsDTO;
import crm.cloudApp.backend.models.data.Settings;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class SettingsMapper extends BaseMapper<SettingsDTO, Settings> {

}
