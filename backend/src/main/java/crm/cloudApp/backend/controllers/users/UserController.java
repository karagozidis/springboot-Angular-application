package crm.cloudApp.backend.controllers.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.auth.JWTResponseDTO;
import crm.cloudApp.backend.dto.auth.LoginDTO;
import crm.cloudApp.backend.dto.data.modules.market.ProductDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.dto.encryption.AesKeyDTO;
import crm.cloudApp.backend.dto.encryption.RsaPublicKeyDTO;
import crm.cloudApp.backend.dto.users.ContactUserDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.dto.users.UserWithKeyDTO;
import crm.cloudApp.backend.services.data.modules.market.ProductService;
import crm.cloudApp.backend.services.data.modules.market.OrderService;
import crm.cloudApp.backend.services.encryption.AesKeyService;
import crm.cloudApp.backend.services.encryption.RsaPublicKeyService;
import crm.cloudApp.backend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
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

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final RsaPublicKeyService rsaPublicKeyService;
  private final AesKeyService aesKeyService;
  private final ProductService productService;
  private final OrderService orderService;


  @Autowired
  public UserController(UserService userService,
      RsaPublicKeyService rsaPublicKeyService,
      AesKeyService aesKeyService,
      ProductService productService,
      OrderService orderService) {
    this.userService = userService;
    this.rsaPublicKeyService = rsaPublicKeyService;
    this.aesKeyService = aesKeyService;
    this.productService = productService;
    this.orderService = orderService;
  }

  @GetMapping
  public @ResponseBody
  Collection<UserDTO> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping(path = "/crm-verified")
  public @ResponseBody
  Collection<UserDTO> getCrmVerifiedUsers() {
    return userService.getCrmVerifiedUsers();
  }


  @GetMapping(path = "/pages")
  public @ResponseBody
  Page<UserDTO> getUsers(Pageable pageable) {
    return userService.getUsers(pageable);
  }

  @GetMapping(path = "/{userId}")
  public UserDTO getUser(@PathVariable("userId") Long userId) {
    return userService.getUser(userId);
  }

  @GetMapping(path = "/current")
  public UserDTO getCurrentUser() {
    return userService.getCurrentUser();
  }

  @PostMapping
  public UserDTO createUser(@RequestBody UserDTO userDTO) {
    return userService.create(userDTO);
  }

  @PostMapping(path = "/metadata")
  public Boolean setUserMetadata(@RequestBody String metadata) {
    return userService.updateUserMetadata(metadata);
  }

  @GetMapping(path = "/metadata")
  public String getUserMetadata() {
    return userService.getUserMetadata();
  }

  @PutMapping(path = "/user-details")
  public UserDTO updateUser(@RequestBody UserDTO userDTO) {
    return this.userService.updateUser(userDTO);
  }

  @PutMapping(path = "/details")
  public UserDTO update(@RequestBody UserDTO userDTO) {
    return this.userService.update(userDTO);
  }

  @PutMapping(path = "/password")
  public Boolean updatePassword(@RequestParam("old") String oldPassword,
      @RequestParam("new") String newPassword) {
    return this.userService.updatePassword(oldPassword, newPassword);
  }

  @PutMapping(path = "/user-password")
  public Boolean updatePassword(@RequestParam("id") Long userId,
      @RequestParam("password") String newPassword) {
    return this.userService.updateUsersPassword(userId, newPassword);
  }

  @DeleteMapping(path = "/{userId}")
  public Boolean delete(@PathVariable("userId") Long userId) {
    return this.userService.delete(userId);
  }

  @GetMapping(path = "/statuses")
  public @ResponseBody
  Collection<AppConstants.Types.UserStatus> getAllStatuses() {
    return Arrays.asList(AppConstants.Types.UserStatus.class.getEnumConstants());
  }

  @GetMapping(path = "/current/contacts")
  public @ResponseBody
  List<ContactUserDTO> getContactsOfCurrentUser(@RequestParam("status") String status) {
    return userService.getContactsOfUser(status);
  }

  @GetMapping(path = "/current/contacts/page")
  public @ResponseBody
    Page<ContactUserDTO> getContactsOfCurrentUserPage(
        @RequestParam("status") String status,
      Pageable pageable) {
    return userService.getContactsOfUserPage(status, pageable);
  }

  @GetMapping(path = "/current/contact-users")
  public @ResponseBody
  List<UserDTO> getContactUsersOfCurrentUser(@RequestParam("status") String status) {
    return userService.getUserContactsOfUser(status);
  }

  @GetMapping(path = "/current/rsa-keys/public")
  public @ResponseBody
  Page<RsaPublicKeyDTO> allRsaPublicKeysbyUser(Pageable pageable) {
    return rsaPublicKeyService.getRsaPublicKeyByUser(pageable);
  }

  @GetMapping(path = "/current/rsa-keys/public/active")
  public @ResponseBody
  RsaPublicKeyDTO findActiveRsaPublicKey() {
    return rsaPublicKeyService.findActiveRsaPublicKey();
  }

  //AES Keys
  @GetMapping(path = "/current/aes-keys")
  public @ResponseBody
  Page<AesKeyDTO> allAESKeysByUser(Pageable pageable) {
    return aesKeyService.getAesKeysByUserId(pageable);
  }

  @GetMapping(path = "/current/user-group/{userGroupId}/aes-keys/active")
  public @ResponseBody
  AesKeyDTO activeAESKeyByUserAndUserGroup(@PathVariable("userGroupId") Long userGroupId) {
    return aesKeyService.getActiveKey(userService.getLoggedInUser().getId(), userGroupId);
  }

  @GetMapping(path = "/current/user-group/{userGroupId}/aes-keys/over/{encryptionVersion}")
  public @ResponseBody
  List<AesKeyDTO> activeAESKeysByUserAndUserGroupOverEncryptionVersion(
      @PathVariable("userGroupId") Long userGroupId,
      @PathVariable("encryptionVersion") int encryptionVersion) {
    return aesKeyService
        .getKeysOverEncryptionVersion(userService.getLoggedInUser().getId(), userGroupId,
            encryptionVersion);
  }

  @GetMapping(path = "/current/market/products")
  public @ResponseBody
  List<ProductDTO> getProducts() {
    return this.productService.getProductsByCurrentUser();
  }


  @GetMapping(path = "/current/market/orders")
  public @ResponseBody
  List<SingleOrderDTO> getOrders(
      @RequestParam("orderType") String orderType,
      @RequestParam("bidTimeFrom") Instant bidTimeFrom,
      @RequestParam("bidTimeTo") Instant bidTimeTo,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {

    return this.orderService.getOrdersByCurrentUser(
        orderType,
        bidTimeFrom,
        bidTimeTo,
        marketCode
    );

  }

  @GetMapping(path = "/current/market/matched-orders")
  public @ResponseBody
  List<TradeDTO> getMatchedOrders(
      @RequestParam("orderType") String orderType,
      @RequestParam("bidTimeFrom") Instant bidTimeFrom,
      @RequestParam("bidTimeTo") Instant bidTimeTo,
      @RequestParam(name = "market-code", defaultValue = "CY", required = false) String marketCode) {

    return this.orderService.getMatchedOrdersByCurrentUser(
        orderType,
        bidTimeFrom,
        bidTimeTo,
        marketCode
    );
  }

  /**
   * Authenticates a user and returns a JWT if authentication was successful.
   *
   * @param loginDTO The email and password of the user to authenticate.
   * @return Returns the JWT.
   */
  @PostMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
  @Transactional
  public JWTResponseDTO authenticate(@RequestBody LoginDTO loginDTO) {
    log.debug("authenticating");

    return userService.authenticate(loginDTO.getUsername(), loginDTO.getPassword());
  }

  @GetMapping(path = "/getUsersWithPublicKeyStatus/all")
  public @ResponseBody
  Collection<UserWithKeyDTO> getAllUsersWithPublicKeyStatus() {
    return userService.getUsersWithPublicKeyStatus();
  }

}
