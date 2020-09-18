package crm.cloudApp.backend.services.encryption;

import crm.cloudApp.backend.dto.encryption.AesKeyDTO;
import crm.cloudApp.backend.mappers.encryption.AesKeyMapper;
import crm.cloudApp.backend.models.encryption.AesKey;
import crm.cloudApp.backend.repositories.encryption.AesKeyRepository;
import crm.cloudApp.backend.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class AesKeyService {

  private final AesKeyRepository aesKeyRepository;
  private final AesKeyMapper aesKeyMapper;
  private final UserService userService;

  @Autowired
  public AesKeyService(AesKeyRepository aesKeyRepository, UserService userService,
      AesKeyMapper aesKeyMapper) {
    this.aesKeyRepository = aesKeyRepository;
    this.userService = userService;
    this.aesKeyMapper = aesKeyMapper;
  }

  public Page<AesKeyDTO> getAesKey(Pageable pageable) {
    Page<AesKey> aesKeys = aesKeyRepository.findAll(pageable);
    return aesKeyMapper.map(aesKeys);
  }

  public Collection<AesKeyDTO> getAllAesKey() {
    Collection<AesKey> aesKeys = aesKeyRepository.findAll();
    return aesKeyMapper.mapDTOs(aesKeys);
  }

  public AesKeyDTO create(AesKeyDTO aesKeyDTO) {
    AesKey aesKey = aesKeyMapper.map(aesKeyDTO);
    aesKey.setCreatedBy(userService.getLoggedInUserName());
    aesKey.setCreatedOn(Instant.now());
    aesKey.setModifiedBy(userService.getLoggedInUserName());
    aesKey.setModifiedOn(Instant.now());
    AesKey createdAesKey = aesKeyRepository.save(aesKey);
    return aesKeyMapper.map(createdAesKey);
  }

  public Page<AesKeyDTO> getAesKeysByUserGroupId(Long userGroupId, Pageable pageable) {
    Page<AesKey> aesKeys = aesKeyRepository.findAesKeysByUserGroupId(userGroupId, pageable);
    return aesKeyMapper.map(aesKeys);
  }

  public Page<AesKeyDTO> getAesKeysByUserId(Pageable pageable) {
    Page<AesKey> aesKeys = aesKeyRepository
        .findAesKeysByUserId(userService.getLoggedInUser().getId(), pageable);
    return aesKeyMapper.map(aesKeys);
  }

  public Page<AesKeyDTO> findAesKeysByUserIdAndUserGroupId(Long userId, Long userGroupId,
      Pageable pageable) {
    Page<AesKey> aesKeys = aesKeyRepository
        .findAesKeysByUserIdAndUserGroupId(userId, userGroupId, pageable);
    return aesKeyMapper.map(aesKeys);
  }

  public Page<AesKeyDTO> getAesKeysByRsaPublicKeyId(Long rsaPublicKeyId, Pageable pageable) {
    Page<AesKey> aesKeys = aesKeyRepository.findAesKeysByRsaPublicKeyId(rsaPublicKeyId, pageable);
    return aesKeyMapper.map(aesKeys);
  }

  public AesKeyDTO getAesKey(Long id) {
    Optional<AesKey> aesKeyOptional = aesKeyRepository.findById(id);
    if (aesKeyOptional.isPresent()) {
      return aesKeyMapper.map(aesKeyOptional.get());
    } else {
      return null;
    }
  }

  public AesKeyDTO update(AesKeyDTO aesKeyDTO) {
    Optional<AesKey> aesKeyOptional = aesKeyRepository.findById(aesKeyDTO.getId());
    if (aesKeyOptional.isPresent()) {
      AesKey aesKey = aesKeyOptional.get();
      aesKey.setSize(aesKeyDTO.getSize());
      aesKey.setEncryptedKey(aesKeyDTO.getEncryptedKey());
      aesKey.setUserGroupId(aesKeyDTO.getUserGroupId());
      aesKey.setRsaPublicKeyId(aesKeyDTO.getRsaPublicKeyId());
      aesKey.setModifiedBy(userService.getLoggedInUserName());
      aesKey.setModifiedOn(Instant.now());
      AesKey savedAesKey = aesKeyRepository.save(aesKey);
      return aesKeyMapper.map(savedAesKey);
    } else {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "No MessageInfo with this id!!");
    }
  }

  public void delete(Long id) {
    try {
      aesKeyRepository.deleteById(id);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
    }
  }

  public AesKeyDTO getActiveKey(Long userId, Long userGroupId) {
    AesKey aesKey = aesKeyRepository.getActiveKey(userId, userGroupId);
    return aesKeyMapper.map(aesKey);
  }

  public List<AesKeyDTO> getKeysOverEncryptionVersion(Long userId, Long userGroupId,
      int encryptionVersion) {
    List<AesKey> aesKey = aesKeyRepository
        .getKeysOverEncryptionVersion(userId, userGroupId, encryptionVersion);
    return aesKeyMapper.map(aesKey);
  }


}
