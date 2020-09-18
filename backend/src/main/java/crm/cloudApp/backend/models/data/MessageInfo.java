package crm.cloudApp.backend.models.data;

import crm.cloudApp.backend.models.common.BaseEntity;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Accessors(chain = true)
public class MessageInfo extends BaseEntity {

  @Column
  private String name;

  @Column
  private String uniqueId;

  @Column
  private String size;

  @Column
  private String tag;

  @Column
  private Instant timeSent;

  @Column(name = "user_group_id")
  private Long userGroupId;

  @Column(nullable = false)
  private int encryptionVersion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_group_id", insertable = false, updatable = false)
  private UserGroup userGroup;

  @Column(name = "sender_id")
  private Long senderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", insertable = false, updatable = false)
  private User sender;

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "message")
  private Message message;

  @Column
  private String metadata;

}
