package crm.cloudApp.backend.mappers.data.modules.market;


import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import org.apache.commons.lang3.SerializationUtils;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses =
    {UserMapper.class})
@DecoratedWith(TradeMapperDecorator.class)
public abstract class TradeMapper extends BaseMapper<TradeDTO, MatchedOrder> {




  @Mapping(ignore = true, target = "singleOrder1.user")
  @Mapping(ignore = true, target = "singleOrder2.user")
  @Mapping(ignore = true, target = "singleOrder1.product.user")
  @Mapping(ignore = true, target = "singleOrder2.product.user")

  @Mapping(ignore = true, target = "buyer.createdBy")
  @Mapping(ignore = true, target = "buyer.createdOn")
  @Mapping(ignore = true, target = "buyer.defaultMarket")
  @Mapping(ignore = true, target = "buyer.email")
  @Mapping(ignore = true, target = "buyer.id")
  @Mapping(ignore = true, target = "buyer.landingPage")
  @Mapping(ignore = true, target = "buyer.market")
  @Mapping(ignore = true, target = "buyer.modulesCrm")
  @Mapping(ignore = true, target = "buyer.modulesMarket")
  @Mapping(ignore = true, target = "buyer.password")
  @Mapping(ignore = true, target = "buyer.rolesCrm")
  @Mapping(ignore = true, target = "buyer.rolesMarket")
  @Mapping(ignore = true, target = "buyer.status")

  @Mapping(ignore = true, target = "seller.createdBy")
  @Mapping(ignore = true, target = "seller.createdOn")
  @Mapping(ignore = true, target = "seller.defaultMarket")
  @Mapping(ignore = true, target = "seller.email")
  @Mapping(ignore = true, target = "seller.id")
  @Mapping(ignore = true, target = "seller.landingPage")
  @Mapping(ignore = true, target = "seller.market")
  @Mapping(ignore = true, target = "seller.modulesCrm")
  @Mapping(ignore = true, target = "seller.modulesMarket")
  @Mapping(ignore = true, target = "seller.password")
  @Mapping(ignore = true, target = "seller.rolesCrm")
  @Mapping(ignore = true, target = "seller.rolesMarket")
  @Mapping(ignore = true, target = "seller.status")

  @Mapping(target = "productName", source = "entity.singleOrder1.product.name")
  public abstract TradeDTO mapWithBuyerSeller(MatchedOrder entity);

  @Mapping(ignore = true, target = "singleOrder1.user")
  @Mapping(ignore = true, target = "singleOrder2.user")
  @Mapping(target = "singleOrder1.user.id", source = "entity.singleOrder1.user.id")
  @Mapping(target = "singleOrder2.user.id", source = "entity.singleOrder2.user.id")
  @Mapping(ignore = true, target = "singleOrder1.product.user")
  @Mapping(ignore = true, target = "singleOrder2.product.user")
  @Mapping(ignore = true, target = "singleOrder2.quantity")
  @Mapping(ignore = true, target = "singleOrder2.current_quantity")
  @Mapping(ignore = true, target = "singleOrder2.price")
  @Mapping(ignore = true, target = "singleOrder2.current_price")
  @Mapping(ignore = true, target = "singleOrder2.orderStatus")
  @Mapping(ignore = true, target = "singleOrder2.orderType")
  @Mapping(ignore = true, target = "singleOrder2.metadata")
  @Mapping(target = "productName", source = "entity.singleOrder1.product.name")
  public abstract TradeDTO mapWithBuyerSellerIgnoringOthers(MatchedOrder entity);


  @Mapping(ignore = true, target = "singleOrder1.user")
  @Mapping(ignore = true, target = "singleOrder2.user")
  @Mapping(ignore = true, target = "singleOrder1.product.user")
  @Mapping(ignore = true, target = "singleOrder2.product.user")
  @Mapping(target = "singleOrder1.user.id", source = "entity.singleOrder1.user.id")
  @Mapping(target = "singleOrder2.user.id", source = "entity.singleOrder2.user.id")
  @Mapping(target = "productName", source = "entity.singleOrder1.product.name")
  public abstract TradeDTO mapWithBuyerSellerOwnOrder(MatchedOrder entity);


