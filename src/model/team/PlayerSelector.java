package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;

public class PlayerSelector implements IPlayerSelector {

    @Override
    public IPlayer selectPlayer(IClub club, IPlayerPosition position) {
        if (club == null || position == null) {
            throw new IllegalArgumentException("Club and Position can't be null");
        }

        IPlayer[] players = club.getPlayers();
        String targetPosition = position.getDescription().toUpperCase();

        for (IPlayer player : players) {
            if (player.getPosition().getDescription().equalsIgnoreCase(targetPosition)) {
                return player;
            }
        }
        throw new IllegalArgumentException("No player found for position: " + targetPosition);
    }
}
