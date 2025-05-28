package model.event.eventTypes;

import model.event.Event;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents the start of a football match as an event.
 * Extends {@link Event}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the minute the match started</li>
 *   <li>Provides a fixed description and type ("Start of match", "Start Event")</li>
 *   <li>Supports exporting the event to a JSON file ("start_event.json")</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     StartEvent event = new StartEvent(0);
 *     event.exportToJson();
 * </pre>
 *
 * @author
 */
public class StartEvent extends Event {

    /**
     * Constructs a StartEvent for the given minute.
     *
     * @param minute The minute the match started (usually 0)
     */
    public StartEvent(int minute) {
        super(minute, "Start of match", "Start Event");
    }

    /**
     * Exports the start event to a JSON file named "start_event.json".
     * The JSON includes the type, description, and minute.
     *
     * @throws IOException if an I/O error occurs while writing the file
     */
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
