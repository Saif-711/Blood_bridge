package blood.bridging.donating.Donor;


import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Auth.UserRepo;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonorService {

    private final DonorRepo donorRepository;
    private final UserRepo userRepo;

    public DonorService(DonorRepo donorRepository, UserRepo userRepo) {
        this.donorRepository = donorRepository;
        this.userRepo = userRepo;
    }

    public Donor addDonor(DonorAddRequest donor) {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user=userRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        Donor don=new Donor();
        don.setName(donor.getName());
        don.setBloodType(donor.getBloodType());
        don.setAvailable(donor.isAvailable());
        don.setLocation(donor.getLocation());
        don.setUser(user);
        return donorRepository.save(don);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public List<Donor>getMyDonors() {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user=userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return donorRepository.findByUser(user);
    }

    public List<Donor> getAvailableDonors() {
        return donorRepository.findByAvailableTrue();
    }

    public List<Donor> getByBloodType(String bloodType) {
        return donorRepository.findByBloodType(bloodType);
    }
}