package blood.bridging.donating.Notification;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationEntity> listMine() {
        return notificationService.myNotifications();
    }

    @PatchMapping("/{id}/read")
    public NotificationEntity markRead(@PathVariable Long id) {
        return notificationService.markRead(id);
    }
}
