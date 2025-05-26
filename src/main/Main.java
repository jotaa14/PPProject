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

        System.out.println("⚽️ Welcome to Football Manager! ⚽️\n");

        while (running) {
            int op = mainMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\n🆕 Starting a New Game...");
                    League league = createLeague(input);
                    runLeagueMenu(input, league);
                    break;

                case 2:
                    System.out.println("\n💾 Loading Saved Game...");
                    // TODO: Implement loading functionality
                    break;

                case 3:
                    System.out.println("\n👋 Exiting the Game. See you soon!");
                    running = false;
                    break;

                default:
                    System.out.println("\n❌ Invalid option. Please try again.");
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
                    System.out.println("\n🏆 Starting a New Season...");
                    Season season = createSeason(input);
                    runSeasonMenu(input, season);
                    break;

                case 2:
                    System.out.println("\n📂 Loading a Season...");
                    loadSeason(input, league);
                    break;

                case 3:
                    System.out.println("\n📋 Listing information...");
                    listSeason(input, league);
                    break;

                case 4:
                    System.out.println("\n⬅️ Exiting league menu.");
                    inSeason = false;
                    break;

                default:
                    System.out.println("\n❌ Invalid option. Please try again.");
            }
        }
    }

    private static void runSeasonMenu(Scanner input, Season season) {
        boolean listing = true;

        while (listing) {
            int op = seasonMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\n🎲 Simulating Game...");
                    simulateGame(input, season.getCurrentClubs());
                    break;

                case 2:
                    System.out.println("\n🔄 Starting a New Season...");
                    runStartSeason(input, season);
                    break;

                case 3:
                    System.out.println("\n➕ Adding a Club...");
                    addClub(input, season);
                    break;

                case 4:
                    System.out.println("\n➖ Removing a Club...");
                    removeClub(input, season);
                    break;

                case 5:
                    System.out.println("\n📋 Listing information...");
                    listSeasonStuff(input, season);
                    break;

                case 6:
                    System.out.println("\n⬅️ Exiting season menu.");
                    listing = false;
                    break;

                default:
                    System.out.println("\n❌ Invalid option. Please try again.");
            }
        }
    }

    private static void runStartSeason(Scanner input, Season season) {
        boolean listing = true;

        while (listing) {
            int op = startSeasonMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\n🗓️ Generating Schedule...");
                    generateSchedule(input, season);
                    break;

                case 2:
                    System.out.println("\n🏁 Simulating Season...");
                    System.out.println("|------------------------------------------|");
                    System.out.println("| 👔 Play as Manager? (Y/N): ");
                    String choice = input.next();
                    if (choice.equalsIgnoreCase("Y")) {
                        System.out.println("🎮 Playing as Manager...");
                        IClub managedClub = chooseClub(input, season);
                        if (managedClub != null) {
                            runManagerMenu(input, season, managedClub);
                        }
                    } else if (choice.equalsIgnoreCase("N")) {
                        System.out.println("🤖 Simulating the season...");
                        startSeason(input, season);
                    } else {
                        System.out.println("❌ Invalid choice. Please enter 'Y' or 'N'.");
                    }
                    break;
                case 3:
                    System.out.println("\n📋 Listing information...");
                    listSeasonStuff(input, season);
                    break;

                case 4:
                    System.out.println("\n📊 Standings information...");
                    listStandings(input, season);
                    break;

                case 5:
                    System.out.println("\n⬅️ Exiting start season menu.");
                    listing = false;
                    break;

                default:
                    System.out.println("\n❌ Invalid option. Please try again.");
            }
        }
    }

    private static void runManagerMenu(Scanner input, Season season, IClub managedClub) {
        boolean listing = true;
        boolean verifySchedule = false;

        while (listing) {
            System.out.println("\n| 👔 Managing: " + managedClub.getName() + " |");
            int op = managerMenu(input);

            switch (op) {
                case 1:
                    System.out.println("\n🔜 Starting Round...");
                    if (!verifySchedule) {
                        System.out.println("🗓️ Generating Schedule...");
                        generateSchedule(input, season);
                        verifySchedule = true;
                    }
                    simulateRound(input, season, managedClub);
                    break;

                case 2:
                    System.out.println("\n📝 Selecting Formation...");
                    selectFormation(input, season, managedClub);
                    break;

                case 3:
                    System.out.println("\n📄 Club Information...");
                    listClubInformation(input, managedClub);
                    System.out.println("👥 Do you want to select players by position? (Y/N)");
                    String choice = input.next();
                    if (choice.equalsIgnoreCase("Y")) {
                        System.out.println("⚙️ Forming players...");
                        selectPlayerByPosition(input, season, managedClub);
                    } else if (choice.equalsIgnoreCase("N")) {
                        System.out.println("⏭️ Skipping player formation.");
                    } else {
                        System.out.println("❌ Invalid choice. Please enter 'Y' or 'N'.");
                    }
                    break;

                case 4:
                    System.out.println("\n🗓️ Listing Schedule...");
                    generateSchedule(input, season);
                    verifySchedule = true;
                    break;

                case 5:
                case 6:
                    System.out.println("\n⬅️ Exiting manager menu.");
                    listing = false;
                    break;

                default:
                    System.out.println("\n❌ Invalid option. Please try again.");
            }
        }
    }
}
