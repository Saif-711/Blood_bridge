package blood.bridging.donating.Donation;

import blood.bridging.donating.Auth.User;
import blood.bridging.donating.Auth.UserRepo;
import blood.bridging.donating.Donor.Donor;
import blood.bridging.donating.Donor.DonorRepo;
import blood.bridging.donating.Request.BloodRequest;
import blood.bridging.donating.Request.BloodRequestRepo;
import blood.bridging.donating.Utils.DonationEligibility;
import blood.bridging.donating.Utils.Enum.DonationStatus;
import blood.bridging.donating.Utils.Enum.RequestStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DonationService {

    private final DonationRepo donationRepo;
    private final DonorRepo donorRepo;
    private final BloodRequestRepo bloodRequestRepo;
    private final UserRepo userRepo;

    public DonationService(DonationRepo donationRepo, DonorRepo donorRepo,
                           BloodRequestRepo bloodRequestRepo, UserRepo userRepo) {
        this.donationRepo = donationRepo;
        this.donorRepo = donorRepo;
        this.bloodRequestRepo = bloodRequestRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Donation accept(DonationAcceptRequest body) {
        if (body.getRequestId() == null) {
            throw new IllegalArgumentException("requestId is required");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        Donor donor = donorRepo.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Donor profile required"));
        //throw exception if cannot donate
        DonationEligibility.requireCanDonate(donor.getLastDonationDate());

        BloodRequest request = bloodRequestRepo.findById(body.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (request.getStatus() == RequestStatus.CANCELLED || request.getStatus() == RequestStatus.COMPLETED) {
            throw new IllegalStateException("Request is closed (CANCELED,OR COMPLETED) ");
        }

        if (donationRepo.findByDonorAndRequest(donor, request).isPresent()) {
            throw new IllegalStateException("You already matched this request");
        }

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setRequest(request);
        donation.setDonationDate(LocalDate.now());
        donation.setStatus(DonationStatus.MATCHED);
        return donationRepo.save(donation);
    }

    @Transactional
    public Donation complete(Long donationId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        Donation donation = donationRepo.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Donation not found"));

        Donor donor = donation.getDonor();
        if (donor.getUser() == null || !donor.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your donation");
        }

        donation.setStatus(DonationStatus.DONATED);
        donor.setLastDonationDate(LocalDate.now());
        donor.setAvailable(false);

        BloodRequest req = donation.getRequest();
        req.setStatus(RequestStatus.COMPLETED);

        donorRepo.save(donor);
        bloodRequestRepo.save(req);
        return donationRepo.save(donation);
    }
}
