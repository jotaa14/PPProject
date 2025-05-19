package main;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import data.Importer;
import model.event.Event;
import model.event.EventManager;
import model.event.eventTypes.*;
import model.match.Match;
import model.player.Player;
import model.simulation.MatchSimulator;
import model.team.Club;
import model.team.Formation;
import model.team.Team;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static Club[] clubs;
    private static EventManager eventManager = new EventManager();

    public static void main(String[] args) {
        try {
            Importer importer = new Importer();

            // Importa clubes
            clubs = importer.importClubs("./JSON/clubs.json");

            // Para cada clube, tenta carregar os jogadores do ficheiro JSON pelo código do clube
            for (Club club : clubs) {
                String playerFilePath = "./JSON/players/" + club.getCode().toUpperCase() + ".json";
                try {
                    Player[] players = importer.importPlayers(playerFilePath);
                    club.setPlayers(players);
                } catch (IOException e) {
                    System.out.println("⚠️ Could not load players for club " + club.getCode() + ": " + e.getMessage());
                }
            }

            // Menu interativo
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n⚽ Football Manager Simulator ⚽");
            System.out.println("-----------------------------");

            while (true) {
                System.out.println("\nMain Menu:");
                System.out.println("1. List all clubs");
                System.out.println("2. Simulate match");
                System.out.println("3. View match events");
                System.out.println("4. Exit");
                System.out.println("5. View players from a club");
                System.out.print("Select option: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        listAllClubs();
                        break;
                    case 2:
                        simulateMatch(scanner);
                        break;
                    case 3:
                        viewMatchEvents();
                        break;
                    case 4:
                        System.out.println("Exiting Football Manager Simulator...");
                        return;
                    case 5:
                        viewPlayers(scanner);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewMatchEvents() {
        if (eventManager == null || eventManager.getEvents() == null || eventManager.getEvents().length == 0) {
            System.out.println("No match events available. Simulate a match first.");
            return;
        }

        System.out.println("\nMatch Events:");
        System.out.println("-------------");
        for (IEvent event : eventManager.getEvents()) {
            if (event != null) {
                System.out.println(event.toString());
            }
        }
    }

    private static void listAllClubs() {
        System.out.println("\nAvailable Clubs:");
        System.out.println("----------------");
        for (Club club : clubs) {
            System.out.printf("%s (%s) - %d players - Strength: %d\n",
                    club.getName(),
                    club.getCode(),
                    club.getPlayerCount(),
                    club.getClubStrength());
        }
    }

    private static void simulateMatch(Scanner scanner) {
        System.out.println("\nSelect home team:");
        Club homeClub = selectClub(scanner);
        if (homeClub == null) return;

        System.out.println("\nSelect away team:");
        Club awayClub = selectClub(scanner);
        if (awayClub == null) return;

        if (homeClub == awayClub) {
            System.out.println("Error: Cannot simulate a match between the same team!");
            return;
        }

        try {
            ITeam homeTeam = new Team(homeClub);
            ITeam awayTeam = new Team(awayClub);

            System.out.println("\nSelect formation for " + homeClub.getName() + ":");
            IFormation homeFormation = selectFormation(scanner);
            if (homeFormation == null) return;

            System.out.println("\nSelect formation for " + awayClub.getName() + ":");
            IFormation awayFormation = selectFormation(scanner);
            if (awayFormation == null) return;

            try {
                homeTeam.setFormation(homeFormation);
                if (homeTeam.getFormation() == null) throw new IllegalStateException("Home team formation was not properly set");
            } catch (Exception e) {
                System.out.println("Error setting home team formation: " + e.getMessage());
                return;
            }

            try {
                awayTeam.setFormation(awayFormation);
                if (awayTeam.getFormation() == null) throw new IllegalStateException("Away team formation was not properly set");
            } catch (Exception e) {
                System.out.println("Error setting away team formation: " + e.getMessage());
                return;
            }

            IMatch match = new Match(homeTeam, awayTeam, 1);

            if (!match.isValid()) {
                System.out.println("Match setup is not valid. Please check team formations.");
                return;
            }

            // USAR O TEU SIMULADOR
            MatchSimulator matchSimulator = new MatchSimulator();
            matchSimulator.simulate(match);

            // GUARDA OS EVENTOS PARA O MENU
            eventManager.setEvents(match.getEvents());

            System.out.println("\nMatch Result:");
            System.out.println("-------------");
            System.out.println(match.getHomeClub().getName() + " " +
                    match.getTotalByEvent(GoalEvent.class, match.getHomeClub()) + " - " +
                    match.getTotalByEvent(GoalEvent.class, match.getAwayClub()) + " " +
                    match.getAwayClub().getName());

        } catch (Exception e) {
            System.out.println("Error simulating match: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper inline version
    private static Player getRandomPlayerFromClub(Club club, Random rand) {
        IPlayer[] players = club.getPlayers();
        if (players == null || players.length == 0) return null;

        for (int i = 0; i < 10; i++) {
            IPlayer candidate = players[rand.nextInt(players.length)];
            if (candidate instanceof Player) return (Player) candidate;
        }
        return null;
    }

    private static IFormation selectFormation(Scanner scanner) {
        System.out.println("Available formations:");
        System.out.println("1. 4-4-2");
        System.out.println("2. 4-3-3");
        System.out.println("3. 3-5-2");
        System.out.println("4. 5-3-2");
        System.out.print("Select formation (1-4): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return switch (choice) {
                case 1 -> new Formation(4, 4, 2);
                case 2 -> new Formation(4, 3, 3);
                case 3 -> new Formation(3, 5, 2);
                case 4 -> new Formation(5, 3, 2);
                default -> {
                    System.out.println("Invalid formation choice.");
                    yield null;
                }
            };
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return null;
        }
    }

    private static Club selectClub(Scanner scanner) {
        listAllClubs();
        System.out.print("\nEnter club code: ");
        String code = scanner.nextLine().trim().toUpperCase();

        for (Club club : clubs) {
            if (club.getCode().equalsIgnoreCase(code)) {
                System.out.println("Selected: " + club.getName());
                return club;
            }
        }
        System.out.println("Invalid club code. Please try again.");
        return null;
    }

    private static void viewPlayers(Scanner scanner) {
        Club selectedClub = selectClub(scanner);
        if (selectedClub == null) return;

        IPlayer[] players = selectedClub.getPlayers();
        if (players == null || players.length == 0) {
            System.out.println("This club has no players.");
            return;
        }

        System.out.println("\nPlayers from " + selectedClub.getName() + ":");
        System.out.println("--------------------------------------");

        int index = 1;
        for (IPlayer player : players) {
            if (player != null) {
                System.out.println(player.toString());
            }
        }
    }
}