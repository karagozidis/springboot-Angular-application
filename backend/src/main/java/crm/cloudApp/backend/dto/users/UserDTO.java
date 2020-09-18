package crm.cloudApp.backend.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.common.BaseDTO;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Builder
public class UserDTO extends BaseDTO implements Serializable {

  @NotNull
  private String username;

  @NotNull
  private String email;

  @NotNull
  private String password;

  @NotNull
  private AppConstants.Types.UserStatus status;

  private CountryDTO country;

  private String rolesCrm;

  private String modulesCrm;

  private String rolesMarket;

  private String modulesMarket;

  private String defaultMarket;

  private String market;

  private String landingPage;

  private Long activeSubstationUserGroupId;

}
