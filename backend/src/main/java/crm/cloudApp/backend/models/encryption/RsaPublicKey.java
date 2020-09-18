package crm.cloudApp.backend.models.encryption;

import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import crm.cloudApp.backend.config.AppConstants.Types.*;

import javax.persistence.*;

import java.util.List;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class RsaPublicKey extends BaseEntity {

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(updatable = false, nullable = false, columnDefinition = "TEXT")
  private String publicKey;

  @Column
  private int size;

  @Column(name = "user_id")
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "rsaPublicKey")
  private List<AesKey> aesKey;

  @Column
  private String keyUUID;
}
