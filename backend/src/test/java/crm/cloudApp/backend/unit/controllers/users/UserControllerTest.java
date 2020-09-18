package crm.cloudApp.backend.unit.controllers.users;

import crm.cloudApp.backend.App;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.services.users.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = App.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
    locations = "classpath:applicationIT.properties")
public class UserControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  UserDTO userDTO;

  Page<UserDTO> mockUserPage;

  @Before
  public void setUp() {

      this. userDTO = UserDTO.builder()
        .username( "existingUser")
        .email("existingUserScenarioUser@mail.com")
        .password("password")
        .status(AppConstants.Types.UserStatus.enabled)
        .country(null)
        .rolesCrm("admin")
        .rolesMarket("admin")
        .modulesCrm("hyppoio")
        .modulesMarket("market")
        .build();

    PageRequest pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "id"));
    mockUserPage = new PageImpl<>(Arrays.asList(this.userDTO), pageable, 1);


  }

  @Test
  public void getAllUsers() throws Exception {

    Mockito
        .when(userService.getAllUsers()).thenReturn(Arrays.asList(userDTO));

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    System.out.println(result.getResponse());
    String expected = "[{\"id\":null,\"createdOn\":null,\"createdBy\":null,\"username\":\"existingUser\",\"email\":\"existingUserScenarioUser@mail.com\",\"password\":\"password\",\"status\":\"enabled\",\"country\":null,\"rolesCrm\":\"admin\",\"modulesCrm\":\"hyppoio\",\"rolesMarket\":\"admin\",\"modulesMarket\":\"market\"}]";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void getCrmVerifiedUsers() throws Exception {
    Mockito
        .when(userService.getCrmVerifiedUsers()).thenReturn(Arrays.asList(userDTO));

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/crm-verified")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    System.out.println(result.getResponse());
    String expected = "[{\"id\":null,\"createdOn\":null,\"createdBy\":null,\"username\":\"existingUser\",\"email\":\"existingUserScenarioUser@mail.com\",\"password\":\"password\",\"status\":\"enabled\",\"country\":null,\"rolesCrm\":\"admin\",\"modulesCrm\":\"hyppoio\",\"rolesMarket\":\"admin\",\"modulesMarket\":\"market\"}]";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);

  }

  @Test
  public void getUsers() {
//
//    Mockito
//        .when(userService.getUsers(Mockito.any())).thenReturn(mockUserPage);
//
//    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/crm-verified")
//        .accept(MediaType.APPLICATION_JSON);
//
//    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//    System.out.println(result.getResponse());
//    String expected = "[{\"id\":null,\"createdOn\":null,\"createdBy\":null,\"username\":\"existingUser\",\"email\":\"existingUserScenarioUser@mail.com\",\"password\":\"password\",\"status\":\"enabled\",\"country\":null,\"rolesCrm\":\"admin\",\"modulesCrm\":\"hyppoio\",\"rolesMarket\":\"admin\",\"modulesMarket\":\"market\"}]";
//
//    JSONAssert.assertEquals(expected, result.getResponse()
//        .getContentAsString(), false);



  }

  @Test
  public void getUser() {
  }

  @Test
  public void getCurrentUser() {
  }

  @Test
  public void createUser() {
  }

  @Test
  public void setUserMetadata() {
  }

  @Test
  public void getUserMetadata() {
  }

  @Test
  public void updateUser() {
  }

  @Test
  public void update() {
  }

  @Test
  public void updatePassword() {
  }

  @Test
  public void testUpdatePassword() {
  }

  @Test
  public void delete() {
  }

  @Test
  public void getAllStatuses() {
  }

  @Test
  public void getContactsOfCurrentUser() {
  }

  @Test
  public void getContactUsersOfCurrentUser() {
  }

  @Test
  public void allRsaPublicKeysbyUser() {
  }

  @Test
  public void findActiveRsaPublicKey() {
  }

  @Test
  public void allAESKeysByUser() {
  }

  @Test
  public void activeAESKeyByUserAndUserGroup() {
  }

  @Test
  public void activeAESKeysByUserAndUserGroupOverEncryptionVersion() {
  }

  @Test
  public void getProducts() {
  }

  @Test
  public void getOrders() {
  }

  @Test
  public void getMatchedOrders() {
  }

  @Test
  public void authenticate() {
  }

  @Test
  public void getAllUsersWithPublicKeyStatus() {
  }



}
