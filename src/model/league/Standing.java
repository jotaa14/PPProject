package model.league;

import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

/**
 * Represents the league standing for a single team, tracking points, wins, draws, losses,
 * goals scored, and goals conceded. Implements the {@link IStanding} interface.
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Tracks and updates team statistics throughout the season</li>
 *   <li>Calculates total matches played and goal difference</li>
 *   <li>Provides a static utility to update standings after a match</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     Standing homeStanding = new Standing(homeTeam);
 *     Standing awayStanding = new Standing(awayTeam);
 *     Standing.updateStandingsAfterMatch(homeStanding, 2, awayStanding, 1);
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Standing implements IStanding {
    /** The team this standing refers to */
    private ITeam team;
    /** Total points accumulated */
    private int points = 0;
    /** Number of wins */
    private int wins = 0;
    /** Number of draws */
    private int draws = 0;
    /** Number of losses */
    private int losses = 0;
    /** Total goals scored */
    private int goalsScored = 0;
    /** Total goals conceded */
    private int goalsConceded = 0;

    /**
     * Constructs a Standing object for the given team.
     * @param team The team this standing is for
     */
    public Standing(ITeam team) {
        this.team = team;
    }

    public Standing(ITeam team, int points, int wins, int draws, int losses,
                    int goalsScored, int goalsConceded) {
        this.team = team;
        this.points = points;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITeam getTeam() {
        return team;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPoints() {
        return points;
    }

    /**
     * Adds the specified number of points to the team's total.
     * @param i Number of points to add
     */
    @Override
    public void addPoints(int i) {
        points += i;
    }

    /**
     * Adds the specified number of wins and updates points accordingly.
     * @param i Number of wins to add
     */
    @Override
    public void addWin(int i) {
        wins += i;
        points += 3 * i;
    }

    /**
     * Adds the specified number of draws and updates points accordingly.
     * @param i Number of draws to add
     */
    @Override
    public void addDraw(int i) {
        draws += i;
        points += 1 * i;
    }

    /**
     * Adds the specified number of losses.
     * @param i Number of losses to add
     */
    @Override
    public void addLoss(int i) {
        losses += i;
        // Points for loss remain unchanged (0)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWins() {
        return wins;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDraws() {
        return draws;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLosses() {
        return losses;
    }

    /**
     * {@inheritDoc}
     * @return Total number of matches played (wins + draws + losses)
     */
    @Override
    public int getTotalMatches() {
        return wins + draws + losses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGoalScored() {
        return goalsScored;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGoalsConceded() {
        return goalsConceded;
    }

    /**
     * {@inheritDoc}
     * @return Goal difference (goals scored - goals conceded)
     */
    @Override
    public int getGoalDifference() {
        return goalsScored - goalsConceded;
    }

    /**
     * Adds goals to the total goals scored.
     * @param goals Number of goals to add
     */
    public void addGoalsScored(int goals) {
        goalsScored += goals;
    }

    /**
     * Adds goals to the total goals conceded.
     * @param goals Number of goals to add
     */
    public void addGoalsConceded(int goals) {
        goalsConceded += goals;
    }

    /**
     * Updates the standings for both home and away teams after a match.
     * Adjusts goals, wins, draws, losses, and points as appropriate.
     *
     * @param homeStanding Standing for the home team
     * @param homeGoals Goals scored by the home team
     * @param awayStanding Standing for the away team
     * @param awayGoals Goals scored by the away team
     */
    public static void updateStandingsAfterMatch(Standing homeStanding, int homeGoals, Standing awayStanding, int awayGoals) {
        homeStanding.addGoalsScored(homeGoals);
        homeStanding.addGoalsConceded(awayGoals);

        awayStanding.addGoalsScored(awayGoals);
        awayStanding.addGoalsConceded(homeGoals);

        if (homeGoals > awayGoals) {
            homeStanding.addWin(1);
            awayStanding.addLoss(1);
        } else if (homeGoals < awayGoals) {
            awayStanding.addWin(1);
            homeStanding.addLoss(1);
        } else {
            homeStanding.addDraw(1);
            awayStanding.addDraw(1);
        }
    }
}
