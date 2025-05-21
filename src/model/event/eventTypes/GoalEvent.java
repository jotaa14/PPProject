package model.event.eventTypes;

import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;

import model.event.PlayerEvent;
import model.player.Player;

public class GoalEvent extends PlayerEvent implements IGoalEvent {
    public GoalEvent(Player player, int minute) {
        super("\uD83D\uDD5B", minute, player, "⚽\uFE0F\uD83D\uDD25 Goal by " + player.getName() + "(" +player.getClub() +")", "Goal");
    }
}