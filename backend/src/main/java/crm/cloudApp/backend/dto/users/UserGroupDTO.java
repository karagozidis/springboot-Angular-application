package crm.cloudApp.backend.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.dto.common.BaseDTO;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class UserGroupDTO extends BaseDTO {

  @NotNull
  private String name;

  @NotNull
  private String description;

  @NotNull
  private int encryptionVersion;

  @NotNull
  private List<UserDTO> users;

  public Boolean hasEnabledPublicKey;
}

