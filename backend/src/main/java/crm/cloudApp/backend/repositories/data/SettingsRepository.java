package crm.cloudApp.backend.repositories.data;

import crm.cloudApp.backend.models.data.Settings;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingsRepository extends BaseRepository<Settings> {

    List<Settings> findAllByNameIsLike(String name);
    List<Settings> findAllByTagLike(String tag);
    List<Settings> findAll();

    @Query("SELECT s FROM Settings s ORDER BY s.createdBy desc ")
    Page<Settings> findPage(Pageable pageable);


}
