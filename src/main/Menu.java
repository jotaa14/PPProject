package main;

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
            System.out.println("| 1) New Game                              |");
            System.out.println("| 2) Load Game                             |");
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
    public static int seasonMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------Season Menu-----------------|");
            System.out.println("| 1) Simulation Match                      |");
            System.out.println("| 2) Start League                          |");
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
    public static int listMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|----------------List Menu-----------------|");
            System.out.println("| 1) List Clubs                            |");
            System.out.println("| 2) List Players                          |");
            System.out.println("| 3) Exit                                  |");
            System.out.println("|                                          |");
            System.out.println("|==========================================|");
            System.out.println("| Enter Your Option: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 3) {
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
    public static int listClubMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------List Club Menu--------------|");
            System.out.println("| 1) All Clubs                             |");
            System.out.println("| 2) By Strength                           |");
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
    public static int listPlayersMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|-------------List Players Menu------------|");
            System.out.println("| 1) By Club                               |");
            System.out.println("| 2) By Strength                           |");
            System.out.println("| 3) By Position                           |");
            System.out.println("| 4) By Status                             |");
            System.out.println("| 5) Exit                                  |");
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
}
