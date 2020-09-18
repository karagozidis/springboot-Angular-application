package crm.cloudApp.backend.services.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.dto.users.UserGroupDTO;
import crm.cloudApp.backend.mappers.users.UserGroupMapper;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.models.encryption.AesKey;
import crm.cloudApp.backend.models.encryption.RsaPublicKey;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.encryption.AesKeyRepository;
import crm.cloudApp.backend.repositories.encryption.RsaPublicKeyRepository;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.repositories.users.UserRepository;
import crm.cloudApp.backend.utils.encryption.AesEncryptionUtil;
import crm.cloudApp.backend.utils.encryption.RsaEncryptionUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class UserGroupService {

  private final UserGroupRepository userGroupRepository;
  private final RsaPublicKeyRepository rsaPublicKeyRepository;
  private final AesKeyRepository aesKeyRepository;
  private final UserRepository userRepository;
  private final UserGroupMapper userGroupMapper;
  private final UserService userService;
  private final UserMapper userMapper;

  public UserGroupService(UserGroupRepository userGroupRepository,
      UserRepository userRepository,
      RsaPublicKeyRepository rsaPublicKeyRepository,
      AesKeyRepository aesKeyRepository,
      UserService userService,
      UserGroupMapper userGroupMapper,
      UserMapper userMapper) {
    this.userGroupRepository = userGroupRepository;
    this.userRepository = userRepository;
    this.rsaPublicKeyRepository = rsaPublicKeyRepository;
    this.aesKeyRepository = aesKeyRepository;
    this.userGroupMapper = userGroupMapper;
    this.userService = userService;

    this.userMapper = userMapper;
  }


  public Page<UserGroupDTO> getUserGroups(Pageable pageable) {
    Page<UserGroup> userGroups = userGroupRepository.findAll(pageable);
    return userGroupMapper.map(userGroups);
  }

  @Transactional
  public UserGroupDTO create(UserGroupDTO userGroupDTO)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
    UserGroup userGroup = userGroupMapper.map(userGroupDTO);

    List<Long> userIds = new ArrayList<>();
    userGroupDTO.getUsers().forEach(e -> userIds.add(e.getId()));
    if (userIds.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No users selected !!");
    }

    List<User> users = userRepository.findAllById(userIds);
    if (users.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No users selected !!");
    }

    User currentUser = userService.getLoggedInUser();
    boolean currentUserExists = users.stream()
        .anyMatch(e -> e.getUsername().equals(currentUser.getUsername()));

    if (!currentUserExists) {
      users.add(currentUser);
    }

    userGroup.setUsers(users);

    userGroup.setEncryptionVersion(1);

    userGroup.setCreatedBy(userService.getLoggedInUserName());
    userGroup.setCreatedOn(Instant.now());
    userGroup.setModifiedBy(userService.getLoggedInUserName());
    userGroup.setModifiedOn(Instant.now());
    userGroup.setStatus(AppConstants.Types.UserGroupStatus.enabled);

    UserGroup createdUserGroup = userGroupRepository.save(userGroup);
    this.updateAesKeysForUserGroup(createdUserGroup);

    return userGroupMapper.map(createdUserGroup);
  }

  //    @Transactional
  //    public UserGroup createUserUniqueUserGroup(User user) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
  //       // UserGroup userGroup = userGroupMapper.map(userGroupDTO);
  //        UserGroup userGroup = new UserGroup();
  //
  //        userGroup.setName("me");
  //        userGroup.setDescription("This is generated on handshake and is default User Group for user " + userService.getLoggedInUserName());
  //        userGroup.setUsers(Arrays.asList(user));
  //        userGroup.setEncryptionVersion(1);
  //
  //        userGroup.setCreatedBy(userService.getLoggedInUserName());
  //        userGroup.setCreatedOn(Instant.now());
  //        userGroup.setModifiedBy(userService.getLoggedInUserName());
  //        userGroup.setModifiedOn(Instant.now());
  //        userGroup.setStatus(UserGroupStatus.enabled);
  //
  //        UserGroup createdUserGroup = userGroupRepository.save(userGroup);
  //        List<User> updatedUsers = this.updateAesKeysForUserGroup(createdUserGroup);
  //
  //        return createdUserGroup;
  //    }

  public List<UserDTO> updateAesKeysForUserGroup(UserGroupDTO userGroupDTO)
      throws NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
    List<User> users = this.updateAesKeysForUserGroup(userGroupMapper.map(userGroupDTO));
    return userMapper.map(users);
  }

  public List<User> updateAesKeysForUserGroup(UserGroup userGroup)
      throws NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {

    UUID uuid = UUID.randomUUID();
    String newKey = AesEncryptionUtil.generateStringKey(uuid.toString());
    List<User> updatedUsers = new ArrayList<>();

    Collection<User> users = userRepository.findUsersByUserGroupId(userGroup.getId());
    for (User user : users) {
      aesKeyRepository.deactivateByUserAndUserGroup(user.getId(), userGroup.getId());

      Optional<RsaPublicKey> rsaPublicKeyOptional = this.rsaPublicKeyRepository
          .findActiveRsaPublicKey(user.getId());
      if (rsaPublicKeyOptional.isPresent()) {
        updatedUsers.add(user);
        RsaPublicKey rsaPublicKey = rsaPublicKeyOptional.get();
        RsaEncryptionUtil rsAencryption = new RsaEncryptionUtil();
        rsAencryption.setKey(rsaPublicKey.getPublicKey());
        String encryptedAES = rsAencryption.encrypt(newKey);

        AesKey aesKey = new AesKey();
        aesKey.setEncryptedKey(encryptedAES);
        aesKey.setSize(encryptedAES.length());
        aesKey.setRsaPublicKeyId(rsaPublicKey.getId());
        aesKey.setUserGroupId(userGroup.getId());
        aesKey.setStatus(AppConstants.Types.Status.enabled);
        aesKey.setUserId(user.getId());
        aesKey.setEncryptionVersion(userGroup.getEncryptionVersion());
        aesKey.setCreatedBy(userService.getLoggedInUserName());
        aesKey.setCreatedOn(Instant.now());
        aesKey.setModifiedBy(userService.getLoggedInUserName());
        aesKey.setModifiedOn(Instant.now());
        aesKey.setKeyUUID(rsaPublicKey.getKeyUUID());
        aesKeyRepository.save(aesKey);

      }
    }
    return updatedUsers;
  }

  public UserGroupDTO getUserGroup(Long id) {
    Optional<UserGroup> userGroupOptional = userGroupRepository.findById(id);
    if (userGroupOptional.isPresent()) {
      return userGroupMapper.map(userGroupOptional.get());
    } else {
      return null;
    }
  }

  @Transactional
  public UserGroupDTO update(Long userGroupId, UserGroupDTO userGroupDTO)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
    Optional<UserGroup> userGroupOptional = userGroupRepository.findById(userGroupId);
    if (userGroupOptional.isPresent()) {
      UserGroup userGroup = userGroupOptional.get();
      userGroup.setDescription(userGroupDTO.getDescription());
      userGroup.setModifiedBy(userService.getLoggedInUserName());
      userGroup.setModifiedOn(Instant.now());
      userGroup.setName(userGroupDTO.getName());

      List<Long> userIds = new ArrayList<>();
      userGroupDTO.getUsers().forEach(e -> userIds.add(e.getId()));
      List<User> users = new ArrayList<>();
      if (!userIds.isEmpty()) {
        users = userRepository.findAllById(userIds);
      }
      userGroup.setUsers(users);

      userGroup.increateByOneEncryptionVersion();
      UserGroup savedUserGroup = userGroupRepository.save(userGroup);
      this.updateAesKeysForUserGroup(savedUserGroup);

      return userGroupMapper.map(savedUserGroup);
    } else {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "No usergroup with this id!!");
    }
  }


  @Transactional
  public boolean delete(Long id) {
    try {
      Optional<UserGroup> userGroupOptional = userGroupRepository.findById(id);
      if (userGroupOptional.isPresent()) {
        UserGroup userGroup = userGroupOptional.get();
        userGroup.setStatus(AppConstants.Types.UserGroupStatus.deleted);
        userGroupRepository.save(userGroup);
      } else {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "This user group does not exist.");
      }

      return true;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
    }
  }


  public Collection<UserGroupDTO> getAllUserGroups() {
    Collection<UserGroup> userGroups = userGroupRepository.findAll();
    return userGroupMapper.mapDTOs(userGroups);
  }

  public Boolean checkActiveAesKeys(Long userGroupId) {
    Collection<User> users = userRepository.findUsersByUserGroupId(userGroupId);
    Collection<AesKey> aesKeys = aesKeyRepository.findActiveAesKeysByUserGroupId(userGroupId);
    return (users.size() == aesKeys.size());
  }

  public Long getUniqueUserGroupId(User user) {
    UserGroup userGroup = userGroupRepository.findHandShakesUserGroupForUser(user.getId());
    if (userGroup == null) {
      return 0L;
    } else {
      return userGroup.getId();
    }
  }

  public Collection<UserGroupDTO> getUserGroupsByCurrentUser() {
    User user = userService.getLoggedInUser();
    Collection<UserGroup> userGroups = userGroupRepository.findUserGroupsByUserId(user.getId());
    return userGroupMapper.mapDTOs(userGroups);
  }

  public Page<UserGroupDTO> getUserGroupsByCurrentUserPage(Pageable pageable) {
    User user = userService.getLoggedInUser();
    Page<UserGroup> userGroups = userGroupRepository.findUserGroupsByUserIdPage(pageable,
        user.getId()
         );
    return userGroupMapper.map(userGroups);
  }


}
