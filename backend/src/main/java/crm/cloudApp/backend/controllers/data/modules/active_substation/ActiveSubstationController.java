package crm.cloudApp.backend.controllers.data.modules.active_substation;

import crm.cloudApp.backend.dto.data.modules.active_substation.ActiveSubstationMetadataDTO;
import crm.cloudApp.backend.services.data.modules.active_substation.ActiveSubstationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/active-substation")
public class ActiveSubstationController {

    private final ActiveSubstationService activeSubstationService;

    public ActiveSubstationController(ActiveSubstationService activeSubstationService) {
        this.activeSubstationService = activeSubstationService;
    }


    @GetMapping(value = "/execution-next-id")
    public String getNextForExecution(@RequestParam("user-group") Long userGroup) throws IOException {
        return this.activeSubstationService.getExecutionNextMetadata(userGroup);
    }

    @GetMapping(value = "/executionList")
    public List<ActiveSubstationMetadataDTO> getExecutionList(@RequestParam("user-group") Long userGroup) throws IOException {
        return this.activeSubstationService.getExecutionListMetadata(userGroup);
    }

    @GetMapping(value = "/list")
    public List<ActiveSubstationMetadataDTO> getEntries(@RequestParam("user-group") Long userGroup) throws IOException {
        return this.activeSubstationService.getMetadata(userGroup);
    }

    @GetMapping(value = "/execution-status")
    public int getExecutionStatus(@RequestParam("user-group") Long userGroup, @RequestHeader Map<String, String> headers) throws IOException {
        return this.activeSubstationService.getExecutionStatus(userGroup, headers);
    }

    @PostMapping(path = "/update-status")
    public void updateStatus(@RequestParam("id") String id, @RequestParam("status") String status) throws IOException {
        this.activeSubstationService.updateStatus(id,status);
    }

    @GetMapping(path = "/loyola-io-authotized")
    public Boolean isLoyolaIoAuthotized() throws IOException {
        return this.activeSubstationService.isLoyolaIoAuthotized();
    }

//    @GetMapping(path = "/get-execution-result")
//    public void updateStatus(@RequestParam("id") String id) throws IOException {
//        this.activeSubstationService.getMetadata();
//        this.activeSubstationService.updateStatus(id,status);
//    }


}
