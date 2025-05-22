package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class OffSideEvent extends PlayerEvent {
    public OffSideEvent(Player player, int minute) {
        super(minute,player,"\uD83D\uDEA9\uD83D\uDE45 " + player.getName() + "(" +player.getClub() +")" + " Was Caught In An Offside Position", "OffSide");
    }
}
