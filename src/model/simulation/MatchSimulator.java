package model.simulation;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.GoalEvent;
import model.player.Player;

import java.util.Random;

public class MatchSimulator implements MatchSimulatorStrategy {

    @Override
    public void simulate(IMatch match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }
        if (match.isPlayed()) {
            throw new IllegalStateException("Match is already played");
        }
        if (!match.isValid()) {
            throw new IllegalStateException("Match is not valid");
        }

        Random rand = new Random();
        IClub homeClub = match.getHomeClub();
        IClub awayClub = match.getAwayClub();

        for (int minute = 1; minute <= 90; minute++) {
            if (rand.nextDouble() < 0.03) { // ~3% de chance por minuto
                IClub scoringClub = rand.nextBoolean() ? homeClub : awayClub;
                Player scorer = getRandomPlayerFromClub(scoringClub);
                if (scorer != null) {
                    match.addEvent(new GoalEvent(scorer, minute));
                }
            }
        }

        match.setPlayed();
    }

    private Player getRandomPlayerFromClub(IClub club) {
        IPlayer[] players = club.getPlayers();
        if (players == null || players.length == 0) return null;

        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            IPlayer candidate = players[rand.nextInt(players.length)];
            if (candidate instanceof Player) return (Player) candidate;
        }
        return null;
    }
}
