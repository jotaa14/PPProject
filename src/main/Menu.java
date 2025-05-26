package main;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
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
            System.out.println("|------------Season Main Menu--------------|");
            System.out.println("| 1) Friendly Match                        |");
            System.out.println("| 2) Season                                |");
            System.out.println("| 3) Add Clubs                             |");
            System.out.println("| 4) Remove Clubs                          |");
            System.out.println("| 5) List                                  |");
            System.out.println("| 6) Exit                                  |");
            System.out.println("|                                          |");
            System.out.println("|==========================================|");
            System.out.println("| Enter Your Option: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 6) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Option (1-6)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Option (1-6)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }
    public static int startSeasonMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------Season Menu-----------------|");
            System.out.println("| 1) Schedule                              |");
            System.out.println("| 2) Start Season                          |");
            System.out.println("| 3) List Information                      |");
            System.out.println("| 4) List Matches                          |");
            System.out.println("| 5) Exit                                  |");
            System.out.println("|                                          |");
            System.out.println("|==========================================|");
            System.out.println("| Enter Your Option: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 5) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Option (1-5)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Option (1-5)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }

    public static int managerMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------Manager Menu----------------|");
            System.out.println("| 1) Start Round                           |");
            System.out.println("| 2) Formation                             |");
            System.out.println("| 3) Club Information                      |");
            System.out.println("| 4) Schedule                              |");
            System.out.println("| 5) Exit                                  |");
            System.out.println("|                                          |");
            System.out.println("|==========================================|");
            System.out.println("| Enter Your Option: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 5) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Option (1-5)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Option (1-5)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }

}
