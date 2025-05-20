package main;

import java.util.Scanner;

import static main.StartMenu.startMenu;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int op = startMenu(input);

        switch (op) {
            case 1:
                System.out.println("Starting a New Game...");

                break;
            case 2:
                System.out.println("Loading Saved Game...");

                break;
            case 3:
                System.out.println("Exiting The Game. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Unexpected error.");
                break;
        }

        input.close();
    }

}
