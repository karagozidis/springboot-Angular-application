package crm.cloudApp.backend.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class BaseDTO implements Serializable {

  private Long id;

  @JsonProperty(access = Access.READ_ONLY)
  private Instant createdOn;

  @JsonProperty(access = Access.READ_ONLY)
  private String createdBy;
}
