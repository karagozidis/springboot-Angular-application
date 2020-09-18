package crm.cloudApp.backend.controllers.users;

import crm.cloudApp.backend.dto.encryption.AesKeyDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.dto.users.UserGroupDTO;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.services.encryption.AesKeyService;
import crm.cloudApp.backend.services.users.UserGroupService;
import crm.cloudApp.backend.services.users.UserService;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@RestController
@Validated
@RequestMapping("/user-groups")
public class UserGroupController {

  private final UserGroupService userGroupService;
  private final UserService userService;
  private final AesKeyService aesKeyService;
  private final UserMapper userMapper;

  @Autowired
  public UserGroupController(UserService userService,
      UserGroupService userGroupService,
      AesKeyService aesKeyService,
      UserMapper userMapper) {
    this.userService = userService;
    this.userGroupService = userGroupService;
    this.aesKeyService = aesKeyService;
    this.userMapper = userMapper;
  }

  @GetMapping
  public @ResponseBody
  Collection<UserGroupDTO> getAllUserGroups() {
    return userGroupService.getAllUserGroups();
  }

  @GetMapping(path = "/users/current")
  public @ResponseBody
  Collection<UserGroupDTO> getUserGroupsByCurrentUser() {
    return userGroupService.getUserGroupsByCurrentUser();
  }

  @GetMapping(path = "/users/current/pages")
  public @ResponseBody
  Page<UserGroupDTO> getUserGroupsByCurrentUserPage(Pageable pageable) {
    return userGroupService.getUserGroupsByCurrentUserPage(pageable);
  }

  @GetMapping(path = "/pages")
  public @ResponseBody
  Page<UserGroupDTO> getUserGroups(Pageable pageable) {
    return userGroupService.getUserGroups(pageable);
  }

  @GetMapping(path = "/{userGroupId}")
  public UserGroupDTO getUserGroup(@PathVariable("userGroupId") Long userGroupId) {
    return userGroupService.getUserGroup(userGroupId);
  }

  @PostMapping
  public UserGroupDTO createUserGroup(@RequestBody UserGroupDTO userGroupDTO)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
    return userGroupService.create(userGroupDTO);
  }

  @PostMapping(path = "/unique-user-group")
  public Boolean createUniqueUserGroup()
      throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {

    User user = userService.getLoggedInUser();

    Long userGroupId = userGroupService.getUniqueUserGroupId(user);
    if (userGroupId > 0) {
      return true;
    }

    UserGroupDTO userGroupDTO = new UserGroupDTO();
    userGroupDTO.setName("me");
    userGroupDTO.setDescription(
        "This is generated on handshake and is default User Group for user " + userService
            .getLoggedInUserName());
    userGroupDTO.setUsers(Arrays.asList(
        userMapper.map(user)
    ));

    userGroupService.create(userGroupDTO);
    return true;
  }

  @GetMapping(path = "/unique-user-group")
  public Boolean existsUniqueUserGroup() {
    User user = userService.getLoggedInUser();
    Long userGroupId = userGroupService.getUniqueUserGroupId(user);
    if (userGroupId > 0) {
      return true;
    } else {
      return false;
    }
  }

  @PutMapping(path = "/{userGroupId}")
  public @ResponseBody
  UserGroupDTO update(@PathVariable("userGroupId") Long userGroupId,
      @RequestBody UserGroupDTO userGroupDTO)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
    return userGroupService.update(userGroupId, userGroupDTO);
  }

  @GetMapping(path = "/{userGroupId}/users")
  public @ResponseBody
  Page<UserDTO> getUsersByUserGroupId(@PathVariable("userGroupId") Long userGroupId,
                                      Pageable pageable) {
    return userService.getUsersByUserGroupId(userGroupId, pageable);
  }

  @GetMapping(path = "/{userGroupId}/aes-keys")
  public @ResponseBody
  Page<AesKeyDTO> aesKeysbyUserGroup(@PathVariable("userGroupId") Long userGroupId,
                                     Pageable pageable) {
    return aesKeyService.getAesKeysByUserGroupId(userGroupId, pageable);
  }

  @GetMapping(path = "/{userGroupId}/users/{userId}/aes-keys/active")
  public @ResponseBody
  AesKeyDTO getActiveAESKeyKey(@PathVariable("userId") Long userId,
      @PathVariable("userGroupId") Long userGroupId) {
    return aesKeyService.getActiveKey(userId, userGroupId);
  }

  @GetMapping(path = "/{userGroupId}/users/{userId}")
  public @ResponseBody
  Page<AesKeyDTO> aesKeybyUserGroupAndUser(@PathVariable("userId") Long userId,
      @PathVariable("userGroupId") Long userGroupId, Pageable pageable) {
    return aesKeyService.findAesKeysByUserIdAndUserGroupId(userId, userGroupId, pageable);
  }

  @GetMapping(path = "/verifyAllAesKeysActived/{userGroupId}")
  public @ResponseBody
  Boolean verifyAllAesKeysActived(@PathVariable("userGroupId") Long userGroupId) {
    return userGroupService.checkActiveAesKeys(userGroupId);
  }

  @DeleteMapping(path = "/{userGroupId}")
  public Boolean delete(@PathVariable("userGroupId") Long userGroupId) {
    return this.userGroupService.delete(userGroupId);
  }

}
