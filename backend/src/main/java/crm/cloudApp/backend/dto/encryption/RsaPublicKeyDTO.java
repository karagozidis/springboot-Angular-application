package crm.cloudApp.backend.dto.encryption;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.common.BaseDTO;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class RsaPublicKeyDTO extends BaseDTO {

  @NotNull
  private String publicKey;

  @NotNull
  private int size;

  @NotNull
  private AppConstants.Types.Status status;

  @NotNull
  private Long userId;

  @NotNull
  private String keyUUID;
}
