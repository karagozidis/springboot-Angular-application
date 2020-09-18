package crm.cloudApp.backend.repositories.users;

import crm.cloudApp.backend.models.users.Country;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends BaseRepository<Country> {

  List<Country> findAll();

}
