package crm.cloudApp.backend.services.data;

import crm.cloudApp.backend.dto.data.SettingsDTO;
import crm.cloudApp.backend.mappers.data.SettingsMapper;
import crm.cloudApp.backend.models.data.Settings;
import crm.cloudApp.backend.repositories.data.SettingsRepository;
import crm.cloudApp.backend.services.users.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@Validated
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final SettingsMapper settingsMapper;
    private final UserService userService;


    public SettingsService(SettingsRepository settingsRepository,
                           SettingsMapper settingsMapper, UserService userService) {
        this.settingsRepository = settingsRepository;
        this.settingsMapper = settingsMapper;
        this.userService = userService;
    }

    public SettingsDTO getByName(String name) {
        List<Settings> settings = settingsRepository.findAllByNameIsLike(name);

        if (settings.size() != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to retrieve this settings");
        }

        return settingsMapper.map(settings.get(0));
    }

    public List<SettingsDTO> getByTag(String tag) {
        List<Settings> settings = settingsRepository.findAllByTagLike(tag);
        return settingsMapper.map(settings);
    }

    @Transactional
    public void create(SettingsDTO settingsDTO) {
        Settings setting = settingsMapper.map(settingsDTO);
        List<Settings> settings = settingsRepository.findAllByNameIsLike(setting.getName());

        if (settings.size() > 0) {
            settingsRepository.deleteAll(settings);
        }
        setting.setCreatedBy(userService.getLoggedInUserName());
        setting.setCreatedOn(Instant.now());
        setting.setModifiedBy(userService.getLoggedInUserName());
        setting.setModifiedOn(Instant.now());

        settingsRepository.save(setting);
    }

    @Transactional
    public void remove(String name) {
        List<Settings> settings = settingsRepository.findAllByNameIsLike(name);

        if (settings.size() > 0) {
            settingsRepository.deleteAll(settings);
        }
    }

    public List<SettingsDTO> get() {
        List<Settings> settings = settingsRepository.findAll();
        return settingsMapper.map(settings);
    }

    public Page<SettingsDTO> getPage(Pageable pageable) {
        Page<Settings> settings = settingsRepository.findPage(pageable);
        return settingsMapper.map(settings);
    }
}
