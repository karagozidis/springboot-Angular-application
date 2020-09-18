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
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.models.data.modules.market.BMEOrderBookEntry;
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

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = App.class)

@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
    locations = "classpath:applicationIT.properties")
public class MarketTest {


  @Autowired
  private MockMvc mvc;


  /*
   *  Set here the number of Products
   *      to be generated on this test.
   */
  private final int totalProducts = 100;


  /*
   *  Set here the number of Orders
   *      to be generated on this test.
   */
  private final int totalOrders = 100;


  /*
   *
   *  Market Reset
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
   * On this Test we
   *          1. Generate random Products
   *          2. Generate random orders, to randomly selected products from the products list
   *          3. Request order book from BME
   *              We call trading.exe and return the BME order book
   *          4. Compare CRM quantities and prices to the equivalent to the BME (get_order_book)
   *
   */
  @Test
  public void mass_order_generation_scenario() throws Exception {

    //Login
    String bearer = this.login();

    //Start counting time
    Instant start = Instant.now();

    //Generate random products
    for (int i = 0; i < totalProducts; i++) {
      this.generateRandomDemoProduct(bearer);
    }

    //request products list from crm
    List<ProductDTO> activeProducts = getActiveProducts(bearer);

    assertThat(activeProducts.size()).isGreaterThan(0);

    //Generate random orders, to randomly selected products from list
    Random random = new Random();
    for (int i = 0; i < totalOrders; i++) {
      int position = random.nextInt(activeProducts.size());
      ProductDTO productDTO = activeProducts.get(position);
      this.generateRandomDemoOrder(productDTO, bearer);
    }

    //End counting time
    Instant finish = Instant.now();

    // Request order book from BME
    // This call instantly calls trading.exe and gets back to us the values from BME Market
    // This is very useful to compare our quantities and prices to be equal to BME
    List<BMEOrderBookEntry> bmeOrderBookEntries = this.get_order_book(bearer);

    //Iterates Crm Active products
    for (ProductDTO productDTO : activeProducts) {
      //Get Crm Orders of product
      List<SingleOrderDTO> orders = this.getOrdersOfProduct(bearer, productDTO.getId().toString());

      //Iterate orders and compare to BME Order book
      for (SingleOrderDTO order : orders) {
        Optional<BMEOrderBookEntry> optionalBMEOrderBookEntry = bmeOrderBookEntries.stream()
            .filter(o -> o.getOrderId().equals(order.getId())).findFirst();
        if (optionalBMEOrderBookEntry.isEmpty()) {
          assertThat(order.getQuantity()).isEqualTo(0);
        } else {
          BMEOrderBookEntry bmeOrderBookEntry = optionalBMEOrderBookEntry.get();
          assertThat(order.getCurrent_quantity()).isEqualTo(bmeOrderBookEntry.getQuantity());
        }
      }

    }

    //Print time
    long timeElapsed = Duration.between(start, finish).toSeconds();
    System.out.println("******* Execution time: " + timeElapsed);

  }


  private String login() throws Exception {

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
    String bearer = JsonPath.parse(loginResponce).read("jwt", String.class);

    return bearer;
  }

  public static ObjectMapper newObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule module = new JavaTimeModule();
    objectMapper.registerModule(module);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    //  objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

