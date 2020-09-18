package crm.cloudApp.backend.dto.encryption;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Builder
public class AesKeyDTO extends BaseDTO {

  @NotNull
  private String encryptedKey;

  @NotNull
  private int size;

  @NotNull
  private int encryptionVersion;

  @NotNull
  private Long userGroupId;

  @NotNull
  private Long rsaPublicKeyId;

  @NotNull
  private String keyUUID;
}
