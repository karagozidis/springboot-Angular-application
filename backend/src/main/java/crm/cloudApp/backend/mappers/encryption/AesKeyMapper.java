package crm.cloudApp.backend.mappers.encryption;

import crm.cloudApp.backend.dto.encryption.AesKeyDTO;
import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.models.encryption.AesKey;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class AesKeyMapper extends BaseMapper<AesKeyDTO, AesKey> {

}
