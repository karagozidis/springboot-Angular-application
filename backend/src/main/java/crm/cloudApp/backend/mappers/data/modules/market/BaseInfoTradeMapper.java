package crm.cloudApp.backend.mappers.data.modules.market;


import crm.cloudApp.backend.mappers.common.BaseMapper;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.data.modules.market.trades.BaseInfoTradeDTO;
import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.dto.users.UserDTO;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;
import crm.cloudApp.backend.models.data.modules.market.SingleOrder;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@DecoratedWith(BaseInfoTradeMapperDecorator.class)
public abstract class BaseInfoTradeMapper extends BaseMapper<BaseInfoTradeDTO, MatchedOrder> {

    @Mapping(ignore = true, target = "buyer")
    @Mapping(ignore = true, target = "seller")
    @Mapping(target = "productName", source = "entity.singleOrder1.product.name")
    public abstract BaseInfoTradeDTO mapBaseTrades(MatchedOrder entity);

    public List<BaseInfoTradeDTO> mapBaseTrades(List<MatchedOrder> matchedOrders) {

        List<BaseInfoTradeDTO> baseInfoTradeDTOS = new ArrayList<>();

        for (MatchedOrder matchedOrder : matchedOrders) {
            BaseInfoTradeDTO baseInfoTradeDTO = this.mapBaseTrades(matchedOrder);
            BaseInfoTradeDTO baseInfoTradeDTOWithBuyerSeller = mapBuyerSeller(matchedOrder,
                    baseInfoTradeDTO);

            baseInfoTradeDTOS.add(baseInfoTradeDTOWithBuyerSeller);
        }

        return baseInfoTradeDTOS;
    }

    private BaseInfoTradeDTO mapBuyerSeller(MatchedOrder matchedOrder,
                                            BaseInfoTradeDTO baseInfoTradeDto) {
        SingleOrder buyerOrder = new SingleOrder();
        SingleOrder sellerOrder = new SingleOrder();

        if (matchedOrder.getSingleOrder1().getOrderDirection()
                == AppConstants.Types.OrderDirection.BUY) {
            buyerOrder = matchedOrder.getSingleOrder1();
            sellerOrder = matchedOrder.getSingleOrder2();
        } else {
            buyerOrder = matchedOrder.getSingleOrder2();
            sellerOrder = matchedOrder.getSingleOrder1();
        }

        UserDTO buyerDTO = new UserDTO();
        buyerDTO.setCountry(new CountryDTO());
        if (buyerOrder.getUser().getCountry() != null)
            buyerDTO.getCountry().setName(buyerOrder.getUser().getCountry().getName());
            buyerDTO.setUsername(buyerOrder.getUser().getUsername());

        baseInfoTradeDto.setBuyer(buyerDTO);

        UserDTO sellerDTO = new UserDTO();
        sellerDTO.setCountry(new CountryDTO());

        if (sellerOrder.getUser().getCountry() != null)
            sellerDTO.getCountry().setName(sellerOrder.getUser().getCountry().getName());
        sellerDTO.setUsername(sellerOrder.getUser().getUsername());
        baseInfoTradeDto.setSeller(sellerDTO);

        return baseInfoTradeDto;
    }

}
