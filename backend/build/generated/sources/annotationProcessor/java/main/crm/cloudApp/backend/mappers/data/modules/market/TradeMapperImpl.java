package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.trades.TradeDTO;
import crm.cloudApp.backend.models.data.modules.market.MatchedOrder;

import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:13+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
@Primary
public class TradeMapperImpl extends TradeMapperDecorator {

    @Autowired
    @Qualifier("delegate")
    private TradeMapper delegate;

    @Override
    public MatchedOrder map(TradeDTO dto)  {
        return delegate.map( dto );
    }

    @Override
    public TradeDTO map(MatchedOrder entity)  {
        return delegate.map( entity );
    }

    @Override
    public void map(TradeDTO dto, MatchedOrder entity)  {
        delegate.map( dto, entity );
    }

    @Override
    public Page<TradeDTO> map(Page<MatchedOrder> all)  {
        return delegate.map( all );
    }

    @Override
    public Iterable<MatchedOrder> mapDTOs(Collection<TradeDTO> all)  {
        return delegate.mapDTOs( all );
    }

    @Override
    public Collection<TradeDTO> mapDTOs(Iterable<MatchedOrder> all)  {
        return delegate.mapDTOs( all );
    }

    @Override
    public List<MatchedOrder> mapDTOs(List<TradeDTO> all)  {
        return delegate.mapDTOs( all );
    }

    @Override
    public List<TradeDTO> map(List<MatchedOrder> all)  {
        return delegate.map( all );
    }

    @Override
    public Long mapToEntityId(MatchedOrder entity)  {
        return delegate.mapToEntityId( entity );
    }

    @Override
    public TradeDTO mapWithBuyerSellerIgnoringOthers(MatchedOrder entity)  {
        return delegate.mapWithBuyerSellerIgnoringOthers( entity );
    }

    @Override
    public TradeDTO mapWithBuyerSellerOwnOrder(MatchedOrder entity)  {
        return delegate.mapWithBuyerSellerOwnOrder( entity );
    }

    @Override
    public void swapSingleOrders(MatchedOrder matchedOrder)  {
        delegate.swapSingleOrders( matchedOrder );
    }

    @Override
    public void swapSingleOrderDTOs(TradeDTO matchedOrder)  {
        delegate.swapSingleOrderDTOs( matchedOrder );
    }
}