    return objectMapper;
  }


  private ProductDTO generateRandomDemoProduct(String bearer) throws Exception {

    ProductDTO productDTO = new ProductDTO();
    productDTO.setName(UUID.randomUUID().toString());

    Random random = new Random();
    int randomMinutes = random.nextInt(360);
    randomMinutes += 180;

    productDTO.setDeliveryTimeStart(
        ZonedDateTime.now().plusMinutes(randomMinutes).toInstant());
    productDTO.setPeriod(AppConstants.Types.ProductDeliveryPeriod.MINUTES15);

    productDTO.setMarketCode("CY");
    ObjectMapper objectMapper = this.newObjectMapper();

    ResultActions resultActions = mvc.perform(post("/modules/market/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDTO))
        .header("Authorization", "Bearer " + bearer)
        .header("Content-Type", "application/json")
    );

    MvcResult result = resultActions.andReturn();
    String productDTO1Str = result.getResponse().getContentAsString();

    ProductDTO productDTOCreated = objectMapper.readValue(productDTO1Str, ProductDTO.class);

    return productDTOCreated;
  }


  private List<BMEOrderBookEntry> get_order_book(String bearer) throws Exception {

    ObjectMapper objectMapper = this.newObjectMapper();
    ResultActions resultActions = mvc.perform(get("/modules/market/orders/get_order_book")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + bearer)
        .header("Content-Type", "application/json")
    );

    resultActions.andExpect(status().isOk());

    MvcResult result = resultActions.andReturn();
    String objectStr = result.getResponse().getContentAsString();
    List<BMEOrderBookEntry> bMEOrderBookEntries = objectMapper
        .readValue(objectStr, new TypeReference<List<BMEOrderBookEntry>>() {
        });
    return bMEOrderBookEntries;
  }

  private List<ProductDTO> getActiveProducts(String bearer) throws Exception {

    ObjectMapper objectMapper = this.newObjectMapper();
    ResultActions resultActions = mvc.perform(get("/modules/market/products/active/")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + bearer)
        .header("Content-Type", "application/json")
    );

    resultActions.andExpect(status().isOk());

    MvcResult result = resultActions.andReturn();
    String objectStr = result.getResponse().getContentAsString();
    List<ProductDTO> products = objectMapper
        .readValue(objectStr, new TypeReference<List<ProductDTO>>() {
        });
    return products;

  }


  private List<SingleOrderDTO> getOrdersOfProduct(String bearer, String productId)
      throws Exception {

    ObjectMapper objectMapper = this.newObjectMapper();
    ResultActions resultActions = mvc
        .perform(get("/modules/market/products/orders?product-id=" + productId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + bearer)
            .header("Content-Type", "application/json")
        );

    resultActions.andExpect(status().isOk());

    MvcResult result = resultActions.andReturn();
    String objectStr = result.getResponse().getContentAsString();
    List<SingleOrderDTO> orders = objectMapper
        .readValue(objectStr, new TypeReference<List<SingleOrderDTO>>() {
        });
    return orders;

  }


  private SingleOrderDTO generateRandomDemoOrder(ProductDTO productDTO, String bearer)
      throws Exception {

    Random random = new Random();
    SingleOrderDTO singleOrderDTO = new SingleOrderDTO();
    singleOrderDTO.setProduct(productDTO);
    singleOrderDTO.setMetadata("");
    singleOrderDTO.setOrderType(AppConstants.Types.SingleOrderType.LIMIT);
    if (random.nextBoolean()) {
      singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.BUY);
    } else {
      singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.SELL);
    }
    Double quantity = 100 + (500 * random.nextDouble());
    Double price = 100 + (00 * random.nextDouble());
    singleOrderDTO.setQuantity(Round(quantity, 1)); //Random Quantity with range 100 - 600
    singleOrderDTO.setPrice(Round(price, 1));   //Random Quantity with range 100 - 200
    UserDTO userDTO = new UserDTO();
    userDTO.setId(2000L);
    singleOrderDTO.setUser(userDTO);

    ObjectMapper objectMapper = this.newObjectMapper();

    ResultActions resultActions = mvc.perform(post("/modules/market/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(singleOrderDTO))
        .header("Authorization", "Bearer " + bearer)
        .header("Content-Type", "application/json")
    );

    resultActions.andExpect(status().isOk());

    MvcResult result = resultActions.andReturn();
    String singleOrderDTOStr = result.getResponse().getContentAsString();
    SingleOrderDTO createdSingleOrderDTO = objectMapper
        .readValue(singleOrderDTOStr, SingleOrderDTO.class);

    return createdSingleOrderDTO;
  }

  private static double Round(double value, int places) {
    if (places < 0) {
      throw new IllegalArgumentException();
    }

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }


}
