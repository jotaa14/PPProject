package main;

import com.ppstudios.footballmanager.api.contracts.team.IClub;
import model.league.Season;
import model.league.League;

import java.util.Scanner;

import static main.Functions.*;
import static main.Menu.*;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        boolean running = true;
        while (running) {
            int op = mainMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Starting a New Game...");
                    League league = createLeague(input);
                    runLeagueMenu(input, league);
                    break;
                case 2:
                    System.out.println("Loading Saved Game...");
                    // Add logic to load a saved game here

                    break;
                case 3:
                    System.out.println("Exiting the Game. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        input.close();
    }


    private static void runLeagueMenu(Scanner input, League league) {
        boolean inSeason = true;
        while (inSeason) {
            int op = leagueMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Starting a New Season...");
                    Season season = createSeason(input);
                    runSeasonMenu(input, season);
                    break;
                case 2:
                    System.out.println("Loading a Season...");
                    loadSeason(input, league);

                    break;
                case 3:
                    System.out.println("Listing information...");
                    listSeason(input, league);
                    break;
                case 4:
                    System.out.println("Exiting season menu.");
                    inSeason = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void runSeasonMenu(Scanner input,Season season) {
        boolean listing = true;
        while (listing) {
            int op = seasonMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Simulating Game...");
                    simulateGame(input, season.getCurrentClubs());
                    break;
                case 2:
                    System.out.println("Starting a new Season...");
                    runStartSeason(input, season);
                    break;
                case 3:
                    System.out.println("Adding a Club...");
                    addClub(input, season);
                    break;
                case 4:
                    System.out.println("Removing a Club...");
                    removeClub(input, season);
                    break;
                case 5:
                    System.out.println("Listing information...");
                    listSeasonStuff(input, season);
                    break;
                case 6:
                    listing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void runStartSeason(Scanner input, Season season) {
        boolean listing = true;
        while (listing) {
            int op = startSeasonMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Generating Schedule...");
                    generateSchedule(input, season);
                    break;
                case 2:
                    System.out.println("Simulating Season...");
                    System.out.println("|------------------------------------------|");
                    System.out.println("| Play as a Manager? (Y/N): ");
                    System.out.println("| Enter 'Y' to play as a manager, 'N' to simulate the season: ");
                    String choice = input.next();
                    if (choice.equalsIgnoreCase("Y")) {
                        System.out.println("Playing as a manager...");
                        IClub managedClub = chooseClub(input, season);
                        if (managedClub != null) {
                            runManagerMenu(input, season, managedClub);
                        }
                    } else if (choice.equalsIgnoreCase("N")) {
                        System.out.println("Simulating the season...");
                        startSeason(input, season);
                    } else {
                        System.out.println("Invalid choice. Please enter 'Y' or 'N'.");
                    }
                    break;
                case 3:
                    System.out.println("Listing information...");
                    listSeasonStuff(input, season);
                    break;
                case 4:
                    System.out.println("Standings information...");
                    listStandings(input, season);
                    break;
                case 5:
                    listing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void runManagerMenu(Scanner input, Season season, IClub managedClub) {
        boolean listing = true;
        boolean verifySchedule = false;
        while (listing) {

            System.out.println("| Managing: " + managedClub.getName());
            int op = managerMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Starting Round...");
                    if (!verifySchedule) {
                        System.out.println("Generating Schedule...");
                        generateSchedule(input, season);
                        verifySchedule = true;
                    }
                    simulateRound(input, season, managedClub);
                    break;
                case 2:
                    System.out.println("Selecting Formation...");
                    selectFormation(input, season, managedClub);
                    break;
                case 3:
                    System.out.println("Listing Club Information...");
                    listClubInformation(input, managedClub);
                    break;
                case 4:
                    System.out.println("Listing Schedule...");
                    generateSchedule(input, season);
                    verifySchedule = true;
                    break;
                case 5:
                    System.out.println("Exiting manager menu.");
                    listing = false;
                    break;
                case 6:
                    listing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
