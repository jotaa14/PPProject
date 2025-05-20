package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class GoalKickEvent extends PlayerEvent {
    public GoalKickEvent(Player player, int minute) {
        super(player, player.getName() + " Took a Goal Kick","Goal Kick", minute);
    }
}
