package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class TurnoverEvent  extends PlayerEvent {
    public TurnoverEvent (Player player, int minute) {
        super( minute, player, player.getName() + "(" +player.getClub() +")" + " lost possession" , "Turnover ");
    }
}
