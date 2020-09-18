package crm.cloudApp.backend.models.data;

import crm.cloudApp.backend.models.common.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class Message extends BaseEntity {

  @Column(columnDefinition = "LONGBLOB")
  private byte[] encryptedMessage;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "message", cascade = CascadeType.ALL)
  private MessageInfo messageInfo;

}
