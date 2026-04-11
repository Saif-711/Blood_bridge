package blood.bridging.donating.Admin;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Request.BloodRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<User> users() {
        return adminService.listUsers();
    }

    @PatchMapping("/requests/{id}/status")
    public BloodRequest updateRequestStatus(
            @PathVariable Long id,
            @RequestBody RequestStatusUpdateDto body) {
        if (body.getStatus() == null) {
            throw new IllegalArgumentException("status is required");
        }
        return adminService.updateRequestStatus(id, body.getStatus());
    }
}
