package blood.bridging.donating.Donor;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Auth.UserRepo;
import blood.bridging.donating.Utils.BloodTypeParser;
import blood.bridging.donating.Utils.Enum.BloodType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DonorService {

    private final DonorRepo donorRepository;
    private final UserRepo userRepo;

    public DonorService(DonorRepo donorRepository, UserRepo userRepo) {
        this.donorRepository = donorRepository;
        this.userRepo = userRepo;
    }

    @Transactional
    public Donor addDonor(DonorAddRequest donor) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (donor.getName() != null && !donor.getName().isBlank()) {
            user.setName(donor.getName().trim());
            userRepo.save(user);
        }

        BloodType bloodType = BloodTypeParser.parse(donor.getBloodType());

        Donor entity = donorRepository.findByUser(user).orElse(new Donor());
        entity.setUser(user);
        entity.setBloodType(bloodType);
        entity.setAvailable(donor.isAvailable());
        entity.setLocation(donor.getLocation() != null ? donor.getLocation().trim() : null);
        return donorRepository.save(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public List<Donor> getMyDonorProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return donorRepository.findByUser(user).map(List::of).orElse(List.of());
    }

    public List<Donor> getAvailableDonors() {
        return donorRepository.findByAvailableTrue();
    }

    public List<Donor> getByBloodType(String bloodType) {
        return donorRepository.findByBloodType(BloodTypeParser.parse(bloodType));
    }

    public List<Donor> filterDonors(String bloodTypeRaw, String location, Boolean available) {
        BloodType bloodType = BloodTypeParser.parse(bloodTypeRaw);
        boolean avail = available == null || available;
        if (location == null || location.isBlank()) {
            return donorRepository.findByBloodTypeAndAvailable(bloodType, avail);
        }
        return donorRepository.findByBloodTypeAndLocationIgnoreCaseTrimmedAndAvailable(
                bloodType, location, avail);
    }
}
