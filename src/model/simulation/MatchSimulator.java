package model.simulation;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.eventTypes.*;
import model.player.Player;
import model.player.PlayerPosition;
import model.player.PlayerPositionType;

import java.util.Random;

public class MatchSimulator implements MatchSimulatorStrategy {

    private final Random rand = new Random();

    private static final double SHOT_CHANCE = 0.05;
    private static final double GOAL_AFTER_SHOT_CHANCE = 0.5;
    private static final double CORNER_AFTER_MISSED_CHANCE = 0.5;

    private static final double FOUL_CHANCE = 0.02;
    private static final double FOUL_NEAR_AREA_CHANCE = 0.4;

    private static final double PENALTY_AFTER_FOUL_CHANCE = 0.3;
    private static final double PENALTY_CONVERT_CHANCE = 0.75;
    private static final double PENALTY_MISS_CORNER_CHANCE = 0.4;

    private static final double FREEKICK_GOAL_CHANCE = 0.3;
    private static final double FREEKICK_MISS_CORNER_CHANCE = 0.5;
    private static final double OFFSIDE_CHANCE = 0.01;

    @Override
    public void simulate(IMatch match) {
        validateMatch(match);
        match.addEvent(new StartEvent(0));

        for (int minute = 1; minute <= 90; minute++) {
            processMinuteEvents(match, minute);
        }

        match.addEvent(new EndEvent(90));
        match.setPlayed();
    }

    private void validateMatch(IMatch match) {
        if (match == null){
            throw new IllegalArgumentException("Match cannot be null");
        }
        if (match.isPlayed()){
            throw new IllegalStateException("Match is already played");
        }
        if (!match.isValid()){
            throw new IllegalStateException("Match is not valid");
        }
    }

    private void processMinuteEvents(IMatch match, int minute) {
        double chance = rand.nextDouble();
        IClub attackingClub = rand.nextBoolean() ? match.getHomeClub() : match.getAwayClub();
        Player attacker = getRandomOutfieldPlayer(attackingClub);
        if (attacker == null) return;

        if (chance < SHOT_CHANCE) {
            handleShotEvent(match, minute, attackingClub, attacker);
        } else if (chance < SHOT_CHANCE + FOUL_CHANCE) {
            handleFoulEvent(match, minute, attackingClub, attacker);
        } else if (chance < SHOT_CHANCE + FOUL_CHANCE + OFFSIDE_CHANCE) {
            match.addEvent(new OffSideEvent(attacker, minute));
        }
    }

    private void handleShotEvent(IMatch match, int minute, IClub attackingClub, Player attacker) {
        match.addEvent(new ShotEvent(attacker, minute));

        if (rand.nextDouble() < GOAL_AFTER_SHOT_CHANCE) {
            match.addEvent(new GoalEvent(attacker, minute));
        } else {
            handleMissedShot(match, minute, attackingClub, attacker);
        }
    }

    private void handleMissedShot(IMatch match, int minute, IClub attackingClub, Player attacker) {
        Player goalkeeper = getOpponentGoalkeeper(match, attackingClub);
        if (goalkeeper == null) return;

        match.addEvent(new DefenseEvent(goalkeeper, minute));

        if (rand.nextDouble() < CORNER_AFTER_MISSED_CHANCE) {
            match.addEvent(new CornerEvent(attacker, minute));
            match.addEvent(new CornerKickEvent(attacker, minute));
        } else {
            match.addEvent(new GoalKickEvent(goalkeeper, minute));
        }
    }

    private void handleFoulEvent(IMatch match, int minute, IClub attackingClub, Player attacker) {
        match.addEvent(new FoulEvent(attacker, minute));

        double cardRoll = rand.nextDouble();
        if (cardRoll < 0.03) match.addEvent(new RedCardEvent(attacker, minute));
        else if (cardRoll < 0.13) match.addEvent(new YellowCardEvent(attacker, minute));

        if (rand.nextDouble() < FOUL_NEAR_AREA_CHANCE) {
            handleDangerousFoul(match, minute, attackingClub, attacker);
        }
    }

    private void handleDangerousFoul(IMatch match, int minute, IClub attackingClub, Player attacker) {
        if (rand.nextDouble() < PENALTY_AFTER_FOUL_CHANCE) {
            handlePenalty(match, minute, attackingClub, attacker);
        } else {
            handleFreeKick(match, minute, attackingClub, attacker);
        }
    }

    private void handlePenalty(IMatch match, int minute, IClub attackingClub, Player attacker) {
        match.addEvent(new PenaltyEvent(attacker, minute));
        Player goalkeeper = getOpponentGoalkeeper(match, attackingClub);
        if (goalkeeper == null) return;

        if (rand.nextDouble() < PENALTY_CONVERT_CHANCE) {
            match.addEvent(new GoalEvent(attacker, minute));
        } else {
            match.addEvent(new DefenseEvent(goalkeeper, minute));
            if (rand.nextDouble() < PENALTY_MISS_CORNER_CHANCE) {
                match.addEvent(new CornerEvent(attacker, minute));
                match.addEvent(new CornerKickEvent(attacker, minute));
            } else {
                match.addEvent(new GoalKickEvent(goalkeeper, minute));
            }
        }
    }

    private void handleFreeKick(IMatch match, int minute, IClub attackingClub, Player attacker) {
        match.addEvent(new ShotEvent(attacker, minute));
        Player goalkeeper = getOpponentGoalkeeper(match, attackingClub);
        if (goalkeeper == null) return;

        if (rand.nextDouble() < FREEKICK_GOAL_CHANCE) {
            match.addEvent(new GoalEvent(attacker, minute));
        } else {
            match.addEvent(new DefenseEvent(goalkeeper, minute));
            if (rand.nextDouble() < FREEKICK_MISS_CORNER_CHANCE) {
                match.addEvent(new CornerEvent(attacker, minute));
                match.addEvent(new CornerKickEvent(attacker, minute));
            } else {
                match.addEvent(new GoalKickEvent(goalkeeper, minute));
            }
        }
    }

    private Player getRandomOutfieldPlayer(IClub club) {
        IPlayer[] allPlayers = club.getPlayers();
        if (allPlayers == null || allPlayers.length == 0) return null;

        int count = 0;
        for (IPlayer p : allPlayers) {
            if (p instanceof Player) {
                Player player = (Player) p;
                if (((PlayerPosition) player.getPosition()).getType() != PlayerPositionType.GOALKEEPER) {
                    count++;
                }
            }
        }
        if (count == 0) return null;

        Player[] outfieldPlayers = new Player[count];
        int idx = 0;
        for (IPlayer p : allPlayers) {
            if (p instanceof Player) {
                Player player = (Player) p;
                if (((PlayerPosition) player.getPosition()).getType() != PlayerPositionType.GOALKEEPER) {
                    outfieldPlayers[idx++] = player;
                }
            }
        }
        return outfieldPlayers[rand.nextInt(count)];
    }

    private Player getOpponentGoalkeeper(IMatch match, IClub attackingClub) {
        IClub opponent = attackingClub == match.getHomeClub() ?
                match.getAwayClub() : match.getHomeClub();

        IPlayer[] players = opponent.getPlayers();
        if (players == null || players.length == 0) return null;

        for (IPlayer p : players) {
            if (p instanceof Player) {
                Player player = (Player) p;
                if (((PlayerPosition) player.getPosition()).getType() == PlayerPositionType.GOALKEEPER) {
                    return player;
                }
            }
        }
        return null;
    }
}
