package blood.bridging.donating.Donor;

import blood.bridging.donating.Auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donors")
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // Donor name
    private String bloodType;   // e.g., A+, O-
    private String location;    // City or hospital
    private boolean available;  // Available for donation
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}