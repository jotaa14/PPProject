package main;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import model.league.League;
import model.league.Season;

import java.util.InputMismatchException;
import java.util.Scanner;

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

    public static Season createSeaoson(Scanner input) {
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
        do{
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
}


