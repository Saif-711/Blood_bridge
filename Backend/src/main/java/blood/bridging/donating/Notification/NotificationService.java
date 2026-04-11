package blood.bridging.donating.Notification;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Auth.UserRepo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final UserRepo userRepo;

    public NotificationService(NotificationRepo notificationRepo, UserRepo userRepo) {
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void notifyUser(User user, String message) {
        NotificationEntity n = new NotificationEntity();
        n.setUser(user);
        n.setMessage(message);
        n.setRead(false);
        notificationRepo.save(n);
    }

    public List<NotificationEntity> myNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        return notificationRepo.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public NotificationEntity markRead(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        NotificationEntity n = notificationRepo.findById(id).orElseThrow();
        if (!n.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your notification");
        }
        n.setRead(true);
        return notificationRepo.save(n);
    }
}
