package crm.cloudApp.backend.services.users;

import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.mappers.users.CountryMapper;
import crm.cloudApp.backend.models.users.Country;
import crm.cloudApp.backend.repositories.users.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class CountryService {


  private final CountryRepository countryRepository;
  private final CountryMapper countryMapper;

  public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
    this.countryRepository = countryRepository;
    this.countryMapper = countryMapper;
  }

  public List<CountryDTO> getAll() {

    List<Country> countries = countryRepository.findAll();
    return countryMapper.map(countries);

  }
}
