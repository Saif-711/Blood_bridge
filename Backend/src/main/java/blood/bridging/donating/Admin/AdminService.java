package blood.bridging.donating.Admin;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Auth.UserRepo;
import blood.bridging.donating.Request.BloodRequest;
import blood.bridging.donating.Request.BloodRequestRepo;
import blood.bridging.donating.Utils.Enum.RequestStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final UserRepo userRepo;
    private final BloodRequestRepo bloodRequestRepo;

    public AdminService(UserRepo userRepo, BloodRequestRepo bloodRequestRepo) {
        this.userRepo = userRepo;
        this.bloodRequestRepo = bloodRequestRepo;
    }

    public List<User> listUsers() {
        return userRepo.findAll();
    }

    @Transactional
    public BloodRequest updateRequestStatus(Long requestId, RequestStatus status) {
        BloodRequest r = bloodRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        r.setStatus(status);
        return bloodRequestRepo.save(r);
    }
}
