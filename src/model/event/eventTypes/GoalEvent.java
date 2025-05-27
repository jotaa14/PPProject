package model.event.eventTypes;

import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;

import model.event.PlayerEvent;
import model.player.Player;

public class GoalEvent extends PlayerEvent implements IGoalEvent {
    public GoalEvent(Player player, int minute) {
        super(minute, player, "Goal by " + player.getName() + "(" +player.getClub() +")", "Goal");
    }
}