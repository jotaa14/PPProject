package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class DefenseEvent extends PlayerEvent {
    public DefenseEvent(Player player, int minute) {
        super(player,"\uD83D\uDEE1\uFE0F " +  player.getName() + "(" +player.getClub() +")" + " Made a Great Save", "Defense", minute);
    }
}
