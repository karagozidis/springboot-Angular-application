package crm.cloudApp.backend.mappers.encryption;

import crm.cloudApp.backend.dto.encryption.RsaPublicKeyDTO;
import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.models.encryption.RsaPublicKey;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RsaPublicKeyMapper extends BaseMapper<RsaPublicKeyDTO, RsaPublicKey> {

}

