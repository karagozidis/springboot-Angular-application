package crm.cloudApp.backend.controllers.data.modules.active_substation;

import crm.cloudApp.backend.dto.data.modules.active_substation.ActiveSubstationForecastMetadataDTO;
import crm.cloudApp.backend.services.data.modules.active_substation.ActiveSubstationForecastsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/modules/active-substation-forecasts")
public class ActiveSubstationForecastsController {

    private final ActiveSubstationForecastsService activeSubstationForecastsService;

    public ActiveSubstationForecastsController(ActiveSubstationForecastsService activeSubstationForecastsService) {
        this.activeSubstationForecastsService = activeSubstationForecastsService;
    }

    @GetMapping
    public List<ActiveSubstationForecastMetadataDTO> getAll() throws IOException {
        return this.activeSubstationForecastsService.getAll();
    }

    @GetMapping(value = "/sync-list")
    public List<ActiveSubstationForecastMetadataDTO> getSyncList(@RequestParam("user-group") Long userGroup) throws IOException {
        List<ActiveSubstationForecastMetadataDTO>  syncList = this.activeSubstationForecastsService.getSyncList(userGroup);
        return syncList;
    }

    @PostMapping(value = "/forecast-is-synced")
    public void updateMetadata(@RequestParam("unique-id") String uniqueId) throws IOException {
        ActiveSubstationForecastMetadataDTO activeSubstationForecastMetadataDTO =  this.activeSubstationForecastsService.getSyncMetaByUniqueId(uniqueId);
        this.activeSubstationForecastsService.updateMetadata(uniqueId, activeSubstationForecastMetadataDTO);
    }

    @PutMapping(value = "/metadata")
    public void updateMetadata(@RequestParam("id") String id, @RequestBody ActiveSubstationForecastMetadataDTO activeSubstationForecastMetadataDTO) throws IOException {
        this.activeSubstationForecastsService.updateMetadata(id, activeSubstationForecastMetadataDTO);
    }




    @GetMapping(value = "/dates")
    public Map<String, Instant> getDates() throws IOException {
        return this.activeSubstationForecastsService.getDates();
    }

    @DeleteMapping
    public void delete(@RequestParam("unique-id") String uniqueId) {
        this.activeSubstationForecastsService.deleteByUniqueId(uniqueId);
    }

}
