package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class GoalKickEvent extends PlayerEvent {
    public GoalKickEvent(Player player, int minute) {
        super( minute, player, player.getName() + "(" +player.getClub() +")" + " Took a Goal Kick","Goal Kick");
    }
}
