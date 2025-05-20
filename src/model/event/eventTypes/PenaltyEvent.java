package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class PenaltyEvent extends PlayerEvent {
    public PenaltyEvent(Player player, int minute) {
        super(player,"\uD83E\uDDB5⚖\uFE0F " + player.getName() + "(" +player.getClub() +")" + " Take the Penalty Kick", "Penalty", minute);
    }
}