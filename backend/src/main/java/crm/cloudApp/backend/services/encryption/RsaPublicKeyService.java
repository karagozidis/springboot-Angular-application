package crm.cloudApp.backend.services.encryption;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.encryption.RsaPublicKeyDTO;
import crm.cloudApp.backend.mappers.encryption.RsaPublicKeyMapper;
import crm.cloudApp.backend.models.encryption.RsaPublicKey;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.encryption.AesKeyRepository;
import crm.cloudApp.backend.repositories.encryption.RsaPublicKeyRepository;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.services.users.UserGroupService;
import crm.cloudApp.backend.services.users.UserService;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
@Validated
public class RsaPublicKeyService {


  private final RsaPublicKeyRepository rsaPublicKeyRepository;
  private final UserGroupRepository userGroupRepository;
  private final RsaPublicKeyMapper rsaPublicKeyMapper;
  private final UserGroupService userGroupService;
  private final UserService userService;


  @Autowired
  public RsaPublicKeyService(RsaPublicKeyRepository rsaPublicKeyRepository,
      UserGroupRepository userGroupRepository,
      AesKeyRepository aesKeyRepository,
      UserGroupService userGroupService,
      UserService userService,
      RsaPublicKeyMapper rsaPublicKeyMapper) {
    this.rsaPublicKeyRepository = rsaPublicKeyRepository;
    this.rsaPublicKeyMapper = rsaPublicKeyMapper;
    this.userGroupRepository = userGroupRepository;
    this.userGroupService = userGroupService;
    this.userService = userService;
  }

  public Page<RsaPublicKeyDTO> getRsaPublicKeys(Pageable pageable) {
    Page<RsaPublicKey> rsaPublicKeys = rsaPublicKeyRepository.findAll(pageable);
    return rsaPublicKeyMapper.map(rsaPublicKeys);
  }

  public Page<RsaPublicKeyDTO> getRsaPublicKeyByUser(Pageable pageable) {
    Page<RsaPublicKey> rsaPublicKeys = rsaPublicKeyRepository
        .findByUserId(userService.getLoggedInUser().getId(), pageable);
    return rsaPublicKeyMapper.map(rsaPublicKeys);
  }


  public Boolean currentUserHasPublicKeys() {
    User user = userService.getLoggedInUser();
    Optional<RsaPublicKey> rsaPublicKey = rsaPublicKeyRepository
        .findActiveRsaPublicKey(user.getId());
    return rsaPublicKey.isPresent();
  }


  public Collection<RsaPublicKeyDTO> getAllRsaPublicKey() {
    Collection<RsaPublicKey> rsaPublicKeys = rsaPublicKeyRepository.findAll();
    return rsaPublicKeyMapper.mapDTOs(rsaPublicKeys);
  }

  @Transactional
  public RsaPublicKeyDTO create(RsaPublicKeyDTO rsaPublicKeyDTO)
      throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {

    rsaPublicKeyDTO.setUserId(userService.getLoggedInUser().getId());

    rsaPublicKeyRepository.disableAll(rsaPublicKeyDTO.getUserId());

    RsaPublicKey rsaPublicKey = rsaPublicKeyMapper.map(rsaPublicKeyDTO);
    rsaPublicKey.setStatus(AppConstants.Types.Status.enabled);
    rsaPublicKey.setCreatedBy(userService.getLoggedInUserName());
    rsaPublicKey.setCreatedOn(Instant.now());
    rsaPublicKey.setModifiedBy(userService.getLoggedInUserName());
    rsaPublicKey.setModifiedOn(Instant.now());

    RsaPublicKey createdRsaPublicKey = rsaPublicKeyRepository.save(rsaPublicKey);

    Collection<UserGroup> userGroups = userGroupRepository
        .findUserGroupsByUserId(rsaPublicKey.getUserId());

    for (UserGroup userGroup : userGroups) {
      userGroup.increateByOneEncryptionVersion();
      userGroupService.updateAesKeysForUserGroup(userGroup);
    }

    return rsaPublicKeyMapper.map(createdRsaPublicKey);

  }

  public RsaPublicKeyDTO getRsaPublicKeys(Long id) {
    Optional<RsaPublicKey> rsaPublicKeyOptional = rsaPublicKeyRepository.findById(id);
    if (rsaPublicKeyOptional.isPresent()) {
      return rsaPublicKeyMapper.map(rsaPublicKeyOptional.get());
    } else {
      return null;
    }
  }

  public RsaPublicKeyDTO update(RsaPublicKeyDTO rsaPublicKeyDTO) {

    Optional<RsaPublicKey> rsaPublicKeyOptional = rsaPublicKeyRepository
        .findById(rsaPublicKeyDTO.getId());
    if (rsaPublicKeyOptional.isPresent()) {
      RsaPublicKey rsaPublicKey = rsaPublicKeyOptional.get();
      rsaPublicKey.setPublicKey(rsaPublicKeyDTO.getPublicKey());
      rsaPublicKey.setSize(rsaPublicKeyDTO.getSize());
      rsaPublicKey.setStatus(rsaPublicKeyDTO.getStatus());
      rsaPublicKey.setUserId(rsaPublicKeyDTO.getUserId());
      rsaPublicKey.setModifiedBy(userService.getLoggedInUserName());
      rsaPublicKey.setModifiedOn(Instant.now());

      RsaPublicKey savedRsaPublicKey = rsaPublicKeyRepository.save(rsaPublicKey);
      return rsaPublicKeyMapper.map(savedRsaPublicKey);
    } else {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "No MessageInfo with this id!!");
    }
  }

  public boolean delete(Long id) {
    try {
      rsaPublicKeyRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
    }
  }

  public RsaPublicKeyDTO findActiveRsaPublicKey() {
    Optional<RsaPublicKey> rsaPublicKeyOptional = rsaPublicKeyRepository
        .findActiveRsaPublicKey(userService.getLoggedInUser().getId());

    if (rsaPublicKeyOptional.isPresent()) {
      return rsaPublicKeyMapper.map(rsaPublicKeyOptional.get());
    } else {
      return null;
    }

  }

}
