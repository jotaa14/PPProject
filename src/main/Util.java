package main;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import model.league.League;
import model.team.Club;
import model.team.Formation;

import java.util.Scanner;

/**
 * Provides utility methods for common football management tasks.
 * Contains static helper methods for listing clubs, selecting formations,
 * and applying default formations.
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Util {

    private static ILeague[] gameLeagues = new ILeague[20];
    private static int currentLeagueIndex = 0;

    public static void setGameLeagues(ILeague[] leagues) {
        for(ILeague league : leagues) {
            if(league != null) {
                gameLeagues[currentLeagueIndex++] = league;
            }
        }
    }

    public static ILeague[] getGameLeagues() {
        return gameLeagues;
    }

    public static void addLeague(League league) {
        gameLeagues[currentLeagueIndex++] = league;
    }

    /**
     * Displays information for all clubs in the provided array.
     * Shows club name, code, player count, and strength rating.
     *
     * @param clubs Array of clubs to display (null entries are skipped)
     * @throws ClassCastException if any element is not a {@link Club} instance
     */
    public static void listAllClubs(IClub[] clubs) {
        for (IClub club : clubs) {
            if (club != null) {
                Club newClub = (Club) club;
                System.out.printf("%s (%s) - %d players - Strength: %d\n",
                        newClub.getName(), newClub.getCode(), newClub.getPlayerCount(), newClub.getClubStrength());
            }
        }
    }

    /**
     * Interactive method to select a formation from predefined options.
     * Displays a menu of formations and returns the selected formation linked to the club.
     *
     * @param scanner Scanner for user input (must not be null)
     * @param club Club to associate with the formation (must not be null)
     * @return Selected formation (4-4-2, 4-3-3, 3-5-2, or 5-3-2)
     * @throws NumberFormatException if non-numeric input is provided
     */
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

        } while (true);
    }

    /**
     * Assigns a default 4-4-2 formation to the specified club.
     *
     * @param club Club to receive the default formation (must not be null)
     * @return Default 4-4-2 formation linked to the club
     */
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
