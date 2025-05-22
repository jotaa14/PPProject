package main;

import model.league.League;
import model.league.Season;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    public static int mainMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|==========================================|");
            System.out.println("|PPFootball Manager v1.0 - Season 2024/2025|");
            System.out.println("|==========================================|");
            System.out.println("|                                          |");
            System.out.println("|----------------Main Menu-----------------|");
            System.out.println("| 1) New League                            |");
            System.out.println("| 2) Load League                           |");
            System.out.println("| 3) Exit                                  |");
            System.out.println("|                                          |");
            System.out.println("|==========================================|");
            System.out.println("| Enter Your Option: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 3) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Option (1-3)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Option (1-3)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }

    public static int leagueMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------League Menu-----------------|");
            System.out.println("| 1) New Season                            |");
            System.out.println("| 2) Load Season                           |");
            System.out.println("| 3) List                                  |");
            System.out.println("| 4) Exit                                  |");
            System.out.println("|                                          |");
            System.out.println("|==========================================|");
            System.out.println("| Enter Your Option: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 4) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Option (1-4)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Option (1-4)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }
    public static int seasonMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------Season Menu-----------------|");
            System.out.println("| 1) Simulating                            |");
            System.out.println("| 2) Start Season                          |");
            System.out.println("| 3) Add Clubs                             |");
            System.out.println("| 4) Remove Clubs                          |");
            System.out.println("| 5) List                                  |");
            System.out.println("| 6) Exit                                  |");
            System.out.println("|                                          |");
            System.out.println("|==========================================|");
            System.out.println("| Enter Your Option: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 4) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Option (1-4)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Option (1-4)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }


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
}


