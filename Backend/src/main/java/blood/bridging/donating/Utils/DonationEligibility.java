package blood.bridging.donating.Utils;

import java.time.LocalDate;

public final class DonationEligibility {

    private static final int MIN_MONTHS_BETWEEN_DONATIONS = 3;

    private DonationEligibility() {
    }

    public static boolean canDonate(LocalDate lastDonationDate) {
        if (lastDonationDate == null) {
            return true;
        }
        LocalDate eligibleFrom = lastDonationDate.plusMonths(MIN_MONTHS_BETWEEN_DONATIONS);
        return !LocalDate.now().isBefore(eligibleFrom);
    }

    public static void requireCanDonate(LocalDate lastDonationDate) {
        if (!canDonate(lastDonationDate)) {
            throw new IllegalStateException(
                    "Donor is not eligible to donate until " + lastDonationDate.plusMonths(MIN_MONTHS_BETWEEN_DONATIONS));
        }
    }
}
