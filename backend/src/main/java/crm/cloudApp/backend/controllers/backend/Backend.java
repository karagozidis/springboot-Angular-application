package crm.cloudApp.backend.controllers.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Validated
@RequestMapping("/backend")
public class Backend {

  @GetMapping(path = "/status")
  public @ResponseBody
  String getStatus() {
    return "active";
  }

}
