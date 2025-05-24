package main;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import data.Importer;
import model.event.Event;
import model.event.EventManager;
import model.event.eventTypes.GoalEvent;
import model.league.League;
import model.match.Match;
import model.league.Schedule;
import model.simulation.MatchSimulator;
import model.team.Club;
import model.team.Formation;
import model.team.Team;
import model.league.Season;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static main.Util.listAllClubs;

public class Functions {

    public static League createLeague(Scanner input) {
        String name = null;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------Create League---------------|");
            System.out.println("| Enter the Name of the League: ");
            name = input.next();
            if (name.length() > 0) {
                verifyInput = true;
            } else {
                System.out.println("Select a Valid Name!");
            }
        } while (!verifyInput);

        return new League(name);
    }

    public static Season createSeason(Scanner input) {
        int year = 0;
        String name = null;
        int maxTeams = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------Create Season---------------|");
            System.out.println("| Enter the Name of the Season: ");
            name = input.next();
            if (name.length() > 0) {
                verifyInput = true;
            } else {
                System.out.println("Select a Valid Name!");
            }
        } while (!verifyInput);
        verifyInput = false;
        do {
            System.out.println("| Enter the Year of the Season: ");
            try {
                year = input.nextInt();
                if (year > 2023) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Year!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Year!");
                input.next();
            }
        } while (!verifyInput);
        verifyInput = false;
        do {
            System.out.println("| Enter the Max Teams of the Season: ");
            try {
                maxTeams = input.nextInt();
                if (maxTeams > 0) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Number of Teams!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Number of Teams!");
                input.next();
            }

            return new Season(name, year, maxTeams);
        } while (!verifyInput);
    }

    public static Season loadSeason(Scanner input, League league) {
        Season[] seasons = (Season[]) league.getSeasons();

        if (seasons.length == 0) {
            System.out.println("| No seasons found in this league.");
            return null;
        }

        System.out.println("|--------------Available Seasons-----------|");
        for (int i = 0; i < seasons.length; i++) {
            System.out.println("| " + (i + 1) + ") " + seasons[i].getName() + " (" + seasons[i].getYear() + ")");
        }

        int selectedIndex = -1;
        boolean validInput = false;

        do {
            System.out.println("| Enter the number of the season to load: ");
            try {
                selectedIndex = input.nextInt();
                if (selectedIndex >= 1 && selectedIndex <= seasons.length) {
                    validInput = true;
                } else {
                    System.out.println("Select a valid season number (1-" + seasons.length + ")!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next();
            }
        } while (!validInput);

        return seasons[selectedIndex - 1];
    }

    public static void listSeason(Scanner input, League league) {
        ISeason[] seasons = league.getSeasons();

        if (seasons.length == 0) {
            System.out.println("| No seasons available in this league.     |");
            return;
        }

        System.out.println("|--------------List of Seasons-------------|");
        for (int i = 0; i < seasons.length; i++) {
            System.out.println("| " + (i + 1) + ") " + seasons[i].getName() + " (" + seasons[i].getYear() + ")");
        }
        System.out.println("|==========================================|");
    }

    public static void simulateGame(Scanner input, IClub[] clubs) {
        System.out.println("|--------------Simulate Game---------------|");
        Util.listAllClubs(clubs);
        System.out.println("| Enter the Home Team (CODE): ");
        String homeTeamCode = input.next();
        System.out.println("| Enter the Away Team (CODE): ");
        String awayTeamCode = input.next();

        Club homeClub = null;
        Club awayClub = null;

        for (IClub club : clubs) {
            if (club.getCode().equalsIgnoreCase(homeTeamCode)) {
                homeClub = (Club) club;
            } else if (club.getCode().equalsIgnoreCase(awayTeamCode)) {
                awayClub = (Club) club;
            }
        }

        if (homeClub == null || awayClub == null) {
            System.out.println("| One or both teams not found.");
            return;
        }

        Team homeTeam = (Team) homeClub.getTeam();
        Team awayTeam = (Team) awayClub.getTeam();

        if (homeTeam == null) {
            homeTeam = new Team(homeClub);
            homeClub.setTeam(homeTeam);
        }
        if (awayTeam == null) {
            awayTeam = new Team(awayClub);
            awayClub.setTeam(awayTeam);
        }

        System.out.println("| Current Home Team Formation: " +
                (homeTeam.getFormation() != null ? homeTeam.getFormation().getDisplayName() : "Not Set"));
        System.out.println("| Do you want to change the Home Team Formation? (Y/N): ");
        if (input.next().equalsIgnoreCase("Y")) {
            homeTeam.setFormation(Util.selectFormation(input, homeClub));
        }

        System.out.println("| Current Away Team Formation: " +
                (awayTeam.getFormation() != null ? awayTeam.getFormation().getDisplayName() : "Not Set"));
        System.out.println("| Do you want to change the Away Team Formation? (Y/N): ");
        if (input.next().equalsIgnoreCase("Y")) {
            awayTeam.setFormation(Util.selectFormation(input, awayClub));
        }

        Match match = new Match(homeTeam, awayTeam, 0);
        MatchSimulator simulator = new MatchSimulator();
        simulator.simulate(match);

        System.out.println("| Match Simulation Complete!");
        System.out.println("\nMatch Result:");
        System.out.println(match.getHomeClub().getName() + " " +
                match.getTotalByEvent(GoalEvent.class, match.getHomeClub()) + " - " +
                match.getTotalByEvent(GoalEvent.class, match.getAwayClub()) + " " +
                match.getAwayClub().getName());

        System.out.println("| Match Events:");
        IEvent[] events = match.getEvents();
        for (int i = 0; i < match.getEventCount(); i++) {
            System.out.println(events[i].toString());
        }

        System.out.println("\n\n");
    }


    public static void addClub(Scanner input, Season season) {
    try {
        Importer importer = new Importer();
        IClub[] clubs = importer.importData();

        System.out.println("|--------------Add Club to Season----------|");
        System.out.println("| Available Clubs:                         |");
        listAllClubs(clubs);
        System.out.println("| Enter the Club Code to add: ");
        String clubCode = input.next();

        IClub selectedClub = null;
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                selectedClub = club;
                break;
            }
        }
        if (selectedClub == null) {
            System.out.println("| Club not found.");
            return;
        }
        System.out.println("| Select The Formation " + selectedClub.getName());
        IFormation formation = Util.selectFormation(input, (Club) selectedClub);
        Team team = new Team(selectedClub);

        try {
            team.setFormation(formation);
            ((Club) selectedClub).setTeam(team);
        } catch (IllegalStateException e) {
            System.out.println("| Error: " + e.getMessage());
            return;
        }

        try {
            season.addClub(selectedClub);
            System.out.println("| Club added to the season successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("| Error: " + e.getMessage());
        }
    } catch (IOException e) {
        System.out.println("| Error importing clubs: " + e.getMessage());
    }
}

    public static void removeClub(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("| No clubs available in this season.");
            return;
        }

        System.out.println("|----------Remove Club from Season---------|");
        System.out.println("| Available Clubs:                         |");
        listAllClubs(clubs);
        System.out.println("| Enter the Club Code to remove: ");
        String clubCode = input.next();
        IClub selectedClub = null;
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                selectedClub = club;
                break;
            }
        }
        if (selectedClub == null) {
            System.out.println("| Club not found.");
            return;
        }

        if (season.removeClub(selectedClub)) {
            System.out.println("| Club removed from the season successfully.");
        } else {
            System.out.println("| Failed to remove club from the season.");
        }
    }
    public static void listSeasonStuff(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("| No clubs available in this season.");
            return;
        }

        System.out.println("|------------List Clubs in Season----------|");
        System.out.println("| Available Clubs:                         |");
        listAllClubs(clubs);
        System.out.println("| Enter the Club Code to view details: ");
        String clubCode = input.next();
        IClub selectedClub = null;
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                selectedClub = club;
                break;
            }
        }
        if (selectedClub == null) {
            System.out.println("| Club not found.");
            return;
        }

        System.out.println("| Club Details:                            |");
        System.out.println("| Name: " + selectedClub.getName());
        System.out.println("| Code: " + selectedClub.getCode());
        System.out.println("| Players: " + selectedClub.getPlayerCount());

        if (selectedClub instanceof Club) {
            Team team = (Team) ((Club) selectedClub).getTeam();
            if (team != null && team.getFormation() != null) {
                System.out.println("| Formation: " + team.getFormation().getDisplayName());
            } else {
                System.out.println("| Formation: Not Set");
            }
        } else {
            System.out.println("| Formation: Not Set (Invalid club type)");
        }

        System.out.println("| Do You Want To See The Players Details? (Y/N): ");
        String choice = input.next();
        if (choice.equalsIgnoreCase("Y")) {
            IPlayer[] players = selectedClub.getPlayers();
            if (players.length == 0) {
                System.out.println("| No players available in this club.");
                return;
            }
            System.out.println("| Players:                                 |");
            for (IPlayer player : players) {
                System.out.println("| " + player.toString());
            }
        } else {
            System.out.println("| Exiting Player Details.");
        }
    }

    public static IClub chooseClub(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("| No clubs available in this season.");
            return null;
        }

        System.out.println("|--------------Choose Club-----------------|");
        System.out.println("| Available Clubs:                         |");
        listAllClubs(clubs);
        System.out.println("| Enter the Club Code to choose: ");
        String clubCode = input.next();
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                System.out.println("| You have chosen: " + club.getName());
                return club;
            }
        }

        System.out.println("| Club not found.");
        return null;
    }

    public static void startSeason(Scanner input, Season season) {
        System.out.println("Generating the schedule...");
        season.generateSchedule();

        System.out.println("Automatically simulating the season...");
        while (!season.isSeasonComplete()) {
            season.simulateRound();
        }
        System.out.println("Season completed!");
    }

    public static void generateSchedule(Scanner input, Season season) {
        if (season.getMaxTeams() <= 1) {
            System.out.println("Unable to generate schedule: not enough teams.");
            return;
        }
        try {
            season.generateSchedule();
            System.out.println("Schedule generated successfully!");

            // Print all the matches in the schedule
            System.out.println("Season Schedule:");
            for (IMatch match : season.getMatches()) {
                if (match != null) {
                    /*
                    String home = match.getHomeTeam() instanceof IClub ? ((IClub) match.getHomeTeam()).getName() : "TBD";
                    String away = match.getAwayTeam() instanceof IClub ? ((IClub) match.getAwayTeam()).getName() : "TBD";
                    */
                    String home = match.getHomeClub().getName();
                    String away = match.getAwayClub().getName();
                    int round = match.getRound();
                    System.out.printf("Round %d: %s vs %s%n", round + 1, home, away);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error generating schedule: " + ex.getMessage());
        }
    }

    public static void listStandings(Scanner input, Season season) {
        System.out.println("|--------------Standings-------------------|");

        IStanding[] standings = season.getLeagueStandings();
        if (standings == null) {
            System.out.println("| No standings available for this season.");
            return;
        }

        int validCount = 0;
        for (IStanding standing : standings) {
            if (standing != null) validCount++;
        }
        if (validCount == 0) {
            System.out.println("| No standings available for this season.");
            return;
        }

        IStanding[] validStandings = new IStanding[validCount];
        int idx = 0;
        for (IStanding standing : standings) {
            if (standing != null) validStandings[idx++] = standing;
        }

        // Sort by points, goal difference, then goals scored
        for (int i = 0; i < validStandings.length - 1; i++) {
            for (int j = 0; j < validStandings.length - i - 1; j++) {
                IStanding a = validStandings[j];
                IStanding b = validStandings[j + 1];
                boolean swap = false;

                if (a.getPoints() < b.getPoints()) {
                    swap = true;
                } else if (a.getPoints() == b.getPoints()) {
                    if (a.getGoalDifference() < b.getGoalDifference()) {
                        swap = true;
                    } else if (a.getGoalDifference() == b.getGoalDifference()) {
                        if (a.getGoalScored() < b.getGoalScored()) {
                            swap = true;
                        }
                    }
                }
                if (swap) {
                    IStanding temp = validStandings[j];
                    validStandings[j] = validStandings[j + 1];
                    validStandings[j + 1] = temp;
                }
            }
        }

        String header = String.format(
            "| %-3s | %-6s | %-2s | %-2s | %-2s | %-2s | %-3s | %-3s | %-3s |",
            "Pos", "Club", "MP", "W", "D", "L", "GF", "GA", "Pts"
        );
        String separator = new String(new char[header.length()]).replace('\0', '-');

        System.out.println(header);
        System.out.println(separator);

        for (int i = 0; i < validStandings.length; i++) {
            IStanding standing = validStandings[i];
            ITeam team = standing.getTeam();
            String row = String.format(
                "| %-3d | %-6s | %-2d | %-2d | %-2d | %-2d | %-3d | %-3d | %-3d |",
                (i + 1),
                team.getClub().getCode(),
                standing.getTotalMatches(),
                standing.getWins(),
                standing.getDraws(),
                standing.getLosses(),
                standing.getGoalScored(),
                standing.getGoalsConceded(),
                standing.getPoints()
            );
            System.out.println(row);
        }
        System.out.println(separator);
    }
}