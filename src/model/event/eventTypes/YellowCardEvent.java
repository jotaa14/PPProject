package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

/**
 * Represents a yellow card event in a football match, indicating that a player received a yellow card.
 * Extends {@link PlayerEvent}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the player who received the yellow card and the minute it occurred</li>
 *   <li>Automatically generates a descriptive message for the event</li>
 *   <li>The event type is set as "Yellow Card"</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     YellowCardEvent event = new YellowCardEvent(player, 21);
 * </pre>
 *
 * @author
 */
public class YellowCardEvent extends PlayerEvent {
    /**
     * Constructs a YellowCardEvent for the given player and minute.
     *
     * @param player The player who received the yellow card
     * @param minute The minute the event occurred
     */
    public YellowCardEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" + player.getClub() + ")" + " Received a Yellow Card", "Yellow Card");
    }
}
