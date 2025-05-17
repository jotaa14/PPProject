package model.event.eventTypes;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.Event;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public class   CornerIEvent  extends Event {
    private Player player;

    public CornerIEvent(Player player, int minute) {
        super(player.getName() + "missed the shot so it's a corner", minute);
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"type\": \"goal\",\n" +
                "  \"description\": \"" + getDescription() + "\",\n" +
                "  \"minute\": " + getMinute() + ",\n" +
                "  \"player\": {\n" +
                "    \"name\": \"" + player.getName() + "\",\n" +
                "    \"number\": " + player.getNumber() + "\n" +
                "  }\n" +
                "}";

        FileWriter writer = new FileWriter("cornerevent.json");
        writer.write(json);
        writer.close();
    }
}