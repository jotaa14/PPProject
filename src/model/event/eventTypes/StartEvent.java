package model.event.eventTypes;

import model.event.Event;

import java.io.FileWriter;
import java.io.IOException;

public class StartEvent extends Event {

    public StartEvent(int minute) {
        super(minute, "Start of match", "Start Event");
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"type\": \"start\",\n" +
                "  \"description\": \"" + getDescription() + "\",\n" +
                "  \"minute\": " + getMinute() + "\n" +
                "}";

        FileWriter writer = new FileWriter("start_event.json");
        writer.write(json);
        writer.close();
    }
}
