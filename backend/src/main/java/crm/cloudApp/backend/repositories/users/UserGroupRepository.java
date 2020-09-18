package crm.cloudApp.backend.repositories.users;

import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends BaseRepository<UserGroup> {

  @Query(" SELECT DISTINCT ug " +
      " FROM UserGroup ug " +
      " LEFT JOIN FETCH ug.users " +
      " WHERE ug.id=:id " +
      " AND ug.status NOT LIKE 'deleted' ")
  Optional<UserGroup> findById(@Param("id") Long id);

  @Query("SELECT DISTINCT ug " +
      "FROM UserGroup ug " +
      "JOIN FETCH ug.users AS u " +
      "WHERE ug.status NOT LIKE 'deleted'"
  )
  Collection<UserGroup> findAll();

  @Query(
      value = " SELECT DISTINCT u " +
          " FROM UserGroup u " +
          " JOIN FETCH u.users " +
          " WHERE u.status NOT LIKE 'deleted' ",
      countQuery = " SELECT COUNT(u) " +
          " FROM UserGroup u " +
          " WHERE u.status NOT LIKE 'deleted'"
  )
  Page<UserGroup> findAll(Pageable pageable);

  @Query(" SELECT DISTINCT ug " +
      " FROM UserGroup ug " +
      " LEFT JOIN FETCH ug.users u " +
      " WHERE ug.id IN (SELECT ug1.id FROM UserGroup ug1 LEFT JOIN ug1.users u1 WHERE u1.id =:id ) "+
      " AND ug.status NOT LIKE 'deleted' ")
  Collection<UserGroup> findUserGroupsByUserId(@Param("id") Long id);

  @Query(
      value = " SELECT ug " +
      " FROM UserGroup ug " +
      " LEFT JOIN FETCH ug.users u " +
      " WHERE ug.id IN (SELECT ug1.id FROM UserGroup ug1 LEFT JOIN ug1.users u1 WHERE u1.id =:id ) " +
      " AND ug.status NOT LIKE 'deleted' ",
      countQuery = " SELECT count(ug) " +
          " FROM UserGroup ug " +
          " WHERE ug.id IN (SELECT ug1.id FROM UserGroup ug1 LEFT JOIN ug1.users u1 WHERE u1.id =:id ) " +
          " AND ug.status NOT LIKE 'deleted' "
  )
  Page<UserGroup> findUserGroupsByUserIdPage(Pageable pageable,
      @Param("id") Long id
      );


  @Query(" SELECT DISTINCT ug " +
      " FROM UserGroup ug " +
      " JOIN FETCH ug.users u " +
      " WHERE u.id=:id " +
      " AND ug.status NOT LIKE 'deleted' " +
      " AND ug.name = 'me' ")
  UserGroup findHandShakesUserGroupForUser(@Param("id") Long id);

  @Query(" SELECT DISTINCT u.id " +
      " FROM UserGroup ug " +
      " JOIN ug.users u " +
      " WHERE ug.name = 'me' " +
      " AND ug.status NOT LIKE 'deleted' ")
  List<Long> findUserIdsWithPersonalUserGroup();

}
