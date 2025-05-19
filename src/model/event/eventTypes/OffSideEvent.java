package model.event.eventTypes;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.Event;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public class OffSideEvent extends Event {
    private Player player;

    public OffSideEvent(Player player, int minute) {
        super(player.getName() + " foi flagrado em posição de impedimento", minute);
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"type\": \"offside\",\n" +
                "  \"description\": \"" + getDescription() + "\",\n" +
                "  \"minute\": " + getMinute() + ",\n" +
                "  \"player\": {\n" +
                "    \"name\": \"" + player.getName() + "\",\n" +
                "    \"number\": " + player.getNumber() + "\n" +
                "  }\n" +
                "}";

        FileWriter writer = new FileWriter("offsideevent.json");
        writer.write(json);
        writer.close();
    }
}
