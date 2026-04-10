package blood.bridging.donating.Request;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Auth.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloodRequestService {
    private final BloodRequestRepo requestRepository;
    private final UserRepo userRepo;

    public BloodRequestService(BloodRequestRepo requestRepository, UserRepo userRepo) {
        this.requestRepository = requestRepository;
        this.userRepo = userRepo;
    }

    public BloodRequest createRequest(BloodRequestAddDTO dto) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user=userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        BloodRequest request = new BloodRequest();
        request.setBloodType(dto.getBloodType());
        request.setHospital(dto.getHospital());
        request.setStatus("PENDING");            // default status
        request.setRequestDate(LocalDateTime.now());
        request.setUser(user);
        return requestRepository.save(request);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<BloodRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<BloodRequest> getMyRequests() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user=userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return requestRepository.findByUser(user);

    }

    public List<BloodRequest> getRequestsByStatus(String status) {
        return requestRepository.findByStatus(status);
    }
}
