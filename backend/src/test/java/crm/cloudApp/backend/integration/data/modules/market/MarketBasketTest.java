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
import crm.cloudApp.backend.dto.data.modules.market.BasketDTO;
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
import org.assertj.core.api.AssertionsForClassTypes;
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

import java.time.Duration;
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
public class MarketBasketTest {

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
    singleOrderRepository.deleteAll();
    basketRepository.deleteAll();
    productRepository.deleteAll();
    bmeCommandRepository.deleteAll();
    bmeOrderBookEntryRepository.deleteAll();
  }


  @Test
  public void basket_test() throws Exception {

    this.doLogin();

    Instant start = Instant.now();

    //Random Product creation
    List<ProductDTO> productDTOs = this.createRandomProducts(1);

    /*
     *  SELL LIMIT ORDER
     *          QTY: 10
     *          PRICE 22
     */
    SingleOrderDTO A_sellOrderQty10Price22 = createLimitOrder(productDTOs.get(0),
        AppConstants.Types.OrderDirection.SELL, 10, 22);

    /*
     *  SELL LIMIT ORDER
     *          QTY: 5
     *          PRICE 22
     */
    SingleOrderDTO B_sellOrderQty5Price22 = createLimitOrder(productDTOs.get(0),
        AppConstants.Types.OrderDirection.SELL, 5, 22);

    /*
     *  BUY LIMIT ORDER
     *          QTY: 20
     *          PRICE 18
     */
    SingleOrderDTO C_buyOrderQty20Price18 = createLimitOrder(productDTOs.get(0),
        AppConstants.Types.OrderDirection.BUY, (double) 20, 18);


    /*
     * ** BASKET ** OF
     *
     *  BUY LIMIT ORDER
     *          QTY: 10
     *          PRICE 22
     *
     *  BUY LIMIT ORDER
     *          QTY: 12
     *          PRICE 25
     *
     *  SELL LIMIT ORDER
     *          QTY: 7.3
     *          PRICE 25
     *
     */
    BasketDTO basketDTO = createBasketOrder(productDTOs.get(0));

    /*
     *  BUY LIMIT ORDER
     *          QTY: 10
     *          PRICE 25
     */
    SingleOrderDTO D_buyOrderQty10Price30 = createLimitOrder(productDTOs.get(0),
        AppConstants.Types.OrderDirection.BUY, (double) 10, 30);

    Instant finish = Instant.now();

    //Matchings of Orders
    List<TradeDTO> tradeDTOS = this.getMatchingsofProducts(productDTOs);

    //Assert that 4 Matchings were created
    assertThat(tradeDTOS.size()).isEqualTo(8);

    //Check the Orders that are participating to the matchings
    //Check the matchings Quantities & Prices
    //Matching 1
    AssertionsForClassTypes.assertThat(tradeDTOS.get(0).getSingleOrder1().getId())
        .isEqualTo(basketDTO.getOrders().get(0).getId());
    AssertionsForClassTypes.assertThat(tradeDTOS.get(0).getSingleOrder2().getId())
        .isEqualTo(A_sellOrderQty10Price22.getId());
    assertThat(tradeDTOS.get(0).getQuantity()).isEqualTo(10);
    assertThat(tradeDTOS.get(0).getPrice()).isEqualTo(22);

    //Matching 2
    AssertionsForClassTypes.assertThat(tradeDTOS.get(2).getSingleOrder1().getId())
        .isEqualTo(basketDTO.getOrders().get(1).getId());
    AssertionsForClassTypes.assertThat(tradeDTOS.get(2).getSingleOrder2().getId())
        .isEqualTo(B_sellOrderQty5Price22.getId());
    assertThat(tradeDTOS.get(2).getQuantity()).isEqualTo(5);
    assertThat(tradeDTOS.get(2).getPrice()).isEqualTo(22);

    //Matching 3
    AssertionsForClassTypes.assertThat(tradeDTOS.get(4).getSingleOrder1().getId())
        .isEqualTo(D_buyOrderQty10Price30.getId());
    AssertionsForClassTypes.assertThat(tradeDTOS.get(4).getSingleOrder2().getId())
        .isEqualTo(B_sellOrderQty5Price22.getId());
    assertThat(tradeDTOS.get(4).getQuantity()).isEqualTo(5);
    assertThat(tradeDTOS.get(4).getPrice()).isEqualTo(22);

    //Matching 4
    AssertionsForClassTypes.assertThat(tradeDTOS.get(6).getSingleOrder1().getId())
        .isEqualTo(D_buyOrderQty10Price30.getId());
    AssertionsForClassTypes.assertThat(tradeDTOS.get(6).getSingleOrder2().getId())
        .isEqualTo(basketDTO.getOrders().get(2).getId());
    assertThat(tradeDTOS.get(6).getQuantity()).isEqualTo(5);
    assertThat(tradeDTOS.get(6).getPrice()).isEqualTo(25);

    long timeElapsed = Duration.between(start, finish).toSeconds();

    System.out.println("******* Execution time: " + timeElapsed);
  }


  public BasketDTO createBasketOrder(ProductDTO productDTO) throws Exception {

    SingleOrderDTO singleOrderDTO = new SingleOrderDTO();
    singleOrderDTO.setProduct(productDTO);
    UserDTO userDTO = new UserDTO();
    userDTO.setId(2000L);
    singleOrderDTO.setUser(userDTO);
    singleOrderDTO.setQuantity(10);
    singleOrderDTO.setPrice(22);
    singleOrderDTO.setOrderStatus(AppConstants.Types.SingleOrderStatus.ACTIVE);
    singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.BUY);
    singleOrderDTO.setOrderType(AppConstants.Types.SingleOrderType.BASKET);
    singleOrderDTO.setMetadata("");

    SingleOrderDTO singleOrderDTO2 = new SingleOrderDTO();
    singleOrderDTO2.setProduct(productDTO);
    singleOrderDTO2.setUser(userDTO);
    singleOrderDTO2.setQuantity(12);
    singleOrderDTO2.setPrice(24);
    singleOrderDTO2.setOrderStatus(AppConstants.Types.SingleOrderStatus.ACTIVE);
    singleOrderDTO2.setOrderDirection(AppConstants.Types.OrderDirection.BUY);
    singleOrderDTO2.setOrderType(AppConstants.Types.SingleOrderType.BASKET);
    singleOrderDTO2.setMetadata("");

    SingleOrderDTO singleOrderDTO3 = new SingleOrderDTO();
    singleOrderDTO3.setProduct(productDTO);
    singleOrderDTO3.setUser(userDTO);
    singleOrderDTO3.setPrice(25);
    singleOrderDTO3.setQuantity(7.3);
    singleOrderDTO3.setOrderStatus(AppConstants.Types.SingleOrderStatus.ACTIVE);
    singleOrderDTO3.setOrderDirection(AppConstants.Types.OrderDirection.SELL);
    singleOrderDTO3.setOrderType(AppConstants.Types.SingleOrderType.BASKET);
    singleOrderDTO3.setMetadata("");

    List<SingleOrderDTO> orders = new ArrayList<>();
    orders.add(singleOrderDTO);
    orders.add(singleOrderDTO2);
    orders.add(singleOrderDTO3);

    BasketDTO basketDTO = new BasketDTO();
    basketDTO.setOrders(orders);
    basketDTO.setBasketType(AppConstants.Types.BasketType.EXCLUSIVE);

    ObjectMapper objectMapper = this.newObjectMapper();

    ResultActions resultActions = mvc.perform(post("/modules/market/baskets/")
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(basketDTO))
        .header("Authorization", "Bearer " + this.bearer)
        .header("Content-Type", "application/json")
    );

    resultActions.andExpect(status().isOk());

    MvcResult result = resultActions.andReturn();
    String createdBasketDTOStr = result.getResponse().getContentAsString();
    BasketDTO createdBasketDTO = objectMapper.readValue(createdBasketDTOStr, BasketDTO.class);

    AssertionsForClassTypes.assertThat(createdBasketDTO.getId()).isNotNull();
    // assertThat(createdBasketDTO.getPrice()).isEqualTo(singleOrderDTO.getPrice());
    //  assertThat(createdBasketDTO.getQuantity()).isEqualTo(singleOrderDTO.getQuantity());

    return createdBasketDTO;
  }


  public SingleOrderDTO createLimitOrder(ProductDTO productDTO,
      AppConstants.Types.OrderDirection direction, double quantity, double price) throws Exception {

    SingleOrderDTO singleOrderDTO = new SingleOrderDTO();
    singleOrderDTO.setProduct(productDTO);
    UserDTO userDTO = new UserDTO();
    userDTO.setId(2000L);
    singleOrderDTO.setUser(userDTO);
    singleOrderDTO.setPrice(price);
    singleOrderDTO.setQuantity(quantity);

    singleOrderDTO.setOrderStatus(AppConstants.Types.SingleOrderStatus.ACTIVE);
    singleOrderDTO.setOrderDirection(direction);

    singleOrderDTO.setOrderType(AppConstants.Types.SingleOrderType.LIMIT);

    singleOrderDTO.setMetadata("");

    ObjectMapper objectMapper = this.newObjectMapper();

    ResultActions resultActions = mvc.perform(
        post("/modules/market/orders/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(singleOrderDTO))
            .header("Authorization", "Bearer " + this.bearer)
            .header("Content-Type", "application/json")
    );

    resultActions.andExpect(status().isOk());

    MvcResult result = resultActions.andReturn();
    String singleOrderDTOStr = result.getResponse().getContentAsString();
    SingleOrderDTO singleOrderDTOCreated = objectMapper
        .readValue(singleOrderDTOStr, SingleOrderDTO.class);

    AssertionsForClassTypes.assertThat(singleOrderDTOCreated.getId()).isNotNull();
    assertThat(singleOrderDTOCreated.getPrice()).isEqualTo(singleOrderDTO.getPrice());
    assertThat(singleOrderDTOCreated.getQuantity()).isEqualTo(singleOrderDTO.getQuantity());

    return singleOrderDTOCreated;
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
      productDTO.setName(String.valueOf(i));
      productDTO.setDeliveryTimeStart(ZonedDateTime.now().plusHours(2).toInstant());
      productDTO.setPeriod(AppConstants.Types.ProductDeliveryPeriod.MINUTES60);
      productDTO.setMarketCode("CY");
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

  public static ObjectMapper newObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule module = new JavaTimeModule();
    objectMapper.registerModule(module);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    //  objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

    return objectMapper;
  }

}
