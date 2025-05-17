package model.event.eventTypes;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.Event;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public class ShotEvent extends Event{
    private Player player;

    public ShotEvent(Player player, int minute) {
        super(player.getName() + "shot on goal", minute);
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"type\": \"shot\",\n" +
                "  \"description\": \"" + getDescription() + "\",\n" +
                "  \"minute\": " + getMinute() + ",\n" +
                "  \"player\": {\n" +
                "    \"name\": \"" + player.getName() + "\",\n" +
                "    \"number\": " + player.getNumber() + "\n" +
                "  }\n" +
                "}";

        FileWriter writer = new FileWriter("shotevent.json");
        writer.write(json);
        writer.close();
    }
}
