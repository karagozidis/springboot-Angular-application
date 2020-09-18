package crm.cloudApp.backend.models.users;

import crm.cloudApp.backend.models.encryption.AesKey;
import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.models.data.MessageInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
@Builder
public class UserGroup extends BaseEntity implements Serializable {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private int encryptionVersion;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_to_user_group",
      joinColumns = @JoinColumn(name = "user_group_id"),
      inverseJoinColumns = {
          @JoinColumn(name = "user_id", referencedColumnName = "id")
      }
  )
  private List<User> users;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "userGroup")
  private List<AesKey> aesKeys;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "userGroup")
  private List<MessageInfo> messageInfo;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private AppConstants.Types.UserGroupStatus status;

  public void increateByOneEncryptionVersion() {
    encryptionVersion += 1;
  }


}
