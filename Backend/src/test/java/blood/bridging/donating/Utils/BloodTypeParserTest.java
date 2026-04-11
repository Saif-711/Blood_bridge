package blood.bridging.donating.Utils;

import blood.bridging.donating.Utils.Enum.BloodType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BloodTypeParserTest {

    @Test
    void parsesShortForm() {
        assertEquals(BloodType.O_NEGATIVE, BloodTypeParser.parse("O-"));
        assertEquals(BloodType.A_POSITIVE, BloodTypeParser.parse("A+"));
    }

    @Test
    void parsesEnumName() {
        assertEquals(BloodType.B_POSITIVE, BloodTypeParser.parse("B_POSITIVE"));
    }

    @Test
    void rejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> BloodTypeParser.parse("  "));
    }
}
