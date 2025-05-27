package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class OffSideEvent extends PlayerEvent {
    public OffSideEvent(Player player, int minute) {
        super(minute,player, player.getName() + "(" +player.getClub() +")" + " Was Caught In An Offside Position", "OffSide");
    }
}
