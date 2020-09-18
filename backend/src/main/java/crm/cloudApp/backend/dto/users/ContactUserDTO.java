package crm.cloudApp.backend.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.common.BaseDTO;
import javax.validation.constraints.NotNull;
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
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ContactUserDTO extends BaseDTO {

  @NotNull
  private AppConstants.Types.ContactStatus contactStatus;

  @NotNull
  private String contactType;

  @NotNull
  private Long userid;

  @NotNull
  private String username;

  @NotNull
  private String email;

  @NotNull
  private AppConstants.Types.UserStatus userStatus;

}
