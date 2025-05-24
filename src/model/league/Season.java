package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.event.eventTypes.GoalEvent;
import model.match.Match;
import model.simulation.MatchSimulator;
import model.team.Club;

import java.io.IOException;
import java.util.Arrays;
import static model.event.eventTypes.GoalEvent.*;
import static model.league.Standing.updateStandingsAfterMatch;

public class Season implements ISeason {

    private String name;
    private int year;
    private int currentRound;
    private int maxTeams;
    private IClub[] clubs;
    private IMatch[] matches;
    private ISchedule schedule;
    private IStanding[] standings;
    private MatchSimulatorStrategy simulator = new MatchSimulator();
    private ITeam[] teams;
    private int numberOfCurrentTeams;

    public Season(String name, int year, int maxTeams) {
        this.name = name;
        this.year = year;
        this.maxTeams = maxTeams;
        this.clubs = new IClub[maxTeams];
        this.teams = new ITeam[maxTeams];
        this.numberOfCurrentTeams = 0;
        this.currentRound = 0;
    }

    @Override
    public int getYear() {
        return year;
    }

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

        ITeam[] originalTempTeams = Arrays.copyOf(tempTeams, tempTeams.length);

        int totalMatches = totalRounds * matchesPerRound;
        this.matches = new IMatch[totalMatches];
        this.schedule = new Schedule(totalRounds, matchesPerRound);
        int matchIndex = 0;

        for (int round = 0; round < totalRounds; round++) {
            if (round == totalRounds / 2) {
                tempTeams = Arrays.copyOf(originalTempTeams, originalTempTeams.length);
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


    @Override
    public IMatch[] getMatches() {
        return matches;
    }

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

    @Override
    public void simulateRound() {
        if (isSeasonComplete()) return;
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


    @Override
    public void simulateSeason() {
        while (!isSeasonComplete()) {
            simulateRound();
        }
    }

    @Override
    public int getCurrentRound() {
        return currentRound;
    }

    @Override
    public boolean isSeasonComplete() {
        return currentRound >= getMaxRounds();
    }

    @Override
    public void resetSeason() {
        currentRound = 0;
        if (matches != null) {
            for (int i = 0; i < matches.length; i++) {
                IMatch match = matches[i];
                if (match != null) {
                    matches[i] = new model.match.Match(match.getHomeTeam(), match.getAwayTeam(), match.getRound());
                }
            }
        }
    }


    @Override
    public String displayMatchResult(IMatch match) {
        if (match == null) return "Invalid match";
        String home = match.getHomeClub().getName();
        String away = match.getAwayClub().getName();
        int homeGoals = match.getTotalByEvent(model.event.eventTypes.GoalEvent.class, match.getHomeClub());
        int awayGoals = match.getTotalByEvent(model.event.eventTypes.GoalEvent.class, match.getAwayClub());
        return home + " " + homeGoals + " x " + awayGoals + " " + away;
    }

    @Override
    public void setMatchSimulator(MatchSimulatorStrategy matchSimulatorStrategy) {
        this.simulator = matchSimulatorStrategy;
    }

    @Override
    public IStanding[] getLeagueStandings() {
        if (standings == null) return null;
        IStanding[] copy = new IStanding[standings.length];
        for (int i = 0; i < standings.length; i++) {
            copy[i] = standings[i];
        }
        return copy;
    }


    @Override
    public ISchedule getSchedule() {
        return schedule;
    }

    @Override
    public int getPointsPerWin() {
        return 3;
    }

    @Override
    public int getPointsPerDraw() {
        return 1;
    }

    @Override
    public int getPointsPerLoss() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxTeams() {
        return maxTeams;
    }

    @Override
    public int getMaxRounds() {
        return (maxTeams - 1) * 2;
    }

    @Override
    public int getCurrentMatches() {
        int count = 0;
        for (IMatch match : matches) {
            if (match != null && match.isPlayed()) count++;
        }
        return count;
    }

    @Override
    public int getNumberOfCurrentTeams() {
        return numberOfCurrentTeams;
    }

    @Override
    public IClub[] getCurrentClubs() {
        IClub[] current = new IClub[numberOfCurrentTeams];
        for (int i = 0; i < numberOfCurrentTeams; i++) {
            current[i] = clubs[i];
        }
        return current;
    }

    @Override
    public void exportToJson() throws IOException {
        java.io.FileWriter writer = new java.io.FileWriter("season.json");
        writer.write("{\n");
        writer.write("\"name\": \"" + name + "\",\n");
        writer.write("\"year\": " + year + ",\n");
        writer.write("\"currentRound\": " + currentRound + ",\n");
        writer.write("\"maxTeams\": " + maxTeams + "\n");
        writer.write("}");
        writer.close();
    }

    public String getLeagueName() {
        return name;
    }
}