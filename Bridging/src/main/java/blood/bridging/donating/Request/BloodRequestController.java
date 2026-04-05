package blood.bridging.donating.Request;

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

    @GetMapping("/status/{status}")
    public List<BloodRequest> getRequestsByStatus(@PathVariable String status) {
        return requestService.getRequestsByStatus(status);
    }
}
