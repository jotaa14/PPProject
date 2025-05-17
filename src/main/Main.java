import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import data.Importer;
import model.event.EventManager;
import model.match.Match;
import model.simulation.MatchSimulator;
import model.team.Club;
import model.team.Formation;
import model.team.Team;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Club[] clubs;
    private static EventManager eventManager = new EventManager();

    public static void main(String[] args) {
        try {
            // Load data from JSON files
            loadData();

            // Display welcome message
            System.out.println("⚽ Football Manager Simulator ⚽");
            System.out.println("-----------------------------");

            // Main menu loop
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nMain Menu:");
                System.out.println("1. List all clubs");
                System.out.println("2. Simulate match");
                System.out.println("3. View match events");
                System.out.println("4. Exit");
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
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadData() throws IOException {
        System.out.println("Loading data...");
        Importer importer = new Importer();
        clubs = importer.importData();
        System.out.println("Data loaded successfully!");
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
        if (clubs.length < 2) {
            System.out.println("Not enough clubs available to simulate a match.");
            return;
        }

        // Select home team
        System.out.println("\nSelect Home Team:");
        Club homeClub = selectClub(scanner);
        if (homeClub == null) return;

        // Select away team
        System.out.println("\nSelect Away Team:");
        Club awayClub = selectClub(scanner);
        if (awayClub == null) return;

        if (homeClub.equals(awayClub)) {
            System.out.println("Home and away teams must be different!");
            return;
        }

        // Create teams
        // Criação dos times
        ITeam homeTeam = new Team(homeClub);
        ITeam awayTeam = new Team(awayClub);

// Defina a formação (ajuste se necessário para bater com seu construtor de Formation)
        homeTeam.setFormation(new Formation(4,4,2));
        awayTeam.setFormation(new Formation(4,3,3));


        // Simulate match
        System.out.println("\n⚽ Starting Match: " + homeClub.getName() + " vs " + awayClub.getName());
        IMatch match = new Match(homeTeam, awayTeam, 1);
        MatchSimulator simulator = new MatchSimulator();
        simulator.simulate(match);

        // Store events
        for (IEvent event : match.getEvents()) {
            eventManager.addEvent(event);
        }

        // Display results
        System.out.println("\nMatch Result:");
        System.out.println("-------------");
        System.out.printf("%s %d - %d %s\n",
                homeClub.getName(),
                match.getTotalByEvent(IGoalEvent.class, homeClub),
                match.getTotalByEvent(IGoalEvent.class, awayClub),
                awayClub.getName());

        ITeam winner = match.getWinner();
        if (winner != null) {
            System.out.println("Winner: " + winner.getClub().getName());
        } else {
            System.out.println("The match ended in a draw");
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

    private static void viewMatchEvents() {
        IEvent[] events = eventManager.getEvents();
        if (events.length == 0) {
            System.out.println("No match events available. Simulate a match first.");
            return;
        }

        System.out.println("\nMatch Events:");
        System.out.println("-------------");
        for (IEvent event : events) {
            System.out.printf("%d' - %s\n",
                    event.getMinute(),
                    event.getDescription());
        }
    }
}
