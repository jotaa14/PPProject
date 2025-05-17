package model.event.eventTypes;

import model.event.Event;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public class FoulEvent extends Event {
    private Player player;

    public FoulEvent(Player player, int minute) {
        super("Foul by " + player.getName(), minute);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"type\": \"foul\",\n" +
                "  \"description\": \"" + getDescription() + "\",\n" +
                "  \"minute\": " + getMinute() + ",\n" +
                "  \"player\": {\n" +
                "    \"name\": \"" + player.getName() + "\",\n" +
                "    \"number\": " + player.getNumber() + "\n" +
                "  }\n" +
                "}";

        FileWriter writer = new FileWriter("foulevent.json");
        writer.write(json);
        writer.close();
    }
}
