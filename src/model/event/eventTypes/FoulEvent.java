package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a foul event in a football match, indicating that a player committed a foul.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who committed the foul and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Foul"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     FoulEvent event = new FoulEvent(player, 34);
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class FoulEvent extends PlayerEvent {
    /**
     * Constructs a FoulEvent for the given player and minute.
     *
     * @param player The player who committed the foul
     * @param minute The minute the event occurred
     */
    public FoulEvent(Player player, int minute) {
        super(minute, player, "Foul In " + player.getName() + "(" + player.getClub() + ")", "Foul");
    }
}
