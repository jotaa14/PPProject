package model.event.eventTypes;

import model.event.Event;

import java.io.FileWriter;
import java.io.IOException;

public class EndEvent extends Event {

    public EndEvent(int minute) {
        super(minute, "End of match", "End Event");
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"type\": \"end\",\n" +
                "  \"description\": \"" + getDescription() + "\",\n" +
                "  \"minute\": " + getMinute() + "\n" +
                "}";

        FileWriter writer = new FileWriter("end_event.json");
        writer.write(json);
        writer.close();
    }
}

