package model.player;

/**
 * Enum representing the possible types of player positions in football.
 * <p>
 * The possible values are:
 * <ul>
 *     <li>{@link #GOALKEEPER}</li>
 *     <li>{@link #DEFENDER}</li>
 *     <li>{@link #MIDFIELDER}</li>
 *     <li>{@link #FORWARD}</li>
 * </ul>
 * This enum provides utility methods for converting from string and for retrieving
 * the string representation of the position type.
 * </p>
 *
 * Example usage:
 * <pre>
 *     PlayerPositionType type = PlayerPositionType.fromString("midfielder");
 *     String desc = type.getPlayerPositionType(); // "MIDFIELDER"
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public enum PlayerPositionType {
    /**
     * Goalkeeper position.
     */
    GOALKEEPER,

    /**
     * Defender position.
     */
    DEFENDER,

    /**
     * Midfielder position.
     */
    MIDFIELDER,

    /**
     * Forward position.
     */
    FORWARD;

    /**
     * Converts a string to a {@code PlayerPositionType} enum constant.
     *
     * @param text the string to convert (case-insensitive)
     * @return the corresponding {@code PlayerPositionType}
     * @throws IllegalArgumentException if the string does not match any enum value
     */
    public static PlayerPositionType fromString(String text) {
        for (PlayerPositionType type : PlayerPositionType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid position type: " + text);
    }

    /**
     * Returns the string representation of this position type.
     *
     * @return the name of the position type (e.g., "GOALKEEPER")
     */
    public String getPlayerPositionType() {
        return this.name();
    }
}
