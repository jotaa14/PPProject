package model.player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;

/**
 * Represents a football player's position on the field.
 * Implements the {@link IPlayerPosition} interface.
 * This class encapsulates the position type and provides
 * a textual description for it.
 *
 * Example usage:
 * <pre>
 *     PlayerPosition pos = new PlayerPosition("MIDFIELDER");
 *     String desc = pos.getDescription(); // "MIDFIELDER"
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class PlayerPosition implements IPlayerPosition {
    /**
     * The type of the player position (e.g., GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD).
     */
    private final PlayerPositionType type;

    /**
     * Constructs a PlayerPosition from a string type.
     *
     * @param type The string representation of the position type.
     *             Must match a valid {@link PlayerPositionType}.
     * @throws IllegalArgumentException if the type string is invalid.
     */
    public PlayerPosition(String type) {
        this.type = PlayerPositionType.fromString(type);
    }

    /**
     * Returns a textual description of the player position type.
     *
     * @return the description of the position type (e.g., "MIDFIELDER").
     */
    @Override
    public String getDescription() {
        return type.getPlayerPositionType();
    }

    /**
     * Returns the enum type of this player position.
     *
     * @return the {@link PlayerPositionType} of this position.
     */
    public PlayerPositionType getType() {
        return type;
    }
}
