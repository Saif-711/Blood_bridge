package blood.bridging.donating.Service;

import blood.bridging.donating.Donor.Donor;
import blood.bridging.donating.Donor.DonorRepo;
import blood.bridging.donating.Notification.NotificationService;
import blood.bridging.donating.Request.BloodRequest;
import blood.bridging.donating.Utils.DonationEligibility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingService {

    private final DonorRepo donorRepo;
    private final NotificationService notificationService;

    public MatchingService(DonorRepo donorRepo, NotificationService notificationService) {
        this.donorRepo = donorRepo;
        this.notificationService = notificationService;
    }

    public void notifyMatchingDonors(BloodRequest request) {
        if (request.getBloodType() == null) {
            return;
        }

        List<Donor> donors;
        if (request.getLocation() == null || request.getLocation().isBlank()) {
            donors = donorRepo.findByBloodTypeAndAvailable(request.getBloodType(), true);
        } else {
            donors = donorRepo.findByBloodTypeAndLocationIgnoreCaseTrimmedAndAvailable(
                    request.getBloodType(), request.getLocation(), true);
        }

        String msg = "New blood request #%d needs %s at %s. Open the app to respond."
                .formatted(
                        request.getId(),
                        request.getBloodType().name(),
                        request.getHospitalName() != null ? request.getHospitalName() : "hospital TBD");

        for (Donor d : donors) {
            if (d.getUser() == null) {
                continue;
            }
            if (!DonationEligibility.canDonate(d.getLastDonationDate())) {
                continue;
            }
            notificationService.notifyUser(d.getUser(), msg);
        }
    }
}
