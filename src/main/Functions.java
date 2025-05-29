package main;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import data.Importer;
import model.event.eventTypes.GoalEvent;
import model.league.League;
import model.match.Match;
import model.player.PlayerPosition;
import model.player.PlayerPositionType;
import model.simulation.MatchSimulator;
import model.team.Club;
import model.team.Formation;
import model.team.Team;
import model.league.Season;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static main.Util.*;

/**
 * Provides core functional operations for the Football Manager application.
 * <p>
 * This class contains static utility methods for creating leagues and seasons,
 * loading and listing seasons, simulating games and rounds, managing clubs,
 * handling player selection, and displaying statistics and standings.
 * It serves as the main logic layer that connects user interface menus
 * to the underlying data models and simulation logic.
 * </p>
 *
 * <h2>Main Functionalities:</h2>
 * <ul>
 *   <li>League and season creation and management</li>
 *   <li>Club import, addition, removal, and listing</li>
 *   <li>Match simulation and schedule generation</li>
 *   <li>Player selection and statistics display</li>
 *   <li>Standing and events visualization</li>
 * </ul>
 *
 * <b>Note:</b> All methods are static and intended to be called from menu handlers.
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Functions {

    /**
     * Creates a new league by prompting the user for a league name.
     * @param input Scanner for user input
     * @return New League instance
     */
    public static League createLeague(Scanner input) {
        String name;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------------------------------------|");
            System.out.println("|             CREATE LEAGUE                  |");
            System.out.println("|--------------------------------------------|");
            System.out.print("Enter the Name of the League: ");
            name = input.next();
            if (name.length() > 0) {
                verifyInput = true;
            } else {
                System.out.println("Select a Valid Name!");
            }
        } while (!verifyInput);

        return new League(name);
    }

    /**
     * Creates a new season by prompting the user for season name, year, and max teams.
     * @param input Scanner for user input
     * @return New Season instance
     */
    public static Season createSeason(Scanner input) {
        int year = 0;
        String name;
        int maxTeams = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------------------------------------|");
            System.out.println("|             CREATE SEASON                  |");
            System.out.println("|--------------------------------------------|");
            System.out.print("Enter the Name of the Season: ");
            name = input.next();
            if (name.length() > 0) {
                verifyInput = true;
            } else {
                System.out.println("Select a Valid Name!");
            }
        } while (!verifyInput);
        verifyInput = false;
        do {
            System.out.print("Enter the Year of the Season: ");
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
            System.out.print("Enter the Max Teams of the Season: ");
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
        } while (!verifyInput);

        return new Season(name, year, maxTeams);
    }

    /**
     * Loads a season from a league by prompting the user for the year.
     * @param input Scanner for user input
     * @param league The league to load the season from
     * @return Loaded ISeason instance, or null if not found
     */
    public static ISeason loadSeason(Scanner input, League league) {
        System.out.println("|--------------------------------------------|");
        System.out.println("|             LOAD SEASON                    |");
        System.out.println("|--------------------------------------------|");
        listSeason(input, league);
        System.out.print("Enter the Year of the Season to Load: ");
        int year = input.nextInt();

        ISeason[] seasons = league.getSeasons();
        for (ISeason season : seasons) {
            if (season.getYear() == year) {
                return season;
            }
        }
        System.out.println("Season for year " + year + " not found in this league.");
        return null;
    }

    /**
     * Lists all seasons available in the given league.
     * @param input Scanner for user input
     * @param league The league whose seasons to list
     */
    public static void listSeason(Scanner input, League league) {
        ISeason[] seasons = league.getSeasons();

        if (seasons.length == 0) {
            System.out.println("|--------------------------------------------|");
            System.out.println("|   No seasons available in this league.     |");
            System.out.println("|--------------------------------------------|");
            return;
        }
        System.out.println("|--------------------------------------------|");
        System.out.println("|             LIST OF SEASONS                |");
        System.out.println("|--------------------------------------------|");
        for (int i = 0; i < seasons.length; i++) {
            System.out.println("  " + (i + 1) + ". " + seasons[i].getName() + " (" + seasons[i].getYear() + ")");
        }
        System.out.println("--------------------------------------------");
    }

    /**
     * Simulates a match between two clubs selected by the user.
     * Prompts for home and away team codes, allows formation selection, and displays match events.
     * @param input Scanner for user input
     * @param clubs Array of available clubs
     */
    public static void simulateGame(Scanner input, IClub[] clubs) {
        System.out.println("|--------------------------------------------|");
        System.out.println("|             SIMULATE GAME                  |");
        System.out.println("|--------------------------------------------|");
        Util.listAllClubs(clubs);
        System.out.print("Enter the Home Team (CODE): ");
        String homeTeamCode = input.next();
        System.out.print("Enter the Away Team (CODE): ");
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
            System.out.println("One or both teams not found.");
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

        System.out.println("Current Home Team Formation: " +
                (homeTeam.getFormation() != null ? homeTeam.getFormation().getDisplayName() : "Not Set"));
        System.out.print("Change the Home Team Formation? (Y/N): ");
        if (input.next().equalsIgnoreCase("Y")) {
            homeTeam.setFormation(Util.selectFormation(input, homeClub));
        }

        System.out.println("Current Away Team Formation: " +
                (awayTeam.getFormation() != null ? awayTeam.getFormation().getDisplayName() : "Not Set"));
        System.out.print("Change the Away Team Formation? (Y/N): ");
        if (input.next().equalsIgnoreCase("Y")) {
            awayTeam.setFormation(Util.selectFormation(input, awayClub));
        }

        Match match = new Match(homeTeam, awayTeam, 0);
        MatchSimulator simulator = new MatchSimulator();
        simulator.simulate(match);

        System.out.println("\nMatch Simulation Complete!");
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

    /**
     * Adds a club to the current season, allowing the user to select from imported clubs and set a formation.
     * @param input Scanner for user input
     * @param season The season to add the club to
     */
    public static void addClub(Scanner input, Season season) {
        try {
            Importer importer = new Importer();
            IClub[] clubs = importer.importData();

            System.out.println("|--------------------------------------------|");
            System.out.println("|           ADD CLUB TO SEASON               |");
            System.out.println("|--------------------------------------------|");
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
                System.out.println("Club not found.");
                return;
            }
            System.out.println("Select the Formation for " + selectedClub.getName() + ":");
            IFormation formation = Util.selectFormation(input, (Club) selectedClub);
            Team team = new Team(selectedClub);

            try {
                team.setFormation(formation);
                ((Club) selectedClub).setTeam(team);
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }

            try {
                season.addClub(selectedClub);
                System.out.println("Club added to the season successfully.");
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error importing clubs: " + e.getMessage());
        }
    }

    /**
     * Imports and adds all available clubs to the season, assigning default formations.
     * @param input Scanner for user input
     * @param season The season to add clubs to
     */
    public static void importAllClubsToSeason(Scanner input, Season season) {
        try {
            Importer importer = new Importer();
            IClub[] clubs = importer.importData();

            for (IClub club : clubs) {
                if (club == null) continue;

                IFormation defaultFormation = Util.getDefaultFormation((Club) club);
                Team team = new Team(club);

                try {
                    team.setFormation(defaultFormation);
                    ((Club) club).setTeam(team);
                } catch (IllegalStateException e) {
                    System.out.println("Error setting formation for " + club.getName() + ": " + e.getMessage());
                    continue;
                }
                team.setAutomaticTeam(club.getPlayers(), (Formation) defaultFormation);

                try {
                    season.addClub(club);
                    System.out.println("Club " + club.getName() + " added to the season successfully.");
                } catch (IllegalStateException | IllegalArgumentException e) {
                    System.out.println("Error adding " + club.getName() + ": " + e.getMessage());
                }
            }
            System.out.println("Automatic club import completed.");
        } catch (IOException e) {
            System.out.println("Error importing clubs: " + e.getMessage());
        }
    }

    /**
     * Removes a club from the season, prompting the user to select which club to remove.
     * @param input Scanner for user input
     * @param season The season to remove the club from
     */
    public static void removeClub(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("No clubs available in this season.");
            return;
        }

        System.out.println("|--------------------------------------------|");
        System.out.println("|         REMOVE CLUB FROM SEASON            |");
        System.out.println("|--------------------------------------------|");
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
            System.out.println("Club not found.");
            return;
        }

        if (season.removeClub(selectedClub)) {
            System.out.println("Club removed from the season successfully.");
        } else {
            System.out.println("Failed to remove club from the season.");
        }
    }

    /**
     * Removes all clubs from the season after user confirmation.
     * @param input Scanner for user input
     * @param season The season to clear clubs from
     */
    public static void removeAllClubsFromSeason(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("No clubs available in this season.");
            return;
        }

        System.out.println("|--------------------------------------------|");
        System.out.println("|         REMOVE ALL CLUBS FROM SEASON       |");
        System.out.println("|--------------------------------------------|");
        System.out.print("Are you sure you want to remove ALL clubs from the season? (y/n): ");
        String confirmation = input.next();

        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("Operation cancelled.");
            return;
        }

        boolean allRemoved = true;
        for (IClub club : clubs) {
            if (club != null) {
                boolean removed = season.removeClub(club);
                if (!removed) {
                    System.out.println("Failed to remove club: " + club.getName());
                    allRemoved = false;
                }
            }
        }

        if (allRemoved) {
            System.out.println("All clubs removed from the season successfully.");
        } else {
            System.out.println("Some clubs could not be removed from the season.");
        }
    }

    /**
     * Lists clubs in the season and displays details for a selected club, including players and formation.
     * @param input Scanner for user input
     * @param season The season to list clubs from
     */
    public static void listSeasonStuff(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("No clubs available in this season.");
            return;
        }

        System.out.println("|--------------------------------------------|");
        System.out.println("|           LIST CLUBS IN SEASON             |");
        System.out.println("|--------------------------------------------|");
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
            System.out.println("Club not found.");
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

        if (selectedClub instanceof Club) {
            Team team = (Team) ((Club) selectedClub).getTeam();
            if (team != null && team.getFormation() != null) {
                System.out.println("Formation: " + team.getFormation().getDisplayName());

                team.printStartingElevenByPosition((Formation) team.getFormation());
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

    /**
     * Lists player statistics for a selected player from a selected club in the season.
     * @param input Scanner for user input
     * @param season The season to query
     */
    public static void listPlayersStandings(Scanner input, Season season) {
        System.out.println("Choose the player club that you want to check stats:");
        for (IClub club : season.getCurrentClubs()) {
            if (club != null) {
                System.out.println(club.getName() + " (" + club.getCode() + ")");
            }
        }

        IClub selectedClub = null;
        while (selectedClub == null) {
            System.out.print("Enter the club code: ");
            String clubCode = input.nextLine().trim();

            for (IClub club : season.getCurrentClubs()) {
                if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                    selectedClub = club;
                    break;
                }
            }
            if (selectedClub == null) {
                System.out.println("Club not found with code: " + clubCode);
            }
        }

        System.out.println("Players in " + selectedClub.getName() + ":");
        for (IPlayer player : selectedClub.getPlayers()) {
            if (player != null) {
                System.out.println(player.getName() + " | Number: " + player.getNumber());
            }
        }

        IPlayer selectedPlayer = null;
        while (selectedPlayer == null) {
            System.out.print("Enter the number of the player you want to get stats for: ");
            String playerInput = input.nextLine().trim();
            int playerNumber;
            try {
                playerNumber = Integer.parseInt(playerInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid player number.");
                continue;
            }

            for (IPlayer player : selectedClub.getPlayers()) {
                if (player != null && player.getNumber() == playerNumber) {
                    selectedPlayer = player;
                    break;
                }
            }
            if (selectedPlayer == null) {
                System.out.println("Player not found with number: " + playerInput);
            }
        }
        season.getPlayerStatistics(selectedPlayer);
    }

    /**
     * Lists player statistics for a selected player from the managed club.
     * @param input Scanner for user input
     * @param season The season to query
     * @param selectedClub The club being managed
     */
    public static void listPlayersStandingsForMangedClub(Scanner input, Season season, IClub selectedClub) {
        if (selectedClub == null) {
            System.out.println("No club selected to manage.");
            return;
        }

        System.out.println("|--------------------------------------------|");
        System.out.println("|           PLAYERS STANDINGS                |");
        System.out.println("|--------------------------------------------|");
        System.out.println("Players in " + selectedClub.getName() + ":");

        IPlayer[] players = selectedClub.getPlayers();
        if (players.length == 0) {
            System.out.println("No players available in this club.");
            return;
        }

        for (IPlayer player : players) {
            if (player != null) {
                System.out.println(player.getName() + " | Number: " + player.getNumber());
            }
        }

        System.out.print("Enter the number of the player you want to get stats for: ");
        int playerNumber = input.nextInt();
        IPlayer selectedPlayer = null;

        for (IPlayer player : players) {
            if (player != null && player.getNumber() == playerNumber) {
                selectedPlayer = player;
                break;
            }
        }

        if (selectedPlayer == null) {
            System.out.println("Player not found with number: " + playerNumber);
            return;
        }

        season.getPlayerStatistics(selectedPlayer);
    }

    /**
     * Lists detailed information for a club, including formation and player details.
     * @param input Scanner for user input
     * @param club The club to display information for
     */
    public static void listClubInformation(Scanner input, IClub club) {
        if (club == null) {
            System.out.println("No club selected.");
            return;
        }
        System.out.println("|--------------------------------------------|");
        System.out.println("|             CLUB INFORMATION               |");
        System.out.println("|--------------------------------------------|");
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

    /**
     * Prompts the user to select a club from the current season.
     * @param input Scanner for user input
     * @param season The season to select from
     * @return The selected club, or null if not found
     */
    public static IClub chooseClub(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("No clubs available in this season.");
            return null;
        }

        System.out.println("|--------------------------------------------|");
        System.out.println("|                CHOOSE CLUB                 |");
        System.out.println("|--------------------------------------------|");
        System.out.println("Available Clubs:");
        listAllClubs(clubs);
        System.out.print("Enter the Club Code to choose: ");
        String clubCode = input.next();
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                return club;
            }
        }

        System.out.println("Club not found.");
        return null;
    }

    /**
     * Starts the season by generating the schedule and simulating all rounds.
     * @param input Scanner for user input
     * @param season The season to start
     */
    public static void startSeason(Scanner input, Season season) {
        System.out.println("|--------------------------------------------|");
        System.out.println("|              STARTING SEASON               |");
        System.out.println("|--------------------------------------------|");
        System.out.println("Generating the schedule...");
        generateSchedule(input, season);

        System.out.println("Automatically simulating the season...");
        while (!season.isSeasonComplete()) {
            season.simulateRound();
        }
        System.out.println("Season completed!");
    }

    /**
     * Simulates a round in the season for the managed club and displays updated standings.
     * @param input Scanner for user input
     * @param season The season to simulate
     * @param managedClub The club being managed
     */
    public static void simulateRound(Scanner input, Season season, IClub managedClub) {
        if (season.getMaxTeams() <= 1) {
            System.out.println("Unable to simulate round: not enough teams.");
            return;
        }
        try {
            season.simulateRound();
            System.out.println("Round simulated successfully!");
            System.out.println("Current Standings:");
            listStandings(input, season);
        } catch (Exception ex) {
            System.out.println("Error simulating round: " + ex.getMessage());
        }
    }

    /**
     * Generates and displays the season schedule.
     * @param input Scanner for user input
     * @param season The season to generate the schedule for
     */
    public static void generateSchedule(Scanner input, Season season) {
        if (season.getMaxTeams() <= 1) {
            System.out.println("Unable to generate schedule: not enough teams.");
            return;
        }
        try {
            season.generateSchedule();
            System.out.println("|============================================|");
            System.out.println("|              SEASON SCHEDULE               |");
            System.out.println("|============================================|");
            int lastRound = -1;
            for (IMatch match : season.getMatches()) {
                if (match != null) {
                    String home = match.getHomeClub().getName();
                    String away = match.getAwayClub().getName();
                    int round = match.getRound();
                    if (round != lastRound) {
                        System.out.printf("\nROUND %02d\n", round + 1);
                        System.out.println("--------------------------------------------");
                        lastRound = round;
                    }
                    System.out.printf("%-25s  vs  %-25s\n", home, away);
                }
            }
            System.out.println("\n============================================");
        } catch (Exception ex) {
            System.out.println("Error generating schedule: " + ex.getMessage());
        }
    }

    /**
     * Allows the user to select a formation for the managed club.
     * @param input Scanner for user input
     * @param season The season context
     * @param managedClub The club being managed
     * @return The selected formation
     */
    public static IFormation selectFormation(Scanner input, Season season, IClub managedClub) {
        if (managedClub == null) {
            System.out.println("|--------------------------------------------|");
            System.out.println("No club selected to manage.");
            System.out.println("|--------------------------------------------|");
            return null;
        }

        System.out.println("|--------------------------------------------|");
        System.out.println("|             SELECT FORMATION               |");
        System.out.println("|--------------------------------------------|");
        System.out.println("Managing: " + managedClub.getName());

        IFormation formation = Util.selectFormation(input, (Club) managedClub);
        if (formation != null) {
            Team team = new Team(managedClub);
            team.setFormation(formation);
            ((Club) managedClub).setTeam(team);
            System.out.println("Formation set successfully for " + managedClub.getName() + ".");
        } else {
            System.out.println("Invalid formation selection.");
        }
        System.out.println("--------------------------------------------");
        return formation;
    }

    /**
     * Allows the user to select the starting eleven by position for a club and formation.
     * @param input Scanner for user input
     * @param managedClub The club being managed
     * @param formation The formation being used
     */
    public static void selectPlayerByPosition(Scanner input, IClub managedClub, IFormation formation) {
        if (managedClub == null) {
            System.out.println("|--------------------------------------------|");
            System.out.println("|No club selected to manage.                 |");
            System.out.println("|--------------------------------------------|");
            return;
        }

        System.out.println("|--------------------------------------------|");
        System.out.println("|      SELECT STARTING XI BY FORMATION       |");
        System.out.println("|--------------------------------------------|");
        System.out.println("Managing: " + managedClub.getName());
        System.out.println("Formation: " + formation.getDisplayName());

        IPlayer[] players = managedClub.getPlayers();
        if (players.length == 0) {
            System.out.println("No players available in this club.");
            return;
        }
        Formation concreteFormation = (Formation) formation;

        int numGoalkeepers = 1;
        int numDefenders = concreteFormation.getDefenders();
        int numMidfielders = concreteFormation.getMidfielders();
        int numForwards = concreteFormation.getForwards();

        IPlayer[] selectedGoalkeepers = new IPlayer[numGoalkeepers];
        IPlayer[] selectedDefenders = new IPlayer[numDefenders];
        IPlayer[] selectedMidfielders = new IPlayer[numMidfielders];
        IPlayer[] selectedForwards = new IPlayer[numForwards];

        selectPlayersForPosition(input, players, PlayerPositionType.GOALKEEPER, numGoalkeepers, selectedGoalkeepers);
        selectPlayersForPosition(input, players, PlayerPositionType.DEFENDER, numDefenders, selectedDefenders);
        selectPlayersForPosition(input, players, PlayerPositionType.MIDFIELDER, numMidfielders, selectedMidfielders);
        selectPlayersForPosition(input, players, PlayerPositionType.FORWARD, numForwards, selectedForwards);

        System.out.println("\n|--------------------------------------------|");
        System.out.println("|           YOUR STARTING XI                 |");
        System.out.println("|--------------------------------------------|");
        printSelectedPlayers("GOALKEEPER", selectedGoalkeepers);
        printSelectedPlayers("DEFENDER", selectedDefenders);
        printSelectedPlayers("MIDFIELDER", selectedMidfielders);
        printSelectedPlayers("FORWARD", selectedForwards);
        System.out.println("--------------------------------------------");
    }

    // Helper methods for player selection and printing (not public API)
    private static void selectPlayersForPosition(Scanner input, IPlayer[] players, PlayerPositionType posType, int numRequired, IPlayer[] selected) {
        int count = 0;
        for (IPlayer player : players) {
            PlayerPositionType playerType = ((PlayerPosition) player.getPosition()).getType();
            if (playerType == posType) {
                count++;
            }
        }

        if (count < numRequired) {
            System.out.println("Not enough players available for position: " + posType.name() +
                    " (required: " + numRequired + ", found: " + count + ")");
            for (int i = 0; i < numRequired; i++) selected[i] = null;
            return;
        }

        IPlayer[] filtered = new IPlayer[count];
        int idx = 0;
        for (IPlayer player : players) {
            PlayerPositionType playerType = ((PlayerPosition) player.getPosition()).getType();
            if (playerType == posType) {
                filtered[idx++] = player;
            }
        }

        boolean[] alreadySelected = new boolean[filtered.length];

        for (int i = 0; i < numRequired; i++) {
            System.out.println("\nSelect " + posType.name() + " #" + (i + 1) + ":");
            for (int j = 0; j < filtered.length; j++) {
                if (!alreadySelected[j]) {
                    System.out.println("  " + (j + 1) + ". " + filtered[j].getName());
                }
            }

            int choice = 0;
            boolean valid = false;
            do {
                System.out.print("Enter the number for your choice: ");
                try {
                    choice = Integer.parseInt(input.nextLine());
                    if (choice >= 1 && choice <= filtered.length && !alreadySelected[choice - 1]) {
                        valid = true;
                    } else {
                        System.out.println("Invalid choice or player already selected. Try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Try again.");
                }
            } while (!valid);

            selected[i] = filtered[choice - 1];
            alreadySelected[choice - 1] = true;
            System.out.println("Selected: " + selected[i].getName() + " for " + posType.name());
        }
    }

    // Helper methods for player selection and printing (not public API)
    private static void printSelectedPlayers(String posName, IPlayer[] selected) {
        for (int i = 0; i < selected.length; i++) {
            if (selected[i] != null) {
                System.out.println(posName + " #" + (i + 1) + ": " + selected[i].getName());
            } else {
                System.out.println(posName + " #" + (i + 1) + ": (no player selected)");
            }
        }
    }

    /**
     * Displays all events for the managed club in the current round.
     * @param input Scanner for user input
     * @param season The season context
     * @param managedClub The club being managed
     */
    public static void viewRoundEventsByManagedTeam(Scanner input, Season season, IClub managedClub) {
        if (managedClub == null) {
            System.out.println("|--------------------------------------------|");
            System.out.println("|No club selected to manage.                 |");
            System.out.println("|--------------------------------------------|");
            return;
        }

        int currentRound = season.getCurrentRound() - 1;
        if (currentRound < 0) currentRound = 0;

        System.out.println("|--------------------------------------------");
        System.out.println("|     EVENTS FOR " + managedClub.getName() + " IN ROUND " + (currentRound + 1));
        System.out.println("|--------------------------------------------");

        IMatch[] matches = season.getMatches(currentRound);
        boolean found = false;
        for (IMatch match : matches) {
            if (match.getHomeClub().equals(managedClub) || match.getAwayClub().equals(managedClub)) {
                for (com.ppstudios.footballmanager.api.contracts.event.IEvent event : match.getEvents()) {
                    System.out.println(event.toString());
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No events for your team in this round.");
        }
        System.out.println("--------------------------------------------");
    }

    /**
     * Allows the user to view all events for a selected match in a selected round.
     * @param input Scanner for user input
     * @param season The season context
     */
    public static void viewGameEventsByRoundAndMatch(Scanner input, Season season) {
        System.out.println("|--------------------------------------------|");
        System.out.println("|           VIEW GAME EVENTS BY ROUND        |");
        System.out.println("|--------------------------------------------|");

        int maxRound = season.getCurrentRound();
        System.out.println(maxRound + "caralho");
        if (maxRound == 0) {
            System.out.println("No rounds have been played yet.");
            return;
        }

        int round = 0;
        boolean validInput = false;
        do {
            System.out.print("Enter the round number to view (1-" + maxRound + "): ");
            try {
                round = input.nextInt();
                if (round >= 1 && round <= maxRound) {
                    validInput = true;
                } else {
                    System.out.println("Please select a valid round number.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next();
            }
        } while (!validInput);

        IMatch[] matches = season.getMatches(round - 1);
        if (matches.length == 0) {
            System.out.println("No matches for this round.");
            return;
        }

        System.out.println("\nMatches in round " + round + ":");
        for (int i = 0; i < matches.length; i++) {
            IMatch match = matches[i];
            System.out.println("  " + (i + 1) + ". " + match.getHomeClub().getName() + " vs " + match.getAwayClub().getName());
        }

        int matchIndex = 0;
        validInput = false;
        do {
            System.out.print("Select the match number to view events (1-" + matches.length + "): ");
            try {
                matchIndex = input.nextInt();
                if (matchIndex >= 1 && matchIndex <= matches.length) {
                    validInput = true;
                } else {
                    System.out.println("Please select a valid match number.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next();
            }
        } while (!validInput);

        IMatch selectedMatch = matches[matchIndex - 1];
        System.out.println("\n--------------------------------------------");
        System.out.println("Events for: " + selectedMatch.getHomeClub().getName() + " vs " + selectedMatch.getAwayClub().getName());
        System.out.println("--------------------------------------------");

        com.ppstudios.footballmanager.api.contracts.event.IEvent[] events = selectedMatch.getEvents();
        if (events.length == 0) {
            System.out.println("No events for this match.");
        } else {
            for (com.ppstudios.footballmanager.api.contracts.event.IEvent event : events) {
                System.out.println(event.toString());
            }
        }
        System.out.println("--------------------------------------------");
    }

    /**
     * Lists the current standings for the season, sorted by points, goal difference, and goals scored.
     * @param input Scanner for user input
     * @param season The season to display standings for
     */
    public static void listStandings(Scanner input, Season season) {
        System.out.println("|--------------------------------------------|");
        System.out.println("|                 STANDINGS                  |");
        System.out.println("|--------------------------------------------|");

        IStanding[] standings = season.getLeagueStandings();
        if (standings == null) {
            System.out.println("No standings available for this season.");
            return;
        }

        int validCount = 0;
        for (IStanding standing : standings) {
            if (standing != null) validCount++;
        }
        if (validCount == 0) {
            System.out.println("No standings available for this season.");
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

    /**
     * Displays a list of available leagues and prompts the user to select one through console input.
     * Handles input validation to ensure a valid league selection.
     *
     * @param input Scanner object for reading user input from console
     * @return The selected ILeague object, or null if no valid selection is made
     */
    public static ILeague loadLeague(Scanner input) {
        ILeague[] gameLeagues = Util.getGameLeagues();

        System.out.println("|--------------------------------------------|");
        System.out.println("|              LOAD LEAGUE                   |");
        System.out.println("|--------------------------------------------|");
        int index;
        for(index = 0; index < gameLeagues.length; index++) {
            if(gameLeagues[index] == null) continue;
            ILeague league = gameLeagues[index];
            System.out.println((index + 1) + ". " + league.getName());
        }

        int choice = 0;
        boolean validInput = false;
        do {
            System.out.print("Select a league to load (1-" + index + "): ");
            try {
                choice = input.nextInt();
                if (choice >= 1 && choice <= index && gameLeagues[choice - 1] != null) {
                    validInput = true;
                } else {
                    System.out.println("Please select a valid league number.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next();
            }
        } while (!validInput);
        return gameLeagues[choice - 1];
    }
}
