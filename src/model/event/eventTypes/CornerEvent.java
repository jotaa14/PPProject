package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class CornerEvent extends PlayerEvent {
    public CornerEvent(Player player, int minute) {
        super(player, player.getName() + "  Missed The shot So It's a Corner", "Corner", minute);
    }
}