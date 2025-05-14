package model.event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;

public class EventManager implements IEventManager {

    private IEvent[] events = new IEvent[10];
    private int size = 0;

    @Override
    public void addEvent(IEvent event) {
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
        IEvent[] result = new IEvent[size];
        for (int i = 0; i < size; i++) {
            result[i] = events[i];
        }
        return result;
    }

    @Override
    public int getEventCount() {
        return size;
    }
}
