package blood.bridging.donating.Donor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DonorRepo extends JpaRepository<Donor, Long> {
    List<Donor> findByBloodType(String bloodType);
    List<Donor> findByAvailableTrue();
}
