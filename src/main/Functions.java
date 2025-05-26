package main;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
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
            System.out.println("╔════════════════════════════════════════════");
            System.out.println("║           🏆  CREATE LEAGUE  🏆            ");
            System.out.println("╚════════════════════════════════════════════");
            System.out.print ("Enter the Name of the League: ");
            name = input.next();
            if (name.length() > 0) {
                verifyInput = true;
            } else {
                System.out.println("⚠️  Select a Valid Name!");
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
            System.out.println("╔═══════════════════════════════════════════");
            System.out.println("║           📅  CREATE SEASON  📅            ");
            System.out.println("╚═══════════════════════════════════════════");
            System.out.print ("Enter the Name of the Season: ");
            name = input.next();
            if (name.length() > 0) {
                verifyInput = true;
            } else {
                System.out.println("⚠️  Select a Valid Name!");
            }
        } while (!verifyInput);
        verifyInput = false;
        do {
            System.out.print ("Enter the Year of the Season: ");
            try {
                year = input.nextInt();
                if (year > 2023) {
                    verifyInput = true;
                } else {
                    System.out.println("⚠️  Select a Valid Year!");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️  Select a Valid Year!");
                input.next();
            }
        } while (!verifyInput);
        verifyInput = false;
        do {
            System.out.print ("Enter the Max Teams of the Season: ");
            try {
                maxTeams = input.nextInt();
                if (maxTeams > 0) {
                    verifyInput = true;
                } else {
                    System.out.println("⚠️  Select a Valid Number of Teams!");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️  Select a Valid Number of Teams!");
                input.next();
            }
        } while (!verifyInput);

        return new Season(name, year, maxTeams);
    }

    public static Season loadSeason(Scanner input, League league) {
        Season[] seasons = (Season[]) league.getSeasons();

        if (seasons.length == 0) {
            System.out.println("╔═══════════════════════════════════════════");
            System.out.println("║      ⚠️  No seasons found in this league.  ");
            System.out.println("╚════════════════════════════════════════════");
            return null;
        }

        System.out.println("╔═══════════════════════════════════════════");
        System.out.println("║         📜  AVAILABLE SEASONS  📜          ");
        System.out.println("╚═══════════════════════════════════════════");
        for (int i = 0; i < seasons.length; i++) {
            System.out.println("  " + (i + 1) + ". " + seasons[i].getName() + " (" + seasons[i].getYear() + ")");
        }

        int selectedIndex = -1;
        boolean validInput = false;

        do {
            System.out.print("Enter the number of the season to load: ");
            try {
                selectedIndex = input.nextInt();
                if (selectedIndex >= 1 && selectedIndex <= seasons.length) {
                    validInput = true;
                } else {
                    System.out.println("⚠️  Select a valid season number (1-" + seasons.length + ")!");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️  Invalid input. Please enter a number.");
                input.next();
            }
        } while (!validInput);

        return seasons[selectedIndex - 1];
    }

    public static void listSeason(Scanner input, League league) {
        ISeason[] seasons = league.getSeasons();

        if (seasons.length == 0) {
            System.out.println("╔═══════════════════════════════════════════");
            System.out.println("║   ⚠️  No seasons available in this league.    ");
            System.out.println("╚═══════════════════════════════════════════");
            return;
        }

        System.out.println("╔═══════════════════════════════════════════");
        System.out.println("║           📅  LIST OF SEASONS  📅           ");
        System.out.println("╚═══════════════════════════════════════════");
        for (int i = 0; i < seasons.length; i++) {
            System.out.println("  " + (i + 1) + ". " + seasons[i].getName() + " (" + seasons[i].getYear() + ")");
        }
        System.out.println("══════════════════════════════════════════════");
    }

    public static void simulateGame(Scanner input, IClub[] clubs) {
        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║         ⚽  SIMULATE GAME  ⚽               ");
        System.out.println("╚════════════════════════════════════════════");
        Util.listAllClubs(clubs);
        System.out.print("🏠 Enter the Home Team (CODE): ");
        String homeTeamCode = input.next();
        System.out.print("🛫 Enter the Away Team (CODE): ");
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
            System.out.println("⚠️  One or both teams not found.");
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

        System.out.println("🏠 Current Home Team Formation: " +
                (homeTeam.getFormation() != null ? homeTeam.getFormation().getDisplayName() : "Not Set"));
        System.out.print("🏠 Change the Home Team Formation? (Y/N): ");
        if (input.next().equalsIgnoreCase("Y")) {
            homeTeam.setFormation(Util.selectFormation(input, homeClub));
        }

        System.out.println("🛫 Current Away Team Formation: " +
                (awayTeam.getFormation() != null ? awayTeam.getFormation().getDisplayName() : "Not Set"));
        System.out.print("🛫 Change the Away Team Formation? (Y/N): ");
        if (input.next().equalsIgnoreCase("Y")) {
            awayTeam.setFormation(Util.selectFormation(input, awayClub));
        }

        Match match = new Match(homeTeam, awayTeam, 0);
        MatchSimulator simulator = new MatchSimulator();
        simulator.simulate(match);

        System.out.println("\n🎲 Match Simulation Complete!");
        System.out.println("Match Result:");
        System.out.println(match.getHomeClub().getName() + " " +
                match.getTotalByEvent(GoalEvent.class, match.getHomeClub()) + " - " +
                match.getTotalByEvent(GoalEvent.class, match.getAwayClub()) + " " +
                match.getAwayClub().getName());

        System.out.println("Match Events:");
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

            System.out.println("╔════════════════════════════════════════════");
            System.out.println("║         ➕  ADD CLUB TO SEASON  ➕          ");
            System.out.println("╚════════════════════════════════════════════");
            System.out.println("Available Clubs:");
            listAllClubs(clubs);
            System.out.print("Enter the Club Code to add: ");
            String clubCode = input.next();

            IClub selectedClub = null;
            for (IClub club : clubs) {
                if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                    selectedClub = club;
                    break;
                }
            }
            if (selectedClub == null) {
                System.out.println("⚠️  Club not found.");
                return;
            }
            System.out.println("Select the Formation for " + selectedClub.getName() + ":");
            IFormation formation = Util.selectFormation(input, (Club) selectedClub);
            Team team = new Team(selectedClub);

            try {
                team.setFormation(formation);
                ((Club) selectedClub).setTeam(team);
            } catch (IllegalStateException e) {
                System.out.println("⚠️  Error: " + e.getMessage());
                return;
            }

            try {
                season.addClub(selectedClub);
                System.out.println("✅ Club added to the season successfully.");
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("⚠️  Error: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("⚠️  Error importing clubs: " + e.getMessage());
        }
    }

    public static void removeClub(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("⚠️  No clubs available in this season.");
            return;
        }

        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║       ➖  REMOVE CLUB FROM SEASON  ➖       ");
        System.out.println("╚════════════════════════════════════════════");
        System.out.println("Available Clubs:");
        listAllClubs(clubs);
        System.out.print("Enter the Club Code to remove: ");
        String clubCode = input.next();
        IClub selectedClub = null;
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                selectedClub = club;
                break;
            }
        }
        if (selectedClub == null) {
            System.out.println("⚠️  Club not found.");
            return;
        }

        if (season.removeClub(selectedClub)) {
            System.out.println("✅ Club removed from the season successfully.");
        } else {
            System.out.println("⚠️  Failed to remove club from the season.");
        }
    }

    public static void listSeasonStuff(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("⚠️  No clubs available in this season.");
            return;
        }

        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║        📋  LIST CLUBS IN SEASON  📋        ");
        System.out.println("╚════════════════════════════════════════════");
        System.out.println("Available Clubs:");
        listAllClubs(clubs);
        System.out.print("Enter the Club Code to view details: ");
        String clubCode = input.next();
        IClub selectedClub = null;
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                selectedClub = club;
                break;
            }
        }
        if (selectedClub == null) {
            System.out.println("⚠️  Club not found.");
            return;
        }

        System.out.println("\nClub Details:");
        System.out.println("Name: " + selectedClub.getName());
        System.out.println("Code: " + selectedClub.getCode());
        System.out.println("Players: " + selectedClub.getPlayerCount());

        if (selectedClub instanceof Club) {
            Team team = (Team) ((Club) selectedClub).getTeam();
            if (team != null && team.getFormation() != null) {
                System.out.println("Formation: " + team.getFormation().getDisplayName());
            } else {
                System.out.println("Formation: Not Set");
            }
        } else {
            System.out.println("Formation: Not Set (Invalid club type)");
        }

        System.out.print("Do You Want To See The Players Details? (Y/N): ");
        String choice = input.next();
        if (choice.equalsIgnoreCase("Y")) {
            IPlayer[] players = selectedClub.getPlayers();
            if (players.length == 0) {
                System.out.println("No players available in this club.");
                return;
            }
            System.out.println("Players:");
            for (IPlayer player : players) {
                System.out.println("  " + player.toString());
            }
        } else {
            System.out.println("Exiting Player Details.");
        }
    }

    public static void listClubInformation(Scanner input, IClub club) {
        if (club == null) {
            System.out.println("⚠️  No club selected.");
            return;
        }
        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║          🏟️  CLUB INFORMATION  🏟️          ");
        System.out.println("╚════════════════════════════════════════════");
        System.out.println("Club Name: " + club.getName());
        System.out.println("Club Code: " + club.getCode());
        System.out.println("Players Count: " + club.getPlayerCount());

        if (club instanceof Club) {
            Team team = (Team) ((Club) club).getTeam();
            if (team != null && team.getFormation() != null) {
                System.out.println("Formation: " + team.getFormation().getDisplayName());
            } else {
                System.out.println("Formation: Not Set");
            }
        } else {
            System.out.println("Formation: Not Set (Invalid club type)");
        }
        System.out.print("Do You Want To See The Players Details? (Y/N): ");
        String choice = input.next();
        if (!choice.equalsIgnoreCase("Y")) {
            System.out.println("Exiting Player Details.");
            return;
        }
        System.out.println("Players in " + club.getName() + ":");
        IPlayer[] players = club.getPlayers();
        if (players.length == 0) {
            System.out.println("No players available in this club.");
            return;
        }

        System.out.println("Players:");
        for (IPlayer player : players) {
            System.out.println("  " + player.toString());
        }
    }

    public static IClub chooseClub(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("⚠️  No clubs available in this season.");
            return null;
        }

        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║            🏟️  CHOOSE CLUB  🏟️             ");
        System.out.println("╚════════════════════════════════════════════");
        System.out.println("Available Clubs:");
        listAllClubs(clubs);
        System.out.print("Enter the Club Code to choose: ");
        String clubCode = input.next();
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                return club;
            }
        }

        System.out.println("⚠️  Club not found.");
        return null;
    }

    public static void startSeason(Scanner input, Season season) {
        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║         🚩  STARTING SEASON  🚩            ");
        System.out.println("╚════════════════════════════════════════════");
        System.out.println("Generating the schedule...");
        generateSchedule(input, season);

        System.out.println("Automatically simulating the season...");
        while (!season.isSeasonComplete()) {
            season.simulateRound();
        }
        System.out.println("✅ Season completed!");
    }

    public static void simulateRound(Scanner input, Season season, IClub managedClub) {
        if (season.getMaxTeams() <= 1) {
            System.out.println("⚠️  Unable to simulate round: not enough teams.");
            return;
        }
        try {
            season.simulateRound();
            System.out.println("✅ Round simulated successfully!");
            System.out.println("Current Standings:");
            listStandings(input, season);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("⚠️  Error simulating round: " + ex.getMessage());
        }
    }

    public static void generateSchedule(Scanner input, Season season) {
        if (season.getMaxTeams() <= 1) {
            System.out.println("⚠️  Unable to generate schedule: not enough teams.");
            return;
        }
        try {
            season.generateSchedule();
            System.out.println("╔════════════════════════════════════════════════════════════════════");
            System.out.println("║              📅  SEASON SCHEDULE  📅                               ");
            System.out.println("╚════════════════════════════════════════════════════════════════════");
            int lastRound = -1;
            for (IMatch match : season.getMatches()) {
                if (match != null) {
                    String home = match.getHomeClub().getName();
                    String away = match.getAwayClub().getName();
                    int round = match.getRound();
                    if (round != lastRound) {
                        System.out.printf("\n🌟 𝗝𝗼𝗿𝗻𝗮𝗱𝗮 %02d 🌟\n", round + 1);
                        System.out.println("-------------------------------------------------------------------");
                        lastRound = round;
                    }
                    System.out.printf("🏠 %-25s  vs  %-25s 🛫\n", home, away);
                }
            }
            System.out.println("\n===================================================================");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("⚠️  Error generating schedule: " + ex.getMessage());
        }
    }

    public static void selectFormation(Scanner input, Season season, IClub managedClub) {
        if (managedClub == null) {
            System.out.println("⚠️  No club selected to manage.");
            return;
        }

        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║        🧩  SELECT FORMATION  🧩             ");
        System.out.println("╚════════════════════════════════════════════");
        System.out.println("Managing: " + managedClub.getName());
        IFormation formation = Util.selectFormation(input, (Club) managedClub);
        if (formation != null) {
            Team team = new Team(managedClub);
            team.setFormation(formation);
            ((Club) managedClub).setTeam(team);
            System.out.println("✅ Formation set successfully for " + managedClub.getName());
        } else {
            System.out.println("⚠️  Invalid formation selection.");
        }
    }

    public static void selectPlayerByPosition(Scanner input, Season season, IClub managedClub) {
        if (managedClub == null) {
            System.out.println("⚠️  No club selected to manage.");
            return;
        }

        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║   🧑‍💼  SELECT PLAYER BY POSITION  🧑‍💼         ");
        System.out.println("╚════════════════════════════════════════════");
        System.out.println("Managing: " + managedClub.getName());

        IPlayer[] players = managedClub.getPlayers();
        if (players.length == 0) {
            System.out.println("⚠️  No players available in this club.");
            return;
        }

        System.out.println("Available Players:");
        for (IPlayer player : players) {
            System.out.println("  " + player.getName() + " - Position: " + player.getPosition());
        }

        System.out.print("Enter the position to filter by (e.g., Forward, Midfielder, Defender, Goalkeeper): ");
        String position = input.next();

    }

    public static void listStandings(Scanner input, Season season) {
        System.out.println("╔════════════════════════════════════════════");
        System.out.println("║             📊  STANDINGS  📊              ");
        System.out.println("╚════════════════════════════════════════════");

        IStanding[] standings = season.getLeagueStandings();
        if (standings == null) {
            System.out.println("⚠️  No standings available for this season.");
            return;
        }

        int validCount = 0;
        for (IStanding standing : standings) {
            if (standing != null) validCount++;
        }
        if (validCount == 0) {
            System.out.println("⚠️  No standings available for this season.");
            return;
        }

        IStanding[] validStandings = new IStanding[validCount];
        int idx = 0;
        for (IStanding standing : standings) {
            if (standing != null) validStandings[idx++] = standing;
        }

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
