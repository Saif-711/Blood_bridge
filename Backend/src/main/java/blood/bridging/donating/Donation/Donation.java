package blood.bridging.donating.Donation;

import blood.bridging.donating.Donor.Donor;
import blood.bridging.donating.Request.BloodRequest;
import blood.bridging.donating.Utils.Enum.DonationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private Donor donor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private BloodRequest request;

    private LocalDate donationDate;

    @Enumerated(EnumType.STRING)
    private DonationStatus status;
}
