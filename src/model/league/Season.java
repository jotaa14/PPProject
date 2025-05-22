package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.match.Match;

import java.io.IOException;

public class Season implements ISeason {
    private String name;
    private int year;
    private int currentRound;
    private int maxTeams;
    private IClub[] clubs;
    private IMatch[] matches;
    private ISchedule schedule;
    private IStanding[] standings;
    private MatchSimulatorStrategy simulator;

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
        if (club == null) {
            throw new IllegalArgumentException("Club cannot be null");
        }

        if (numberOfCurrentTeams >= maxTeams) {
            throw new IllegalStateException("League is full");
        }

        for (int i = 0; i < numberOfCurrentTeams; i++) {
            if (clubs[i].equals(club)) {
                throw new IllegalArgumentException("Club already exists");
            }
        }

        clubs[numberOfCurrentTeams++] = club;
        return true;
    }

    @Override
    public boolean removeClub(IClub club) {
        if (club == null) {
            throw new IllegalArgumentException("Club cannot be null");
        }

        boolean found = false;
        for (int i = 0; i < numberOfCurrentTeams; i++) {
            if (clubs[i].equals(club)) {
                found = true;
                // Shift to the left
                for (int j = i; j < numberOfCurrentTeams - 1; j++) {
                    clubs[j] = clubs[j + 1];
                }
                clubs[numberOfCurrentTeams - 1] = null;
                numberOfCurrentTeams--;
                break;
            }
        }

        if (!found) {
            throw new IllegalStateException("Club not found in the league");
        }

        return true;
    }

    @Override
    public void generateSchedule() {
        int totalRounds = getMaxRounds();  // número total de rondas (ex: 10 para 6 equipas ida e volta)
        int maxMatchesPerRound = numberOfCurrentTeams / 2;

        this.schedule = new Schedule(totalRounds, maxMatchesPerRound);
        this.matches = new IMatch[numberOfCurrentTeams * (numberOfCurrentTeams - 1)];

        int matchIndex = 0;

        // Ida: equipa i casa contra equipa j fora
        for (int round = 0; round < totalRounds / 2; round++) {
            for (int i = 0; i < numberOfCurrentTeams; i++) {
                for (int j = 0; j < numberOfCurrentTeams; j++) {
                    if (i != j && (i + j + round) % (numberOfCurrentTeams - 1) == 0 && i < j) {
                        IMatch match = new model.match.Match(teams[i], teams[j], round);
                        ((Schedule) schedule).addMatchToRound(round, match);
                        matches[matchIndex++] = match;
                    }
                }
            }
        }

        // Volta: equipa j casa contra equipa i fora
        for (int round = totalRounds / 2; round < totalRounds; round++) {
            for (int i = 0; i < numberOfCurrentTeams; i++) {
                for (int j = 0; j < numberOfCurrentTeams; j++) {
                    if (i != j && (i + j + round) % (numberOfCurrentTeams - 1) == 0 && i < j) {
                        IMatch match = new model.match.Match(teams[j], teams[i], round);
                        ((Schedule) schedule).addMatchToRound(round, match);
                        matches[matchIndex++] = match;
                    }
                }
            }
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
            if (match.getRound() == round) count++;
        }
        IMatch[] roundMatches = new IMatch[count];
        int idx = 0;
        for (IMatch match : matches) {
            if (match.getRound() == round) {
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
            for (IMatch match : matches) {
                if (match != null && match.isPlayed()) {
                    match = new model.match.Match(match.getHomeTeam(), match.getAwayTeam(), match.getRound());
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
        return standings;
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
