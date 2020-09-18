package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.mappers.users.UserMapper;
import crm.cloudApp.backend.config.AppConstants.Types.OrderDirection;
import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class TradeMapperDecorator extends TradeMapper {

  @Autowired
  @Qualifier("delegate")
  private TradeMapper delegate;

  @Autowired
  private UserMapper userMapper;

  @Override
  public TradeDTO mapWithBuyerSeller(MatchedOrder entity) {
    TradeDTO tradeDTO = delegate.map(entity);

    if (entity.getSingleOrder1().getOrderDirection() == OrderDirection.BUY) {
      tradeDTO.setBuyer(
          this.userMapper.mapBasic(entity.getSingleOrder1().getUser())
      );
      tradeDTO.setSeller(
          this.userMapper.mapBasic(entity.getSingleOrder2().getUser())
      );
    } else {
      tradeDTO.setSeller(
          this.userMapper.mapBasic(entity.getSingleOrder1().getUser())
      );
      tradeDTO.setBuyer(
          this.userMapper.mapBasic(entity.getSingleOrder2().getUser())
      );
    }

    tradeDTO.setProductName(entity.getSingleOrder1().getProduct().getName());

    tradeDTO.getSingleOrder1().setUser(null);
    tradeDTO.getSingleOrder2().setUser(null);
    tradeDTO.getSingleOrder1().getProduct().setUser(null);
    tradeDTO.getSingleOrder2().getProduct().setUser(null);

    tradeDTO.getBuyer().setCreatedBy(null);
    tradeDTO.getBuyer().setCreatedOn(null);
    tradeDTO.getBuyer().setDefaultMarket(null);
    tradeDTO.getBuyer().setEmail(null);
    tradeDTO.getBuyer().setId(null);
    tradeDTO.getBuyer().setLandingPage(null);
    tradeDTO.getBuyer().setMarket(null);
    tradeDTO.getBuyer().setModulesCrm(null);
    tradeDTO.getBuyer().setModulesMarket(null);
    tradeDTO.getBuyer().setPassword(null);
    tradeDTO.getBuyer().setRolesCrm(null);
    tradeDTO.getBuyer().setRolesMarket(null);
    tradeDTO.getBuyer().setStatus(null);

    tradeDTO.getSeller().setCreatedBy(null);
    tradeDTO.getSeller().setCreatedOn(null);
    tradeDTO.getSeller().setDefaultMarket(null);
    tradeDTO.getSeller().setEmail(null);
    tradeDTO.getSeller().setId(null);
    tradeDTO.getSeller().setLandingPage(null);
    tradeDTO.getSeller().setMarket(null);
    tradeDTO.getSeller().setModulesCrm(null);
    tradeDTO.getSeller().setModulesMarket(null);
    tradeDTO.getSeller().setPassword(null);
    tradeDTO.getSeller().setRolesCrm(null);
    tradeDTO.getSeller().setRolesMarket(null);
    tradeDTO.getSeller().setStatus(null);

    return tradeDTO;
  }

}
