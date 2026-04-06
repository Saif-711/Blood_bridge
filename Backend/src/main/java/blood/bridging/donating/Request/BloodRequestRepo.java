package blood.bridging.donating.Request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepo extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByStatus(String status);
}
