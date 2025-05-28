package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a passing event in a football match, indicating that a player made a successful pass.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who made the pass and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Pass"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     PassingEvent event = new PassingEvent(player, 12);
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class PassingEvent extends PlayerEvent {
    /**
     * Constructs a PassingEvent for the given player and minute.
     *
     * @param player The player who made the pass
     * @param minute The minute the event occurred
     */
    public PassingEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " Made a Beautiful Pass ", "Pass");
    }
}
