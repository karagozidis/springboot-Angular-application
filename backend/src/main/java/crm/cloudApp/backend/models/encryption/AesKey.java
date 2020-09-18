package crm.cloudApp.backend.models.encryption;

import crm.cloudApp.backend.config.AppConstants.Types.Status;
import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class AesKey extends BaseEntity {

  @Column(columnDefinition = "TEXT")
  private String encryptedKey;

  @Column
  private int size;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "user_group_id")
  private Long userGroupId;

  @Column(nullable = false)
  private int encryptionVersion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_group_id", insertable = false, updatable = false)
  private UserGroup userGroup;

  @Column(name = "rsaPublicKey_id")
  private Long rsaPublicKeyId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rsaPublicKey_id", insertable = false, updatable = false)
  private RsaPublicKey rsaPublicKey;

  @Column(name = "user_id")
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;

  @Column
  private String keyUUID;

}
