package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.dto.users.ContactDTO;
import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.models.users.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ContactMapper extends BaseMapper<ContactDTO, Contact> {

}
