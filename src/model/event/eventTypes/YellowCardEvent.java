package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class YellowCardEvent extends PlayerEvent {
    public YellowCardEvent(Player player, int minute) {
        super(player, player.getName() + " Received a Yellow Card", "Yellow Card", minute);
    }
}
