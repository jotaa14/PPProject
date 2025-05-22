package main;

import model.league.Season;
import model.team.Club;
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
                    createSeaoson(input);
                    runSeasonMenu(input);
                    break;
                case 2:
                    System.out.println("Loading a Season...");
                    loadSeason(input, league);

                    break;
                case 3:
                    System.out.println("Listing information...");
                    listSeason(input, league);//nao esta a guardar
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

    private static void runSeasonMenu(Scanner input) {
        boolean listing = true;
        while (listing) {
            int op = seasonMenu(input);

            switch (op) {
                case 1:
                    System.out.println("Simulating Game...");
                    //simulateGame(input);
                    break;
                case 2:
                    System.out.println("Starting a new Season...");
                    //startSeason(input);
                    break;
                case 3:
                    System.out.println("Adding a Club...");
                    //addClub(input);
                    break;
                case 4:
                    System.out.println("Removing a Club...");
                    //removeClub(input);
                    break;
                case 5:
                    System.out.println("Listing information...");
                    //listSeason(input);
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
