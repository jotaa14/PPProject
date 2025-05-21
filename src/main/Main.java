package main;

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
                    System.out.println("Simulating match...");
                    // Simulate match logic
                    break;
                case 2:
                    System.out.println("Starting a new league...");
                    // Start new league logic
                    break;
                case 3:
                    System.out.println("Listing information...");
                    // List information logic
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
}