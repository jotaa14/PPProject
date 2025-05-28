package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a red card event in a football match, indicating that a player has been sent off.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who received the red card and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Red Card"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     RedCardEvent event = new RedCardEvent(player, 82);
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class RedCardEvent extends PlayerEvent {
    /**
     * Constructs a RedCardEvent for the given player and minute.
     *
     * @param player The player who received the red card
     * @param minute The minute the event occurred
     */
    public RedCardEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " received a red card", "Red Card");
    }
}
