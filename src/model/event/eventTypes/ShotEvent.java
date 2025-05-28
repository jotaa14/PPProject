package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a shot event in a football match, indicating that a player took a shot on goal.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who took the shot and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Shot"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     ShotEvent event = new ShotEvent(player, 30);
 * </pre>
 *
 * @author
 */
public class ShotEvent extends PlayerEvent {
    /**
     * Constructs a ShotEvent for the given player and minute.
     *
     * @param player The player who took the shot
     * @param minute The minute the event occurred
     */
    public ShotEvent(Player player, int minute) {
        super(minute, player, "Shot on Goal by " + player.getName() + "(" + player.getClub() + ")", "Shot");
    }
}
