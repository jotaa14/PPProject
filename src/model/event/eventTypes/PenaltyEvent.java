package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a penalty event in a football match, indicating that a player took a penalty kick.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who took the penalty and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>Event type is set as "Penalty"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     PenaltyEvent event = new PenaltyEvent(player, 75);
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class PenaltyEvent extends PlayerEvent {
    /**
     * Constructs a PenaltyEvent for the given player and minute.
     *
     * @param player The player who took the penalty kick
     * @param minute The minute the event occurred
     */
    public PenaltyEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " Take the Penalty Kick", "Penalty");
    }
}
