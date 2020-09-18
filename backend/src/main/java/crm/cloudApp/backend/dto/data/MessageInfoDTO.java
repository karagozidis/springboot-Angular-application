package crm.cloudApp.backend.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.dto.common.BaseDTO;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class MessageInfoDTO extends BaseDTO implements Serializable {

  @NotNull
  private String name;

  private String uniqueId;

  @NotNull
  private String size;

  @NotNull
  private String tag;

  @NotNull
  private Instant timeSent;

  @NotNull
  private int encryptionVersion;

  @NotNull
  private Long senderId;

  @NotNull
  private Long userGroupId;

  private String userGroupName;

  private MessageDTO message;

  private String metadata;

}
