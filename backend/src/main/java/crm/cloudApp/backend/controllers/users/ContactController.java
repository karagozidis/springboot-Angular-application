package crm.cloudApp.backend.controllers.users;

import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.services.users.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/contacts")
public class ContactController {

  private final ContactService contactService;

  @Autowired
  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @PostMapping
  public List<UserDTO> create(@RequestBody List<String> usernames) {
    return contactService.create(usernames);
  }


  @PutMapping(path = "/{contact-id}")
  public @ResponseBody
  Boolean update(@PathVariable("contact-id") Long contactId,
      @RequestParam("status") String status) {
    return this.contactService.updateStatus(contactId, status);
  }

  @DeleteMapping(path = "/{contactId}")
  public @ResponseBody
  Boolean deleteContact(@PathVariable("contactId") Long contactId) {
    return this.contactService.deleteContact(contactId);
  }

}
