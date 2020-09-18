package crm.cloudApp.backend.mappers.data.modules.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseInfoTradeMapperDecorator extends BaseInfoTradeMapper {
  @Autowired
  @Qualifier("delegate")
  private BaseInfoTradeMapper delegate;

//  @Override
//  public BaseInfoTradeDTO mapBaseTrades(MatchedOrder entity) {
//    BaseInfoTradeDTO baseInfoTradeDTO = delegate.map(entity);
//    SingleOrder buyerOrder = new SingleOrder();
//    SingleOrder sellerOrder = new SingleOrder();
//
//    if (entity.getSingleOrder1().getOrderDirection() == OrderDirection.BUY) {
//      buyerOrder = entity.getSingleOrder1();
//      sellerOrder = entity.getSingleOrder2();
//    } else {
//      buyerOrder = entity.getSingleOrder2();
//      sellerOrder = entity.getSingleOrder1();
//    }
//
//    UserDTO buyerDTO = new UserDTO();
//    buyerDTO.setCountry(new CountryDTO());
//    buyerDTO.getCountry().setName(buyerOrder.getUser().getCountry().getName());
//    baseInfoTradeDTO.setBuyer(buyerDTO);
//
//    UserDTO sellerDTO = new UserDTO();
//    sellerDTO.setCountry(new CountryDTO());
//    sellerDTO.getCountry().setName(sellerOrder.getUser().getCountry().getName());
//    baseInfoTradeDTO.setSeller(sellerDTO);
//
//    return baseInfoTradeDTO;
//  }

}
