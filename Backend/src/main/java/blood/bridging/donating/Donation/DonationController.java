package blood.bridging.donating.Donation;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donations")
@CrossOrigin(origins = "http://localhost:5173")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping("/accept")
    public Donation accept(@RequestBody DonationAcceptRequest body) {
        return donationService.accept(body);
    }

    @PostMapping("/{id}/complete")
    public Donation complete(@PathVariable Long id) {
        return donationService.complete(id);
    }
}
