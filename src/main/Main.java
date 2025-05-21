package main;

import model.league.Season;

import java.util.Scanner;
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
                    // Add logic to start a new game here
                    runSeasonMenu(input);
                    break;
                case 2:
                    System.out.println("Loading Saved Game...");
                    // Add logic to load a saved game here
                    runSeasonMenu(input);
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

    private static void runSeasonMenu(Scanner input) {
        boolean inSeason = true;
        while (inSeason) {
            int op = seasonMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Starting a New Game...");
                    // simulation logic (free play)
                    runSeasonMenu(input);
                case 2:
                    System.out.println("Starting a new league...");
                    // Start new league logic
                    break;
                case 3:
                    System.out.println("Listing information...");
                    runListMenu(input);
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

    private static void runListMenu(Scanner input) {
        boolean listing = true;
        while (listing) {
            int op = listMenu(input);

            switch (op) {
                case 1:
                    runListClubMenu(input);
                    break;
                case 2:
                    runListPlayersMenu(input);
                    break;
                case 3:
                    listing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void runListClubMenu(Scanner input) {
        boolean listing = true;
        while (listing) {
            int op = listClubMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Listing all clubs...");
                    // Logic to list all clubs
                    break;
                case 2:
                    System.out.println("Listing clubs by strength...");
                    // Logic to list clubs by strength
                    break;
                case 3:
                    listing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void runListPlayersMenu(Scanner input) {
        boolean listing = true;
        while (listing) {
            int op = listPlayersMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Listing players by club...");
                    // Logic to list players by club
                    break;
                case 2:
                    System.out.println("Listing players by strength...");
                    // Logic to list players by strength
                    break;
                case 3:
                    System.out.println("Listing players by position...");
                    // Logic to list players by position
                    break;
                case 4:
                    System.out.println("Listing players by status...");
                    // Logic to list players by status
                    break;
                case 5:
                    listing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
