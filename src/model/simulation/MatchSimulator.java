package model.simulation;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import model.event.eventTypes.*;
import model.player.Player;

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
        if (match == null) throw new IllegalArgumentException("Match cannot be null");
        if (match.isPlayed()) throw new IllegalStateException("Match is already played");
        if (!match.isValid()) throw new IllegalStateException("Match is not valid");

        match.addEvent(new StartEvent(0));

        for (int minute = 1; minute <= 90; minute++) {
            double chance = rand.nextDouble();
            IClub club = rand.nextBoolean() ? match.getHomeClub() : match.getAwayClub();
            Player player = getRandomPlayerFromClub(club);
            if (player == null) continue;

            // SHOT
            if (chance < SHOT_CHANCE) {
                match.addEvent(new ShotEvent(player, minute));

                if (rand.nextDouble() < GOAL_AFTER_SHOT_CHANCE) {
                    match.addEvent(new GoalEvent(player, minute));
                } else if (rand.nextDouble() < CORNER_AFTER_MISSED_CHANCE) {
                    match.addEvent(new DefenseEvent(player, minute));
                    match.addEvent(new CornerEvent(player, minute));
                    match.addEvent(new CornerKickEvent(player, minute));
                } else {
                    match.addEvent(new DefenseEvent(player, minute));
                    match.addEvent(new GoalKickEvent(player, minute));
                }
            }

            // FOUL
            else if (chance < SHOT_CHANCE + FOUL_CHANCE) {
                match.addEvent(new FoulEvent(player, minute));

                double cardRoll = rand.nextDouble();
                if (cardRoll < 0.03) match.addEvent(new RedCardEvent(player, minute));
                else if (cardRoll < 0.13) match.addEvent(new YellowCardEvent(player, minute));

                if (rand.nextDouble() < FOUL_NEAR_AREA_CHANCE) {
                    if (rand.nextDouble() < PENALTY_AFTER_FOUL_CHANCE) {
                        match.addEvent(new PenaltyEvent(player, minute));

                        if (rand.nextDouble() < PENALTY_CONVERT_CHANCE) {
                            match.addEvent(new GoalEvent(player, minute));
                        } else if (rand.nextDouble() < PENALTY_MISS_CORNER_CHANCE) {
                            match.addEvent(new DefenseEvent(player, minute));
                            match.addEvent(new CornerEvent(player, minute));
                            match.addEvent(new CornerKickEvent(player, minute));
                        } else {
                            match.addEvent(new DefenseEvent(player, minute));
                            match.addEvent(new GoalKickEvent(player, minute));
                        }
                    } else {
                        match.addEvent(new ShotEvent(player, minute));
                        if (rand.nextDouble() < FREEKICK_GOAL_CHANCE) {
                            match.addEvent(new GoalEvent(player, minute));
                        } else if (rand.nextDouble() < FREEKICK_MISS_CORNER_CHANCE) {
                            match.addEvent(new DefenseEvent(player, minute));
                            match.addEvent(new CornerEvent(player, minute));
                            match.addEvent(new CornerKickEvent(player, minute));
                        } else {
                            match.addEvent(new DefenseEvent(player, minute));
                            match.addEvent(new GoalKickEvent(player, minute));
                        }
                    }
                }
            }

            // OFFSIDE
            else if (chance < SHOT_CHANCE + FOUL_CHANCE + OFFSIDE_CHANCE) {
                match.addEvent(new OffSideEvent(player, minute));
            }
        }

        match.addEvent(new EndEvent(90));
        match.setPlayed();
    }


    private Player getRandomPlayerFromClub(IClub club) {
        IPlayer[] players = club.getPlayers();
        if (players == null || players.length == 0) return null;

        for (int i = 0; i < 10; i++) {
            IPlayer candidate = players[rand.nextInt(players.length)];
            if (candidate instanceof Player) return (Player) candidate;
        }
        return null;
    }
}
