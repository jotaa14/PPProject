package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a goal kick event in a football match, indicating that a player took a goal kick.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who took the goal kick and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Goal Kick"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     GoalKickEvent event = new GoalKickEvent(player, 60);
 * </pre>
 *
 * @author
 */
public class GoalKickEvent extends PlayerEvent {
    /**
     * Constructs a GoalKickEvent for the given player and minute.
     *
     * @param player The player who took the goal kick
     * @param minute The minute the event occurred
     */
    public GoalKickEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " Took a Goal Kick", "Goal Kick");
    }
}
