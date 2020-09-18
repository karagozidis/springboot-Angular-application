package crm.cloudApp.backend.controllers.encryption;

import crm.cloudApp.backend.dto.encryption.AesKeyDTO;
import crm.cloudApp.backend.services.encryption.AesKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@Validated
@RequestMapping("/encryption/aes-keys")
public class AesKeyController {

    private final AesKeyService aesKeyService;

    @Autowired
    public AesKeyController(AesKeyService aesKeyService) {
        this.aesKeyService = aesKeyService;
    }

    @GetMapping
    public Collection<AesKeyDTO> getAllAesKeys() {
        return aesKeyService.getAllAesKey();
    }

    @GetMapping(path = "/pages")
    public @ResponseBody
    Page<AesKeyDTO> getAesKeys(Pageable pageable) {
        return aesKeyService.getAesKey(pageable);
    }

    @GetMapping(path = "/{aesKeyId}")
    public AesKeyDTO getAesKey(@PathVariable("aesKeyId") Long aesKeyId) {
        return aesKeyService.getAesKey(aesKeyId);
    }
}
