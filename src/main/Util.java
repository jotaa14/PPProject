package main;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import model.team.Club;
import model.team.Formation;

import java.util.Scanner;

public class Util {

    public static void listAllClubs(IClub[] clubs) {
        for (IClub club : clubs) {
            if (club != null) {
                Club newClub = (Club) club;
                System.out.printf("%s (%s) - %d players - Strength: %d\n",
                        newClub.getName(), newClub.getCode(), newClub.getPlayerCount(), newClub.getClubStrength());
            }

        }
    }

    public static IFormation selectFormation(Scanner scanner, Club club) {
        do {
            scanner.nextLine();
            System.out.println("Select a formation for " + club.getName() + ":");
            System.out.println("1. 4-4-2");
            System.out.println("2. 4-3-3");
            System.out.println("3. 3-5-2");
            System.out.println("4. 5-3-2");
            System.out.print("Select formation (1-4): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                Formation formation;
                switch (choice) {
                    case 1:
                        formation = new Formation(4, 4, 2);
                        break;
                    case 2:
                        formation = new Formation(4, 3, 3);
                        break;
                    case 3:
                        formation = new Formation(3, 5, 2);
                        break;
                    case 4:
                        formation = new Formation(5, 3, 2);
                        break;
                    default:
                        System.out.println("Invalid formation choice.");
                        formation = null;
                        break;
                }

                if (formation != null) {
                    formation.setClub(club);
                    int value = formation.calculateOverAllValue();
                    System.out.println("→ Selected: " + formation.getDisplayName() +
                            " | Tactical Value: " + value);
                }

                return formation;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

        }while (true) ;
    }

    public static IFormation getDefaultFormation(Club club) {
        Formation formation = new Formation(4, 4, 2);  // Default 4-4-2
        formation.setClub(club);
        int value = formation.calculateOverAllValue();
        System.out.println("→ Default formation set for " + club.getName() +
                ": " + formation.getDisplayName() +
                " | Value: " + value);
        return formation;
    }
}