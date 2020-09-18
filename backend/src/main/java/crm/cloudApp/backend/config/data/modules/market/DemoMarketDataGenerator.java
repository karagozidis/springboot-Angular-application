package crm.cloudApp.backend.config.data.modules.market;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class DemoMarketDataGenerator {

  //    @Value("${server.port}")
  //    private String port;
  //
  //    @Value("${server.servlet.context-path}")
  //    private String contextPath;
  //
  ////    @Scheduled(initialDelay = 10000, fixedDelay  = 60000)
  //    @Scheduled(fixedDelay  = 3600000)
  //    public void generateDemoProducts() throws Exception {
  //       String bearer = this.login();
  //       for(int i=0;i<100;i++) this.generateRandomDemoProduct(bearer);
  //    }
  //
  //    @Scheduled( initialDelay = 60000, fixedDelay  = 60000)
  //    public void generateDemoOrders() {
  //        String bearer = this.login();
  //        List<ProductDTO> activeProducts = getActiveProducts(bearer);
  //        if(activeProducts.size() <= 0 ) return;
  //
  //        Random random = new Random();
  //        for(int i=0;i<200;i++) {
  //            int position = random.nextInt(activeProducts.size() );
  //            ProductDTO productDTO = activeProducts.get(position);
  //            this.generateRandomDemoOrder(productDTO, bearer);
  //        }
  //
  //    }
  //
  //    private String login()
  //    {
  //
  //        String credentials =    " {                             "+
  //                                " 	\"username\":\"admin\",     "+
  //                                " 	\"password\":\"11223345\"   "+
  //                                " }                             ";
  //
  //        RestTemplate restTemplate = new RestTemplate();
  //        HttpHeaders headers = new HttpHeaders();
  //        headers.set("Content-Type","application/json");
  //        HttpEntity<String> httpEntity = new HttpEntity<String>(credentials, headers);
  //
  //        String loginResponce = restTemplate.postForObject(
  //                "http://localhost:"+port+contextPath+"/users/auth",
  //                httpEntity,
  //                String.class
  //        );
  //
  //        String bearer = JsonPath.parse(loginResponce).read("jwt",String.class);
  //        return bearer;
  //    }
  //
  //
  //    private List<ProductDTO> getActiveProducts(String bearer)
  //    {
  //
  //        HttpHeaders headers = new HttpHeaders();
  //        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
  //        headers.set("authorization",bearer);
  //        HttpEntity<String> httpEntity = new HttpEntity<String>("parameters", headers);
  //
  //        RestTemplate restTemplate = new RestTemplate();
  //        ResponseEntity<List<ProductDTO>> products = restTemplate.exchange
  //                (
  //                        URI.create("http://localhost:"+port+contextPath+"/modules/market/products/active/"),
  //                        HttpMethod.GET,
  //                        httpEntity,
  //                        new ParameterizedTypeReference<List<ProductDTO>>(){}
  //                );
  //
  //
  //        if(products.getStatusCode().value() != 200)
  //        {
  //            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Connection with Crm Central error!!");
  //        }
  //
  //        return products.getBody();
  //    }
  //
  //
  //
  //    private ProductDTO generateRandomDemoProduct(String bearer)
  //    {
  //
  //        ProductDTO productDTO = new ProductDTO();
  //        productDTO.setName(UUID.randomUUID().toString());
  //
  //        Random random = new Random();
  //        int randomMinutes = random.nextInt(360);
  //        randomMinutes += 180;
  //
  //        productDTO.setDeliveryTimeStart(
  //                ZonedDateTime.now().plusMinutes(randomMinutes).toInstant());
  //        productDTO.setPeriod(AppConstants.Types.ProductDeliveryPeriod.MINUTES60);
  //      //  productDTO.setDeliveryTimeEnd( ZonedDateTime.now().plusMinutes(randomMinutes + 30).toInstant());
  //
  //
  //        RestTemplate restTemplate = new RestTemplate();
  //        HttpHeaders headers = new HttpHeaders();
  //        headers.set("authorization",bearer);
  //        HttpEntity<ProductDTO> httpEntity = new HttpEntity<ProductDTO>(productDTO, headers);
  //
  //        ProductDTO createdProductDTO = restTemplate.postForObject(
  //                "http://localhost:"+port+contextPath+"/modules/market/products",
  //                httpEntity,
  //                ProductDTO.class
  //        );
  //
  //        return createdProductDTO;
  //    }
  //
  //
  //    private SingleOrderDTO generateRandomDemoOrder(ProductDTO productDTO, String bearer)
  //    {
  //
  //        Random random = new Random();
  //        SingleOrderDTO singleOrderDTO = new SingleOrderDTO();
  //        singleOrderDTO.setProduct(productDTO);
  //        singleOrderDTO.setMetadata("");
  //        singleOrderDTO.setOrderType(AppConstants.Types.SingleOrderType.LIMIT);
  //        if(random.nextBoolean()) singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.BUY);
  //        else singleOrderDTO.setOrderDirection(AppConstants.Types.OrderDirection.SELL);
  //        Double quantity = 100+(500*random.nextDouble());
  //        Double price = 100+(00*random.nextDouble());
  //        singleOrderDTO.setQuantity(Round(quantity,1)); //Random Quantity with range 100 - 600
  //        singleOrderDTO.setPrice(Round(price,1));   //Random Quantity with range 100 - 200
  //        UserDTO userDTO = new UserDTO();
  //        userDTO.setId(2000L);
  //        singleOrderDTO.setUser(userDTO);
  //
  //        RestTemplate restTemplate = new RestTemplate();
  //        HttpHeaders headers = new HttpHeaders();
  //        headers.set("authorization",bearer);
  //        HttpEntity<SingleOrderDTO> httpEntity = new HttpEntity<SingleOrderDTO>(singleOrderDTO, headers);
  //
  //        SingleOrderDTO createdSingleOrderDTO = restTemplate.postForObject(
  //                "http://localhost:"+port+contextPath+"/modules/market/orders",
  //                httpEntity,
  //                SingleOrderDTO.class
  //        );
  //
  //        return createdSingleOrderDTO;
  //    }
  //
  //    private static double Round(double value, int places) {
  //        if (places < 0) throw new IllegalArgumentException();
  //
  //        long factor = (long) Math.pow(10, places);
  //        value = value * factor;
  //        long tmp = Math.round(value);
  //        return (double) tmp / factor;
  //    }

}
