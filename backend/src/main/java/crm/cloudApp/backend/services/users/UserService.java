package crm.cloudApp.backend.services.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.auth.JWTResponseDTO;
import crm.cloudApp.backend.dto.users.ContactUserDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.dto.users.UserWithKeyDTO;
import crm.cloudApp.backend.mappers.users.ContactUserMapper;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.mappers.users.UserWithKeyMapper;
import crm.cloudApp.backend.models.users.Contact;
import crm.cloudApp.backend.models.users.Country;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.repositories.encryption.RsaPublicKeyRepository;
import crm.cloudApp.backend.repositories.users.ContactRepository;
import crm.cloudApp.backend.repositories.users.CountryRepository;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.repositories.users.UserRepository;
import crm.cloudApp.backend.services.auth.JWTService;

import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@Validated
public class UserService {

  private final ContactRepository contactRepository;
  private final UserRepository userRepository;
  private final UserGroupRepository userGroupRepository;
  private final CountryRepository countryRepository;
  private final UserMapper userMapper;
  private final UserWithKeyMapper userWithKeyMapper;
  private final PasswordEncoder passwordEncoder;
  private final JWTService jwtService;
  private final ContactUserMapper contactUserMapper;
  private final RsaPublicKeyRepository rsaPublicKeyRepository;
  private static final String COUNTRY_DOES_NOT_EXIST_MESSAGE = "Country does not exist!!";

  @Autowired
  public UserService(
      ContactRepository contactRepository,
      UserRepository userRepository,
      UserGroupRepository userGroupRepository,
      CountryRepository countryRepository,
      UserMapper userMapper,
      PasswordEncoder passwordEncoder,
      JWTService jwtService,
      UserWithKeyMapper userWithKeyMapper,
      ContactUserMapper contactUserMapper,
      RsaPublicKeyRepository rsaPublicKeyRepository) {

    this.contactRepository = contactRepository;
    this.userRepository = userRepository;
    this.userGroupRepository = userGroupRepository;
    this.countryRepository = countryRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.userWithKeyMapper = userWithKeyMapper;
    this.contactUserMapper = contactUserMapper;
    this.rsaPublicKeyRepository = rsaPublicKeyRepository;
  }

  public Collection<UserWithKeyDTO> getUsersWithPublicKeyStatus() {
    Collection<User> users = userRepository.findAllByStatusIsNotLike(AppConstants.Types.UserStatus.deleted);
    return userWithKeyMapper.mapDTOs(users);
  }

  public Collection<UserDTO> getAllUsers() {
    Collection<User> users = userRepository.findAllByStatusIsNotLike(AppConstants.Types.UserStatus.deleted);
    return userMapper.mapDTOs(users);
  }

  public Page<UserDTO> getUsers(Pageable pageable) {
    Page<User> users = userRepository.findAllNotDeleted(pageable);
    return userMapper.map(users);
  }

  public Boolean updateUserMetadata(String metadata) {
    User user = this.getLoggedInUser();
    userRepository.updateUserMetadata(metadata, user.getId());
    return true;
  }

  public UserDTO create(UserDTO userDTO) {
    User user = userMapper.map(userDTO);
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    boolean existingUser = userRepository.userExists(userDTO.getUsername());
    if (existingUser) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User already exists!!");
    } else {

      if (user.getCountry() != null) {
        Optional<Country> optionalCountry = this.countryRepository
            .findById(user.getCountry().getId());
        if (!optionalCountry.isPresent()) {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
              COUNTRY_DOES_NOT_EXIST_MESSAGE);
        } else {
          user.setCountry(optionalCountry.get());
        }
      }

      user.setCreatedBy(this.getLoggedInUserName());
      user.setCreatedOn(Instant.now());
      user.setModifiedBy(this.getLoggedInUserName());
      user.setModifiedOn(Instant.now());

      User createdUser = userRepository.save(user);

      UserDTO responseUserDTO = userMapper.map(createdUser);
      responseUserDTO.setPassword("");

