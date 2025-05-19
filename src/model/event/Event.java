package model.event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import java.io.IOException;

public class Event implements IEvent {
    private String description;
    private int minute;

    public Event(String description, int minute) {
        this.description = description;
        this.minute = minute;
    }

    @Override
    public String getDescription() {
        return description; }

    @Override
    public int getMinute() {
        return minute; }

    @Override
    public String toString() {
        return description + " at " + minute + " min";
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
