package model.event.eventTypes;

import model.event.PlayerEvent;
import model.player.Player;

public class ShotEvent extends PlayerEvent {
    public ShotEvent(Player player, int minute) {
        super("\uD83D\uDD5B",minute,player,"\uD83C\uDFAF Shot on Goal by " + player.getName() + "(" +player.getClub() +")","Shot" );
    }
}