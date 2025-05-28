package main;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

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
            System.out.println("|  6. Standings                              |");
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
}
