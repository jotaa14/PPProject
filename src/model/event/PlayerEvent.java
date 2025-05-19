package model.event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.Event;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public abstract class PlayerEvent extends Event {
    private Player player;
    private String type;

    public PlayerEvent(Player player, String description, String type, int minute) {
        super(description, minute);
        this.player = player;
        this.type = type;
    }

    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"type\": \"" + type + "\",\n" +
                "  \"description\": \"" + getDescription() + "\",\n" +
                "  \"minute\": " + getMinute() + ",\n" +
                "  \"player\": {\n" +
                "    \"name\": \"" + player.getName() + "\",\n" +
                "    \"number\": " + player.getNumber() + "\n" +
                "  }\n" +
                "}";

        FileWriter writer = new FileWriter(type + ".json");
        writer.write(json);
        writer.close();
    }
}
