package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.config.AppConstants.Types.SingleOrderStatus;
import crm.cloudApp.backend.dto.data.modules.market.ProductDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.models.data.modules.market.Product;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Mapper(componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    uses = {UserMapper.class, SingleOrderMapper.class},
    imports = {ChronoUnit.class, Duration.class})
public abstract class ProductMapper extends BaseMapper<ProductDTO, Product> {

  @Mapping(ignore = true, target = "user")
  @Mapping(expression = "java(product.getDeliveryTimeStart().minus(1, ChronoUnit.HOURS))", target = "closesAt")
  public abstract ProductDTO map(Product product);

  public List<ProductDTO> mapProductsForBook(List<Product> products) {
    List<ProductDTO> productBookDTOS = new ArrayList<>();
    for (Product product : products) {

      ProductDTO productDTO = this.map(product);

      if (product.getOrders().size() > 0) {
        productDTO.setHasOrders(true);
      } else {
        productDTO.setHasOrders(false);
      }

      product.getOrders().sort(Comparator.comparing(SingleOrder::getBidTime));

      SingleOrder latestSellOrder =
          product.getOrders().stream()
              .sorted(Comparator.comparing(SingleOrder::getCurrent_price).reversed()
                  .thenComparing(SingleOrder::getBidTime).reversed())
              .filter(o -> o.getOrderDirection() == AppConstants.Types.OrderDirection.SELL
                  && !o.getOrderStatus().equals(SingleOrderStatus.DEACTIVATED)
                  && !o.getOrderStatus().equals(SingleOrderStatus.REMOVED)
                  && !o.getOrderStatus().equals(SingleOrderStatus.DRAFT)
                  && !o.getOrderStatus().equals(SingleOrderStatus.MATCHED)
                  && !o.getOrderStatus().equals(SingleOrderStatus.EXPIRED)
              )
              .findFirst().orElse(null);

      SingleOrder latestBuyOrder = product.getOrders().stream()
          .sorted(Comparator.comparing(SingleOrder::getCurrent_price)
              .thenComparing(SingleOrder::getBidTime).reversed())
          .filter(o -> o.getOrderDirection() == AppConstants.Types.OrderDirection.BUY
              && !o.getOrderStatus().equals(SingleOrderStatus.DEACTIVATED)
              && !o.getOrderStatus().equals(SingleOrderStatus.REMOVED)
              && !o.getOrderStatus().equals(SingleOrderStatus.DRAFT)
              && !o.getOrderStatus().equals(SingleOrderStatus.MATCHED)
              && !o.getOrderStatus().equals(SingleOrderStatus.EXPIRED)
          )
          .findFirst().orElse(null);

      if (latestSellOrder != null) {
        productDTO.setLatestSellOrder(this.mapSelectedOrder(latestSellOrder));
      }
      if (latestBuyOrder != null) {
        productDTO.setLatestBuyOrder(this.mapSelectedOrder(latestBuyOrder));
      }

      productBookDTOS.add(productDTO);
    }
    return productBookDTOS;
  }

  public List<ProductDTO> mapProductsForHistory(List<Product> products) {
    List<ProductDTO> productBookDTOS = new ArrayList<>();
    for (Product product : products) {
      ProductDTO productDTO = this.map(product);

      if (product.getOrders().size() > 0) {
        productDTO.setHasOrders(true);
      } else {
        productDTO.setHasOrders(false);
      }

      productDTO.setTotalOrders(product.getOrders().size());

      productBookDTOS.add(productDTO);
    }
    return productBookDTOS;
  }


  @Mapping(ignore = true, target = "product")
  @Mapping(ignore = true, target = "user")
  public abstract SingleOrderDTO mapSelectedOrder(SingleOrder singleOrder);

}
