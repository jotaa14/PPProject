package model.event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;

/**
 * Manages a dynamic list of football match events, implementing the {@link IEventManager} interface.
 * Provides methods for adding, retrieving, and counting events, as well as replacing the event list.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Dynamically resizes the internal array as events are added</li>
 *   <li>Prevents duplicate events from being stored</li>
 *   <li>Supports retrieval and replacement of the entire event list</li>
 *   <li>Provides the current count of stored events</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     EventManager manager = new EventManager();
 *     manager.addEvent(new GoalEvent(...));
 *     IEvent[] allEvents = manager.getEvents();
 *     int count = manager.getEventCount();
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class EventManager implements IEventManager {

    /** Internal array to store event objects */
    private IEvent[] events = new IEvent[10];
    /** Current number of events stored */
    private int size = 0;

    /**
     * Adds an event to the manager.
     * Dynamically resizes the array if necessary and prevents duplicates.
     *
     * @param event The event to add (must not be null)
     * @throws IllegalArgumentException if event is null
     * @throws IllegalStateException if event is already stored
     */
    @Override
    public void addEvent(IEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }

        for (int i = 0; i < size; i++) {
            if (events[i].equals(event)) {
                throw new IllegalStateException("Event is already stored.");
            }
        }

        if (size == events.length) {
            IEvent[] newEvents = new IEvent[events.length * 2];
            for (int i = 0; i < events.length; i++) {
                newEvents[i] = events[i];
            }
            events = newEvents;
        }

        events[size++] = event;
    }

    /**
     * Returns a copy of all stored events.
     *
     * @return Array containing all events
     */
    @Override
    public IEvent[] getEvents() {
        IEvent[] copy = new IEvent[size];
        for (int i = 0; i < size; i++) {
            copy[i] = events[i];
        }
        return copy;
    }

    /**
     * Replaces the current event list with a new array of events.
     *
     * @param events The new array of events (must not be null)
     * @throws IllegalArgumentException if the events array is null
     */
    public void setEvents(IEvent[] events) {
        if (events == null) {
            throw new IllegalArgumentException("Events array cannot be null.");
        }
        this.events = new IEvent[events.length];
        for (int i = 0; i < events.length; i++) {
            this.events[i] = events[i];
        }
        this.size = events.length;
    }

    /**
     * Returns the number of events currently stored.
     *
     * @return Event count
     */
    @Override
    public int getEventCount() {
        return size;
    }
}
