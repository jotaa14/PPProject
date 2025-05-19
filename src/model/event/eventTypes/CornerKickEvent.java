package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class CornerKickEvent extends PlayerEvent {
    public CornerKickEvent(Player player, int minute) {
        super(player, player.getName() + " executou um escanteio", "Corner Kick", minute);
    }
}
