package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class CornerEvent extends PlayerEvent {
    public CornerEvent(Player player, int minute) {
        super("\uD83D\uDD5B ",minute,player,"\uD83D\uDEA9 " + player.getName() + "(" +player.getClub() +")" + "  Missed The shot So It's a Corner", "Corner");
    }
}