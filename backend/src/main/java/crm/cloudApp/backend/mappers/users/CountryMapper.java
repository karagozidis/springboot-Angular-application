package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.models.users.Country;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class CountryMapper extends BaseMapper<CountryDTO, Country> {

}
