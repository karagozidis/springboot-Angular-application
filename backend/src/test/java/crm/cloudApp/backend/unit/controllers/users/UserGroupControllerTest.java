package crm.cloudApp.backend.unit.controllers.users;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import crm.cloudApp.backend.App;
import crm.cloudApp.backend.dto.encryption.AesKeyDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.dto.users.UserGroupDTO;
import crm.cloudApp.backend.services.encryption.AesKeyService;
import crm.cloudApp.backend.services.users.UserGroupService;
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

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = App.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
    locations = "classpath:applicationIT.properties")
public class UserGroupControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserGroupService userGroupService;

  @MockBean
  private AesKeyService aesKeyService;

  UserGroupDTO mockUserGroupDTO;

  Page<UserGroupDTO> mockUserGroupPage;

  Page<UserDTO> mockUserPage;

  Page<AesKeyDTO> mockAesKeyPage;

  AesKeyDTO aesKeyDTO;

  @Before
  public void setUp() {
    mockUserGroupDTO = new UserGroupDTO();
    mockUserGroupDTO.setDescription("UserGroupDescription");
    mockUserGroupDTO.setName("UserGroupName");
    mockUserGroupDTO.setEncryptionVersion(10);
    mockUserGroupDTO.setId(199L);
    mockUserGroupDTO.setHasEnabledPublicKey(true);
    mockUserGroupDTO.setCreatedBy("admin");
    mockUserGroupDTO.setCreatedOn(
        Instant.now().atZone(ZoneOffset.UTC)
            .withYear(2000)
            .withMonth(01)
            .withDayOfMonth(01)
            .withHour(00)
            .withMinute(00)
            .withSecond(00)
            .withNano(00)
            .toInstant());
    UserDTO userDto = new UserDTO();
    userDto.setId(1L);
    userDto.setEmail("user1@onCloudApp.gr");
    userDto.setUsername("user1");

    UserDTO userDto2 = new UserDTO();
    userDto2.setId(2L);
    userDto2.setEmail("user2@onCloudApp.gr");
    userDto2.setUsername("user2");

    mockUserGroupDTO.setUsers(Arrays.asList(userDto, userDto2));

    PageRequest pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "id"));
    mockUserGroupPage =
        new PageImpl<>(Arrays.asList(this.mockUserGroupDTO), pageable, 1);

    mockUserPage = new PageImpl<>(Arrays.asList(userDto), pageable, 1);

    aesKeyDTO = AesKeyDTO.builder()
        .encryptedKey("encryptedKey")
        .encryptionVersion(1)
        .keyUUID("keyUUID")
        .rsaPublicKeyId(1L)
        .userGroupId(1L)
        .size(1)
        .build();

    mockAesKeyPage =
        new PageImpl<>(Arrays.asList(aesKeyDTO),
            pageable, 1);

  }

  @Test
  public void getAllUserGroupsTest() throws Exception {

    Mockito
        .when(userGroupService.getAllUserGroups()).thenReturn(Arrays.asList(mockUserGroupDTO));

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
        "/user-groups/").accept(
        MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    System.out.println(result.getResponse());
    String expected = "[{\"id\":199,\"createdOn\":\"2000-01-01T00:00:00Z\",\"createdBy\":\"admin\",\"name\":\"UserGroupName\",\"description\":\"UserGroupDescription\",\"encryptionVersion\":10,\"users\":[{\"id\":1,\"createdOn\":null,\"createdBy\":null,\"username\":\"user1\",\"email\":\"user1@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null},{\"id\":2,\"createdOn\":null,\"createdBy\":null,\"username\":\"user2\",\"email\":\"user2@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null}],\"hasEnabledPublicKey\":true}]";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void getUserGroupsByCurrentUserTest() throws Exception {

    Mockito
        .when(userGroupService.getUserGroupsByCurrentUser())
        .thenReturn(Arrays.asList(mockUserGroupDTO));

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
        "/user-groups/users/current").accept(
        MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    System.out.println(result.getResponse());
    String expected = "[{\"id\":199,\"createdOn\":\"2000-01-01T00:00:00Z\",\"createdBy\":\"admin\",\"name\":\"UserGroupName\",\"description\":\"UserGroupDescription\",\"encryptionVersion\":10,\"users\":[{\"id\":1,\"createdOn\":null,\"createdBy\":null,\"username\":\"user1\",\"email\":\"user1@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null},{\"id\":2,\"createdOn\":null,\"createdBy\":null,\"username\":\"user2\",\"email\":\"user2@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null}],\"hasEnabledPublicKey\":true}]";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void getUserGroupsPageTest() throws Exception {

    Mockito.when(userGroupService.getUserGroups(Mockito.any())).thenReturn(mockUserGroupPage);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/user-groups/pages")
        .param("page", "0")
        .param("size", "10")
        .param("sort", "id,desc")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "{\"content\":[{\"id\":199,\"createdOn\":\"2000-01-01T00:00:00Z\","
        + "\"createdBy\":\"admin\",\"name\":\"UserGroupName\",\"description\":\"UserGroupDescription\",\"encryptionVersion\":10,\"users\":[{\"id\":1,\"createdOn\":null,\"createdBy\":null,\"username\":\"user1\",\"email\":\"user1@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null},{\"id\":2,\"createdOn\":null,\"createdBy\":null,\"username\":\"user2\",\"email\":\"user2@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null}],\"hasEnabledPublicKey\":true}],\"pageable\":{\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"offset\":0,\"pageNumber\":0,\"pageSize\":10,\"unpaged\":false,\"paged\":true},\"totalPages\":1,\"totalElements\":1,\"last\":true,\"size\":10,\"number\":0,\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"numberOfElements\":1,\"first\":true,\"empty\":false}";
    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void getUserGroupTest() throws Exception {

    Mockito.when(userGroupService.getUserGroup(Mockito.anyLong()))
        .thenReturn(this.mockUserGroupDTO);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/user-groups/1")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "{\"id\":199,\"createdOn\":\"2000-01-01T00:00:00Z\",\"createdBy\":\"admin\",\"name\":\"UserGroupName\",\"description\":\"UserGroupDescription\",\"encryptionVersion\":10,\"users\":[{\"id\":1,\"createdOn\":null,\"createdBy\":null,\"username\":\"user1\",\"email\":\"user1@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null},{\"id\":2,\"createdOn\":null,\"createdBy\":null,\"username\":\"user2\",\"email\":\"user2@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null}],\"hasEnabledPublicKey\":true}";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void createUserGroupTest() throws Exception {

    Mockito.when(userGroupService.getAllUserGroups())
        .thenReturn(Arrays.asList(this.mockUserGroupDTO));

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/user-groups")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "[{\"id\":199,\"createdOn\":\"2000-01-01T00:00:00Z\",\"createdBy\":\"admin\",\"name\":\"UserGroupName\",\"description\":\"UserGroupDescription\",\"encryptionVersion\":10,\"users\":[{\"id\":1,\"createdOn\":null,\"createdBy\":null,\"username\":\"user1\",\"email\":\"user1@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null},{\"id\":2,\"createdOn\":null,\"createdBy\":null,\"username\":\"user2\",\"email\":\"user2@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null}],\"hasEnabledPublicKey\":true}]";
    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void createUniqueUserGroupTest() throws Exception {

    Mockito.when(userGroupService.getUniqueUserGroupId(null))
        .thenReturn(0L);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/user-groups/unique-user-group")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String expected = "true";

    assertThat(result.getResponse().getContentAsString())
        .isEqualTo(expected);
  }

  @Test
  public void existsUniqueUserGroupTest() throws Exception {

    Mockito.when(userGroupService.getUniqueUserGroupId(null))
        .thenReturn(1L);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/user-groups/unique-user-group")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String expected = "true";

    assertThat(result.getResponse().getContentAsString())
        .isEqualTo(expected);
  }

  @Test
  public void updateTest() throws Exception {
    Mockito.when(userGroupService.update(Mockito.any(), Mockito.any()))
        .thenReturn(mockUserGroupDTO);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/user-groups/" + mockUserGroupDTO.getId())
        .content(new ObjectMapper().writeValueAsString(mockUserGroupDTO))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String expected = "{\"id\":199,\"createdOn\":\"2000-01-01T00:00:00Z\",\"createdBy\":\"admin\",\"name\":\"UserGroupName\",\"description\":\"UserGroupDescription\",\"encryptionVersion\":10,\"users\":[{\"id\":1,\"createdOn\":null,\"createdBy\":null,\"username\":\"user1\",\"email\":\"user1@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null},{\"id\":2,\"createdOn\":null,\"createdBy\":null,\"username\":\"user2\",\"email\":\"user2@onCloudApp.gr\",\"password\":null,\"status\":null,\"country\":null,\"rolesCrm\":null,\"modulesCrm\":null,\"rolesMarket\":null,\"modulesMarket\":null}],\"hasEnabledPublicKey\":true}";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void aesKeysByUserGroupTest() throws Exception {
    Mockito.when(aesKeyService.getAesKeysByUserGroupId(Mockito.any(), Mockito.any()))
        .thenReturn(mockAesKeyPage);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/user-groups/1/aes-keys")
        .param("page", "0")
        .param("size", "10")
        .param("sort", "id,desc")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String expected = "{\"content\":[{\"id\":null,\"createdOn\":null,\"createdBy\":null,"
        + "\"encryptedKey\":\"encryptedKey\",\"size\":1,\"encryptionVersion\":1,\"userGroupId\":1,\"rsaPublicKeyId\":1,\"keyUUID\":\"keyUUID\"}],\"pageable\":{\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"offset\":0,\"pageSize\":10,\"pageNumber\":0,\"unpaged\":false,\"paged\":true},\"totalPages\":1,\"last\":true,\"totalElements\":1,\"size\":10,\"number\":0,\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"first\":true,\"numberOfElements\":1,\"empty\":false}";
    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void getActiveAESKeyKeyTest() throws Exception {
    Mockito.when(aesKeyService.getActiveKey(Mockito.any(), Mockito.any()))
        .thenReturn(aesKeyDTO);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/user-groups/1/users/1/aes-keys/active")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String expected = "{\"id\":null,\"createdOn\":null,\"createdBy\":null,"
        + "\"encryptedKey\":\"encryptedKey\",\"size\":1,\"encryptionVersion\":1,\"userGroupId\":1,\"rsaPublicKeyId\":1,\"keyUUID\":\"keyUUID\"}";
    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void aesKeybyUserGroupAndUserTest() throws Exception {
    Mockito.when(aesKeyService
        .findAesKeysByUserIdAndUserGroupId(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(mockAesKeyPage);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/user-groups/1/users/1")
        .param("page", "0")
        .param("size", "10")
        .param("sort", "id,desc")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String expected = "{\"content\":[{\"id\":null,\"createdOn\":null,\"createdBy\":null,\"encryptedKey\":\"encryptedKey\",\"size\":1,\"encryptionVersion\":1,\"userGroupId\":1,\"rsaPublicKeyId\":1,\"keyUUID\":\"keyUUID\"}],\"pageable\":{\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"pageNumber\":0,\"pageSize\":10,\"offset\":0,\"unpaged\":false,\"paged\":true},\"last\":true,\"totalPages\":1,\"totalElements\":1,\"numberOfElements\":1,\"first\":true,\"size\":10,\"number\":0,\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"empty\":false}\n";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void verifyAllAesKeysActivedTest() throws Exception {
    Mockito.when(userGroupService.checkActiveAesKeys(Mockito.any()))
        .thenReturn(true);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/user-groups/verifyAllAesKeysActived/1")

        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String expected = "true";

    assertThat(result.getResponse().getContentAsString())
        .isEqualTo(expected);
  }


  @Test
  public void deleteTest() throws Exception {
    Mockito.when(userGroupService.delete(Mockito.any()))
        .thenReturn(true);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/user-groups/1")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String expected = "true";

    assertThat(result.getResponse().getContentAsString())
        .isEqualTo(expected);
  }

}
