package blood.bridging.donating.Donation;

import blood.bridging.donating.Donor.Donor;
import blood.bridging.donating.Request.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonationRepo extends JpaRepository<Donation, Long> {
    Optional<Donation> findByDonorAndRequest(Donor donor, BloodRequest request);
}
