package model.event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public abstract class PlayerEvent extends Event {
    private Player player;
    private String type;
    private String clock;

    public PlayerEvent(String clock, int minute, Player player, String description, String type) {
        super(minute, description);
        this.player = player;
        this.type = type;
        this.clock = clock;
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
