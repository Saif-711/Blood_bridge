package blood.bridging.donating.Donor;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Utils.Enum.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DonorRepo extends JpaRepository<Donor, Long> {
    List<Donor> findByBloodType(BloodType bloodType);

    List<Donor> findByBloodTypeAndAvailable(BloodType bloodType, boolean available);

    List<Donor> findByAvailableTrue();

    Optional<Donor> findByUser(User user);

    @Query("""
            SELECT d FROM Donor d
            WHERE d.available = :available
            AND d.bloodType = :bloodType
            AND LOWER(TRIM(d.location)) = LOWER(TRIM(:location))
            """)
    List<Donor> findByBloodTypeAndLocationIgnoreCaseTrimmedAndAvailable(
            @Param("bloodType") BloodType bloodType,
            @Param("location") String location,
            @Param("available") boolean available
    );
}
