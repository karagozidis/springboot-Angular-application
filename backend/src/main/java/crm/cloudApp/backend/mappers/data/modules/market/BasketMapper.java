package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.dto.data.modules.market.BasketDTO;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.models.data.modules.market.Basket;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {
    SingleOrderMapper.class})
public abstract class BasketMapper extends BaseMapper<BasketDTO, Basket> {

  //   public abstract Basket mapBasket2(SingleOrderDTO basketDTO);

  public Basket mapBasket(BasketDTO basketDTO) {
    Basket basket = this.map(basketDTO);

    for (SingleOrder order : basket.getOrders()) {
      order.setCurrent_quantity(order.getQuantity());
      order.setCurrent_price(order.getPrice());
    }

    return basket;
  }

  @Mapping(ignore = true, target = "modifiedBy")
  @Mapping(ignore = true, target = "modifiedOn")
  @Mapping(ignore = true, target = "createdBy")
  @Mapping(ignore = true, target = "version")
  protected abstract Basket map(SingleOrderDTO basketDTO);


}
