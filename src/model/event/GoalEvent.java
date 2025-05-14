package model.event;

import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.player.Player;

public class GoalEvent extends Event implements IGoalEvent {
    private Player player;

    public GoalEvent(Player player, int minute) {
        super("Golo de " + player.getName(), minute);
        this.player = player;
    }

    @Override
    public IPlayer getPlayer() { return player; }
}

