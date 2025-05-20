package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class PenaltyEvent extends PlayerEvent {
    public PenaltyEvent(Player player, int minute) {
        super(player,player.getName() + "Take the Penalty Kick", "Penalty", minute);

    }
}