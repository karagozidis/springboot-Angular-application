package crm.cloudApp.backend.repositories.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

  @Query(" SELECT (count (u)>0) " +
      " FROM User u " +
      " WHERE u.username=:username " +
      " AND u.status NOT LIKE 'deleted'")
  boolean userExists(@Param("username") String username);

  @Modifying
  @Query(" UPDATE User u " +
      " SET u.encryptedPrivateKey =:encryptedPrivateKey " +
      " WHERE u.id =:id ")
  void updateUserMetadata(@Param("encryptedPrivateKey") String encryptedPrivateKey,
      @Param("id") Long id);

  User findUserById(@Param("id") Long id);

  @Query("SELECT u FROM User u " +
      " WHERE u.id IN (:ids)")
  List<User> findAllById(@Param("ids") List<Long> ids);

  @Query(" SELECT u " +
      " FROM User u " +
      " WHERE u.id IN " +
      " ( SELECT c.receiver.id " +
      " FROM Contact c " +
      " WHERE c.receiver.id IN (:ids) " +
      " AND c.sender.id =:id " +
      " AND c.status ='accepted' )")
  List<User> getOnlyContactsOfUserId(@Param("ids") List<Long> ids, @Param("id") Long id);

  Collection<User> findAllByStatusIsNotLike(AppConstants.Types.UserStatus status);

  @Query(" SELECT u " +
      " FROM User u " +
      " WHERE u.status NOT LIKE 'deleted' ")
  Page<User> findAllNotDeleted(Pageable pageable);

  @Query(" SELECT u " +
      " FROM User u " +
      " WHERE u.status " +
      " NOT LIKE 'deleted' " +
      " AND u.id in (:ids) ")
  Collection<User> findByIds(@Param("ids") List<Long> ids);

  @Query(" SELECT DISTINCT u FROM User u " +
      " INNER JOIN u.userGroups ug " +
      " WHERE ug.id = :userGroupId " +
      " AND u.status NOT LIKE 'deleted' ")
  Page<User> findUsersByUserGroupId(@Param("userGroupId") Long userGroupId, Pageable pageable);

  @Query(" SELECT DISTINCT u FROM User u " +
      " INNER JOIN u.userGroups ug " +
      " WHERE ug.id = :userGroupId " +
      " AND u.status NOT LIKE 'deleted' ")
  Collection<User> findUsersByUserGroupId(@Param("userGroupId") Long userGroupId);

  @Query(" SELECT DISTINCT u FROM User u " +
      " WHERE u.id IN (select c.receiver.id FROM Contact c WHERE c.sender.id =:id AND c.status LIKE :contactStatus ) "
      +
      " OR u.id IN (select c.sender.id FROM Contact c WHERE c.receiver.id =:id AND c.status LIKE :contactStatus ) "
      +
      " AND u.id <> :id ")
  List<User> getContactsOfUser(@Param("id") Long id,
      @Param("contactStatus") AppConstants.Types.ContactStatus contactStatus);

  @Query(" SELECT DISTINCT u FROM User u " +
      " WHERE ( u.id IN (SELECT c.receiver.id FROM Contact c WHERE c.sender.id =:id )" +
      " OR u.id IN (SELECT c.sender.id FROM Contact c WHERE c.receiver.id =:id ) )" +
      " AND u.id <> :id ")
  List<User> getContactsOfUser(@Param("id") Long id);

  @Query(" SELECT u " +
          " FROM User u " +
          " WHERE u.status " +
          " NOT LIKE 'deleted' " +
          " AND u.username = :username ")
  Optional<User> findByUsername(@Param("username") String username);
}

