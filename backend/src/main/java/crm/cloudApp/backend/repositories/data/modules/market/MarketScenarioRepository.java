package crm.cloudApp.backend.repositories.data.modules.market;

import crm.cloudApp.backend.models.data.modules.market.MarketScenario;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketScenarioRepository extends BaseRepository<MarketScenario> {

    @Query("SELECT s FROM MarketScenario s ORDER BY s.createdBy desc")
    Page<MarketScenario> findPage(Pageable pageable);

}
