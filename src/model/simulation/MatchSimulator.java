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

/**
 * Simulates a football match between two teams, generating events such as shots, goals,
 * passes, fouls, penalties, and offsides according to probabilities inspired by real-world data.
 * <p>
 * This class implements the {@link MatchSimulatorStrategy} interface and provides a realistic
 * simulation of a football match by randomly generating key match events minute-by-minute
 * based on statistical probabilities.
 * </p>
 *
 * <h2>Probabilities Used</h2>
 * <ul>
 *     <li>Shot chance per minute: 18%</li>
 *     <li>Passing chance per minute: 25%</li>
 *     <li>Goal after shot: 11%</li>
 *     <li>Corner after missed shot: 25%</li>
 *     <li>Foul chance per minute: 10%</li>
 *     <li>Foul near area: 18%</li>
 *     <li>Penalty after foul: 2%</li>
 *     <li>Penalty conversion: 78%</li>
 *     <li>Penalty miss leads to corner: 35%</li>
 *     <li>Free kick goal: 7%</li>
 *     <li>Free kick miss leads to corner: 18%</li>
 *     <li>Offside chance per minute: 2.5%</li>
 *     <li>Pass to goal chance: 8%</li>
 *     <li>Pass success: 82%</li>
 * </ul>
 *
 * <p>
 * The simulation covers 90 minutes and generates a sequence of events that are added to the match.
 * </p>
 *
 * <b>Example usage:</b>
 * <pre>
 *     MatchSimulator simulator = new MatchSimulator();
 *     simulator.simulate(match);
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class MatchSimulator implements MatchSimulatorStrategy {

    private final Random rand = new Random();

    private static final double SHOT_CHANCE = 0.18;
    private static final double PASSING_CHANCE = 0.25;
    private static final double GOAL_AFTER_SHOT_CHANCE = 0.11;
    private static final double CORNER_AFTER_MISSED_CHANCE = 0.25;

    private static final double FOUL_CHANCE = 0.10;
    private static final double FOUL_NEAR_AREA_CHANCE = 0.18;

    private static final double PENALTY_AFTER_FOUL_CHANCE = 0.02;
    private static final double PENALTY_CONVERT_CHANCE = 0.78;
    private static final double PENALTY_MISS_CORNER_CHANCE = 0.35;

    private static final double FREEKICK_GOAL_CHANCE = 0.07;
    private static final double FREEKICK_MISS_CORNER_CHANCE = 0.18;

    private static final double OFFSIDE_CHANCE = 0.025;
    private static final double PASS_TO_GOAL_CHANCE = 0.08;
    private static final double PASS_SUCCESS_CHANCE = 0.82;

    /**
     * Simulates the given match, generating events for each minute.
     *
     * @param match The match to simulate.
     * @throws IllegalArgumentException if the match is null.
     * @throws IllegalStateException if the match is already played or invalid.
     */
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

    /**
     * Validates the match before simulation.
     *
     * @param match The match to validate.
     * @throws IllegalArgumentException if match is null.
     * @throws IllegalStateException if match is already played or invalid.
     */
    private void validateMatch(IMatch match) {
        if (match == null) throw new IllegalArgumentException("Match cannot be null");
        if (match.isPlayed()) throw new IllegalStateException("Match is already played");
        if (!match.isValid()) throw new IllegalStateException("Match is not valid");
    }

    /**
     * Processes and generates events for a single minute of the match.
     *
     * @param match The match being simulated.
     * @param minute The current minute.
     */
    private void processMinuteEvents(IMatch match, int minute) {
        double roll = rand.nextDouble();
        IClub attackingClub = rand.nextBoolean() ? match.getHomeClub() : match.getAwayClub();
        Player attacker = getRandomOutfieldPlayer(attackingClub);
        if (attacker == null) return;

        if (roll < SHOT_CHANCE) {
            handleShotEvent(match, minute, attackingClub, attacker);
        } else if (roll < SHOT_CHANCE + PASSING_CHANCE) {
            handlePassingEvent(match, minute, attackingClub, attacker);
        } else if (roll < SHOT_CHANCE + PASSING_CHANCE + FOUL_CHANCE) {
            handleFoulEvent(match, minute, attackingClub, attacker);
        } else if (roll < SHOT_CHANCE + PASSING_CHANCE + FOUL_CHANCE + OFFSIDE_CHANCE) {
            match.addEvent(new OffSideEvent(attacker, minute));
        }
    }

    /**
     * Handles a shot event, possibly resulting in a goal or a missed shot.
     */
    private void handleShotEvent(IMatch match, int minute, IClub attackingClub, Player attacker) {
        match.addEvent(new ShotEvent(attacker, minute));

        if (rand.nextDouble() < GOAL_AFTER_SHOT_CHANCE) {
            match.addEvent(new GoalEvent(attacker, minute));
        } else {
            handleMissedShot(match, minute, attackingClub, attacker);
        }
    }

    /**
     * Handles a missed shot, possibly resulting in a corner or goal kick.
     */
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

    /**
     * Handles a passing event, possibly resulting in a shot or turnover.
     */
    private void handlePassingEvent(IMatch match, int minute, IClub attackingClub, Player passer) {
        match.addEvent(new PassingEvent(passer, minute));

        if (rand.nextDouble() < PASS_SUCCESS_CHANCE) {
            Player receiver = getRandomOutfieldPlayer(attackingClub);
            if (receiver != null && !receiver.equals(passer)) {
                match.addEvent(new PassingEvent(receiver, minute));
                if (rand.nextDouble() < PASS_TO_GOAL_CHANCE) {
                    match.addEvent(new ShotEvent(receiver, minute));
                    if (rand.nextDouble() < GOAL_AFTER_SHOT_CHANCE) {
                        match.addEvent(new GoalEvent(receiver, minute));
                    } else {
                        handleMissedShot(match, minute, attackingClub, receiver);
                    }
                }
            }
        } else {
            match.addEvent(new TurnoverEvent(passer, minute));
        }
    }

    /**
     * Handles a foul event, possibly resulting in cards, penalties, or free kicks.
     */
    private void handleFoulEvent(IMatch match, int minute, IClub attackingClub, Player attacker) {
        match.addEvent(new FoulEvent(attacker, minute));

        if (attacker.isSentOff()) return;

        double cardRoll = rand.nextDouble();
        if (cardRoll < 0.03) {
            attacker.sendOff();
            match.addEvent(new RedCardEvent(attacker, minute));
            System.out.println("Player " + attacker.getName() + " was sent off!");
        } else if (cardRoll < 0.13) {
            attacker.addYellowCard();
            match.addEvent(new YellowCardEvent(attacker, minute));
            if (attacker.getYellowCards() == 2) {
                attacker.sendOff();
                match.addEvent(new RedCardEvent(attacker, minute));
                System.out.println("Player " + attacker.getName() + " was sent off (2nd yellow)!");
            }
        }

        if (rand.nextDouble() < FOUL_NEAR_AREA_CHANCE) {
            handleDangerousFoul(match, minute, attackingClub, attacker);
        }
    }

    /**
     * Handles a dangerous foul, possibly resulting in a penalty or free kick.
     */
    private void handleDangerousFoul(IMatch match, int minute, IClub attackingClub, Player attacker) {
        if (rand.nextDouble() < PENALTY_AFTER_FOUL_CHANCE) {
            handlePenalty(match, minute, attackingClub, attacker);
        } else {
            handleFreeKick(match, minute, attackingClub, attacker);
        }
    }

    /**
     * Handles a penalty event, possibly resulting in a goal, corner, or goal kick.
     */
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

    /**
     * Handles a free kick event, possibly resulting in a goal, corner, or goal kick.
     */
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

    /**
     * Selects a random outfield player (not goalkeeper, not sent off) from a club.
     *
     * @param club The club to select from.
     * @return A random outfield {@link Player}, or null if none available.
     */
    private Player getRandomOutfieldPlayer(IClub club) {
        IPlayer[] allPlayers = club.getPlayers();
        if (allPlayers == null || allPlayers.length == 0) return null;

        int count = 0;
        for (IPlayer p : allPlayers) {
            if (p instanceof Player) {
                Player player = (Player) p;
                if (((PlayerPosition) player.getPosition()).getType() != PlayerPositionType.GOALKEEPER
                        && !player.isSentOff()) {
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
                if (((PlayerPosition) player.getPosition()).getType() != PlayerPositionType.GOALKEEPER
                        && !player.isSentOff()) {
                    outfieldPlayers[idx++] = player;
                }
            }
        }
        return outfieldPlayers[rand.nextInt(count)];
    }

    /**
     * Returns the opponent's goalkeeper for the given attacking club.
     *
     * @param match The match being simulated.
     * @param attackingClub The club currently attacking.
     * @return The opponent's {@link Player} goalkeeper, or null if not found.
     */
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
