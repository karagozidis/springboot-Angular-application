package crm.cloudApp.backend.controllers.encryption;

import crm.cloudApp.backend.dto.encryption.RsaPublicKeyDTO;
import crm.cloudApp.backend.services.encryption.RsaPublicKeyService;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@RestController
@Validated
@RequestMapping("/encryption/rsa-keys")
public class RsaPublicKeyController {

  private final RsaPublicKeyService rsaPublicKeyService;

  @Autowired
  public RsaPublicKeyController(RsaPublicKeyService rsaPublicKeyService) {
    this.rsaPublicKeyService = rsaPublicKeyService;
  }

  @PostMapping(path = "/public")
  public RsaPublicKeyDTO createRsaPublicKey(@RequestBody RsaPublicKeyDTO rsaPublicKeyDTO)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
    return rsaPublicKeyService.create(rsaPublicKeyDTO);
  }

  @GetMapping(path = "/public/established")
  public @ResponseBody
  Boolean getAllRsaPublicKeys() {
    return rsaPublicKeyService.currentUserHasPublicKeys();
  }

}
