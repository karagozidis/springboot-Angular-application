package crm.cloudApp.backend.controllers.data;

import crm.cloudApp.backend.dto.data.MessageInfoDTO;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.services.data.MessageInfoService;
import crm.cloudApp.backend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/message-infos")
public class MessageInfoController {

  private final MessageInfoService messageInfoService;
  private final UserService userService;
  private final UserGroupRepository userGroupRepository;

  @Autowired
  public MessageInfoController(MessageInfoService messageInfoService,
      UserService userService, UserGroupRepository userGroupRepository) {
    this.messageInfoService = messageInfoService;
    this.userService = userService;
    this.userGroupRepository = userGroupRepository;
  }

  @GetMapping
  public List<MessageInfoDTO> get(
      @RequestParam("user_group_id") Long userGroupId,
      @RequestParam("tag") String tag,
      @RequestParam("unique-id") String uniqueId,
      @RequestParam("metadata") String metadata) {
    return messageInfoService.getMessageInfos(userGroupId, tag, uniqueId, metadata);
  }

  @GetMapping(path = "/all")
  public @ResponseBody
  Collection<MessageInfoDTO> getAllMessageInfos() {
    return messageInfoService.getAllMessageInfos();
  }

  @GetMapping(path = "/page")
  public @ResponseBody
  Page<MessageInfoDTO> getMessageInfosPage(
      @RequestParam("name") String name,
      @RequestParam("tag") String tag,
      @RequestParam("from") Instant dateFrom,
      @RequestParam("to") Instant dateTo,
      Pageable pageable) {
    return messageInfoService.getMessageInfosPage(
        name,
        tag,
        dateFrom,
        dateTo,
        pageable
    );
  }

  @GetMapping(path = "/messageInfo/{id}")
  public MessageInfoDTO getMessageInfo(@PathVariable("id") Long id) {
    return messageInfoService.getMessageInfo(id);
  }

  @GetMapping(path = "/user-group/{userGroupId}")
  public @ResponseBody
  List<MessageInfoDTO> getAllMessageInfosByUserGroupId(
      @PathVariable("userGroupId") Long userGroupId) {
    return messageInfoService.getMessageInfosByUserGroupId(userGroupId);
  }

  @GetMapping(path = "/by-unique-id")
  public @ResponseBody
  MessageInfoDTO getMessageInfoByUniqueId(@RequestParam("unique-id") String uniqueId) {
    return messageInfoService.getMessageInfosByUniqueId(uniqueId);
  }

  @GetMapping(path = "/by-metadata")
  public @ResponseBody
  MessageInfoDTO getMessageInfoByMetadata(@RequestParam("metadata") String metadata) {
    return messageInfoService.getMessageInfosByMetadata(metadata);
  }

  @GetMapping(path = "/by-metadata-and-tag")
  public @ResponseBody
  MessageInfoDTO getMessageInfoByMetadata(@RequestParam("metadata") String metadata, @RequestParam("tag") String tag) {
    return messageInfoService.getMessageInfosByMetadataAndTag(metadata,tag);
  }


  @GetMapping(path = "/user-group/{userGroupId}/tag/{tag}")
  public @ResponseBody
  List<MessageInfoDTO> getAllMessageInfosByUserGroupIdTagAndUniqueId(
      @PathVariable("userGroupId") Long userGroupId,
      @PathVariable("tag") String tag
  ) {
    return messageInfoService.getMessageInfosByUserGroupIdTagAndUniqueId(userGroupId, tag, "");
  }


  @GetMapping(path = "/by-usergroup-and-tag")
  public @ResponseBody
  MessageInfoDTO getUniqueMessageInfosByUserGroupIdTagAndUniqueId(
          @RequestParam("userGroupId") Long userGroupId,
          @RequestParam("tag") String tag
  ) {
    List<MessageInfoDTO> messageinfos = messageInfoService.getMessageInfosByUserGroupIdTagAndUniqueId(userGroupId, tag, "");
    if(messageinfos.size() == 0) return null;
    else return messageinfos.get(0);


  }

  @PostMapping(path = "/create")
  public MessageInfoDTO createMessageInfo(@RequestBody MessageInfoDTO messageInfoDTO) {

    if (messageInfoDTO.getUserGroupId().equals(0L)) {
      User user = userService.getLoggedInUser();
      UserGroup userGroup = userGroupRepository.findHandShakesUserGroupForUser(user.getId());
      if (userGroup == null) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "No Unique usergroup for user");
      }
      messageInfoDTO.setUserGroupId(userGroup.getId());
    }
    return messageInfoService.create(messageInfoDTO);
  }

  @PostMapping(path = "/create-unique-for-tag-and-usergroup")
  public MessageInfoDTO createUniqueMessageInfoForTagAndUserGroup(@RequestBody MessageInfoDTO messageInfoDTO) {

    if (messageInfoDTO.getUserGroupId().equals(0L)) {
      User user = userService.getLoggedInUser();
      UserGroup userGroup = userGroupRepository.findHandShakesUserGroupForUser(user.getId());
      if (userGroup == null) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "No Unique usergroup for user");
      }
      messageInfoDTO.setUserGroupId(userGroup.getId());
    }
    return messageInfoService.createUniqueMessageInfoForTagAndUserGroup(messageInfoDTO);
  }


  @PutMapping(path = "/update")
  public @ResponseBody
  MessageInfoDTO update(@RequestBody MessageInfoDTO messageInfoDTO) {
    return this.messageInfoService.update(messageInfoDTO);
  }


  @DeleteMapping(path = "/{id}")
  public boolean delete(@PathVariable("id") Long id) {
    return this.messageInfoService.delete(id);
  }

  @DeleteMapping(path = "by-unique-id/{uniqueId}")
  public boolean deleteByUniqueId(@PathVariable("uniqueId") String uniqueId) {
    return this.messageInfoService.deleteByUniqueId(uniqueId);
  }

}
