package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class CornerKickEvent extends PlayerEvent {
    public CornerKickEvent(Player player, int minute) {
        super("\uD83D\uDD5B", minute, player,"\uD83D\uDEA9\uD83E\uDDB5 " + player.getName() + "(" +player.getClub() +")" + " Took The Corner Kick", "Corner Kick");
    }
}
