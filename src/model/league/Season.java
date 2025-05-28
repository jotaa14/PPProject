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

import java.io.IOException;
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
        boolean isOdd = numberOfCurrentTeams % 2 != 0;
        return (isOdd ? numberOfCurrentTeams : numberOfCurrentTeams - 1) * 2;
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