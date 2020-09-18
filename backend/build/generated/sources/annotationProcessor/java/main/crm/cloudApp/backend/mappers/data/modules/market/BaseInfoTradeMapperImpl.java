package crm.cloudApp.backend.mappers.data.modules.market;

import crm.cloudApp.backend.dto.data.modules.market.trades.BaseInfoTradeDTO;
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
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
@Primary
public class BaseInfoTradeMapperImpl extends BaseInfoTradeMapperDecorator {

    @Autowired
    @Qualifier("delegate")
    private BaseInfoTradeMapper delegate;

    @Override
    public MatchedOrder map(BaseInfoTradeDTO dto)  {
        return delegate.map( dto );
    }

    @Override
    public BaseInfoTradeDTO map(MatchedOrder entity)  {
        return delegate.map( entity );
    }

    @Override
    public void map(BaseInfoTradeDTO dto, MatchedOrder entity)  {
        delegate.map( dto, entity );
    }

    @Override
    public Page<BaseInfoTradeDTO> map(Page<MatchedOrder> all)  {
        return delegate.map( all );
    }

    @Override
    public Iterable<MatchedOrder> mapDTOs(Collection<BaseInfoTradeDTO> all)  {
        return delegate.mapDTOs( all );
    }

    @Override
    public Collection<BaseInfoTradeDTO> mapDTOs(Iterable<MatchedOrder> all)  {
        return delegate.mapDTOs( all );
    }

    @Override
    public List<MatchedOrder> mapDTOs(List<BaseInfoTradeDTO> all)  {
        return delegate.mapDTOs( all );
    }

    @Override
    public List<BaseInfoTradeDTO> map(List<MatchedOrder> all)  {
        return delegate.map( all );
    }

    @Override
    public Long mapToEntityId(MatchedOrder entity)  {
        return delegate.mapToEntityId( entity );
    }

    @Override
    public BaseInfoTradeDTO mapBaseTrades(MatchedOrder entity)  {
        return delegate.mapBaseTrades( entity );
    }

    @Override
    public List<BaseInfoTradeDTO> mapBaseTrades(List<MatchedOrder> matchedOrders)  {
        return delegate.mapBaseTrades( matchedOrders );
    }
}
