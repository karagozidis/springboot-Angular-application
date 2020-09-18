package crm.cloudApp.backend.dto.action;

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

import java.time.Instant;

@Getter
@Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class NotificationDTO extends BaseDTO {

  @NotNull
  private String name;

  @NotNull
  private String description;

  @NotNull
  private Instant timeSent;

  //    @NotNull
  //    private boolean viewed;

  @NotNull
  private AppConstants.Types.NotificationStatus status;

  @NotNull
  private AppConstants.Types.NotificationType type;

  @NotNull
  private Long senderId;

  @NotNull
  private Long receiverId;

}
