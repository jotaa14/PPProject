package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a defense event in a football match, indicating that a player (typically a goalkeeper)
 * made a significant save.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who made the save and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Defense"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     DefenseEvent event = new DefenseEvent(player, 78);
 * </pre>
 *
 * @author
 */
public class DefenseEvent extends PlayerEvent {
    /**
     * Constructs a DefenseEvent for the given player and minute.
     *
     * @param player The player who made the save
     * @param minute The minute the event occurred
     */
    public DefenseEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " Made a Great Save", "Defense");
    }
}
