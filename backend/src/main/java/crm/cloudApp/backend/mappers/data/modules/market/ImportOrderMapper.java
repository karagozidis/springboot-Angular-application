package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.data.modules.market.ImportOrderDTO;
import crm.cloudApp.backend.models.data.modules.market.ImportOrder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ImportOrderMapper extends BaseMapper<ImportOrderDTO, ImportOrder> {

}
