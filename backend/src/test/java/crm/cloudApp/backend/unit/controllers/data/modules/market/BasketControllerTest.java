package crm.cloudApp.backend.unit.controllers.data.modules.market;

import crm.cloudApp.backend.App;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.market.BasketDTO;
import crm.cloudApp.backend.services.data.modules.market.BasketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = App.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
    locations = "classpath:applicationIT.properties")
public class BasketControllerTest {

  @Autowired
  private MockMvc mockMvc;


  @MockBean
  private BasketService basketService;

  BasketDTO basketDTO;

  @Before
  public void setUp() {

    this.basketDTO = BasketDTO.builder()
        .basketType(AppConstants.Types.BasketType.VOLUME_CONSTRAINED)
        .metadata("metadata")
        .orders(null)
        .build();

  }

  @Test
  public void getById() throws Exception {

    Mockito
        .when(basketService.getByBasketId(Mockito.anyLong())).thenReturn(basketDTO);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
        "/modules/market/baskets/by-id?basket-id=1").accept(
        MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "{\"id\":null,\"createdOn\":null,\"createdBy\":null,\"basketType\":\"VOLUME_CONSTRAINED\",\"metadata\":\"metadata\",\"orders\":null}";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);

  }

  @Test
  public void getByOrderId() throws Exception {
    Mockito
        .when(basketService.getByOrderId(Mockito.anyLong())).thenReturn(basketDTO);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
        "/modules/market/baskets/by-order-id?order-id=1").accept(
        MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "{\"id\":null,\"createdOn\":null,\"createdBy\":null,\"basketType\":\"VOLUME_CONSTRAINED\",\"metadata\":\"metadata\",\"orders\":null}";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

}
