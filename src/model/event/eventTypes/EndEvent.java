package model.event.eventTypes;

import model.event.Event;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents the end of a football match as an event.
 * Extends {@link Event}.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores the minute the match ended</li>
 *   <li>Provides a fixed description and type ("End of match", "End Event")</li>
 *   <li>Supports exporting the event to a JSON file ("end_event.json")</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     EndEvent event = new EndEvent(90);
 *     event.exportToJson();
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class EndEvent extends Event {

    /**
     * Constructs an EndEvent for the given minute.
     *
     * @param minute The minute the match ended
     */
    public EndEvent(int minute) {
        super(minute, "End of match", "End Event");
    }

    /**
     * Exports the end event to a JSON file named "end_event.json".
     * The JSON includes the type, description, and minute.
     *
     * @throws IOException if an I/O error occurs while writing the file
     */
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
