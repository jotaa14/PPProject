package model.event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;

public class EventManager implements IEventManager {

    private IEvent[] events = new IEvent[10];
    private int size = 0;

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

    @Override
    public IEvent[] getEvents() {
        IEvent[] copy = new IEvent[size];
        for (int i = 0; i < size; i++) {
            copy[i] = events[i];
        }
        return copy;
    }

    @Override
    public int getEventCount() {
        return size;
    }
}
