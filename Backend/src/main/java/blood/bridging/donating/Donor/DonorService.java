package blood.bridging.donating.Donor;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonorService {

    private final DonorRepo donorRepository;

    public DonorService(DonorRepo donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor addDonor(DonorAddRequest donor) {
        Donor don=new Donor();
        don.setName(donor.getName());
        don.setBloodType(donor.getBloodType());
        don.setAvailable(donor.isAvailable());
        don.setLocation(donor.getLocation());
        return donorRepository.save(don);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public List<Donor> getAvailableDonors() {
        return donorRepository.findByAvailableTrue();
    }

    public List<Donor> getByBloodType(String bloodType) {
        return donorRepository.findByBloodType(bloodType);
    }
}