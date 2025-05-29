package main;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Provides menu display and input validation functionality for the football manager console interface.
 * Contains static methods for rendering different application menus and handling user selections.
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Menu {

    /**
     * Displays and validates input for the main menu.
     * @param input Scanner instance for user input
     * @return Validated user selection (1-3)
     */
    public static int mainMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------------------------------------|");
            System.out.println("|         PPFootball Manager v1.0            |");
            System.out.println("|--------------------------------------------|");
            System.out.println("|                MAIN MENU                   |");
            System.out.println("|--------------------------------------------|");
            System.out.println("|  1. New League                             |");
            System.out.println("|  2. Load League                            |");
            System.out.println("|  3. Exit                                   |");
            System.out.println("|--------------------------------------------|");
            System.out.print("> Enter your option [1-3]: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 3) {
                    verifyInput = true;
                } else {
                    System.out.println("! Select a Valid Option (1-3)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("! Select a Valid Option (1-3)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }

    /**
     * Displays and validates input for the league management menu.
     * @param input Scanner instance for user input
     * @return Validated user selection (1-4)
     */
    public static int leagueMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------------------------------------|");
            System.out.println("|              LEAGUE MENU                   |");
            System.out.println("|--------------------------------------------|");
            System.out.println("|  1. New Season                             |");
            System.out.println("|  2. Load Season                            |");
            System.out.println("|  3. List                                   |");
            System.out.println("|  4. Exit                                   |");
            System.out.println("|--------------------------------------------|");
            System.out.print("> Enter your option [1-4]: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 4) {
                    verifyInput = true;
                } else {
                    System.out.println("! Select a Valid Option (1-4)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("! Select a Valid Option (1-4)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }

    /**
     * Displays and validates input for the season management menu.
     * @param input Scanner instance for user input
     * @return Validated user selection (1-9)
     */
    public static int seasonMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------------------------------------|");
            System.out.println("|           SEASON MAIN MENU                 |");
            System.out.println("|--------------------------------------------|");
            System.out.println("|  1. Friendly Match                         |");
            System.out.println("|  2. Season                                 |");
            System.out.println("|  3. Add Clubs                              |");
            System.out.println("|  4. Add Clubs (AUTO)                       |");
            System.out.println("|  5. Remove Clubs                           |");
            System.out.println("|  6. Remove Clubs (AUTO)                    |");
            System.out.println("|  7. List                                   |");
            System.out.println("|  8. Restart Season                         |");
            System.out.println("|  9. Exit                                   |");
            System.out.println("|--------------------------------------------|");
            System.out.print("> Enter your option [1-9]: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 9) {
                    verifyInput = true;
                } else {
                    System.out.println("! Select a Valid Option (1-9)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("! Select a Valid Option (1-9)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }

    /**
     * Displays and validates input for the season setup menu.
     * @param input Scanner instance for user input
     * @return Validated user selection (1-7)
     */
    public static int startSeasonMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------------------------------------|");
            System.out.println("|             SEASON MENU                    |");
            System.out.println("|--------------------------------------------|");
            System.out.println("|  1. Schedule                               |");
            System.out.println("|  2. Start Season                           |");
            System.out.println("|  3. List Information                       |");
            System.out.println("|  4. Club Standings                         |");
            System.out.println("|  5. Players Standings                      |");
            System.out.println("|  6. Events                                 |");
            System.out.println("|  7. Exit                                   |");
            System.out.println("|--------------------------------------------|");
            System.out.print("> Enter your option [1-7]: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 7) {
                    verifyInput = true;
                } else {
                    System.out.println("! Select a Valid Option (1-7)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("! Select a Valid Option (1-7)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }

    /**
     * Displays and validates input for the team management menu.
     * @param input Scanner instance for user input
     * @return Validated user selection (1-8)
     */
    public static int managerMenu(Scanner input) {
        int op = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------------------------------------|");
            System.out.println("|              MANAGER MENU                  |");
            System.out.println("|--------------------------------------------|");
            System.out.println("|  1. Start Round                            |");
            System.out.println("|  2. Formation                              |");
            System.out.println("|  3. Club Information                       |");
            System.out.println("|  4. Schedule                               |");
            System.out.println("|  5. Events                                 |");
            System.out.println("|  6. Club Standings                         |");
            System.out.println("|  7. Players Standings                      |");
            System.out.println("|  8. Exit                                   |");
            System.out.println("|--------------------------------------------|");
            System.out.print("> Enter your option [1-8]: ");

            try {
                op = input.nextInt();
                if (op >= 1 && op <= 8) {
                    verifyInput = true;
                } else {
                    System.out.println("! Select a Valid Option (1-8)!");
                }
            } catch (InputMismatchException e) {
                System.out.println("! Select a Valid Option (1-8)!");
                input.next();
            }
        } while (!verifyInput);

        return op;
    }
}
