package model.event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import java.io.IOException;

/**
 * Abstract base class for football match events, implementing the {@link IEvent} interface.
 * Stores common event details such as description, minute, and type.
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Encapsulates event description, minute of occurrence, and event type</li>
 *   <li>Provides accessors for all core event properties</li>
 *   <li>Defines a standard string representation for events</li>
 *   <li>Supports export to JSON (to be implemented in subclasses)</li>
 * </ul>
 *
 * <b>Usage:</b> Extend this class to implement specific event types (e.g., GoalEvent, FoulEvent).
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public abstract class Event implements IEvent {
    /** Description of the event */
    private String description;
    /** Minute in which the event occurred */
    private int minute;
    /** Type/category of the event (e.g., "GOAL", "FOUL") */
    private String type;

    /**
     * Constructs an Event with the specified minute, description, and type.
     *
     * @param minute Minute in which the event occurred
     * @param description Description of the event
     * @param type Event type/category
     */
    public Event(int minute, String description, String type) {
        this.description = description;
        this.minute = minute;
        this.type = type;
    }

    /**
     * {@inheritDoc}
     * @return Event description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     * @return Minute the event occurred
     */
    @Override
    public int getMinute() {
        return minute;
    }

    /**
     * Returns the type/category of the event.
     * @return Event type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns a formatted string describing the event.
     * @return String in the format "|minute min| description"
     */
    @Override
    public String toString() {
        return "|" + minute + " min| " + description;
    }

    /**
     * Exports the event to JSON format.
     * This method should be overridden by subclasses.
     *
     * @throws IOException if export fails
     */
    @Override
    public void exportToJson() throws IOException {
        // To be implemented by subclasses if needed
    }
}
