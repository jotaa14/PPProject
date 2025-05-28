package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents an offside event in a football match, indicating that a player was caught in an offside position.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who was offside and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "OffSide"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     OffSideEvent event = new OffSideEvent(player, 45);
 * </pre>
 *
 * @author
 */
public class OffSideEvent extends PlayerEvent {
    /**
     * Constructs an OffSideEvent for the given player and minute.
     *
     * @param player The player who was caught offside
     * @param minute The minute the event occurred
     */
    public OffSideEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " Was Caught In An Offside Position", "OffSide");
    }
}
