package main;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import data.Importer;
import model.event.EventManager;
import model.event.eventTypes.GoalEvent;
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
            clubs = importer.importClubs("./JSON/clubs.json");

            for (Club club : clubs) {
                String playerFilePath = "./JSON/players/" + club.getCode().toUpperCase() + ".json";
                try {
                    Player[] players = importer.importPlayers(playerFilePath);
                    club.setPlayers(players);
                } catch (IOException e) {
                    System.out.println("⚠️ Could not load players for club " + club.getCode() + ": " + e.getMessage());
                }
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("\n⚽ Football Manager Simulator ⚽");

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
                    case 1 -> listAllClubs();
                    case 2 -> simulateMatch(scanner);
                    case 3 -> viewMatchEvents();
                    case 4 -> {
                        System.out.println("Exiting Football Manager Simulator...");
                        return;
                    }
                    case 5 -> viewPlayers(scanner);
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listAllClubs() {
        System.out.println("\nAvailable Clubs:");
        for (Club club : clubs) {
            System.out.printf("%s (%s) - %d players - Strength: %d\n",
                    club.getName(), club.getCode(), club.getPlayerCount(), club.getClubStrength());
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
            System.out.println("❌ Cannot simulate a match between the same team!");
            return;
        }

        try {
            ITeam homeTeam = new Team(homeClub);
            ITeam awayTeam = new Team(awayClub);

            System.out.println("\nSelect formation for " + homeClub.getName() + ":");
            IFormation homeFormation = selectFormation(scanner, homeClub);
            if (homeFormation == null) return;

            System.out.println("\nSelect formation for " + awayClub.getName() + ":");
            IFormation awayFormation = selectFormation(scanner, awayClub);
            if (awayFormation == null) return;

            int advantage = homeFormation.getTacticalAdvantage(awayFormation);
            System.out.println("\n📊 Tactical Advantage Score: " + advantage);
            if (advantage > 0) {
                System.out.println("→ Tactical edge to: " + homeClub.getName());
            } else if (advantage < 0) {
                System.out.println("→ Tactical edge to: " + awayClub.getName());
            } else {
                System.out.println("→ Tactical balance between teams.");
            }

            homeTeam.setFormation(homeFormation);
            awayTeam.setFormation(awayFormation);

            IMatch match = new Match(homeTeam, awayTeam, 1);
            if (!match.isValid()) {
                System.out.println("⚠️ Match setup is not valid.");
                return;
            }

            MatchSimulator matchSimulator = new MatchSimulator();
            matchSimulator.simulate(match);
            eventManager.setEvents(match.getEvents());

            System.out.println("\nMatch Result:");
            System.out.println(match.getHomeClub().getName() + " " +
                    match.getTotalByEvent(GoalEvent.class, match.getHomeClub()) + " - " +
                    match.getTotalByEvent(GoalEvent.class, match.getAwayClub()) + " " +
                    match.getAwayClub().getName());

        } catch (Exception e) {
            System.out.println("⚠️ Error simulating match: " + e.getMessage());
        }
    }

    private static IFormation selectFormation(Scanner scanner, Club club) {
        System.out.println("Available formations:");
        System.out.println("1. 4-4-2");
        System.out.println("2. 4-3-3");
        System.out.println("3. 3-5-2");
        System.out.println("4. 5-3-2");
        System.out.print("Select formation (1-4): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            Formation formation = switch (choice) {
                case 1 -> new Formation(4, 4, 2);
                case 2 -> new Formation(4, 3, 3);
                case 3 -> new Formation(3, 5, 2);
                case 4 -> new Formation(5, 3, 2);
                default -> {
                    System.out.println("Invalid formation choice.");
                    yield null;
                }
            };

            if (formation != null) {
                formation.setClub(club);
                int value = formation.calculateOverAllValue();
                System.out.println("→ Selected: " + formation.getDisplayName() +
                        " | Tactical Value: " + value);
            }

            return formation;
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
        System.out.println("Invalid club code.");
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
        for (IPlayer player : players) {
            System.out.println(player.toString());
        }
    }

    private static void viewMatchEvents() {
        if (eventManager.getEvents() == null || eventManager.getEvents().length == 0) {
            System.out.println("No match events available. Simulate a match first.");
            return;
        }

        System.out.println("\nMatch Events:");
        for (IEvent event : eventManager.getEvents()) {
            System.out.println(event.toString());
        }
    }
}
