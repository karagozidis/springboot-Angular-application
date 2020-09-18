package crm.cloudApp.backend.repositories.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.users.Contact;
import crm.cloudApp.backend.repositories.common.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContactRepository extends BaseRepository<Contact> {

  @Query(" SELECT c " +
      " FROM Contact c " +
      " JOIN FETCH c.receiver " +
      " WHERE c.sender.id =:id " +
      " OR c.receiver.id =:id ")
  List<Contact> getContactsOfUser(@Param("id") Long id);

  @Query(" SELECT distinct c " +
      " FROM Contact c " +
      " JOIN FETCH  c.receiver " +
      " WHERE ( c.sender.id =:id " +
      " OR c.receiver.id =:id ) " +
      " AND c.status =:contactStatus ")
  List<Contact> getContactsOfUserByStatus(@Param("id") Long id,
      @Param("contactStatus") AppConstants.Types.ContactStatus contactStatus);

  @Modifying
  @Transactional
  @Query(" UPDATE Contact " +
      " SET  status =:status " +
      " WHERE id =:contactId ")
  void updateStatus(@Param("contactId") Long contactId,
      @Param("status") AppConstants.Types.ContactStatus status);

  @Query(
      value = " SELECT c " +
          " FROM Contact c " +
          " JOIN FETCH c.receiver " +
          " WHERE c.sender.id =:id " +
          " OR c.receiver.id =:id ",
      countQuery = " SELECT COUNT(c) " +
          " FROM Contact c " +
          " WHERE c.sender.id =:id " +
          " OR c.receiver.id =:id "
  )
  Page<Contact> getContactsOfUserPage(@Param("id") Long id, Pageable pageable);


  @Query(
      value = " SELECT DISTINCT c " +
          " FROM Contact c " +
          " JOIN FETCH  c.receiver " +
          " WHERE ( c.sender.id =:id " +
          " OR c.receiver.id =:id ) " +
          " AND c.status =:contactStatus ",
      countQuery = " SELECT  COUNT(DISTINCT c) " +
          " FROM Contact c " +
          " WHERE ( c.sender.id =:id " +
          " OR c.receiver.id =:id ) " +
          " AND c.status =:contactStatus "
  )
  Page<Contact> getContactsOfUserByStatusPage(
      @Param("id") Long id,
      @Param("contactStatus") AppConstants.Types.ContactStatus contactStatus,
      Pageable pageable);
}
