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
public class MarketIcebergTest {


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
    productRepository.deleteAll();
    bmeCommandRepository.deleteAll();
    bmeOrderBookEntryRepository.deleteAll();
    basketRepository.deleteAll();
  }


  /*
   *
   * On this test we create
   *          1. a Buy Iceberg Order with   Quantity: 10   Price:22   Iceberg quantity:100   Price Delta:1
   *          2. a Sell Limit Order with    Quantity: 10   Price:22
   *          3. a Sell Limit Order with    Quantity: 5    Price:22
   *          4. a Sell Limit Order with    Quantity: 85   Price:22
   *
   *          and we expect to get back the equivalent matchings, quantities and prices
   *
   */
  @Test
  public void iceberg_test() throws Exception {

    this.doLogin();

    Instant start = Instant.now();

    //1. Create Product
    List<ProductDTO> productDTOs = this.createRandomProducts(1);

    //2. Create ICEBERG Order for Product
    SingleOrderDTO sellOrder = createIcebergBuyOrder(productDTOs.get(0),
        AppConstants.Types.OrderDirection.BUY, 10, 100, 22, 1);

    //3. Create LIMIT Order for Product
    SingleOrderDTO buyOrder10 = createlimitSellOrder(productDTOs.get(0),
        AppConstants.Types.OrderDirection.SELL, 10, 22);

    //4. Get matchings of Orders
    List<TradeDTO> tradeDTOS = this.getMatchingsofProducts(productDTOs);

    assertThat(tradeDTOS.size()).isEqualTo(2);
    assertThat(tradeDTOS.get(0).getPrice()).isEqualTo(22);
    assertThat(tradeDTOS.get(0).getQuantity()).isEqualTo(10);
    assertThat(tradeDTOS.size()).isEqualTo(2);

    // Check if matching contains the right Ordeer Ids
    List<Long> ids = new ArrayList<>();
    ids.add(sellOrder.getId());
    ids.add(buyOrder10.getId());
    AssertionsForClassTypes.assertThat(tradeDTOS.get(0).getSingleOrder1().getId()).isIn(ids);
    AssertionsForClassTypes.assertThat(tradeDTOS.get(0).getSingleOrder2().getId()).isIn(ids);

    //5. Create LIMIT Order for Product
    SingleOrderDTO buyOrder5 = createlimitSellOrder(productDTOs.get(0),
        AppConstants.Types.OrderDirection.SELL, (double) 5, 22);

    //6. Get matchings of Orders
    List<TradeDTO> tradeDTOs2 = this.getMatchingsofProducts(productDTOs);

    assertThat(tradeDTOs2.size()).isEqualTo(4);
    assertThat(tradeDTOs2.get(2).getPrice()).isEqualTo(23);
    assertThat(tradeDTOs2.get(2).getQuantity()).isEqualTo(5);

    // Check if matching contains the right Order Ids
    ids = new ArrayList<>();
    ids.add(sellOrder.getId());
    ids.add(buyOrder5.getId());
    AssertionsForClassTypes.assertThat(tradeDTOs2.get(2).getSingleOrder1().getId()).isIn(ids);
    AssertionsForClassTypes.assertThat(tradeDTOs2.get(2).getSingleOrder2().getId()).isIn(ids);

    //7. Create LIMIT Order for Product
    SingleOrderDTO buyOrder85 = createlimitSellOrder(productDTOs.get(0),
        AppConstants.Types.OrderDirection.SELL, (double) 85, 22);

    //6. Get matchings of Orders
    List<TradeDTO> tradeDTOs3 = this.getMatchingsofProducts(productDTOs);

    assertThat(tradeDTOs3.size()).isEqualTo(22);
    assertThat(tradeDTOs3.get(4).getPrice()).isEqualTo(23);
    assertThat(tradeDTOs3.get(4).getQuantity()).isEqualTo(5);

    // Check if matching contains the right Order Ids
    ids = new ArrayList<>();
    ids.add(sellOrder.getId());
    ids.add(buyOrder85.getId());
    AssertionsForClassTypes.assertThat(tradeDTOs3.get(4).getSingleOrder1().getId()).isIn(ids);
    AssertionsForClassTypes.assertThat(tradeDTOs3.get(4).getSingleOrder2().getId()).isIn(ids);

    for (int i = 3; i < 11; i++) {

      assertThat(tradeDTOs3.get(i * 2).getPrice()).isEqualTo(21 + i);
      assertThat(tradeDTOs3.get(i * 2).getQuantity()).isEqualTo(10);

      // Check if matching contains the right Order Ids
      ids = new ArrayList<>();
      ids.add(sellOrder.getId());
      ids.add(buyOrder85.getId());
      AssertionsForClassTypes.assertThat(tradeDTOs3.get(i * 2).getSingleOrder1().getId()).isIn(ids);
      AssertionsForClassTypes.assertThat(tradeDTOs3.get(i * 2).getSingleOrder2().getId()).isIn(ids);
    }

    Instant finish = Instant.now();

    long timeElapsed = Duration.between(start, finish).toSeconds();

    assertThat(tradeDTOS.size()).isGreaterThan(0);

    System.out.println("******* Execution time: " + timeElapsed);
  }


  public SingleOrderDTO createIcebergBuyOrder(ProductDTO productDTO,
      AppConstants.Types.OrderDirection direction, double qty, double iceberg_qty, double price,
      double price_delta) throws Exception {

    SingleOrderDTO singleOrderDTO = new SingleOrderDTO();
    singleOrderDTO.setProduct(productDTO);
    UserDTO userDTO = new UserDTO();
    userDTO.setId(2000L);
    singleOrderDTO.setUser(userDTO);
    singleOrderDTO.setPrice(price);
    singleOrderDTO.setQuantity(qty);
    singleOrderDTO.setMetadata(
        "{\"icebergQuantity\":\"" + iceberg_qty + "\",\"icebergPriceDelta\":\"" + price_delta
            + "\"}");

    singleOrderDTO.setOrderStatus(AppConstants.Types.SingleOrderStatus.ACTIVE);
    singleOrderDTO.setOrderDirection(direction);

    singleOrderDTO.setOrderType(AppConstants.Types.SingleOrderType.ICEBERG);

    //  singleOrderDTO.setMetadata("");

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

    assertThat(singleOrderDTOCreated.getId()).isNotNull();
    assertThat(singleOrderDTOCreated.getPrice()).isEqualTo(singleOrderDTO.getPrice());
    assertThat(singleOrderDTOCreated.getQuantity()).isEqualTo(singleOrderDTO.getQuantity());

    return singleOrderDTOCreated;
  }


  public SingleOrderDTO createlimitSellOrder(ProductDTO productDTO,
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

    assertThat(singleOrderDTOCreated.getId()).isNotNull();
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
