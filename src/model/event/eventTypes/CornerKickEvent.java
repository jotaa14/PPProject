package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a corner kick event in a football match, indicating that a player took a corner kick.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who took the corner kick and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Corner Kick"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     CornerKickEvent event = new CornerKickEvent(player, 67);
 * </pre>
 *
 * @author
 */
public class CornerKickEvent extends PlayerEvent {
    /**
     * Constructs a CornerKickEvent for the given player and minute.
     *
     * @param player The player who took the corner kick
     * @param minute The minute the event occurred
     */
    public CornerKickEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " Took The Corner Kick", "Corner Kick");
    }
}
