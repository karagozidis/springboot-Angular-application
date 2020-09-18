package crm.cloudApp.backend.repositories.encryption;

import crm.cloudApp.backend.models.encryption.AesKey;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface AesKeyRepository extends BaseRepository<AesKey> {

  Collection<AesKey> findAll();

  Page<AesKey> findAll(Pageable pageable);

  Page<AesKey> findAesKeysByUserGroupId(@Param("userGroupId") Long userGroupId, Pageable pageable);

  Page<AesKey> findAesKeysByRsaPublicKeyId(@Param("rsaPublicKeyId") Long rsaPublicKeyId,
      Pageable pageable);

  @Query("SELECT DISTINCT a FROM AesKey a " +
      " INNER JOIN a.rsaPublicKey r" +
      " WHERE r.userId =:userId")
  Page<AesKey> findAesKeysByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query(" SELECT DISTINCT a FROM AesKey a " +
      " INNER JOIN a.rsaPublicKey r " +
      " WHERE r.userId =:userId " +
      " AND a.userGroupId =:userGroupId")
  Page<AesKey> findAesKeysByUserIdAndUserGroupId(@Param("userId") Long userId,
      @Param("userGroupId") Long userGroupId, Pageable pageable);

  @Modifying
  @Transactional
  @Query(" UPDATE AesKey set status ='disabled' " +
      " WHERE userGroupId = :userGroupId " +
      " AND userId = :userId"
  )
  void deactivateByUserAndUserGroup(@Param("userId") Long userId,
      @Param("userGroupId") Long userGroupId);

  @Query("SELECT a FROM AesKey a " +
      " INNER JOIN a.userGroup g" +
      " WHERE a.userId =:userId" +
      " AND g.id =:userGroupId" +
      " AND a.status = 'enabled' " +
      " AND a.encryptionVersion = g.encryptionVersion"
  )
  AesKey getActiveKey(Long userId, Long userGroupId);

  @Query("SELECT a FROM AesKey a " +
      " INNER JOIN a.userGroup g" +
      " WHERE a.userId =:userId" +
      " AND g.id =:userGroupId" +
      " AND a.encryptionVersion > :encryptionVersion " +
      " AND a.encryptionVersion = g.encryptionVersion"
  )
  List<AesKey> getKeysOverEncryptionVersion(@Param("userId") Long userId,
      @Param("userGroupId") Long userGroupId,
      @Param("encryptionVersion") int encryptionVersion);

  @Query("SELECT a FROM AesKey a " +
      " INNER JOIN a.userGroup g" +
      " WHERE g.id =:userGroupId" +
      " AND a.status = 'enabled' " +
      " AND a.encryptionVersion = g.encryptionVersion")
  Collection<AesKey> findActiveAesKeysByUserGroupId(@Param("userGroupId") Long userGroupId);
}
