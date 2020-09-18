package crm.cloudApp.backend.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.dto.common.BaseDTO;
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
public class MessageDTO extends BaseDTO {

  private byte[] encryptedMessage;

}
