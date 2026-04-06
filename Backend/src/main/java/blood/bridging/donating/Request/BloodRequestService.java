package blood.bridging.donating.Request;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloodRequestService {
    private final BloodRequestRepo requestRepository;
    public BloodRequestService(BloodRequestRepo requestRepository) {
        this.requestRepository = requestRepository;
    }

    public BloodRequest createRequest(BloodRequestAddDTO dto) {
        BloodRequest request = new BloodRequest();
        request.setBloodType(dto.getBloodType());
        request.setHospital(dto.getHospital());
        request.setStatus("PENDING");            // default status
        request.setRequestDate(LocalDateTime.now());

        return requestRepository.save(request);
    }

    public List<BloodRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<BloodRequest> getRequestsByStatus(String status) {
        return requestRepository.findByStatus(status);
    }
}
