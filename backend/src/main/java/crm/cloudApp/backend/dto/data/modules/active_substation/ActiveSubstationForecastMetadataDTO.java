package crm.cloudApp.backend.dto.data.modules.active_substation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class ActiveSubstationForecastMetadataDTO {

    private String uniqueId;
    private String originalFileName;
    private String fileNameByDate;
    private Instant fromDate;
    private Instant toDate;
    private int res;
    private Instant createdOn;
    private String status = "TOCRM";
    private String tag;
    private String description;
    private String type;
}
