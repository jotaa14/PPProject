package main;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import data.Importer;
import model.league.Season;
import model.team.Club;

import java.util.Scanner;

import static main.Menu.*;

public class Main {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int op = mainMenu(input);

        switch (op) {
            case 1:
                System.out.println("Starting a New Game...");

                break;
            case 2:
                System.out.println("Loading Saved Game...");
                //LoadSeason();
                break;
            case 3:
                System.out.println("Exiting the Game. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Unexpected error.");
                break;
        }

        input.close();
    }

}
