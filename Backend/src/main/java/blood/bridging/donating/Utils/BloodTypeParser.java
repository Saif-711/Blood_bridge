package blood.bridging.donating.Utils;

import blood.bridging.donating.Utils.Enum.BloodType;

public final class BloodTypeParser {

    private BloodTypeParser() {
    }

    /**
     * Accepts enum names (A_POSITIVE) or common forms (A+, O-, AB+).
     */
    public static BloodType parse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("bloodType is required");
        }
        String s = raw.trim().toUpperCase().replace(" ", "");
        try {
            return BloodType.valueOf(s);
        } catch (IllegalArgumentException ignored) {
            // fall through
        }
        return switch (s) {
            case "A+" -> BloodType.A_POSITIVE;
            case "A-" -> BloodType.A_NEGATIVE;
            case "B+" -> BloodType.B_POSITIVE;
            case "B-" -> BloodType.B_NEGATIVE;
            case "AB+" -> BloodType.AB_POSITIVE;
            case "AB-" -> BloodType.AB_NEGATIVE;
            case "O+" -> BloodType.O_POSITIVE;
            case "O-" -> BloodType.O_NEGATIVE;
            default -> throw new IllegalArgumentException("Unknown blood type: " + raw);
        };
    }
}
