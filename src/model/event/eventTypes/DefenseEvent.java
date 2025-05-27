package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class DefenseEvent extends PlayerEvent {
    public DefenseEvent(Player player, int minute) {
        super( minute, player, player.getName() + "(" +player.getClub() +")" + " Made a Great Save", "Defense");
    }
}