  public List<TradeDTO> mapForUserCloningOwnOrders(List<MatchedOrder> matchedOrders, Long userId) {

    for (MatchedOrder matchedOrder : matchedOrders) {
      if (matchedOrder.getSingleOrder1().getUser().getId() != userId) {
        swapSingleOrders(matchedOrder);
      }
    }

    List<TradeDTO> matchedOrderDTOs = new ArrayList<>();

    for (MatchedOrder matchedOrder : matchedOrders) {
      TradeDTO matchedOrderDTO = this.mapWithBuyerSeller(matchedOrder);
      matchedOrderDTOs.add(matchedOrderDTO);
      if (matchedOrder.getSingleOrder1().getUser().getId() == matchedOrder.getSingleOrder2()
          .getUser().getId()) {
        TradeDTO clonedMatchedOrderDTO = SerializationUtils.clone(matchedOrderDTO);
        swapSingleOrderDTOs(clonedMatchedOrderDTO);
        matchedOrderDTOs.add(clonedMatchedOrderDTO);
      }
    }

    return matchedOrderDTOs;
  }


  public List<TradeDTO> mapAndFilterForUserCloningOwnOrders(List<MatchedOrder> matchedOrders,
      Long userId) {

    for (MatchedOrder matchedOrder : matchedOrders) {
      if (matchedOrder.getSingleOrder1().getUser().getId() != userId) {
        swapSingleOrders(matchedOrder);
      }
    }

    List<TradeDTO> matchedOrderDTOs = new ArrayList<>();

    for (MatchedOrder matchedOrder : matchedOrders) {
      TradeDTO matchedOrderDTO;
      if (matchedOrder.getSingleOrder2().getUser().getId().equals(userId)) {
        matchedOrderDTO = this.mapWithBuyerSellerOwnOrder(matchedOrder);
      } else {
        matchedOrderDTO = this.mapWithBuyerSellerIgnoringOthers(matchedOrder);
      }
      matchedOrderDTOs.add(matchedOrderDTO);

      if (matchedOrder.getSingleOrder1().getUser().getId() == matchedOrder.getSingleOrder2()
          .getUser().getId()) {
        TradeDTO clonedMatchedOrderDTO = SerializationUtils.clone(matchedOrderDTO);
        swapSingleOrderDTOs(clonedMatchedOrderDTO);
        matchedOrderDTOs.add(clonedMatchedOrderDTO);
      }

    }
    return matchedOrderDTOs;
  }


  public List<TradeDTO> mapAndFilterForUser(List<MatchedOrder> matchedOrders, Long userId) {

    for (MatchedOrder matchedOrder : matchedOrders) {
      if (matchedOrder.getSingleOrder1().getUser().getId() != userId) {
        swapSingleOrders(matchedOrder);
      }
    }

    List<TradeDTO> matchedOrderDTOs = new ArrayList<>();

    for (MatchedOrder matchedOrder : matchedOrders) {
      TradeDTO matchedOrderDTO;
      if (matchedOrder.getSingleOrder2().getUser().getId().equals(userId)) {
        matchedOrderDTO = this.mapWithBuyerSellerOwnOrder(matchedOrder);
      } else {
        matchedOrderDTO = this.mapWithBuyerSellerIgnoringOthers(matchedOrder);
      }
      matchedOrderDTOs.add(matchedOrderDTO);
    }
    return matchedOrderDTOs;
  }


  void swapSingleOrders(MatchedOrder matchedOrder) {
    SingleOrder singleOrder1 = matchedOrder.getSingleOrder1();
    Double order1_price = matchedOrder.getOrder1_price();
    Double order1_quantity = matchedOrder.getOrder1_quantity();

    matchedOrder.setSingleOrder1(matchedOrder.getSingleOrder2());
    matchedOrder.setOrder1_price(matchedOrder.getOrder2_price());
    matchedOrder.setOrder1_quantity(matchedOrder.getOrder2_quantity());

    matchedOrder.setSingleOrder2(singleOrder1);
    matchedOrder.setOrder2_price(order1_price);
    matchedOrder.setOrder2_quantity(order1_quantity);
  }


  void swapSingleOrderDTOs(TradeDTO matchedOrder) {
    SingleOrderDTO singleOrder1 = matchedOrder.getSingleOrder1();
    Double order1_price = matchedOrder.getOrder1_price();
    Double order1_quantity = matchedOrder.getOrder1_quantity();

    matchedOrder.setSingleOrder1(matchedOrder.getSingleOrder2());
    matchedOrder.setOrder1_price(matchedOrder.getOrder2_price());
    matchedOrder.setOrder1_quantity(matchedOrder.getOrder2_quantity());

    matchedOrder.setSingleOrder2(singleOrder1);
    matchedOrder.setOrder2_price(order1_price);
    matchedOrder.setOrder2_quantity(order1_quantity);
  }


}
