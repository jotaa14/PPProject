package model.league;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.event.*;
import model.event.eventTypes.*;
import model.match.Match;
import model.simulation.MatchSimulator;
import model.team.Club;
import model.team.Team;

import java.io.IOException;

import static model.league.Standing.updateStandingsAfterMatch;

/**
 * Represents a football season, managing clubs, schedule, matches, standings, and simulation logic.
 * Implements the {@link ISeason} interface and provides methods for adding/removing clubs,
 * generating the schedule, simulating rounds and the entire season, and exporting data.
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Manages clubs and teams participating in the season</li>
 *   <li>Generates a round-robin schedule for the league</li>
 *   <li>Simulates rounds and the season using a match simulator strategy</li>
 *   <li>Tracks standings and match results</li>
 *   <li>Provides player statistics and exports season data</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *   Season season = new Season("Premier League", 2025, 20);
 *   season.addClub(club);
 *   season.generateSchedule();
 *   season.simulateSeason();
 *   season.exportToJson();
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Season implements ISeason {

    /** Name of the season or league */
    private String name;
    /** Year of the season */
    private int year;
    /** Current round number (0-based) */
    private int currentRound;
    /** Maximum number of teams allowed */
    private int maxTeams;
    /** Array of participating clubs */
    private IClub[] clubs;
    /** Array of all matches in the season */
    private IMatch[] matches;
    /** Schedule object managing rounds and matches */
    private ISchedule schedule;
    /** Standings for each team */
    private IStanding[] standings;
    /** Simulator for match simulation */
    private MatchSimulatorStrategy simulator = new MatchSimulator();
    /** Array of participating teams */
    private ITeam[] teams;
    /** Current number of teams in the season */
    private int numberOfCurrentTeams;

    /**
     * Constructs a new Season.
     *
     * @param name Name of the season or league
     * @param year Year of the season
     * @param maxTeams Maximum number of teams allowed
     */
    public Season(String name, int year, int maxTeams) {
        this.name = name;
        this.year = year;
        this.maxTeams = maxTeams;
        this.clubs = new IClub[maxTeams];
        this.teams = new ITeam[maxTeams];
        this.numberOfCurrentTeams = 0;
        this.currentRound = 0;
    }

    public Season(String name, int year,int currentRound ,int maxTeams, IClub[] club, ITeam[] teams, IMatch[] matches, ISchedule schedule, IStanding[] standings) {
        this.name = name;
        this.year = year;
        this.maxTeams = maxTeams;
        this.clubs = club;
        this.teams = teams;
        this.matches = matches;
        this.schedule = schedule;
        this.standings = standings;
        this.numberOfCurrentTeams = club != null ? club.length : 0;
        this.currentRound = 0;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getYear() {
        return year;
    }

    public ITeam[] getTeams(){
        return teams;
    }

    /**
     * Adds a club to the season.
     *
     * @param club Club to add
     * @return true if the club was added successfully
     * @throws IllegalArgumentException if club is null or already exists
     * @throws IllegalStateException if league is full
     */
    @Override
    public boolean addClub(IClub club) {
        if (club == null) throw new IllegalArgumentException("Club cannot be null");
        if (numberOfCurrentTeams >= maxTeams) throw new IllegalStateException("League is full");
        for (int i = 0; i < numberOfCurrentTeams; i++) {
            if (clubs[i].equals(club)) throw new IllegalArgumentException("Club already exists");
        }

        clubs[numberOfCurrentTeams] = club;
        if (standings == null) {
            standings = new IStanding[maxTeams];
        }
        standings[numberOfCurrentTeams] = new Standing(((Club)club).getTeam());
        numberOfCurrentTeams++;
        return true;
    }

    /**
     * Removes a club from the season.
     *
     * @param club Club to remove
     * @return true if the club was removed
     * @throws IllegalArgumentException if club is null
     * @throws IllegalStateException if club is not found
     */
    @Override
    public boolean removeClub(IClub club) {
        if (club == null) throw new IllegalArgumentException("Club cannot be null");
        boolean found = false;
        for (int i = 0; i < numberOfCurrentTeams; i++) {
            if (clubs[i].equals(club)) {
                found = true;
                for (int j = i; j < numberOfCurrentTeams - 1; j++) {
                    clubs[j] = clubs[j + 1];
                }
                clubs[numberOfCurrentTeams - 1] = null;
                numberOfCurrentTeams--;
                break;
            }
        }
        if (!found) throw new IllegalStateException("Club not found in the league");
        return true;
    }

    /**
     * Generates a round-robin schedule for the season.
     * Throws if there are not enough teams.
     */
    @Override
    public void generateSchedule() {
        if (numberOfCurrentTeams < 2) {
            throw new IllegalStateException("Not enough teams to generate schedule");
        }

        boolean isOdd = numberOfCurrentTeams % 2 != 0;
        int n = isOdd ? numberOfCurrentTeams + 1 : numberOfCurrentTeams;
        int totalRounds = (isOdd ? numberOfCurrentTeams : numberOfCurrentTeams - 1) * 2;
        int matchesPerRound = n / 2;

        ITeam[] tempTeams = new ITeam[n];
        for (int i = 0; i < numberOfCurrentTeams; i++) {
            tempTeams[i] = ((Club) clubs[i]).getTeam();
        }
        if (isOdd) {
            tempTeams[n - 1] = null;
        }

        ITeam[] originalTempTeams = new ITeam[tempTeams.length];
        for (int i = 0; i < tempTeams.length; i++) {
            originalTempTeams[i] = tempTeams[i];
        }

        int totalMatches = totalRounds * matchesPerRound;
        this.matches = new IMatch[totalMatches];
        this.schedule = new Schedule(totalRounds, matchesPerRound);
        int matchIndex = 0;

        for (int round = 0; round < totalRounds; round++) {
            if (round == totalRounds / 2) {
                for (int i = 0; i < originalTempTeams.length; i++) {
                    tempTeams[i] = originalTempTeams[i];
                }
            }

            for (int i = 0; i < matchesPerRound; i++) {
                int t1 = i;
                int t2 = n - 1 - i;
                ITeam home, away;
                if (round < totalRounds / 2) {
                    home = (round % 2 == 0) ? tempTeams[t1] : tempTeams[t2];
                    away = (round % 2 == 0) ? tempTeams[t2] : tempTeams[t1];
                } else {
                    home = (round % 2 == 0) ? tempTeams[t2] : tempTeams[t1];
                    away = (round % 2 == 0) ? tempTeams[t1] : tempTeams[t2];
                }
                if (home != null && away != null) {
                    IMatch match = new Match(home, away, round);
                    ((Schedule) schedule).addMatchToRound(round, match);
                    matches[matchIndex++] = match;
                }
            }

            ITeam last = tempTeams[n - 1];
            for (int k = n - 1; k > 1; k--) {
                tempTeams[k] = tempTeams[k - 1];
            }
            tempTeams[1] = last;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMatch[] getMatches() {
        return matches;
    }

    /**
     * Returns all matches for a specific round.
     *
     * @param round Round number
     * @return Array of matches for the round
     */
    @Override
    public IMatch[] getMatches(int round) {
        int count = 0;
        for (IMatch match : matches) {
            if (match != null && match.getRound() == round) count++;
        }
        IMatch[] roundMatches = new IMatch[count];
        int idx = 0;
        for (IMatch match : matches) {
            if (match != null && match.getRound() == round) {
                roundMatches[idx++] = match;
            }
        }
        return roundMatches;
    }

    /**
     * Simulates all matches in the current round and updates standings.
     * Advances to the next round.
     */
    @Override
    public void simulateRound() {
        if (isSeasonComplete()){
            return;
        }
        System.out.println("\n--------------------------------------------");
        System.out.println("               ROUND " + (currentRound + 1));
        System.out.println("--------------------------------------------\n");

        IMatch[] roundMatches = getMatches(currentRound);

        for (IMatch match : roundMatches) {
            if (match.isValid() && !match.isPlayed()) {
                simulator.simulate(match);
                match.setPlayed();
                System.out.println("Match simulated: " + displayMatchResult(match));

                IStanding[] standings = getLeagueStandings();
                ITeam homeTeam = match.getHomeTeam();
                ITeam awayTeam = match.getAwayTeam();

                Standing homeStanding = null;
                Standing awayStanding = null;
                for (IStanding standing : standings) {
                    if (standing != null) {
                        if (standing.getTeam().equals(homeTeam)) {
                            homeStanding = (Standing) standing;
                        }
                        if (standing.getTeam().equals(awayTeam)) {
                            awayStanding = (Standing) standing;
                        }
                    }
                }

                if (homeStanding != null && awayStanding != null) {
                    int homeGoals = match.getTotalByEvent(GoalEvent.class, match.getHomeClub());
                    int awayGoals = match.getTotalByEvent(GoalEvent.class, match.getAwayClub());

                    updateStandingsAfterMatch(homeStanding, homeGoals, awayStanding, awayGoals);
                }
            }
        }
        currentRound++;
    }

    /**
     * Simulates all remaining rounds in the season.
     */
    @Override
    public void simulateSeason() {
        while (!isSeasonComplete()) {
            simulateRound();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Checks if the season is complete (all rounds played).
     *
     * @return true if all rounds are complete
     */
    @Override
    public boolean isSeasonComplete() {
        return currentRound >= getMaxRounds();
    }

    /**
     * Resets the season, clearing all matches and standings.
     */
    @Override
    public void resetSeason() {
        currentRound = 0;
        if (matches != null) {
            for (int i = 0; i < matches.length; i++) {
                IMatch match = matches[i];
                if (match != null) {
                    matches[i] = new Match(match.getHomeTeam(), match.getAwayTeam(), match.getRound());
                }
            }
            for(int j = 0; j < standings.length; j++) {
                if (standings[j] != null) {
                    standings[j] = new Standing(standings[j].getTeam());
                }
            }
        }
    }

    /**
     * Returns a formatted string representing the result of a match.
     *
     * @param match Match to display
     * @return String with the result and winner/draw
     */
    @Override
    public String displayMatchResult(IMatch match) {
        if (match == null) return "Invalid match!";
        String home = match.getHomeClub().getName();
        String away = match.getAwayClub().getName();
        int homeGoals = match.getTotalByEvent(GoalEvent.class, match.getHomeClub());
        int awayGoals = match.getTotalByEvent(GoalEvent.class, match.getAwayClub());
        String result = home + " " + homeGoals + " x " + awayGoals + " " + away;
        if (homeGoals > awayGoals) {
            result += "  - " + home + " wins!";
        } else if (awayGoals > homeGoals) {
            result += "  - " + away + " wins!";
        } else {
            result += "  - Draw!";
        }
        return result;
    }

    /**
     * Sets the match simulator strategy for this season.
     *
     * @param matchSimulatorStrategy Simulator to use
     */
    @Override
    public void setMatchSimulator(MatchSimulatorStrategy matchSimulatorStrategy) {
        this.simulator = matchSimulatorStrategy;
    }

    /**
     * Returns a copy of the league standings array.
     *
     * @return Array of standings
     */
    @Override
    public IStanding[] getLeagueStandings() {
        if (standings == null) return null;
        IStanding[] copy = new IStanding[standings.length];
        for (int i = 0; i < standings.length; i++) {
            copy[i] = standings[i];
        }
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ISchedule getSchedule() {
        return schedule;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPointsPerWin() {
        return 3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPointsPerDraw() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPointsPerLoss() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxTeams() {
        return maxTeams;
    }

    /**
     * Calculates the maximum number of rounds for the current number of teams.
     *
     * @return Number of rounds
     */
    @Override
    public int getMaxRounds() {
        boolean isOdd = numberOfCurrentTeams % 2 != 0;
        return (isOdd ? numberOfCurrentTeams : numberOfCurrentTeams - 1) * 2;
    }

    /**
     * Returns the number of matches that have been played.
     *
     * @return Number of played matches
     */
    @Override
    public int getCurrentMatches() {
        int count = 0;
        for (IMatch match : matches) {
            if (match != null && match.isPlayed()) count++;
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfCurrentTeams() {
        return numberOfCurrentTeams;
    }

    /**
     * Returns the array of currently participating clubs.
     *
     * @return Array of clubs
     */
    @Override
    public IClub[] getCurrentClubs() {
        IClub[] current = new IClub[numberOfCurrentTeams];
        for (int i = 0; i < numberOfCurrentTeams; i++) {
            current[i] = clubs[i];
        }
        return current;
    }

    /**
     * Prints detailed statistics for a given player for the current season.
     *
     * @param player Player to analyze
     */
    public void getPlayerStatistics(IPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        if (matches == null || matches.length == 0) {
            System.out.println("No matches available.");
            return;
        }

        int cornerKicks = 0;
        int foul = 0;
        int yellowCards = 0;
        int redCards = 0;
        int shot = 0;
        int penalties = 0;
        int offSide = 0;
        int goals = 0;
        int defenses = 0;
        int passes = 0;
        int lostBalls = 0;

        for (IMatch iMatch : matches) {
            if (iMatch != null && iMatch.isPlayed()) {
                for (IEvent event : iMatch.getEvents()) {
                    if (event != null && event instanceof PlayerEvent && ((PlayerEvent) event).getPlayer().equals(player)) {
                        if (event instanceof GoalEvent) {
                            goals++;
                        } else if (event instanceof CornerKickEvent) {
                            cornerKicks++;
                        } else if (event instanceof FoulEvent) {
                            foul++;
                        } else if (event instanceof YellowCardEvent) {
                            yellowCards++;
                        } else if (event instanceof RedCardEvent) {
                            redCards++;
                        } else if (event instanceof ShotEvent) {
                            shot++;
                        } else if (event instanceof PenaltyEvent) {
                            penalties++;
                        } else if (event instanceof OffSideEvent) {
                            offSide++;
                        } else if (event instanceof DefenseEvent) {
                            defenses++;
                        } else if (event instanceof PassingEvent) {
                            passes++;
                        } else if (event instanceof TurnoverEvent) {
                            lostBalls++;
                        }
                    }
                }
            }
        }

        int totalGames = (currentRound);
        double goalsPerGame = totalGames > 0 ? (double) goals / totalGames : 0.0;
        double shotsPerGame = totalGames > 0 ? (double) shot / totalGames : 0.0;
        double shotAccuracy = shot > 0 ? ((double) goals / shot) * 100 : 0.0;
        double passesPerGame = totalGames > 0 ? (double) passes / totalGames : 0.0;
        double passAccuracy = passes > 0 ? ((double) (passes - lostBalls) / passes) * 100 : 0.0;

        System.out.println();
        System.out.println("+==============================================+");
        System.out.printf("|         Player Statistics - %-14s|\n", player.getName());
        System.out.println("+=======================+======================+");
        System.out.printf("| %-21s | %-20s |\n", "Number", player.getNumber());
        System.out.println("+-----------------------+----------------------+");
        System.out.printf("| %-21s | %-20d |\n", "Goals", goals);
        System.out.printf("| %-21s | %-20d |\n", "Shots", shot);
        System.out.printf("| %-21s | %-20d |\n", "Passes", passes);
        System.out.printf("| %-21s | %-20d |\n", "Lost Balls", lostBalls);
        System.out.printf("| %-21s | %-20d |\n", "Defenses", defenses);
        System.out.printf("| %-21s | %-20d |\n", "Penalties", penalties);
        System.out.printf("| %-21s | %-20d |\n", "Fouls", foul);
        System.out.printf("| %-21s | %-20d |\n", "Yellow Cards", yellowCards);
        System.out.printf("| %-21s | %-20d |\n", "Red Cards", redCards);
        System.out.printf("| %-21s | %-20d |\n", "Corner Kicks", cornerKicks);
        System.out.printf("| %-21s | %-20d |\n", "Offside", offSide);
        System.out.printf("| %-21s | %-20d |\n", "Total Games", totalGames);
        System.out.println("+-----------------------+----------------------+");
        System.out.printf("| %-21s | %-20.2f |\n", "Goals per Game", goalsPerGame);
        System.out.printf("| %-21s | %-20.2f |\n", "Shots per Game", shotsPerGame);
        System.out.printf("| %-21s | %-19.2f%% |\n", "Shot Accuracy (%)", shotAccuracy);
        System.out.printf("| %-21s | %-20.2f |\n", "Passes per Game(F.G)", passesPerGame);
        System.out.printf("| %-21s | %-19.2f%% |\n", "Pass Accuracy(F.G) (%)", passAccuracy);
        System.out.println("+=======================+======================+");
        System.out.println("Caption: (F.G) - For Goal");
        System.out.println();
    }

    /**
     * {@inheritDoc}
     *
     * <p><b>Note:</b> This method is intentionally left unimplemented in this class,
     * as JSON export is handled centrally by a component responsible for exporting
     * the complete state of the application.</p>
     *
     * <p>This implementation exists solely to satisfy the requirements of the
     * {@code Exportable} interface and has no practical use in this specific class.</p>
     *
     * @throws IOException Not applicable in this implementation
     */
    @Override
    public void exportToJson() throws IOException {
        // Not applicable in this class
    }

    /**
     * Returns the name of the league/season.
     * @return Name of the season
     */
    public String getLeagueName() {
        return name;
    }
}
