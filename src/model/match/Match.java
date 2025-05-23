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

public class Match implements IMatch {
    private ITeam homeTeam;
    private ITeam awayTeam;
    private boolean played = false;
    private int round;

    private IEventManager eventManager = new EventManager();

    public Match(ITeam homeTeam, ITeam awayTeam, int round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.round = round;
    }

    @Override
    public IClub getHomeClub() {
        if (homeTeam == null){
            throw new IllegalStateException("Home team is not set.");
        }
        return homeTeam.getClub();
    }

    @Override
    public IClub getAwayClub() {
        if (awayTeam == null){
            throw new IllegalStateException("Away team is not set.");
        }
        return awayTeam.getClub();
    }

    @Override
    public ITeam getHomeTeam() {
        if (homeTeam == null){
            throw new IllegalStateException("Home team is not set.");
        }
        return homeTeam;
    }

    @Override
    public ITeam getAwayTeam() {
        if (awayTeam == null){
            throw new IllegalStateException("Away team is not set.");
        }
        return awayTeam;
    }

    @Override
    public boolean isPlayed() {
        return played;
    }

    @Override
    public void setPlayed() {
        this.played = true;
    }

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

    @Override
    public int getRound() {
        return round;
    }

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

    @Override
    public void addEvent(IEvent event) {
        if (event == null){
            throw new IllegalArgumentException("Event cannot be null.");
        }
        eventManager.addEvent(event);
    }

    @Override
    public IEvent[] getEvents() {
        return eventManager.getEvents();
    }

    @Override
    public int getEventCount() {
        return eventManager.getEventCount();
    }

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
