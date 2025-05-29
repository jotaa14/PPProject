package main;

import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import data.Exporter;
import data.Importer;
import model.league.Season;
import model.league.League;

import java.io.IOException;
import java.util.Scanner;

import static main.Functions.*;
import static main.Menu.*;

/**
 * Entry point and main controller for the Football Manager console application.
 * <p>
 * This class manages the main program loop, user interaction via menus, and navigation
 * between different game states such as starting a new league, managing seasons,
 * and handling club management. It coordinates the flow of the application and
 * delegates specific actions to helper classes and menu utilities.
 * </p>
 *
 * <h2>Main Responsibilities:</h2>
 * <ul>
 *   <li>Display and handle the main menu (new game, load game, exit)</li>
 *   <li>Manage navigation between league, season, and manager menus</li>
 *   <li>Coordinate creation and management of leagues, seasons, and clubs</li>
 *   <li>Delegate simulation and information listing to utility functions</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 *   public static void main(String[] args) {
 *       Main.main(args);
 *   }
 * </pre>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Main {

    /**
     * Application entry point. Displays the main menu and manages navigation.
     * Handles starting a new game, loading a saved game, or exiting the application.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        Importer importer = new Importer();
        importer.importAllLeagues();

        while (running) {
            int op = mainMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\nStarting a New Game...");
                    League league = createLeague(input);
                    Util.addLeague(league);
                    runLeagueMenu(input, league);
                    break;
                case 2:
                    System.out.println("\nLoading Saved Game...");
                    runLeagueMenu(input, (League) loadLeague(input));
                    break;
                case 3:
                    System.out.println("\nExiting the Game. See you soon!");
                    Exporter exporter = new Exporter();
                    exporter.exportToJson();
                    exporter.exportHtmlReports();
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
        input.close();
    }

    /**
     * Handles the league management menu, allowing the user to start new seasons,
     * load existing seasons, list information, or exit to the main menu.
     *
     * @param input  Scanner for user input
     * @param league The league being managed
     */
    private static void runLeagueMenu(Scanner input, League league) {
        boolean inSeason = true;

        while (inSeason) {
            int op = leagueMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\nStarting a New Season...");
                    Season season = createSeason(input);
                    league.createSeason(season);
                    runSeasonMenu(input, season);
                    break;
                case 2:
                    System.out.println("\nLoading a Season...");
                    Season loadSeason = (Season) loadSeason(input, league);
                    if(loadSeason == null) {
                        System.out.println("No season loaded. Returning to league menu.");
                    } else {
                        runSeasonMenu(input, loadSeason);
                    }
                    break;
                case 3:
                    System.out.println("\nListing information...");
                    listSeason(input, league);
                    break;
                case 4:
                    System.out.println("\nExiting league menu.");
                    inSeason = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }

    /**
     * Handles the season management menu, allowing the user to simulate games,
     * manage clubs, list information, restart the season, or exit to the league menu.
     *
     * @param input  Scanner for user input
     * @param season The season being managed
     */
    private static void runSeasonMenu(Scanner input, Season season) {
        boolean listing = true;

        while (listing) {
            int op = seasonMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\nSimulating Game...");
                    simulateGame(input, season.getCurrentClubs());
                    break;
                case 2:
                    System.out.println("\nStarting a New Season...");
                    runStartSeason(input, season);
                    break;
                case 3:
                    System.out.println("\nAdding a Club...");
                    addClub(input, season);
                    break;
                case 4:
                    System.out.println("\nAdding Clubs...");
                    importAllClubsToSeason(input, season);
                    break;
                case 5:
                    System.out.println("\nRemoving a Club...");
                    removeClub(input, season);
                    break;
                case 6:
                    System.out.println("\nRemoving Clubs...");
                    removeAllClubsFromSeason(input, season);
                    break;
                case 7:
                    System.out.println("\nListing information...");
                    listSeasonStuff(input, season);
                    break;
                case 8:
                    System.out.println("\nRestarting Season...");
                    season.resetSeason();
                    System.out.println("Season Has Been Restarted.");
                    break;
                case 9:
                    System.out.println("\nExiting season menu.");
                    listing = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }

    /**
     * Handles the start season menu, allowing the user to generate a schedule,
     * simulate the season, list information, view standings, view events, or exit.
     *
     * @param input  Scanner for user input
     * @param season The season being managed
     */
    private static void runStartSeason(Scanner input, Season season) {
        boolean listing = true;

        while (listing) {
            int op = startSeasonMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\nGenerating Schedule...");
                    generateSchedule(input, season);
                    break;
                case 2:
                    System.out.println("\nSimulating Season...");
                    System.out.println("--------------------------------------------");
                    System.out.print("Play as Manager? (Y/N): ");
                    String choice = input.next();
                    if (choice.equalsIgnoreCase("Y")) {
                        System.out.println("Playing as Manager...");
                        IClub managedClub = chooseClub(input, season);
                        if (managedClub != null) {
                            runManagerMenu(input, season, managedClub);
                        }
                    } else if (choice.equalsIgnoreCase("N")) {
                        System.out.println("Simulating The Season...");
                        startSeason(input, season);
                    } else {
                        System.out.println("Invalid Choice. Please Enter 'Y' or 'N'.");
                    }
                    break;
                case 3:
                    System.out.println("\nListing Information...");
                    listSeasonStuff(input, season);
                    break;
                case 4:
                    System.out.println("\nStandings Information...");
                    listStandings(input, season);
                    break;
                case 5:
                    System.out.println("\nListing Players Standings...");
                    listPlayersStandings(input, season);
                    break;
                case 6:
                    System.out.println("\nListing Events...");
                    viewGameEventsByRoundAndMatch(input, season);
                    break;
                case 7:
                    System.out.println("\nExiting start season menu.");
                    listing = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }

    /**
     * Handles the manager menu, allowing the user to manage a specific club within a season.
     * Supports starting rounds, selecting formations, viewing club information, listing schedules,
     * events, standings, and player standings, or exiting to the previous menu.
     *
     * @param input        Scanner for user input
     * @param season       The season being managed
     * @param managedClub  The club being managed by the user
     */
    private static void runManagerMenu(Scanner input, Season season, IClub managedClub) {
        boolean listing = true;
        boolean verifySchedule = false;

        while (listing) {
            System.out.println("\n--------------------------------------------");
            System.out.println("         Managing: " + managedClub.getName());
            System.out.println("--------------------------------------------");
            int op = managerMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\nStarting Round...");
                    if (!verifySchedule) {
                        System.out.println("Generating Schedule...");
                        generateSchedule(input, season);
                        verifySchedule = true;
                    }
                    simulateRound(input, season, managedClub);
                    viewRoundEventsByManagedTeam(input, season, managedClub);
                    break;

                case 2:
                    System.out.println("\nSelecting Formation...");

                    IFormation formation = selectFormation(input, season, managedClub);
                    System.out.print("Do you want to select players by position? (Y/N): ");
                    String choice = input.next();
                    if (choice.equalsIgnoreCase("Y")) {
                        System.out.println("Forming players...");
                        selectPlayerByPosition(input, managedClub, formation);
                    } else if (choice.equalsIgnoreCase("N")) {
                        System.out.println("Skipping player formation.");
                    } else {
                        System.out.println("Invalid choice. Please enter 'Y' or 'N'.");
                    }
                    break;

                case 3:
                    System.out.println("\nClub Information...");
                    listClubInformation(input, managedClub);
                    break;

                case 4:
                    System.out.println("\nListing Schedule...");
                    generateSchedule(input, season);
                    verifySchedule = true;
                    break;

                case 5:
                    System.out.println("\nListing Event Information...");
                    viewGameEventsByRoundAndMatch(input, season);
                    break;

                case 6:
                    System.out.println("\nListing Standings...");
                    listStandings(input, season);
                    break;
                case 7:
                    System.out.println("\nListing Players Standings...");
                    listPlayersStandingsForMangedClub(input, season, managedClub);
                    break;

                case 8:
                    System.out.println("\nExiting manager menu.");
                    listing = false;
                    break;

                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }
}
