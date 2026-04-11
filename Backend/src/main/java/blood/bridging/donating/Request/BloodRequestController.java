package blood.bridging.donating.Request;

import blood.bridging.donating.Utils.Enum.RequestStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "http://localhost:5173")
public class BloodRequestController {
    private final BloodRequestService requestService;

    public BloodRequestController(BloodRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public BloodRequest createRequest(@RequestBody BloodRequestAddDTO request) {
        return requestService.createRequest(request);
    }

    @GetMapping
    public List<BloodRequest> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/my-requests")
    public List<BloodRequest> getMyRequests() {
        return requestService.getMyRequests();
    }

    @GetMapping("/status/{status}")
    public List<BloodRequest> getRequestsByStatus(@PathVariable RequestStatus status) {
        return requestService.getRequestsByStatus(status);
    }

    @GetMapping("/{id}")
    public BloodRequest getById(@PathVariable Long id) {
        return requestService.getById(id);
    }

    @PostMapping("/{id}/match")
    public BloodRequest triggerMatch(@PathVariable Long id) {
        return requestService.triggerMatch(id);
    }
}
