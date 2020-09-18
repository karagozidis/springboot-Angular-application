package crm.cloudApp.backend.repositories.actions;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.actions.Notification;
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
public interface NotificationRepository extends BaseRepository<Notification> {

  Collection<Notification> findAll();

  @Query("select n from Notification n where n.senderId = :senderId order by n.createdOn desc ")
  Page<Notification> getNotificationsBySenderId(@Param("senderId") Long senderId,
      Pageable pageable);

  @Query("select n from Notification n where n.receiverId = :receiverId order by n.createdOn desc ")
  Page<Notification> getNotificationsByReceiverId(@Param("receiverId") Long receiverId,
      Pageable pageable);

  @Query("select n from Notification n where n.senderId = :senderId and n.receiverId = :receiverId order by n.createdOn desc ")
  Page<Notification> getNotificationsBySenderAndReceiverId(@Param("senderId") Long senderId,
      @Param("receiverId") Long receiverId,
      Pageable pageable);

  @Query("select n from Notification n where n.receiverId = :receiverId order by n.createdOn desc ")
  List<Notification> getAllNotificationsByReceiverId(@Param("receiverId") Long receiverId);

  @Query("select n from Notification n where n.senderId = :senderId and n.receiverId = :receiverId order by n.createdOn desc ")
  List<Notification> getAllNotificationsBySenderAndReceiverId(@Param("senderId") Long senderId,
      @Param("receiverId") Long receiverId);


  @Query("select n from Notification n where n.senderId = :senderId and n.status = :status order by n.createdOn desc ")
  Page<Notification> getNotificationsBySenderId(@Param("senderId") Long senderId,
      Pageable pageable,
      @Param("status") AppConstants.Types.NotificationStatus status);

  @Query("select n from Notification n where n.receiverId = :receiverId and n.status = :status order by n.createdOn desc ")
  Page<Notification> getNotificationsByReceiverId(@Param("receiverId") Long receiverId,
      Pageable pageable,
      @Param("status") AppConstants.Types.NotificationStatus status);


  @Query("select n from Notification n where n.receiverId = :receiverId and n.status = :status order by n.createdOn desc ")
  List<Notification> getAllNotificationsByReceiverId(@Param("receiverId") Long receiverId,
      @Param("status") AppConstants.Types.NotificationStatus status);

  @Query("select n from Notification n where n.senderId = :senderId and n.receiverId = :receiverId and n.status = :status order by n.createdOn desc ")
  Page<Notification> getNotificationsBySenderAndReceiverId(@Param("senderId") Long senderId,
      @Param("receiverId") Long receiverId,
      Pageable pageable,
      @Param("status") AppConstants.Types.NotificationStatus status);

  @Modifying
  @Transactional
  @Query("update Notification set status =:status where id =:id")
  void updateStatusOfNotification(@Param("id") Long id,
      @Param("status") AppConstants.Types.NotificationStatus status);

  @Modifying
  @Transactional
  @Query("update Notification set status =:status where receiverId =:receiverId")
  void updateStatusOfNotificationsByReceiverId(@Param("receiverId") Long receiverId,
      @Param("status") AppConstants.Types.NotificationStatus status);
}
