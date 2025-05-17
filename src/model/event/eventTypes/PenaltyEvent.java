package model.event.eventTypes;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.Event;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public class PenaltyEvent extends Event {
    private Player player;

    public PenaltyEvent(Player player, int minute) {
        super(player.getName() + "take the penalty kick" , minute);
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"type\": \"penalty\",\n" +
                "  \"description\": \"" + getDescription() + "\",\n" +
                "  \"minute\": " + getMinute() + ",\n" +
                "  \"player\": {\n" +
                "    \"name\": \"" + player.getName() + "\",\n" +
                "    \"number\": " + player.getNumber() + "\n" +
                "  }\n" +
                "}";

        FileWriter writer = new FileWriter("penaltyevent.json");
        writer.write(json);
        writer.close();
    }
}

