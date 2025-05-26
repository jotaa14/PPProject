package model.player;

/**
 * Represents football player position types as enumerated constants.
 * Provides conversion methods between string representations and enum values.
 *
 * <p>Example usage:
 * <pre>
 * PlayerPositionType gk = PlayerPositionType.fromString("goalkeeper");
 * String positionName = gk.getPlayerPositionType(); // returns "GOALKEEPER"
 * </pre>
 */
public enum PlayerPositionType {
    /** Goalkeeper position */
    GOALKEEPER,
    /** Defender position */
    DEFENDER,
    /** Midfielder position */
    MIDFIELDER,
    /** Forward position */
    FORWARD;

    /**
     * Gets the position name in uppercase format.
     * @return String representation of the position (e.g. "GOALKEEPER")
     */
    public String getPlayerPositionType() {
        return this.name();
    }

    /**
     * Converts a string to the corresponding PlayerPositionType enum (case-insensitive).
     *
     * @param type String representation of position (e.g. "defender")
     * @return Matching PlayerPositionType enum
     * @throws IllegalArgumentException if no matching enum is found
     */
    public static PlayerPositionType fromString(String type) {
        for(PlayerPositionType p : values()) {
            if (p.name().equalsIgnoreCase(type)) {
                return p;
            }
        }
        throw new IllegalArgumentException("No constant with text " + type + " found");
    }

    /**
     * Validates and returns the official position name for a given string input.
     *
     * @param type Candidate position name (case-insensitive)
     * @return Official position name in uppercase
     * @throws IllegalArgumentException if input doesn't match any position
     */
    public static String getPlayerPositionType(String type) {
        for(PlayerPositionType p : values()) {
            if (p.name().equalsIgnoreCase(type)) {
                return p.name();
            }
        }
        throw new IllegalArgumentException("No constant with text " + type + " found");
    }
}
