package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class PassingEvent extends PlayerEvent {
    public PassingEvent(Player player, int minute) {
        super(minute,player,"\uD83E\uDDB6 " + player.getName() + "(" +player.getClub() +")" + " Made a Beautiful Pass ", "Pass");
    }
}