package crm.cloudApp.backend.services.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.models.users.Contact;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.repositories.users.ContactRepository;
import crm.cloudApp.backend.repositories.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class ContactService {

  private final ContactRepository contactRepository;
  private final UserMapper userMapper;
  private final UserService userService;
  private final UserRepository userRepository;

  @Autowired
  public ContactService(ContactRepository contactRepository,
      UserService userService,
      UserMapper userMapper,
      UserRepository userRepository) {
    this.contactRepository = contactRepository;
    this.userService = userService;
    this.userMapper = userMapper;
    this.userRepository = userRepository;
  }

  //    public Collection<ContactDTO> getAll() {
  //        Collection<Contact> contacts= contactRepository.findAll();
  //        return contactMapper.mapDTOs(contacts);
  //    }

  //    public Page<ContactDTO> getContacts(Pageable pageable) {
  //        Page<Contact> contacts = contactRepository.findAll(pageable);
  //        return contactMapper.map(contacts);
  //    }

  //    public ContactDTO getContact(Long id) {
  //        Optional<Contact> contactOptional = contactRepository.findById(id);
  //        if(contactOptional.isPresent())
  //        {
  //            return contactMapper.map(contactOptional.get());
  //        }
  //        else
  //        {
  //            return null;
  //        }
  //    }

  public List<UserDTO> create(List<String> usernames) {
    List<User> users = new ArrayList<>();

    for (String username : usernames) {
      Optional<User> userOptional = userRepository.findByUsername(username);
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        Contact contact = Contact.builder()
            .sender(userService.getLoggedInUser())
            .receiver(user)
            .status(AppConstants.Types.ContactStatus.pending)
            .build();

        if (contact.getSender().getId().equals(contact.getReceiver().getId())) {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User error!!");
        }

        contact.setCreatedBy(userService.getLoggedInUserName());
        contact.setCreatedOn(Instant.now());
        contact.setModifiedBy(userService.getLoggedInUserName());
        contact.setModifiedOn(Instant.now());

        contactRepository.save(contact);
        users.add(user);
      }
    }

    return userMapper.map(users);
  }

  public Boolean deleteContact(Long contactId) {
    contactRepository.deleteById(contactId);
    return true;
  }

  public Boolean updateStatus(Long contactId, String status) {

    if (AppConstants.Types.ContactStatus.valueOf(status)
        == AppConstants.Types.ContactStatus.accepted) {
      Optional<Contact> contactOptional = contactRepository.findById(contactId);

      if (!contactOptional.isPresent()) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Contact not available !!");
      }

      if (!contactOptional.get().getReceiver().getId()
          .equals(this.userService.getLoggedInUser().getId())) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Only receiver can set status to accepted !!");
      }

    }

    contactRepository.updateStatus(contactId, AppConstants.Types.ContactStatus.valueOf(status));
    return true;

  }

  //    public ContactDTO update(Long contactId, ContactDTO contactDTO) {
  //        Optional<Contact> contactOptional = contactRepository.findById(contactId);
  //        if(contactOptional.isPresent())
  //        {
  //            Contact contact = contactOptional.get();
  //            contact.setStatus(contactDTO.getStatus());
  //            contact.setSenderId(contactDTO.getSenderId());
  //            contact.setReceiverId(contactDTO.getReceiverId());
  //            contact.setModifiedBy(userService.getLoggedInUserName());
  //            contact.setModifiedOn(Instant.now());
  //
  //            Contact savedContact = contactRepository.save(contact);
  //            return contactMapper.map(savedContact);
  //        }
  //        else
  //        {
  //            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No Contact with this id!!");
  //        }
  //    }

  //    public Boolean delete(Long id) {
  //        try {
  //            contactRepository.deleteById(id);
  //            return true;
  //
  //        } catch (Exception e) {
  //            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
  //        }
  //    }


}
