package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.dto.data.modules.market.SingleOrderDTO;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {
    UserMapper.class, ProductMapper.class})
public abstract class SingleOrderMapper extends BaseMapper<SingleOrderDTO, SingleOrder> {

  @Mapping(ignore = true, target = "modifiedBy")
  @Mapping(ignore = true, target = "modifiedOn")
  @Mapping(ignore = true, target = "createdBy")
  @Mapping(ignore = true, target = "version")
  @Mapping(ignore = true, target = "id")
  //    @Mapping(target="metadata", source="dto.id")
  @Mapping(target = "price", source = "dto.price")
  @Mapping(target = "current_price", source = "dto.price")
  @Mapping(target = "quantity", source = "dto.quantity")
  @Mapping(target = "current_quantity", source = "dto.quantity")
  public abstract SingleOrder mapOrder(SingleOrderDTO dto);

  //    @Mapping(target="price", source="entity.current_price")
  //    @Mapping(target="quantity", source="entity.current_quantity")
  public abstract SingleOrderDTO mapOrder(SingleOrder entity);

  //    @Mapping(target="price", source="entity.current_price")
  //    @Mapping(target="quantity", source="entity.current_quantity")
  @Mapping(target = "user", qualifiedByName = {"mapBasic"})
  public abstract SingleOrderDTO map(SingleOrder entity);

  @Override
  public List<SingleOrderDTO> map(List<SingleOrder> orders) {
    return orders.stream().map(this::map).collect(Collectors.toList());
  }



}

