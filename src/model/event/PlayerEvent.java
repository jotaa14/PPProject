package model.event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public abstract class PlayerEvent extends Event {
    private Player player;

    public PlayerEvent( int minute, Player player, String description, String type) {
        super(minute, description, type);
        this.player = player;
    }

    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public void exportToJson() throws IOException {
    }
}
