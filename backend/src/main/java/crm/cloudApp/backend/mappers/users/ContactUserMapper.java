package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.users.ContactUserDTO;
import crm.cloudApp.backend.models.users.Contact;
import crm.cloudApp.backend.models.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ContactUserMapper extends BaseMapper<ContactUserDTO, Contact> {

  public List<ContactUserDTO> mapUserContacts(List<Contact> contacts, User user) {

    List<ContactUserDTO> contactUserDTOs = new ArrayList<>();
    for (Contact contact : contacts) {
      if (
          !(user.getId().equals(contact.getReceiver().getId()) &&
              user.getId().equals(contact.getSender().getId()))
      ) {
        ContactUserDTO contactUserDTO = this.mapToUserContact(contact, user);
        contactUserDTOs.add(contactUserDTO);
      }
    }

    return contactUserDTOs;
  }


  public Page<ContactUserDTO> mapUserContacts(Page<Contact> contactsPage, User user) {

    List<ContactUserDTO> contactUserDTOs =  new ArrayList<>();
    for (Contact contact : contactsPage.getContent()) {
      if (
          !(user.getId().equals(contact.getReceiver().getId()) &&
              user.getId().equals(contact.getSender().getId()))
      ) {
        ContactUserDTO contactUserDTO = this.mapToUserContact(contact, user);
        contactUserDTOs.add(contactUserDTO);
      }
    }

    Page<ContactUserDTO> contactUserDTOsPage = new PageImpl<ContactUserDTO>(contactUserDTOs,
        contactsPage.getPageable(), contactsPage.getTotalElements());
    return contactUserDTOsPage;
  }

  public ContactUserDTO mapToUserContact(Contact contact, User user) {
    ContactUserDTO contactUserDTO = new ContactUserDTO();
    contactUserDTO.setId(contact.getId());
    contactUserDTO.setCreatedBy(contact.getCreatedBy());
    contactUserDTO.setCreatedOn(contact.getCreatedOn());
    contactUserDTO.setContactStatus(contact.getStatus());
    if (user.getId().equals(contact.getSender().getId())) {
      contactUserDTO.setContactType("RECEIVER");
      contactUserDTO.setUserid(contact.getReceiver().getId());
      contactUserDTO.setEmail(contact.getReceiver().getEmail());
      contactUserDTO.setUsername(contact.getReceiver().getUsername());
      contactUserDTO.setUserStatus(contact.getReceiver().getStatus());
    }
    if (user.getId().equals(contact.getReceiver().getId())) {
      contactUserDTO.setContactType("SENDER");
      contactUserDTO.setUserid(contact.getSender().getId());
      contactUserDTO.setEmail(contact.getSender().getEmail());
      contactUserDTO.setUsername(contact.getSender().getUsername());
      contactUserDTO.setUserStatus(contact.getSender().getStatus());
    }
    return contactUserDTO;
  }


}
