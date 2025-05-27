package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class RedCardEvent extends PlayerEvent {
    public RedCardEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" +player.getClub() +")" + " received a red card", "Red Card");
        }
}