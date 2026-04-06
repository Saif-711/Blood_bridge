package blood.bridging.donating.Request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blood_requests")
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bloodType;       // e.g., A+, O-
    private String hospital;        // Hospital name
    private String status;          // PENDING, APPROVED, COMPLETED
    private LocalDateTime requestDate = LocalDateTime.now();
}
