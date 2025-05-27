package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class PenaltyEvent extends PlayerEvent {
    public PenaltyEvent(Player player, int minute) {
        super(minute, player, player.getName() + "(" +player.getClub() +")" + " Take the Penalty Kick", "Penalty");
    }
}