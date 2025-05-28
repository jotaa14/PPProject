package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a turnover event in a football match, indicating that a player lost possession of the ball.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who lost possession and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Turnover"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     TurnoverEvent event = new TurnoverEvent(player, 53);
 * </pre>
 *
 * @author
 */
public class TurnoverEvent extends PlayerEvent {
    /**
     * Constructs a TurnoverEvent for the given player and minute.
     *
     * @param player The player who lost possession
     * @param minute The minute the event occurred
     */
    public TurnoverEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " lost possession", "Turnover");
    }
}
