package crm.cloudApp.backend.controllers.actions;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.action.NotificationDTO;
import crm.cloudApp.backend.services.action.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(path = "/statuses")
    public @ResponseBody
    Collection<AppConstants.Types.NotificationStatus> getAllStatuses() {
        return Arrays.asList(AppConstants.Types.NotificationStatus.class.getEnumConstants());
    }

    @PostMapping
    public NotificationDTO create(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.create(notificationDTO);
    }

    @PutMapping(path = "/status")
    public @ResponseBody
    void updateStatus(@RequestParam("notification-id") Long notificationId,
                      @RequestParam("status") String status) {
        this.notificationService.updateStatus(notificationId, status);
    }

    @PutMapping(path = "/status/all")
    public @ResponseBody
    void updateStatusByReceiverId(@RequestParam("status") String status) {
        this.notificationService.updateStatusByReceiverId(status);
    }

    @DeleteMapping(path = "/delete/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return this.notificationService.delete(id);
    }

    @GetMapping(path = "/sent-by/current")
    public @ResponseBody
    Page<NotificationDTO> bySerderId(Pageable pageable,
                                     @RequestParam("status") String status) {
        return notificationService.getNotificationsBySenderId(pageable, status);
    }

    @GetMapping(path = "/received-by/current")
    public @ResponseBody
    Page<NotificationDTO> byReceiverId(
            Pageable pageable,
            @RequestParam("status") String status) {
        return notificationService.getNotificationsByReceiverId(pageable, status);
    }

    @GetMapping(path = "/notification-id/{id}")
    public NotificationDTO getById(@PathVariable("id") Long id) {
        return this.notificationService.getById(id);
    }


    @GetMapping(path = "/received-by/current/all")
    public @ResponseBody
    List<NotificationDTO> getAllByReceiverId(
            @RequestParam("status") String status) {
        return notificationService.getAllNotificationsByReceiverId(status);
    }

    @GetMapping(path = "/sent-by/current/sent-to/{receiverId}")
    public @ResponseBody
    Page<NotificationDTO> notificationsSentToUser(@PathVariable("receiverId") Long receiverId,
                                                  Pageable pageable,
                                                  @RequestParam("status") String status) {
        return notificationService
                .getNotificationsSentByCurrentUserToReceiver(receiverId, pageable, status);
    }

    @GetMapping(path = "/sent-by/{senderId}/sent-to/current")
    public @ResponseBody
    Page<NotificationDTO> notificationsReceivedByUser(@RequestParam("senderId") Long senderId,
                                                      Pageable pageable,
                                                      @RequestParam("status") String status) {
        return notificationService
                .getNotificationsSentToCurrentUserBySenderId(senderId, pageable, status);
    }
}
