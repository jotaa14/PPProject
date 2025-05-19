package model.event.eventTypes;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.PlayerEvent;
import model.player.Player;

public class CornerEvent extends PlayerEvent {
    public CornerEvent(Player player, int minute) {
        super(player, player.getName() + "missed the shot so it's a corner", "Corner", minute);
    }
}