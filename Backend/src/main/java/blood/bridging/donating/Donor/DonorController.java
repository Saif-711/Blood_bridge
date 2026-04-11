package blood.bridging.donating.Donor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donors")
@CrossOrigin(origins = "http://localhost:5173")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    public Donor addDonor(@RequestBody DonorAddRequest donor) {
        return donorService.addDonor(donor);
    }

    @GetMapping
    public List<Donor> getAllDonors() {
        return donorService.getAllDonors();
    }

    @GetMapping("/my-profile")
    public List<Donor> getMyDonorProfile() {
        return donorService.getMyDonorProfile();
    }

    @GetMapping("/available")
    public List<Donor> getAvailableDonors() {
        return donorService.getAvailableDonors();
    }

    @GetMapping("/filter")
    public List<Donor> filter(
            @RequestParam String bloodType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean available) {
        return donorService.filterDonors(bloodType, location, available);
    }

    @GetMapping("/blood-type/{type}")
    public List<Donor> getByBloodType(@PathVariable String type) {
        return donorService.getByBloodType(type);
    }
}
