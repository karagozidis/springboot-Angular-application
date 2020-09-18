package crm.cloudApp.backend.dto.data.modules.hyppoio;

import crm.cloudApp.backend.config.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class HyppoIoMetaDataDTO {

  private String uuid;

  private String name;

  private String description;

  private AppConstants.Types.ExecStatus status;

  private Instant createdOn;

  private String userGroupName;

  private String senderUsername;

  private String location = "CRMCENTRAL";

  private long size;

  private long usergroup;
}
