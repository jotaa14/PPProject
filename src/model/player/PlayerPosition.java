package model.player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;

/**
 * Represents a specific player position in football (e.g., GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD).
 * Encapsulates the position type and provides a description for use in player-related logic.
 *
 * Example usage:
 * <pre>
 *     PlayerPosition pos = new PlayerPosition("GOALKEEPER");
 *     String desc = pos.getDescription(); // returns "GOALKEEPER"
 * </pre>
 */
public class PlayerPosition implements IPlayerPosition {
    private final PlayerPositionType type;

    /**
     * Constructs a PlayerPosition from a string representation of the position type.
     *
     * @param type String representing the position type (e.g., "GOALKEEPER", "DEFENDER")
     * @throws IllegalArgumentException if the string does not correspond to a valid PlayerPositionType
     */
    public PlayerPosition(String type) {
        this.type = PlayerPositionType.fromString(type);
    }

    /**
     * Returns a string description of the player position type.
     *
     * @return The position type as a string (e.g., "GOALKEEPER")
     */
    @Override
    public String getDescription() {
        return type.getPlayerPositionType();
    }

    /**
     * Gets the PlayerPositionType enum associated with this position.
     *
     * @return The PlayerPositionType of this position
     */
    public PlayerPositionType getType() {
        return type;
    }
}
