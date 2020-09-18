package crm.cloudApp.backend.config.data.modules.market;

import crm.cloudApp.backend.mappers.data.modules.market.BasketMapper;
import crm.cloudApp.backend.mappers.data.modules.market.SingleOrderMapper;
import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.models.data.modules.market.ImportOrder;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.services.data.modules.market.BasketService;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.config.AppConstants.Types.SingleOrderStatus;
import crm.cloudApp.backend.dto.data.modules.market.BasketDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.repositories.data.modules.market.BasketRepository;
import crm.cloudApp.backend.repositories.data.modules.market.ProductRepository;
import crm.cloudApp.backend.repositories.data.modules.market.SingleOrderRepository;
import crm.cloudApp.backend.services.data.modules.market.ImportOrderService;
import crm.cloudApp.backend.services.data.modules.market.OrderService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableScheduling
public class MarketImportOrders {

    private final ImportOrderService importOrderService;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final SingleOrderRepository singleOrderRepository;
    private final SingleOrderMapper singleOrderMapper;
    private final BasketRepository basketRepository;
    private final BasketService basketService;
    private final BasketMapper basketMapper;

    public MarketImportOrders(ImportOrderService importOrderService,
                              ProductRepository productRepository,
                              OrderService orderService,
                              SingleOrderRepository singleOrderRepository,
                              SingleOrderMapper singleOrderMapper, BasketRepository basketRepository, BasketService basketService, BasketMapper basketMapper) {
        this.importOrderService = importOrderService;
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.singleOrderRepository = singleOrderRepository;
        this.singleOrderMapper = singleOrderMapper;
        this.basketRepository = basketRepository;
        this.basketService = basketService;
        this.basketMapper = basketMapper;
    }

    // This scheduler imports the auto generated orders to the market
    // Runs every minute
    @Scheduled(cron = "0 0/1 * * * ?")
    public void importOrdersToTheMarket() throws IOException {
        List<ImportOrder> importOrders = importOrderService.getOrdersByTimestamp(Instant.now());
        for (ImportOrder importOrder : importOrders) {

            if (importOrder.getAction().toLowerCase().equals("modify")
                    && importOrder.getActiveGroupId() != null) {
                Optional<SingleOrder> optionalSingleOrder = this.singleOrderRepository
                        .findById(importOrder.getActiveGroupId());
                if (optionalSingleOrder.isPresent()) {
                    SingleOrder singleOrder = optionalSingleOrder.get();
                    if (singleOrder.getOrderStatus().equals(SingleOrderStatus.ACTIVE) || singleOrder
                            .getOrderStatus().equals(SingleOrderStatus.PARTLY_MATCHED)) {
                        this.orderService.remove(singleOrder.getId());
                    }
                }
            }

            List<Product> products = productRepository.findByName(importOrder.getProduct_name(),
                    importOrder.getMarketCode());
            if (!products.isEmpty()) {
                SingleOrder singleOrder = new SingleOrder();
                singleOrder.setProduct(products.get(0));
                singleOrder.setPrice(importOrder.getPrice());
                singleOrder.setQuantity(importOrder.getQuantity());
                singleOrder.setCurrent_price(importOrder.getPrice());
                singleOrder.setCurrent_quantity(importOrder.getQuantity());
                singleOrder.setOrderDirection(importOrder.getOrderDirection());
                singleOrder.setOrderType(importOrder.getOrderType());
                singleOrder.setMetadata(importOrder.getOrder_meta());
                User user = new User();
                user.setId(importOrder.getUserId());
                singleOrder.setUser(user);

                if (singleOrder.getQuantity() > 0 && singleOrder.getPrice() > 0) {
                    SingleOrderDTO createdOrder = orderService
                            .create(singleOrderMapper.map(singleOrder));
                    this.importOrderService
                            .connectMarketEntityToImportOrder(importOrder, createdOrder.getId());
                }
            }
        }
    }

    // This scheduler imports the auto generated Baskets to the market
    // Runs every minute
    @Scheduled(cron = "0 0/1 * * * ?")
    public void importBasketsToTheMarket() throws IOException {
        List<List<ImportOrder>> importOrderGroups = importOrderService.getBasketsByTimestamp(Instant.now());
        for (List<ImportOrder> importOrderGroup : importOrderGroups) {

            if (importOrderGroup.size() == 0) continue;

            if (importOrderGroup.get(0).getAction().toLowerCase().equals("modify")
                    && importOrderGroup.get(0).getActiveGroupId() != null) {

                Optional<Basket> optionalBasket = this.basketRepository
                        .findById(importOrderGroup.get(0).getActiveGroupId());
                if (optionalBasket.isPresent()) {
                    Basket basket = optionalBasket.get();
                    this.basketService.remove(basket.getId());
                }
            }

            List<SingleOrder> singleOrders = new ArrayList<>();
            for (ImportOrder importOrder : importOrderGroup) {
                List<Product> products = productRepository.findByName(importOrder.getProduct_name(),
                        importOrder.getMarketCode());
                if (!products.isEmpty()) {
                    SingleOrder singleOrder = new SingleOrder();
                    singleOrder.setProduct(products.get(0));
                    singleOrder.setPrice(importOrder.getPrice());
                    singleOrder.setQuantity(importOrder.getQuantity());
                    singleOrder.setCurrent_price(importOrder.getPrice());
                    singleOrder.setCurrent_quantity(importOrder.getQuantity());
                    singleOrder.setOrderDirection(importOrder.getOrderDirection());
                    singleOrder.setOrderType(importOrder.getOrderType());
                    singleOrder.setMetadata(importOrder.getOrder_meta());
                    User user = new User();
                    user.setId(importOrder.getUserId());
                    singleOrder.setUser(user);
                    if (singleOrder.getQuantity() > 0 && singleOrder.getPrice() > 0) {
                        singleOrders.add(singleOrder);
                    }
                }

            }

            if (singleOrders.size() == 0) continue;

            Basket basket = new Basket();
            basket.setOrders(singleOrders);
            basket.setBasketType(AppConstants.Types.BasketType.valueOf(importOrderGroup.get(0).getBasket_Type()));
            basket.setMetadata(importOrderGroup.get(0).getBasket_meta());
            BasketDTO createdBasketDTO = this.basketService.create(this.basketMapper.map(basket));

            this.importOrderService
                    .connectMarketBasketToImportOrder(importOrderGroup.get(0), createdBasketDTO.getId());

            this.importOrderService
                    .connectMarketBasketToImportOrderList(importOrderGroup, createdBasketDTO.getId());



        }
    }


}
