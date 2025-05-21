package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class YellowCardEvent extends PlayerEvent {
    public YellowCardEvent(Player player, int minute) {
        super("\uD83D\uDD5B",minute,player,"\uD83D\uDFE8 "+ player.getName() + "(" +player.getClub() +")" + " Received a Yellow Card", "Yellow Card");
    }
}
