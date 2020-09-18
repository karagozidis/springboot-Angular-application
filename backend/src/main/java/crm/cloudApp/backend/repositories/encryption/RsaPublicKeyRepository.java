package crm.cloudApp.backend.repositories.encryption;

import crm.cloudApp.backend.models.encryption.RsaPublicKey;
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
import java.util.Optional;

@Repository
public interface RsaPublicKeyRepository extends BaseRepository<RsaPublicKey> {

  Collection<RsaPublicKey> findAll();

  @Query(" SELECT r " +
      " FROM RsaPublicKey r " +
      " WHERE r.status = 'enabled' " +
      " AND r.userId = :userId ")
  Optional<RsaPublicKey> findActiveRsaPublicKey(@Param("userId") Long userId);


  Page<RsaPublicKey> findAll(Pageable pageable);

  Page<RsaPublicKey> findByUserId(@Param("userId") Long userId, Pageable pageable);

  @Modifying
  @Transactional
  @Query(" UPDATE RsaPublicKey " +
      " SET status = 'disabled' " +
      " WHERE userId = :userId")
  void disableAll(@Param("userId") Long userId);

  @Query(" SELECT r.userId " +
      " FROM RsaPublicKey r " +
      " WHERE r.status = 'enabled' "
  )
  List<Long> findUserIdsWithKey();


}
