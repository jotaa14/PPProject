package model.event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.player.Player;

import java.io.IOException;

/**
 * Abstract base class for football match events that involve a specific player.
 * Extends {@link Event} and adds a reference to the involved {@link Player}.
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Stores a reference to the involved player</li>
 *   <li>Provides access to the player via {@link #getPlayer()}</li>
 *   <li>Supports JSON export (to be implemented by subclasses if needed)</li>
 * </ul>
 *
 * <b>Usage:</b> Extend this class to implement specific player-related event types (e.g., GoalEvent, FoulEvent).
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public abstract class PlayerEvent extends Event {
    /** The player involved in this event */
    private Player player;

    /**
     * Constructs a PlayerEvent.
     *
     * @param minute Minute in which the event occurred
     * @param player The player involved
     * @param description Description of the event
     * @param type Type/category of the event
     */
    public PlayerEvent(int minute, Player player, String description, String type) {
        super(minute, description, type);
        this.player = player;
    }

    /**
     * Returns the player involved in this event.
     *
     * @return The involved player
     */
    public IPlayer getPlayer() {
        return player;
    }

    /**
     * {@inheritDoc}
     *
     * <p><b>Note:</b> This method is intentionally left unimplemented in this class,
     * as JSON export is handled centrally by a component responsible for exporting
     * the complete state of the application.</p>
     *
     * <p>This implementation exists solely to satisfy the requirements of the
     * {@code Exportable} interface and has no practical use in this specific class.</p>
     *
     * @throws IOException Not applicable in this implementation
     */
    @Override
    public void exportToJson() throws IOException {
        // Not applicable in this class
    }
}