      return responseUserDTO;
    }
  }

  public UserDTO getUser(Long id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
      return userMapper.map(userOptional.get());
    } else {
      return null;
    }
  }

  public UserDTO getUserByUsername(String username) {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      return userMapper.map(userOptional.get());
    } else {
      return null;
    }
  }

  public UserDTO update(UserDTO userDTO) {
    User user = this.getLoggedInUser();

    user.setEmail(userDTO.getEmail());

    if (userDTO.getCountry() != null) {
      Optional<Country> optionalCountry = this.countryRepository
          .findById(userDTO.getCountry().getId());
      if (!optionalCountry.isPresent()) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            COUNTRY_DOES_NOT_EXIST_MESSAGE);
      } else {
        user.setCountry(optionalCountry.get());
      }
    } else {
      user.setCountry(null);
    }

    user.setModifiedBy(this.getLoggedInUserName());
    user.setModifiedOn(Instant.now());

    User savedUser = userRepository.save(user);
    return userMapper.map(savedUser);
  }

  public UserDTO updateUser(UserDTO userDTO) {
    User user = userRepository.findUserById(userDTO.getId());
    user.setUsername(userDTO.getUsername());
    user.setEmail(userDTO.getEmail());

    user.setRolesCrm(userDTO.getRolesCrm());
    user.setRolesMarket(userDTO.getRolesMarket());
    user.setModulesCrm(userDTO.getModulesCrm());
    user.setModulesMarket(userDTO.getModulesMarket());
    user.setDefaultMarket(userDTO.getDefaultMarket());
    user.setMarket(userDTO.getMarket());
    user.setLandingPage(userDTO.getLandingPage());
    user.setActiveSubstationUserGroupId(userDTO.getActiveSubstationUserGroupId());

    user.setStatus(userDTO.getStatus());

    if (userDTO.getCountry() != null) {
      Optional<Country> optionalCountry = this.countryRepository
          .findById(userDTO.getCountry().getId());
      if (!optionalCountry.isPresent()) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            COUNTRY_DOES_NOT_EXIST_MESSAGE);
      } else {
        user.setCountry(optionalCountry.get());
      }
    } else {
      user.setCountry(null);
    }

    user.setModifiedBy(this.getLoggedInUserName());
    user.setModifiedOn(Instant.now());

    User savedUser = userRepository.save(user);
    return userMapper.map(savedUser);
  }

  public Boolean delete(Long id) {
    try {
      User user = userRepository.findUserById(id);
      user.setStatus(AppConstants.Types.UserStatus.deleted);
      userRepository.save(user);
      return true;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
    }
  }

  public Page<UserDTO> getUsersByUserGroupId(Long userGroupId, Pageable pageable) {
    Page<User> users = userRepository.findUsersByUserGroupId(userGroupId, pageable);
    return userMapper.map(users);
  }

  //    public Page<UserDTO> getContactsOfUser(Pageable pageable) {
  ////        Page<User> users = userRepository.getContactsOfUser(getLoggedInUser().getId(), ContactStatus.accepted,pageable);
  ////        return userMapper.map(users);
  //        return null;
  //    }

  public String getLoggedInUserName() {
    try {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String username = "";
      if (principal instanceof UserDetails) {
        username = ((UserDetails) principal).getUsername();
      } else {
        username = principal.toString();
      }
      return username;
    } catch (Exception ex) {
      return "";
    }
  }

  public User getLoggedInUser() {

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      return null;
    }

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = "";
    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }
    Optional<User> userOptional = this.userRepository.findByUsername(username);
    if (!userOptional.isPresent()) {
      return null;
    }

    return userOptional.get();
  }

  private boolean verifyPassword(User user, String enteredPassword) {
    return passwordEncoder.matches(enteredPassword, user.getPassword());
  }

  /**
   * Attempts to authenticate a user.
   *
   * @param username The email to authenticate with.
   * @param enteredPassword The password to authenticate with.
   * @return Returns a JWT if authentication was successful, or null otherwise.
   */
  @Transactional
  public JWTResponseDTO authenticate(@NotBlank String username, @NotBlank String enteredPassword) {

    String jwt = "";
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      log.debug("User exists");
      User user = userOptional.get();

      if (verifyPassword(user, enteredPassword)) {
        jwt = jwtService.generateJwt(user.getUsername(), userOptional.get().getId());
        log.debug("User is verified");
      }
    }
    return new JWTResponseDTO().setJwt(jwt);
  }



  public Page<ContactUserDTO> getContactsOfUserPage(String status, Pageable pageable) {

    Page<Contact> contactsPage;
    if (status.equals("") || status.equals("all")) {
      contactsPage = contactRepository.getContactsOfUserPage(this.getLoggedInUser().getId(), pageable);
    } else {
      contactsPage = contactRepository
          .getContactsOfUserByStatusPage(this.getLoggedInUser().getId(),
              AppConstants.Types.ContactStatus.valueOf(status),
              pageable);
    }

    return this.contactUserMapper.mapUserContacts(contactsPage, this.getLoggedInUser());
  }


  public List<ContactUserDTO> getContactsOfUser(String status) {

    List<Contact> contacts;
    if (status.equals("") || status.equals("all")) {
      contacts = contactRepository.getContactsOfUser(this.getLoggedInUser().getId());
    } else {
      contacts = contactRepository
          .getContactsOfUserByStatus(this.getLoggedInUser().getId(), AppConstants.Types.ContactStatus.valueOf(status));
    }

    return this.contactUserMapper.mapUserContacts(contacts, this.getLoggedInUser());
  }


  public List<UserDTO> getUserContactsOfUser(String status) {

    this.getLoggedInUser().getId();
    List<User> users;
    if (status.equals("") || status.equals("all")) {
      users = userRepository.getContactsOfUser(this.getLoggedInUser().getId());
    } else {
      users = userRepository
          .getContactsOfUser(this.getLoggedInUser().getId(), AppConstants.Types.ContactStatus.valueOf(status));
    }

    return this.userMapper.map(users);
  }

  public UserDTO getCurrentUser() {
    User user = this.getLoggedInUser();
    return userMapper.map(user);
  }

  public Collection<UserDTO> getCrmVerifiedUsers() {

    List<Long> usersWithPersonalUserGroup = userGroupRepository.findUserIdsWithPersonalUserGroup();
    List<Long> usersWithRsaKey = rsaPublicKeyRepository.findUserIdsWithKey();
    List<Long> union = usersWithPersonalUserGroup.stream()
        .filter(id -> usersWithRsaKey.contains(id)).collect(Collectors.toList());

    Collection<User> users = new ArrayList<>();
    if (!union.isEmpty()) {
      users = userRepository.findByIds(union);
    }
    return userMapper.mapDTOs(users);
  }

  public String getUserMetadata() {
    User user = this.getLoggedInUser();
    return user.getEncryptedPrivateKey();
  }


  public Boolean updatePassword(@NotBlank String oldPassword, @NotBlank String newPassword) {
    User user = this.getLoggedInUser();

    if (verifyPassword(user, oldPassword)) {
      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(user);
      return true;
    }

    return false;
  }

  public Boolean updateUsersPassword(Long userId, @NotBlank String newPassword) {
    Optional<User> userOptional = this.userRepository.findById(userId);
    if (!userOptional.isPresent()) {
      return false;
    }
    User user = userOptional.get();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    return true;
  }


}
