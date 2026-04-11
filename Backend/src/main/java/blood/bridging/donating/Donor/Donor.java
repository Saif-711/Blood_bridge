package blood.bridging.donating.Donor;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Utils.Enum.BloodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donors")
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    private String location;    // City or hospital
    private boolean available;  // Available for donation
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    private LocalDate lastDonationDate;
}