package crm.cloudApp.backend.integration.data.modules.market;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import crm.cloudApp.backend.App;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.market.ProductDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.repositories.data.modules.market.BMECommandRepository;
import crm.cloudApp.backend.repositories.data.modules.market.BMEOrderBookEntryRepository;
import crm.cloudApp.backend.repositories.data.modules.market.BasketRepository;
import crm.cloudApp.backend.repositories.data.modules.market.MatchedOrderRepository;
import crm.cloudApp.backend.repositories.data.modules.market.ProductRepository;
import crm.cloudApp.backend.repositories.data.modules.market.SingleOrderRepository;
import org.junit.Before;
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

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = App.class)

@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
    locations = "classpath:applicationIT.properties")
public class MarketTestSimple {

  @Autowired
  private MockMvc mvc;

  String bearer;


  /*
   *
   *  Market Reset
   *      Delete old Market data
   *      Using Repositories
   *      On Before runing the Test
   *
   */
  @Autowired
  private SingleOrderRepository singleOrderRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private MatchedOrderRepository matchedOrderRepository;

  @Autowired
  private BasketRepository basketRepository;

  @Autowired
  private BMECommandRepository bmeCommandRepository;

  @Autowired
  private BMEOrderBookEntryRepository bmeOrderBookEntryRepository;

  @Before
  public void setUp() {
    matchedOrderRepository.deleteAll();
    basketRepository.deleteAll();
    singleOrderRepository.deleteAll();
    productRepository.deleteAll();
    bmeCommandRepository.deleteAll();
    bmeOrderBookEntryRepository.deleteAll();
  }


  @Test
  public void mass_order_generation_scenario() throws Exception {

    this.doLogin();

    //1. Create random Products
    List<ProductDTO> productDTOs = this.createRandomProducts(5);

    //2. Create random Orders for Products
    List<SingleOrderDTO> singleOrderDTOs = this.createRandomOrders(productDTOs, 50);

    //3. Get matchings of Orders
    List<TradeDTO> tradeDTOS = this.getMatchingsofProducts(productDTOs);

    assertThat(tradeDTOS.size()).isGreaterThan(0);

  }


  private List<TradeDTO> getMatchingsofProducts(List<ProductDTO> productDTOs) throws Exception {

    List<TradeDTO> tradeDTOS = new ArrayList<>();

    for (ProductDTO productDTO : productDTOs) {

      ObjectMapper objectMapper = this.newObjectMapper();
      ResultActions resultActions = mvc
          .perform(get("/modules/market/products/matched-orders?product-id=" + productDTO.getId())
              .contentType(MediaType.APPLICATION_JSON)
              .header("Authorization", "Bearer " + this.bearer)
              .header("Content-Type", "application/json")
          );

      resultActions.andExpect(status().isOk());

      MvcResult result = resultActions.andReturn();
      String objectStr = result.getResponse().getContentAsString();
      List<TradeDTO> tradeDTOsOfProduct = objectMapper
          .readValue(objectStr, new TypeReference<List<TradeDTO>>() {
          });

      assertThat(tradeDTOsOfProduct).isNotNull();
      assertThat(tradeDTOsOfProduct.size()).isGreaterThan(0);

      tradeDTOS.addAll(tradeDTOsOfProduct);
    }
    return tradeDTOS;

  }

  public void doLogin() throws Exception {

    ResultActions resultActions = mvc.perform(post("/users/auth/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
            "  { " +
                "       \"username\":\"admin\", " +
                "       \"password\":\"11223345\" " +
                "  } "
        )
    );

    resultActions.andExpect(status().isOk());

    MvcResult result = resultActions.andReturn();
    String loginResponce = result.getResponse().getContentAsString();
    bearer = JsonPath.parse(loginResponce).read("jwt", String.class);
  }

