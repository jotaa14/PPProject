package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class CornerKickEvent extends PlayerEvent {
    public CornerKickEvent(Player player, int minute) {
        super(player, player.getName() + " Took The Corner Kick", "Corner Kick", minute);
    }
}
