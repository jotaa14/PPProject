package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class GoalKickEvent extends PlayerEvent {
    public GoalKickEvent(Player player, int minute) {
        super( minute, player,"\uD83E\uDDE4\uD83E\uDDB5 " + player.getName() + "(" +player.getClub() +")" + " Took a Goal Kick","Goal Kick");
    }
}
