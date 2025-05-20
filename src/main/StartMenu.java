package main;

import java.util.InputMismatchException;
import java.util.Scanner;

public class StartMenu {

    public static int startMenu(Scanner input) {
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
            System.out.print("| Enter your option: ");

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


}
