package crm.cloudApp.backend.models.users;

import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.models.encryption.RsaPublicKey;
import crm.cloudApp.backend.config.AppConstants.Types.UserStatus;
import crm.cloudApp.backend.models.data.MessageInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
public class User extends BaseEntity {

  @Column(updatable = false, nullable = false)
  private String username;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  public List<RsaPublicKey> rsaPublicKeys;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
  private List<UserGroup> userGroups;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
  private List<MessageInfo> messageInfo;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "country_id")
  private Country country;

  @Column(nullable = false)
  private String rolesCrm;

  @Column(nullable = false)
  private String modulesCrm;

  @Column(nullable = false)
  private String rolesMarket;

  @Column(nullable = false)
  private String modulesMarket;

  @Column(nullable = true, columnDefinition = "TEXT")
  private String encryptedPrivateKey;

  @Column
  private String defaultMarket;

  @Column
  private String market;

  @Column
  private String landingPage;

  @Column
  private Long activeSubstationUserGroupId;

}
