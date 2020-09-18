package crm.cloudApp.backend.controllers.users;

import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.services.users.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@Validated
@RequestMapping("/countries")
public class CountryController {

  private final CountryService countryService;

  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @GetMapping
  public @ResponseBody
  Collection<CountryDTO> getAll() {
    return countryService.getAll();
  }


}
