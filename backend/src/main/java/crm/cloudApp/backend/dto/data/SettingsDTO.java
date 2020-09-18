package crm.cloudApp.backend.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import crm.cloudApp.backend.dto.common.BaseDTO;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class SettingsDTO extends BaseDTO {

    private String tag;

    private String name;

    private String value;

}
