package main;

import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
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
                    System.out.println("\nStarting a New Game...");
                    League league = createLeague(input);
                    runLeagueMenu(input, league);
                    break;
                case 2:
                    System.out.println("\nLoading Saved Game...");
                    // TODO: Implement loading functionality
                    break;
                case 3:
                    System.out.println("\nExiting the Game. See you soon!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
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
                    System.out.println("\nStarting a New Season...");
                    Season season = createSeason(input);
                    runSeasonMenu(input, season);
                    break;
                case 2:
                    System.out.println("\nLoading a Season...");
                    loadSeason(input, league);
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
