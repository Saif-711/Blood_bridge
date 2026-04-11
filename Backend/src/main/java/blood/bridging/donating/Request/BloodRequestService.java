package blood.bridging.donating.Request;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Auth.UserRepo;
import blood.bridging.donating.Service.MatchingService;
import blood.bridging.donating.Utils.BloodTypeParser;
import blood.bridging.donating.Utils.Enum.RequestStatus;
import blood.bridging.donating.Utils.Enum.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloodRequestService {
    private final BloodRequestRepo requestRepository;
    private final UserRepo userRepo;
    private final MatchingService matchingService;

    public BloodRequestService(BloodRequestRepo requestRepository, UserRepo userRepo,
                               MatchingService matchingService) {
        this.requestRepository = requestRepository;
        this.userRepo = userRepo;
        this.matchingService = matchingService;
    }

    @Transactional
    public BloodRequest createRequest(BloodRequestAddDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        BloodRequest request = new BloodRequest();
        request.setBloodType(BloodTypeParser.parse(dto.getBloodType()));
        request.setHospitalName(dto.getHospitalName());
        request.setLocation(dto.getLocation() != null ? dto.getLocation().trim() : null);
        request.setQuantity(dto.getQuantity());
        request.setStatus(RequestStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());
        request.setUser(user);

        BloodRequest saved = requestRepository.save(request);
        matchingService.notifyMatchingDonors(saved);
        return saved;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<BloodRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<BloodRequest> getMyRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return requestRepository.findByUser(user);
    }

    public List<BloodRequest> getRequestsByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    public BloodRequest getById(Long id) {
        BloodRequest r = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepo.findByEmail(email).orElseThrow();
        boolean admin = current.getRole() == Role.ADMIN;
        if (!admin && (r.getUser() == null || !r.getUser().getId().equals(current.getId()))) {
            throw new AccessDeniedException("Not allowed");
        }
        return r;
    }

    @Transactional
    public BloodRequest triggerMatch(Long id) {
        BloodRequest r = getById(id);
        matchingService.notifyMatchingDonors(r);
        return r;
    }
}
