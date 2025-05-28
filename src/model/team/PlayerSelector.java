package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;

/**
 * Implements player selection logic to find the first available player
 * in a specified position within a club's roster.
 * <p>
 * This selector follows a simple strategy of returning the first player
 * found matching the requested position.
 * </p>
 *
 * @author
 */
public class PlayerSelector implements IPlayerSelector {

    /**
     * Selects the first player in the specified club that matches the given position.
     *
     * @param club Club to search for players
     * @param position Required player position
     * @return First player found in the requested position
     * @throws IllegalArgumentException if:
     * <ul>
     *   <li>{@code club} or {@code position} is null</li>
     *   <li>No player exists for the requested position</li>
     * </ul>
     */
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
