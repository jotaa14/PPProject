package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a corner event in a football match, triggered when a player misses a shot
 * and the result is a corner kick for their team.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player involved and the minute the event occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Corner"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     CornerEvent event = new CornerEvent(player, 55);
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class CornerEvent extends PlayerEvent {
    /**
     * Constructs a CornerEvent for the given player and minute.
     *
     * @param player The player involved in the corner event
     * @param minute The minute the event occurred
     */
    public CornerEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + "  Missed The shot So It's a Corner", "Corner");
    }
}
