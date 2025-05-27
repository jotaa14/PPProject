package model.event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import java.io.IOException;

public abstract class Event implements IEvent {
    private String description;
    private int minute;
    private String type;

    public Event( int minute, String description, String type) {
        this.description = description;
        this.minute = minute;
        this.type = type;
    }

    @Override
    public String getDescription() {
        return description; }

    @Override
    public int getMinute() {
        return minute;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "|" + minute + " min| " + description;
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
