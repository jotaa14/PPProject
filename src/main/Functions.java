package main;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import data.Importer;
import model.event.Event;
import model.event.EventManager;
import model.event.eventTypes.GoalEvent;
import model.league.League;
import model.league.Season;
import model.match.Match;
import model.simulation.MatchSimulator;
import model.team.Club;
import model.team.Formation;
import model.team.Team;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static main.Util.listAllClubs;

public class Functions {

    public static League createLeague(Scanner input) {
        String name = null;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------Create League---------------|");
            System.out.println("| Enter the Name of the League: ");
            name = input.next();
            if (name.length() > 0) {
                verifyInput = true;
            } else {
                System.out.println("Select a Valid Name!");
            }
        } while (!verifyInput);

        return new League(name);
    }

    public static Season createSeaoson(Scanner input) {
        int year = 0;
        String name = null;
        int maxTeams = 0;
        boolean verifyInput = false;

        do {
            System.out.println("|--------------Create Season---------------|");
            System.out.println("| Enter the Name of the Season: ");
            name = input.next();
            if (name.length() > 0) {
                verifyInput = true;
            } else {
                System.out.println("Select a Valid Name!");
            }
        } while (!verifyInput);
        verifyInput = false;
        do {
            System.out.println("| Enter the Year of the Season: ");
            try {
                year = input.nextInt();
                if (year > 2023) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Year!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Year!");
                input.next();
            }
        } while (!verifyInput);
        verifyInput = false;
        do {
            System.out.println("| Enter the Max Teams of the Season: ");
            try {
                maxTeams = input.nextInt();
                if (maxTeams > 0) {
                    verifyInput = true;
                } else {
                    System.out.println("Select a Valid Number of Teams!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a Valid Number of Teams!");
                input.next();
            }

            return new Season(name, year, maxTeams);
        } while (!verifyInput);
    }

    public static Season loadSeason(Scanner input, League league) {
        Season[] seasons = (Season[]) league.getSeasons();

        if (seasons.length == 0) {
            System.out.println("| No seasons found in this league.");
            return null;
        }

        System.out.println("|--------------Available Seasons-----------|");
        for (int i = 0; i < seasons.length; i++) {
            System.out.println("| " + (i + 1) + ") " + seasons[i].getName() + " (" + seasons[i].getYear() + ")");
        }

        int selectedIndex = -1;
        boolean validInput = false;

        do {
            System.out.println("| Enter the number of the season to load: ");
            try {
                selectedIndex = input.nextInt();
                if (selectedIndex >= 1 && selectedIndex <= seasons.length) {
                    validInput = true;
                } else {
                    System.out.println("Select a valid season number (1-" + seasons.length + ")!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next();
            }
        } while (!validInput);

        return seasons[selectedIndex - 1];
    }

    public static void listSeason(Scanner input, League league) {
        ISeason[] seasons = league.getSeasons();

        if (seasons.length == 0) {
            System.out.println("| No seasons available in this league.     |");
            return;
        }

        System.out.println("|--------------List of Seasons-------------|");
        for (int i = 0; i < seasons.length; i++) {
            System.out.println("| " + (i + 1) + ") " + seasons[i].getName() + " (" + seasons[i].getYear() + ")");
        }
        System.out.println("|==========================================|");
    }

    public static void simulateGame(Scanner input,  IClub[] clubs ) {
        System.out.println("|--------------Simulate Game---------------|");
        Util.listAllClubs(clubs);
        System.out.println("| Enter the Home Team(CODE): ");
        String homeTeamName = input.next();
        Util.listAllClubs(clubs);
        System.out.println("| Enter the Away Team(CODE): ");
        String awayTeamName = input.next();

        Club homeClub = null;
        Club awayClub = null;

        for (IClub club : clubs) {
            if (club.getCode().equalsIgnoreCase(homeTeamName)) {
                homeClub = (Club) club;
            } else if (club.getCode().equalsIgnoreCase(awayTeamName)) {
                awayClub = (Club) club;
            }
        }

        if (homeClub == null || awayClub == null) {
            System.out.println("| One or both teams not found.");
            return;
        }

        Team homeTeam = new Team(homeClub);
        Team awayTeam = new Team(awayClub);

        homeTeam.setFormation(Util.selectFormation(input, homeClub));
        awayTeam.setFormation(Util.selectFormation(input, awayClub));

        Match match = new Match(homeTeam, awayTeam, 0);
        MatchSimulator simulator = new MatchSimulator();
        simulator.simulate(match);

        System.out.println("| Match Simulation Complete!");
        System.out.println("\nMatch Result:");
        System.out.println(match.getHomeClub().getName() + " " +
                match.getTotalByEvent(GoalEvent.class, match.getHomeClub()) + " - " +
                match.getTotalByEvent(GoalEvent.class, match.getAwayClub()) + " " +
                match.getAwayClub().getName());

        System.out.println("| Match Events:");
        IEvent[] events = match.getEvents();
        for (int i = 0; i < match.getEventCount(); i++) {
            System.out.println(events[i].toString());
        }
        System.out.println("\n\n\n\n\n");
    }

    public static void addClub(Scanner input, Season season) {
        IClub[] clubs = null;
        try{
            Importer importer = new Importer();
            clubs = importer.importData();
        }catch (IOException e){
            System.out.println("| Error importing clubs: " + e.getMessage());
            return;
        }
        System.out.println("|--------------Add Club to Season----------|");
        System.out.println("| Available Clubs:                         |");
        listAllClubs(clubs);
        System.out.println("| Enter the Club Code to add: ");
        String clubCode = input.next();
        IClub selectedClub = null;
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                selectedClub = club;
                break;
            }
        }
        if (selectedClub == null) {
            System.out.println("| Club not found.");
            return;
        }
        try{
            if (season.addClub(selectedClub)) {
                System.out.println("| Club added to the season successfully.");
            } else {
                System.out.println("| Failed to add club to the season.");
            }
        }catch (IllegalStateException e){
            System.out.println("| Error: " + e.getMessage());
        }

    }

    public static void removeClub(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("| No clubs available in this season.");
            return;
        }

        System.out.println("|--------------Remove Club from Season-----|");
        System.out.println("| Available Clubs:                         |");
        listAllClubs(clubs);
        System.out.println("| Enter the Club Code to remove: ");
        String clubCode = input.next();
        IClub selectedClub = null;
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                selectedClub = club;
                break;
            }
        }
        if (selectedClub == null) {
            System.out.println("| Club not found.");
            return;
        }

        if (season.removeClub(selectedClub)) {
            System.out.println("| Club removed from the season successfully.");
        } else {
            System.out.println("| Failed to remove club from the season.");
        }
    }
    public static void listSeasonStuff(Scanner input, Season season) {
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("| No clubs available in this season.");
            return;
        }

        System.out.println("|--------------List Clubs in Season--------|");
        System.out.println("| Available Clubs:                         |");
        listAllClubs(clubs);
        System.out.println("| Enter the Club Code to view details: ");
        String clubCode = input.next();
        IClub selectedClub = null;
        for (IClub club : clubs) {
            if (club != null && club.getCode().equalsIgnoreCase(clubCode)) {
                selectedClub = club;
                break;
            }
        }
        if (selectedClub == null) {
            System.out.println("| Club not found.");
            return;
        }

        System.out.println("| Club Details:                            |");
        System.out.println("| Name: " + selectedClub.getName());
        System.out.println("| Code: " + selectedClub.getCode());
        System.out.println("| Players: " + selectedClub.getPlayerCount());
        System.out.println("| You Want To Dee The Players Details=? (Y/N): ");
        String choice = input.next();
        if (choice.equalsIgnoreCase("Y")) {
            IPlayer[] players = selectedClub.getPlayers();
            if (players.length == 0) {
                System.out.println("| No players available in this club.");
                return;
            }
            System.out.println("| Players:                                 |");
            for (IPlayer player : players) {
                System.out.println("| " + player.toString());
            }
        } else {
            System.out.println("| Exiting Player Details.");
        }
    }

    public static void startSeason(Scanner input, Season season) {
        System.out.println("|--------------Start Season----------------|");
        IClub[] clubs = season.getCurrentClubs();
        if (clubs.length == 0) {
            System.out.println("| No clubs available in this season.");
            return;
        }

        System.out.println("| Starting the season with the following clubs:");
        for (IClub club : clubs) {
            System.out.println("| " + club.getName());
        }

        // Simulate the season
        for (int i = 0; i < clubs.length; i++) {
            for (int j = i + 1; j < clubs.length; j++) {
                simulateGame(input, new IClub[]{clubs[i], clubs[j]});
            }
        }
        System.out.println("| Season started successfully.");
    }
}


