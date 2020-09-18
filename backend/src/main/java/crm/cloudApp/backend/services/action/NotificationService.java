package crm.cloudApp.backend.services.action;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.action.NotificationDTO;
import crm.cloudApp.backend.mappers.actions.NotificationMapper;
import crm.cloudApp.backend.models.actions.Notification;
import crm.cloudApp.backend.repositories.actions.NotificationRepository;
import crm.cloudApp.backend.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final UserService userService;

  @Autowired
  public NotificationService(NotificationRepository notificationRepository, UserService userService,
      NotificationMapper notificationMapper) {
    this.notificationRepository = notificationRepository;
    this.userService = userService;
    this.notificationMapper = notificationMapper;
  }

  public Collection<NotificationDTO> getAll() {
    Collection<Notification> notifications = notificationRepository.findAll();
    return notificationMapper.mapDTOs(notifications);
  }

  public Page<NotificationDTO> getAll(Pageable pageable) {
    Page<Notification> notifications = notificationRepository.findAll(pageable);
    return notificationMapper.map(notifications);
  }

  public NotificationDTO getNotification(Long id) {
    Optional<Notification> notificationOptional = notificationRepository.findById(id);
    if (notificationOptional.isPresent()) {
      return notificationMapper.map(notificationOptional.get());
    } else {
      return null;
    }
  }

  public NotificationDTO create(NotificationDTO notificationDTO) {
    Notification notification = notificationMapper.map(notificationDTO);

    notification.setCreatedBy(userService.getLoggedInUserName());
    notification.setCreatedOn(Instant.now());
    notification.setModifiedBy(userService.getLoggedInUserName());
    notification.setModifiedOn(Instant.now());

    if (notification.getSenderId() == null) {
      notification.setSenderId(userService.getLoggedInUser().getId());
    }
    if (notification.getReceiverId() == null) {
      notification.setReceiverId(userService.getLoggedInUser().getId());
    }

    Notification createdNotification = notificationRepository.save(notification);
    return notificationMapper.map(createdNotification);
  }

  public NotificationDTO update(NotificationDTO notificationDTO) {

    Optional<Notification> notificationOptional = notificationRepository
        .findById(notificationDTO.getId());
    if (notificationOptional.isPresent()) {
      Notification notification = notificationOptional.get();
      notification.setName(notificationDTO.getName());
      notification.setDescription(notificationDTO.getDescription());
      notification.setTimeSent(notificationDTO.getTimeSent());
      notification.setStatus(notificationDTO.getStatus());
      // notification.setSenderId(notificationDTO.getSenderId());
      notification.setReceiverId(notificationDTO.getReceiverId());
      notification.setModifiedBy(userService.getLoggedInUserName());
      notification.setModifiedOn(Instant.now());
      Notification savedNotification = notificationRepository.save(notification);
      return notificationMapper.map(savedNotification);
    } else {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "No MessageInfo with this id!!");
    }

  }

  public boolean delete(Long id) {
    try {

      Optional<Notification> notificationOptional = notificationRepository.findById(id);
      if (!notificationOptional.isPresent()) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "No notification with this id!!");
      } else {
        notificationRepository.deleteById(id);
      }

      return true;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
    }
  }

  public Page<NotificationDTO> getNotificationsBySenderId(Pageable pageable, String status) {
    Page<Notification> notifications;
    if (status.equals("") || status.equals("all")) {
      notifications = notificationRepository
          .getNotificationsBySenderId(userService.getLoggedInUser().getId(), pageable);
    } else {
      notifications = notificationRepository
          .getNotificationsBySenderId(userService.getLoggedInUser().getId(), pageable,
              AppConstants.Types.NotificationStatus.valueOf(status));
    }
    return notificationMapper.map(notifications);
  }

  public List<NotificationDTO> getAllNotificationsByReceiverId(String status) {
    List<Notification> notifications;
    if (status.equals("") || status.equals("all")) {
      notifications = notificationRepository
          .getAllNotificationsByReceiverId(userService.getLoggedInUser().getId());
    } else {
      notifications = notificationRepository
          .getAllNotificationsByReceiverId(userService.getLoggedInUser().getId(),
              AppConstants.Types.NotificationStatus.valueOf(status));
    }
    return notificationMapper.map(notifications);
  }

  public Page<NotificationDTO> getNotificationsByReceiverId(Pageable pageable, String status) {
    Page<Notification> notifications;
    if (status.equals("") || status.equals("all")) {
      notifications = notificationRepository
          .getNotificationsByReceiverId(userService.getLoggedInUser().getId(), pageable);
    } else {
      notifications = notificationRepository
          .getNotificationsByReceiverId(userService.getLoggedInUser().getId(), pageable,
              AppConstants.Types.NotificationStatus.valueOf(status));
    }
    return notificationMapper.map(notifications);
  }

  public Page<NotificationDTO> getNotificationsSentToCurrentUserBySenderId(Long senderId,
      Pageable pageable, String status) {
    Page<Notification> notifications;
    if (status.equals("") || status.equals("all")) {
      notifications = notificationRepository
          .getNotificationsBySenderAndReceiverId(senderId, userService.getLoggedInUser().getId(),
              pageable);
    } else {
      notifications = notificationRepository
          .getNotificationsBySenderAndReceiverId(senderId, userService.getLoggedInUser().getId(),
              pageable, AppConstants.Types.NotificationStatus.valueOf(status));
    }
    return notificationMapper.map(notifications);
  }

  public Page<NotificationDTO> getNotificationsSentByCurrentUserToReceiver(Long receiverId,
      Pageable pageable, String status) {
    Page<Notification> notifications;
    if (status.equals("") || status.equals("all")) {
      notifications = notificationRepository
          .getNotificationsBySenderAndReceiverId(userService.getLoggedInUser().getId(), receiverId,
              pageable);
    } else {
      notifications = notificationRepository
          .getNotificationsBySenderAndReceiverId(userService.getLoggedInUser().getId(), receiverId,
              pageable, AppConstants.Types.NotificationStatus.valueOf(status));
    }
    return notificationMapper.map(notifications);
  }

  @Transactional
  public void updateStatus(Long notificationId, String status) {
    notificationRepository.updateStatusOfNotification(notificationId,
        AppConstants.Types.NotificationStatus.valueOf(status));
  }

  @Transactional
  public void updateStatusByReceiverId(String status) {
    notificationRepository
        .updateStatusOfNotificationsByReceiverId(userService.getLoggedInUser().getId(),
            AppConstants.Types.NotificationStatus.valueOf(status));
  }

  public NotificationDTO getById(Long id) {
    Optional<Notification> notificationOptional = notificationRepository.findById(id);
    if (!notificationOptional.isPresent()) {
      return null;
    }
    return notificationMapper.map(notificationOptional.get());
  }
}
