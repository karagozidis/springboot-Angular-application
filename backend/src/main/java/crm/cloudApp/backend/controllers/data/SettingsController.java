package crm.cloudApp.backend.controllers.data;

import crm.cloudApp.backend.dto.data.SettingsDTO;
import crm.cloudApp.backend.services.data.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public List<SettingsDTO> get() {
        return settingsService.get();
    }

    @GetMapping(path = "/page")
    public Page<SettingsDTO> getPage(Pageable pageable) {
        return settingsService.getPage(pageable);
    }

    @GetMapping(path = "/by-name")
    public SettingsDTO getByName(@RequestParam("name") String name) {
        return settingsService.getByName(name);
    }

    @GetMapping(path = "/by-tag")
    public List<SettingsDTO> getByTag(@RequestParam("tag") String tag) {
        return settingsService.getByTag(tag);
    }

    @PostMapping
    public void create(
            @RequestBody SettingsDTO settingsDTO) {
        settingsService.create(settingsDTO);
    }

    @DeleteMapping
    public void remove(@RequestParam("name") String name) {
        settingsService.remove(name);
    }


}
