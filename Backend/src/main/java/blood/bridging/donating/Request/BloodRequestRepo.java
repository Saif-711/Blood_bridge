package blood.bridging.donating.Request;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Utils.Enum.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepo extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByStatus(RequestStatus status);

    List<BloodRequest> findByUser(User user);
}
