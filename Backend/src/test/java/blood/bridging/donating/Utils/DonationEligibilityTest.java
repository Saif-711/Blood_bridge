package blood.bridging.donating.Utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DonationEligibilityTest {

    @Test
    void eligibleWhenNeverDonated() {
        assertTrue(DonationEligibility.canDonate(null));
    }

    @Test
    void notEligibleWithinThreeMonths() {
        LocalDate recent = LocalDate.now().minusMonths(1);
        assertFalse(DonationEligibility.canDonate(recent));
    }

    @Test
    void eligibleAfterThreeMonths() {
        LocalDate old = LocalDate.now().minusMonths(4);
        assertTrue(DonationEligibility.canDonate(old));
    }
}
