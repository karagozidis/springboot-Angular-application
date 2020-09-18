package crm.cloudApp.backend.controllers.data.modules.hyppoio;

import crm.cloudApp.backend.dto.data.modules.hyppoio.HyppoIoMetaDataDTO;
import crm.cloudApp.backend.services.data.modules.hyppoio.HyppoIoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/hyppo-io")
public class HyppoIoController {

  private final HyppoIoService hyppoIoService;

  public HyppoIoController(HyppoIoService hyppoIoService) {
    this.hyppoIoService = hyppoIoService;
  }

  @GetMapping(value = "/history")
  public List<HyppoIoMetaDataDTO> getHistory() throws IOException {
    return this.hyppoIoService.getHistory();
  }

}
