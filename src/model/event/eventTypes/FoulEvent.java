package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class FoulEvent extends PlayerEvent {
    public FoulEvent(Player player, int minute) {
        super(player, "❌ Foul In " + player.getName() + "(" +player.getClub() +")" , "Foul" ,  minute);
    }
}
