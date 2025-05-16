package model.match;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.player.Player;

import java.io.FileWriter;
import java.io.IOException;

public class Match implements IMatch {
    private ITeam homeTeam;
    private ITeam awayTeam;
    private boolean played = false;
    private int round;

    private IEvent[] events = new IEvent[10];
    private int eventCount = 0;

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
        for (int i = 0; i < eventCount; i++) {
            if (events[i] == event){
                throw new IllegalStateException("Event already added.");
            }
        }

        if (eventCount == events.length) {
            IEvent[] newEvents = new IEvent[events.length * 2];
            for (int i = 0; i < events.length; i++) {
                newEvents[i] = events[i];
            }
            events = newEvents;
        }
        events[eventCount++] = event;
    }

    @Override
    public IEvent[] getEvents() {
        IEvent[] result = new IEvent[eventCount];
        for (int i = 0; i < eventCount; i++) {
            result[i] = events[i];
        }
        return result;
    }

    @Override
    public int getEventCount() {
        return eventCount;
    }

    @Override
    public int getTotalByEvent(Class eventClass, IClub club) {
        int total = 0;
        for (int i = 0; i < eventCount; i++) {
            if (eventClass.isInstance(events[i])) {
                if (events[i] instanceof IGoalEvent) {
                    IPlayer p = ((IGoalEvent) events[i]).getPlayer();
                    if (((Player)p).getClub().equals(club.getCode())) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    @Override
    public ITeam getWinner() {
        int homeGoals = getTotalByEvent(IGoalEvent.class, getHomeClub());
        int awayGoals = getTotalByEvent(IGoalEvent.class, getAwayClub());

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
