package crm.cloudApp.backend.integration.users;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import crm.cloudApp.backend.App;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.encryption.RsaPublicKeyDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.utils.encryption.RsaEncryptionUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = App.class)

@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
    locations = "classpath:applicationIT.properties")
public class UserTest {

  @Autowired
  private MockMvc mvc;

  List<UserDTO> userDTOs;

  @Test
  public void existing_username_on_creation_scenario() throws Exception {

    String username = "existingUserScenarioUser";

    UserDTO userDTO = UserDTO.builder()
        .username(username)
        .email("existingUserScenarioUser@mail.com")
        .password(RandomStringUtils.random(10, true, true))
        .status(AppConstants.Types.UserStatus.enabled)
        .country(null)
        .rolesCrm("admin")
        .rolesMarket("admin")
        .modulesCrm("hyppoio")
        .modulesMarket("market")
        .build();

    ResultActions resultActions = mvc.perform(post("/users/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userDTO))
    );
    UserDTO userDTO2 = UserDTO.builder()
        .username(username)
        .email("existingUser2ScenarioUser@mail.com")
        .password(RandomStringUtils.random(10, true, true))
        .status(AppConstants.Types.UserStatus.enabled)
        .country(null)
        .rolesCrm("admin")
        .rolesMarket("admin")
        .modulesCrm("hyppoio")
        .modulesMarket("market")
        .build();

    resultActions = mvc.perform(post("/users/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userDTO))
    );

    resultActions.andExpect(status().is5xxServerError());
    MvcResult result = resultActions.andReturn();
    assertThat(result.getResponse().getErrorMessage()).isEqualTo("User already exists!!");

  }


  @Test
  public void generate_and_delete_users_scenario() throws Exception {

    this.userDTOs = createRandomUsers(24);

    for (UserDTO userDTO : userDTOs) {

      ResultActions resultActions = mvc.perform(delete("/users/" + userDTO.getId())
          .contentType(MediaType.APPLICATION_JSON)
      );

      resultActions.andExpect(status().isOk());

      MvcResult result = resultActions.andReturn();
      String userDTO1Str = result.getResponse().getContentAsString();
      Boolean userIsDeleted = new ObjectMapper().readValue(userDTO1Str, Boolean.class);

      assertThat(userIsDeleted).isEqualTo(true);

    }

  }


  /*
   *
   * RsaPublicKeys creation for users
   * The above steps are taking place for every user
   *  1. A random key is generated
   *  2. A logIn request is made to the Crm central for the specific user
   *  3. A RsaPublicKey action is done to the Crm central
   *
   *   Returns
   *  1. An Objects List that contains A List of RsaPublicKeyDTO and a List of the keyPairs
   *
   */
  public List<Object> createRandomRsaPublicKeysForUsers(List<UserDTO> userDTOs, int totalRSAKeys)
      throws Exception {
    List<RsaPublicKeyDTO> rsaPublicKeyDTOs = new ArrayList<>();
    List<KeyPair> keyPairs = new ArrayList<>();

    for (int i = 0; i < totalRSAKeys; i++) {

      KeyPair keyPair = RsaEncryptionUtil.generateKey();
      String publicKey = RsaEncryptionUtil.getKeyAsString(keyPair.getPublic());
      keyPairs.add(keyPair);

      RsaPublicKeyDTO rsaPublicKeyDTO = new RsaPublicKeyDTO(
          publicKey,
          publicKey.length(),
          AppConstants.Types.Status.enabled,
          userDTOs.get(i).getId(),
          "UUID" + userDTOs.get(i).getId()
      );

      /*
       * Login with user i, in order to create your RSA Public key
       */
      ResultActions resultActions = mvc.perform(post("/users/auth/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(
              "{\"username\":\"" + userDTOs.get(i).getUsername() + "\",\"password\":\"" + userDTOs
                  .get(i).getPassword() + "\"}")
      );
      MvcResult result = resultActions.andReturn();
      String loginResponce = result.getResponse().getContentAsString();
      String jwt = JsonPath.parse(loginResponce).read("jwt", String.class);

      resultActions.andExpect(status().isOk());
      assertThat(jwt).isNotEqualTo("");


      /*
       * Create your RSA Public key for user i
       */
      resultActions = mvc.perform(post("/encryption/rsa-keys/public/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(rsaPublicKeyDTO))
          .header("Authorization", "Bearer " + jwt)
      );
      result = resultActions.andReturn();
      String rsaPublicKeyDTOStr = result.getResponse().getContentAsString();
      RsaPublicKeyDTO createdRsaPublicKeyDTO = new ObjectMapper()
          .readValue(rsaPublicKeyDTOStr, RsaPublicKeyDTO.class);

      resultActions.andExpect(status().isOk());
      assertThat(createdRsaPublicKeyDTO).isNotNull();
      assertThat(createdRsaPublicKeyDTO.getUserId()).isEqualTo(rsaPublicKeyDTO.getUserId());
      assertThat(createdRsaPublicKeyDTO.getStatus()).isEqualTo(AppConstants.Types.Status.enabled);

      rsaPublicKeyDTOs.add(createdRsaPublicKeyDTO);
    }

    return Arrays.asList(rsaPublicKeyDTOs, keyPairs);
  }

  /*
   *
   * Create random users
   *  The above steps are taking place for every user
   *  1. A random user is generated
   *  2. A user creation request is made to the Crm central
   *
   * Returns
   *  1. A List of UserDTOs that were generated
   *
   */
  public List<UserDTO> createRandomUsers(int totalUsers) throws Exception {
    List<UserDTO> userDTOs = new ArrayList<>();

    for (int i = 0; i < totalUsers; i++) {
      UserDTO userDTO = createRandomUserDTO();

      ResultActions resultActions = mvc.perform(post("/users/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(userDTO))
      );

      resultActions.andExpect(status().isOk());

      MvcResult result = resultActions.andReturn();
      String userDTO1Str = result.getResponse().getContentAsString();
      UserDTO userDTOCreated = new ObjectMapper().readValue(userDTO1Str, UserDTO.class);
      userDTOCreated.setPassword(userDTO.getPassword());
      userDTOs.add(userDTOCreated);

      assertThat(userDTOCreated.getId()).isNotNull();
      assertThat(userDTOCreated.getStatus()).isEqualTo(userDTO.getStatus());

    }

    return userDTOs;
  }

  private UserDTO createRandomUserDTO() {

    UserDTO userDTO = UserDTO.builder()
        .username(RandomStringUtils.random(10, true, true))
        .email(RandomStringUtils.random(5, true, true) + "@mail.com")
        .password(RandomStringUtils.random(10, true, true))
        .status(AppConstants.Types.UserStatus.enabled)
        .country(null)
        .rolesCrm("admin")
        .rolesMarket("admin")
        .modulesCrm("hyppoio")
        .modulesMarket("market")
        .build();

    return userDTO;
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
