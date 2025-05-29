package model.match;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.event.EventManager;
import model.event.PlayerEvent;
import model.event.eventTypes.GoalEvent;
import model.player.Player;
import model.team.Club;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a football match between two teams, implementing the {@link IMatch} interface.
 * Manages match details, events, and provides methods for result calculation and data export.
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Stores home/away teams and match status</li>
 *   <li>Tracks match events through an {@link IEventManager}</li>
 *   <li>Validates match configuration</li>
 *   <li>Calculates match results and statistics</li>
 *   <li>Exports match data to JSON format</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     ITeam home = new Team(homeClub);
 *     ITeam away = new Team(awayClub);
 *     IMatch match = new Match(home, away, 5);
 * </pre>
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Match implements IMatch {
    /** The home team */
    private ITeam homeTeam;
    /** The away team */
    private ITeam awayTeam;
    /** Flag indicating if the match has been played */
    private boolean played = false;
    /** The round number this match belongs to */
    private int round;
    /** Manager for handling match events */
    private IEventManager eventManager = new EventManager();

    /**
     * Constructs a Match with default event management.
     *
     * @param homeTeam Home team (must not be null)
     * @param awayTeam Away team (must not be null)
     * @param round Round number (positive integer)
     */
    public Match(ITeam homeTeam, ITeam awayTeam, int round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.round = round;
    }

    /**
     * Constructs a Match with a custom event manager.
     *
     * @param homeTeam Home team (must not be null)
     * @param awayTeam Away team (must not be null)
     * @param round Round number (positive integer)
     * @param eventManager Custom event manager (must not be null)
     * @throws IllegalArgumentException if eventManager is null
     */
    public Match(ITeam homeTeam, ITeam awayTeam, int round, IEventManager eventManager, boolean isPlayed) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.round = round;
        if (eventManager == null) {
            throw new IllegalArgumentException("Event manager cannot be null.");
        }
        this.eventManager = eventManager;
        this.played = isPlayed;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalStateException if a home team is not set
     */
    @Override
    public IClub getHomeClub() {
        if (homeTeam == null){
            throw new IllegalStateException("Home team is not set.");
        }
        return homeTeam.getClub();
    }

    /**
     * {@inheritDoc}
     * @throws IllegalStateException if away team is not set
     */
    @Override
    public IClub getAwayClub() {
        if (awayTeam == null){
            throw new IllegalStateException("Away team is not set.");
        }
        return awayTeam.getClub();
    }

    /**
     * {@inheritDoc}
     * @throws IllegalStateException if home team is not set
     */
    @Override
    public ITeam getHomeTeam() {
        if (homeTeam == null){
            throw new IllegalStateException("Home team is not set.");
        }
        return homeTeam;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalStateException if away team is not set
     */
    @Override
    public ITeam getAwayTeam() {
        if (awayTeam == null){
            throw new IllegalStateException("Away team is not set.");
        }
        return awayTeam;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlayed() {
        return played;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayed() {
        this.played = true;
    }

    /**
     * {@inheritDoc}
     * @return true if both teams are set, belong to different clubs, and have valid formations
     */
    @Override
    public boolean isValid() {
        if (homeTeam == null || awayTeam == null) {
            return false;
        }
        if (homeTeam.getClub() == null || awayTeam.getClub() == null){
            return false;
        }
        if (homeTeam.getClub().equals(awayTeam.getClub())) {
            return false;
        }
        if (homeTeam.getFormation() == null || awayTeam.getFormation() == null) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRound() {
        return round;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if team is null
     * @throws IllegalStateException if match is already played or club doesn't belong to match
     */
    @Override
    public void setTeam(ITeam team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null.");
        }
        if (isPlayed()) {
            throw new IllegalStateException("Match is already played.");
        }

        if (team.getClub().equals(getHomeClub())) {
            this.homeTeam = team;
        } else if (team.getClub().equals(getAwayClub())) {
            this.awayTeam = team;
        } else {
            throw new IllegalStateException("Club does not belong to this match.");
        }
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if event is null
     */
    @Override
    public void addEvent(IEvent event) {
        if (event == null){
            throw new IllegalArgumentException("Event cannot be null.");
        }
        eventManager.addEvent(event);
    }

    /**
     * {@inheritDoc}
     * @return Array of all recorded events (may be empty)
     */
    @Override
    public IEvent[] getEvents() {
        return eventManager.getEvents();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEventCount() {
        return eventManager.getEventCount();
    }

    /**
     * {@inheritDoc}
     * @param eventClass The class of events to count (e.g., GoalEvent.class)
     * @param club The club to filter events for
     * @return Total events of specified type for the club
     */
    @Override
    public int getTotalByEvent(Class eventClass, IClub club) {
        int total = 0;
        IEvent[] events = getEvents();
        for (int i = 0; i < getEventCount(); i++) {
            if (events[i].getClass().equals(eventClass) && events[i] instanceof PlayerEvent) {
                IPlayer p = ((PlayerEvent) events[i]).getPlayer();
                if (((Player)p).getClub().equals(club.getCode())) {
                    total++;
                }
            }
        }
        return total;
    }

    /**
     * {@inheritDoc}
     * @return Winning team, or null if draw
     */
    @Override
    public ITeam getWinner() {
        int homeGoals = getTotalByEvent(GoalEvent.class, getHomeClub());
        int awayGoals = getTotalByEvent(GoalEvent.class, getAwayClub());

        if (homeGoals > awayGoals) {
            return homeTeam;
        }
        if (awayGoals > homeGoals){
            return awayTeam;
        }
        return null;
    }

    /**
     * Exports match data to a JSON file named "match.json".
     * Includes round number, team names, played status, and event count.
     *
     * @throws IOException if writing to file fails
     */
    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"round\": " + round + ",\n" +
                "  \"homeTeam\": \"" + getHomeClub().getName() + "\",\n" +
                "  \"awayTeam\": \"" + getAwayClub().getName() + "\",\n" +
                "  \"played\": " + played + ",\n" +
                "  \"eventCount\": " + getEventCount() + "\n" +
                "}";

        FileWriter writer = new FileWriter("match.json");
        writer.write(json);
        writer.close();
    }
}