  /*
   *
   *  1. Random products are generated
   *  2. Product creation request is made to the Crm central
   *
   * Returns
   *  1. A List of ProductDTOs that were generated
   *
   */
  public List<ProductDTO> createRandomProducts(int totalProducts) throws Exception {
    List<ProductDTO> productDTOs = new ArrayList<>();

    for (int i = 0; i < totalProducts; i++) {

      ProductDTO productDTO = new ProductDTO();
      productDTO.setDeliveryTimeStart(ZonedDateTime.now().plusHours(2).toInstant());
      productDTO.setPeriod(AppConstants.Types.ProductDeliveryPeriod.MINUTES15);
      productDTO.setMarketCode("CY");
      productDTO.setName(String.valueOf(i));
      productDTO.setProductStatus(AppConstants.Types.ProductStatus.OPEN);
      UserDTO userDTO = new UserDTO();
      userDTO.setId(2000L);
      productDTO.setUser(userDTO);

      ObjectMapper objectMapper = this.newObjectMapper();

      ResultActions resultActions = mvc.perform(post("/modules/market/products/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productDTO))
          .header("Authorization", "Bearer " + this.bearer)
          .header("Content-Type", "application/json")
      );

      resultActions.andExpect(status().isOk());

      MvcResult result = resultActions.andReturn();
      String productDTO1Str = result.getResponse().getContentAsString();

      ProductDTO productDTOCreated = objectMapper.readValue(productDTO1Str, ProductDTO.class);

      productDTOs.add(productDTOCreated);

      assertThat(productDTOCreated.getId()).isNotNull();
      assertThat(productDTOCreated.getProductStatus()).isEqualTo(productDTO.getProductStatus());
      assertThat(productDTOCreated.getName()).isEqualTo(productDTO.getName());
    }

    return productDTOs;
  }


  /*
   *
   *  1. Random orders are generated
   *  2. Order creation request is made to the Crm central
   *
   * Returns
   *  1. A List of OrderDTOs that were generated
   *
   */
  public List<SingleOrderDTO> createRandomOrders(List<ProductDTO> productDTOs,
      int totalOrdersPerProduct) throws Exception {

    List<SingleOrderDTO> singleOrderDTOs = new ArrayList<>();

    for (ProductDTO productDTO : productDTOs) {
      for (int i = 0; i < totalOrdersPerProduct; i++) {

        SingleOrderDTO singleOrderDTO = new SingleOrderDTO();
        singleOrderDTO.setProduct(productDTO);
        singleOrderDTO.setBidTime(Instant.now());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(2000L);
        singleOrderDTO.setUser(userDTO);
        singleOrderDTO.setPrice(10);
        singleOrderDTO.setQuantity(500);
        singleOrderDTO.setOrderStatus(AppConstants.Types.SingleOrderStatus.ACTIVE);

        if (i > (totalOrdersPerProduct / 2)) {
          singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.BUY);
        } else {
          singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.SELL);
        }

        singleOrderDTO.setOrderType(AppConstants.Types.SingleOrderType.LIMIT);
        singleOrderDTO.setMetadata("");

        ObjectMapper objectMapper = this.newObjectMapper();

        ResultActions resultActions = mvc.perform(post("/modules/market/orders/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(singleOrderDTO))
            .header("Authorization", "Bearer " + this.bearer)
            .header("Content-Type", "application/json")
        );

        resultActions.andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String singleOrderDTOStr = result.getResponse().getContentAsString();
        SingleOrderDTO singleOrderDTOCreated = objectMapper
            .readValue(singleOrderDTOStr, SingleOrderDTO.class);

        singleOrderDTOs.add(singleOrderDTOCreated);

        assertThat(singleOrderDTOCreated.getId()).isNotNull();
        assertThat(singleOrderDTOCreated.getPrice()).isEqualTo(singleOrderDTO.getPrice());
        assertThat(singleOrderDTOCreated.getQuantity()).isEqualTo(singleOrderDTO.getQuantity());
      }
    }
    return singleOrderDTOs;
  }


  public static ObjectMapper newObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule module = new JavaTimeModule();
    objectMapper.registerModule(module);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    //  objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

    return objectMapper;
  }


}
