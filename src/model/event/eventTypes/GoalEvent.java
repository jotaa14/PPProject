package model.event.eventTypes;

import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;
import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a goal event in a football match, indicating that a player scored a goal.
 * Extends {@link PlayerEvent} and implements {@link IGoalEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who scored and the minute the goal was scored</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Goal"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     GoalEvent event = new GoalEvent(player, 23);
 * </pre>
 *
 * @author
 */
public class GoalEvent extends PlayerEvent implements IGoalEvent {
    /**
     * Constructs a GoalEvent for the given player and minute.
     *
     * @param player The player who scored the goal
     * @param minute The minute the goal was scored
     */
    public GoalEvent(Player player, int minute) {
        super(minute, player, "Goal by " + player.getName() + "(" + player.getClub() + ")", "Goal");
    }
}
