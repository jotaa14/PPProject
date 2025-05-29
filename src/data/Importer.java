package data;

import com.ppstudios.footballmanager.api.contracts.event.*;
import com.ppstudios.footballmanager.api.contracts.league.*;
import com.ppstudios.footballmanager.api.contracts.match.*;
import com.ppstudios.footballmanager.api.contracts.player.*;
import com.ppstudios.footballmanager.api.contracts.team.*;
import main.Util;
import model.event.*;
import model.event.eventTypes.*;
import model.league.*;
import model.match.Match;
import model.player.*;
import model.team.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.Random;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Handles importing player, club, league, season, match, and event data from JSON files.
 * Provides methods to parse and reconstruct the full object graph for the Football Manager application.
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Importer {

    /**
     * Imports players from a JSON file at the given file path.
     * If certain attributes are missing, generates random values for them.
     *
     * @param filePath The path to the player JSON file.
     * @return An array of Player objects parsed from the file.
     * @throws IOException If there is a problem reading or parsing the file.
     */
    public Player[] importPlayers(String filePath) throws IOException {
        try {
            FileReader file = new FileReader(filePath);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(file);

            JSONArray squadArray = (JSONArray) json.get("squad");
            Player[] players = new Player[squadArray.size()];

            for (int i = 0; i < squadArray.size(); i++) {
                JSONObject p = (JSONObject) squadArray.get(i);
                String name = (String) p.get("name");
                LocalDate birthDate = LocalDate.parse((String) p.get("birthDate"));
                String nationality = (String) p.get("nationality");
                String position = (String) p.get("basePosition");
                String photo = (String) p.get("photo");
                int number = ((Number) p.get("number")).intValue();

                float height;
                float weight;
                int stamina;
                int speed;
                int shooting;
                int passing;
                int defense;
                int goalkeeping;
                PreferredFoot preferredFoot;
                PlayerPosition playerPosition = new PlayerPosition(position);

                String clubCode = filePath.split("/")[3].replace(".json", "");

                if (p.containsKey("height") && p.get("height") instanceof Number) {
                    height = ((Number) p.get("height")).floatValue();
                } else {
                    height = generateRandomHeight();
                }

                if (p.containsKey("weight") && p.get("weight") instanceof Number) {
                    weight = ((Number) p.get("weight")).floatValue();
                } else {
                    weight = generateRandomWeight();
                }

                if (p.containsKey("stamina") && p.get("stamina") instanceof Number) {
                    stamina = ((Number) p.get("stamina")).intValue();
                } else {
                    stamina = generateRandomStamina(playerPosition);
                }

                if (p.containsKey("speed") && p.get("speed") instanceof Number) {
                    speed = ((Number) p.get("speed")).intValue();
                } else {
                    speed = generateRandomSpeed(playerPosition);
                }

                if (p.containsKey("shooting") && p.get("shooting") instanceof Number) {
                    shooting = ((Number) p.get("shooting")).intValue();
                } else {
                    shooting = generateRandomShooting(playerPosition);
                }

                if (p.containsKey("passing") && p.get("passing") instanceof Number) {
                    passing = ((Number) p.get("passing")).intValue();
                } else {
                    passing = generateRandomPassing(playerPosition);
                }

                if (p.containsKey("defense") && p.get("defense") instanceof Number) {
                    defense = ((Number) p.get("defense")).intValue();
                } else {
                    defense = generateRandomDefense(playerPosition);
                }

                if (p.containsKey("goalkeeping") && p.get("goalkeeping") instanceof Number) {
                    goalkeeping = ((Number) p.get("goalkeeping")).intValue();
                } else {
                    goalkeeping = generateRandomGoalkeeping(playerPosition);
                }

                if (p.containsKey("preferredFoot") && p.get("preferredFoot") instanceof String) {
                    preferredFoot = PreferredFoot.fromString((String) p.get("preferredFoot"));
                } else {
                    preferredFoot = generateRandomPreferredFoot();
                }

                players[i] = new Player(name, birthDate, nationality, playerPosition, photo, number,
                        shooting, passing, stamina, speed, defense, goalkeeping ,height, weight, preferredFoot, clubCode);
            }

            file.close();
            return players;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error reading player file:" + e.getMessage());
        }
    }

    /**
     * Generates a random player height between 1.50m and 2.00m.
     * @return A random float value for height.
     */
    private static float generateRandomHeight() {
        Random random = new Random();
        return 1.50f + random.nextFloat() * (2.00f - 1.50f);
    }

    /**
     * Generates a random player weight between 60kg and 100kg.
     * @return A random float value for weight.
     */
    private static float generateRandomWeight() {
        Random random = new Random();
        return 60.00f + random.nextFloat() * (100.00f - 60.00f);
    }

    /**
     * Generates a random speed value based on player position.
     * @param position The player's position.
     * @return A random integer value for speed.
     */
    private static int generateRandomSpeed(PlayerPosition position) {
        Random random = new Random();
        switch (position.getDescription()) {
            case "GOALKEEPER":
                return random.nextInt(30) + 30;
            case "DEFENDER":
                return random.nextInt(40) + 40;
            case "MIDFIELDER":
                return random.nextInt(50) + 50;
            case "FORWARD":
                return random.nextInt(50) + 50;
            default:
                return 0;
        }
    }

    /**
     * Generates a random stamina value based on player position.
     * @param position The player's position.
     * @return A random integer value for stamina.
     */
    private static int generateRandomStamina(PlayerPosition position) {
        Random random = new Random();
        switch (position.getDescription()) {
            case "GOALKEEPER":
                return random.nextInt(40) + 40;
            case "DEFENDER":
                return random.nextInt(50) + 50;
            case "MIDFIELDER":
                return random.nextInt(50) + 50;
            case "FORWARD":
                return random.nextInt(60) + 40;
            default:
                return 0;
        }
    }

    /**
     * Generates a random shooting value based on player position.
     * @param position The player's position.
     * @return A random integer value for shooting.
     */
    private static int generateRandomShooting(PlayerPosition position) {
        Random random = new Random();
        switch (position.getDescription()) {
            case "GOALKEEPER":
                return random.nextInt(20) + 30;
            case "DEFENDER":
                return random.nextInt(50) + 30;
            case "MIDFIELDER":
                return random.nextInt(70) + 30;
            case "FORWARD":
                return random.nextInt(60) + 40;
            default:
                return 0;
        }
    }

    /**
     * Generates a random defense value based on player position.
     * @param position The player's position.
     * @return A random integer value for defense.
     */
    private static int generateRandomDefense(PlayerPosition position) {
        Random random = new Random();
        switch (position.getDescription()) {
            case "GOALKEEPER":
                return random.nextInt(50) + 50;
            case "DEFENDER":
                return random.nextInt(50) + 50;
            case "MIDFIELDER":
                return random.nextInt(35) + 30;
            case "FORWARD":
                return random.nextInt(20) + 30;
            default:
                return 0;
        }
    }

    /**
     * Generates a random passing value based on player position.
     * @param position The player's position.
     * @return A random integer value for passing.
     */
    private static int generateRandomPassing(PlayerPosition position) {
        Random random = new Random();
        switch (position.getDescription()) {
            case "GOALKEEPER":
                return random.nextInt(60) + 30;
            case "DEFENDER":
                return random.nextInt(60) + 30;
            case "MIDFIELDER":
                return random.nextInt(60) + 30;
            case "FORWARD":
                return random.nextInt(60) + 30;
            default:
                return 0;
        }
    }

    /**
     * Generates a random goalkeeping value based on player position.
     * @param position The player's position.
     * @return A random integer value for goalkeeping.
     */
    private static int generateRandomGoalkeeping(PlayerPosition position) {
        Random random = new Random();
        switch (position.getDescription()) {
            case "GOALKEEPER":
                return random.nextInt(30) + 70;
            case "DEFENDER":
                return random.nextInt(40) + 10;
            case "MIDFIELDER":
                return random.nextInt(30) + 10;
            case "FORWARD":
                return random.nextInt(30) + 10;
            default:
                return 0;
        }
    }

    /**
     * Randomly selects a preferred foot for a player.
     * @return A PreferredFoot enum value.
     */
    private static PreferredFoot generateRandomPreferredFoot() {
        Random random = new Random();
        int chance = random.nextInt(10) + 1; // 1 a 10

        if (chance <= 2) {
            return PreferredFoot.Both;
        } else if (chance <= 5) {
            return PreferredFoot.Left;
        } else {
            return PreferredFoot.Right;
        }
    }

    /**
     * Imports clubs from a JSON file at the given file path.
     *
     * @param filePath The path to the clubs JSON file.
     * @return An array of Club objects parsed from the file.
     * @throws IOException If there is a problem reading or parsing the file.
     */
    public Club[] importClubs(String filePath) throws IOException {
        try {
            FileReader file = new FileReader(filePath);
            JSONParser parser = new JSONParser();
            JSONArray clubArray = (JSONArray) parser.parse(file);

            Club[] clubs = new Club[clubArray.size()];

            for (int i = 0; i < clubArray.size(); i++) {
                JSONObject c = (JSONObject) clubArray.get(i);
                String name = (String) c.get("name");
                String code = (String) c.get("code");
                String country = (String) c.get("country");
                long foundedLong = (Long) c.get("founded");
                int founded = (int) foundedLong;
                boolean isNationalTeam = (boolean) c.get("isNationalTeam");
                String stadium = (String) c.get("stadium");
                String logo = (String) c.get("logo");


                clubs[i] = new Club(name, code, country, founded, isNationalTeam, stadium, logo);
            }

            file.close();
            return clubs;

        } catch (Exception e) {
            throw new IOException("Error reading club file: " + e.getMessage());
        }
    }

    /**
     * Imports all clubs and their players from the default JSON files.
     * Each club's players are loaded from a separate file.
     *
     * @return An array of Club objects with their players set.
     * @throws IOException If there is a problem reading or parsing the files.
     */
    public Club[] importData() throws IOException {
        try {
            Club[] clubs = importClubs("./JSON/clubs.json");

            for (Club club : clubs) {
                Player[] players = importPlayers("./JSON/players/"+ club.getCode()+".json");
                    club.setPlayers(players);
            }
            return clubs;

        }catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error reading club file: " + e.getMessage());
        }
    }

    /**
     * Imports all leagues from the default leagues JSON file and sets them in the game state.
     * Handles parsing of nested seasons, clubs, matches, and related objects.
     */
    public void importAllLeagues() {
        try {
            FileReader file = new FileReader("./JSON/leagues.json");
            JSONParser parser = new JSONParser();
            JSONArray leaguesArray = (JSONArray) parser.parse(file);

            ILeague[] leagues = ILeagueJSONtoArray(leaguesArray);
            Util.setGameLeagues(leagues);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading leagues file: " + e.getMessage());
        }
    }

    /**
     * Converts a JSONArray of leagues to an array of ILeague objects.
     * @param jsonArray The JSONArray representing leagues.
     * @return An array of ILeague objects.
     */
    private ILeague[] ILeagueJSONtoArray(JSONArray jsonArray){
        ILeague[] leagues = new ILeague[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            ILeague league = this.ILeagueJSONtoObject((JSONObject) jsonArray.get(i));
            leagues[i] = league;
        }

        return leagues;
    }

    /**
     * Converts a JSONObject to an ILeague object, including its seasons.
     * @param jsonObject The JSONObject representing a league.
     * @return An ILeague object.
     */
    private ILeague ILeagueJSONtoObject(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        ISeason[] seasons = ISeasonJSONtoArray((JSONArray) jsonObject.get("seasons"));

        return new League(name, seasons);
    }

    /**
     * Converts a JSONArray of seasons to an array of ISeason objects.
     * @param jsonArray The JSONArray representing seasons.
     * @return An array of ISeason objects.
     */
    private ISeason[] ISeasonJSONtoArray(JSONArray jsonArray) {
        ISeason[] seasons = new ISeason[jsonArray.size()];

        for(int i = 0; i < jsonArray.size(); i++) {
            seasons[i] = this.ISeasonJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return seasons;
    }

    /**
     * Converts a JSONObject to an ISeason object, including clubs, teams, matches, schedule, and standings.
     * @param jsonObject The JSONObject representing a season.
     * @return An ISeason object.
     */
    private ISeason ISeasonJSONtoObject(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        int year = ((Long) jsonObject.get("year")).intValue();
        int currentRound = ((Long) jsonObject.get("current_round")).intValue();
        int maxTeams = ((Long) jsonObject.get("max_teams")).intValue();
        IClub[] clubs = IClubJSONtoArray((JSONArray) jsonObject.get("clubs"));
        IMatch[] matches = IMatchJSONtoArray((JSONArray) jsonObject.get("matches"));
        ISchedule schedule = IScheduleJSONtoObject((JSONObject) jsonObject.get("schedule"));
        IStanding[] standings = IStandingJSONtoArray((JSONArray) jsonObject.get("standings"));
        ITeam[] teams = ITeamJSONtoArray((JSONArray) jsonObject.get("teams"));

        return new Season(name, year, currentRound, maxTeams, clubs, teams, matches, schedule, standings);
    }

    /**
     * Converts a JSONArray of clubs to an array of IClub objects.
     * @param jsonArray The JSONArray representing clubs.
     * @return An array of IClub objects.
     */
    private IClub[] IClubJSONtoArray(JSONArray jsonArray) {
        IClub[] clubs = new IClub[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            clubs[i] = this.IClubJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return clubs;
    }

    /**
     * Converts a JSONObject to an IClub object, including players and team.
     * @param jsonObject The JSONObject representing a club.
     * @return An IClub object.
     */
    private IClub IClubJSONtoObject(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        String code = (String) jsonObject.get("code");
        String stadium = (String) jsonObject.get("stadium");
        String logo = (String) jsonObject.get("logo");
        String country = (String) jsonObject.get("country");
        int founded = ((Long) jsonObject.get("foundedYear")).intValue();
        boolean isNationalTeam = (boolean) jsonObject.get("isNationalTeam");
        IPlayer[] players = IPlayerJSONtoArray((JSONArray) jsonObject.get("players"));
        ITeam team = clubITeamJSONtoObject((JSONObject) jsonObject.get("team"));


        Club club =  new Club(name, code, stadium, logo, country, founded, isNationalTeam, players, team);
        club.setClubInTeam();
        return club;
    }

    /**
     * Converts a JSONObject to an ITeam object for a club.
     * @param jsonObject The JSONObject representing a team.
     * @return An ITeam object.
     */
    private ITeam clubITeamJSONtoObject(JSONObject jsonObject) {
        IFormation formation = IFormationStringToObject((String) jsonObject.get("formation"));
        IPlayer[] players = IPlayerJSONtoArray((JSONArray) jsonObject.get("players"));

        return new Team(formation, null, players);
    }


    /**
     * Converts a JSONArray of matches to an array of IMatch objects.
     * @param jsonArray The JSONArray representing matches.
     * @return An array of IMatch objects.
     */
    private IMatch[] IMatchJSONtoArray(JSONArray jsonArray) {
        IMatch[] matches = new IMatch[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            matches[i] = this.IMatchJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return matches;
    }

    /**
     * Converts a JSONObject to an IMatch object, including teams, round, events, and play status.
     * @param jsonObject The JSONObject representing a match.
     * @return An IMatch object.
     */
    private IMatch IMatchJSONtoObject(JSONObject jsonObject) {
        ITeam homeTeam = this.ITeamJSONtoObject((JSONObject) jsonObject.get("home_team"));
        ITeam awayTeam = this.ITeamJSONtoObject((JSONObject) jsonObject.get("away_team"));

        int round = ((Long) jsonObject.get("round")).intValue();
        IEventManager eventManager = IEventManagerJSONtoArray((JSONArray) jsonObject.get("events"));
        boolean isPlayed = (boolean) jsonObject.get("is_played");

        return new Match(homeTeam, awayTeam, round, eventManager, isPlayed);
    }

    /**
     * Converts a JSONArray of teams to an array of ITeam objects.
     * @param jsonArray The JSONArray representing teams.
     * @return An array of ITeam objects.
     */
    private ITeam[] ITeamJSONtoArray(JSONArray jsonArray) {
        ITeam[] teams = new ITeam[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            teams[i] = this.ITeamJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return teams;
    }

    /**
     * Converts a JSONObject to an ITeam object, including formation, club, and players.
     * @param jsonObject The JSONObject representing a team.
     * @return An ITeam object.
     */
    private ITeam ITeamJSONtoObject(JSONObject jsonObject) {
        IFormation formation = IFormationStringToObject((String) jsonObject.get("formation"));
        IClub club = this.IClubJSONtoObject((JSONObject) jsonObject.get("club"));
        IPlayer[] players = IPlayerJSONtoArray((JSONArray) jsonObject.get("players"));

        return new Team(formation, club, players);
    }

    /**
     * Converts a JSONArray of players to an array of IPlayer objects.
     * @param jsonArray The JSONArray representing players.
     * @return An array of IPlayer objects.
     */
    private IPlayer[] IPlayerJSONtoArray(JSONArray jsonArray) {
        IPlayer[] players = new IPlayer[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            players[i] = this.IPlayerJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return players;
    }

    /**
     * Converts a JSONObject to an IPlayer object, including all attributes.
     * @param jsonObject The JSONObject representing a player.
     * @return An IPlayer object.
     */
    private IPlayer IPlayerJSONtoObject(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        String stringPosition = (String) jsonObject.get("position");
        int number = ((Long) jsonObject.get("number")).intValue();
        int shooting = ((Long) jsonObject.get("shooting")).intValue();
        int passing = ((Long) jsonObject.get("passing")).intValue();
        int stamina = ((Long) jsonObject.get("stamina")).intValue();
        int speed = ((Long) jsonObject.get("speed")).intValue();
        int defense = ((Long) jsonObject.get("defense")).intValue();
        int goalkeeping = ((Long) jsonObject.get("goalkeeping")).intValue();
        float height = ((Double) jsonObject.get("height")).floatValue();
        float weight = ((Double) jsonObject.get("weight")).floatValue();
        String nationality = (String) jsonObject.get("nationality");
        PreferredFoot preferredFoot = PreferredFoot.fromString((String) jsonObject.get("preferredFoot"));
        String photo = (String) jsonObject.get("photo");
        LocalDate birthDate = LocalDate.parse((String) jsonObject.get("birthDate"));
        String clubCode = (String) jsonObject.get("clubCode");

        PlayerPosition playerPosition = new PlayerPosition(stringPosition);
        return new Player(name, playerPosition, number, shooting, passing, stamina, speed,
                defense, goalkeeping, height, weight, nationality, preferredFoot, photo, birthDate, clubCode);

    }


    /**
     * Converts a string representation of a formation (e.g., "4-4-2") to an IFormation object.
     * @param formation The formation string.
     * @return An IFormation object.
     * @throws IllegalArgumentException If the format is invalid.
     */
    private IFormation IFormationStringToObject(String formation) {
        String[] parts = formation.split("-");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Formato inválido. Deve ser 'X-X-X'");
        }

        try {
            int defenders = Integer.parseInt(parts[0]);
            int midfielders = Integer.parseInt(parts[1]);
            int forwards = Integer.parseInt(parts[2]);

            return new Formation(defenders, midfielders, forwards);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Os valores da formação devem ser números inteiros.");
        }
    }

    /**
     * Converts a JSONObject to an ISchedule object, including all matches.
     * @param jsonObject The JSONObject representing a schedule.
     * @return An ISchedule object.
     */
    private ISchedule IScheduleJSONtoObject(JSONObject jsonObject) {
        int numberOfRounds = ((Long) jsonObject.get("number_of_rounds")).intValue();
        int maxMatchesPerRound = ((Long) jsonObject.get("max_matches_per_round")).intValue();
        Schedule schedule = new Schedule(numberOfRounds, maxMatchesPerRound);
        IMatch[] matches = IMatchJSONtoArray((JSONArray) jsonObject.get("matches"));
        for(IMatch match : matches) {
            schedule.addMatchToRound(match.getRound(), match);
        }
        return schedule;
    }

    /**
     * Converts a JSONArray of standings to an array of IStanding objects.
     * @param jsonArray The JSONArray representing standings.
     * @return An array of IStanding objects.
     */
    private IStanding[] IStandingJSONtoArray(JSONArray jsonArray) {
        IStanding[] standings = new IStanding[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            standings[i] = this.IStandingJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return standings;
    }

    /**
     * Converts a JSONObject to an IStanding object, including statistics and team.
     * @param jsonObject The JSONObject representing a standing.
     * @return An IStanding object.
     */
    private IStanding IStandingJSONtoObject(JSONObject jsonObject) {
        ITeam team = ITeamJSONtoObject((JSONObject) jsonObject.get("team"));
        int points = ((Long) jsonObject.get("points")).intValue();
        int wins = ((Long) jsonObject.get("wins")).intValue();
        int draws = ((Long) jsonObject.get("draws")).intValue();
        int losses = ((Long) jsonObject.get("losses")).intValue();
        int goalsScored = ((Long) jsonObject.get("goals_scored")).intValue();
        int goalsConceded = ((Long) jsonObject.get("goals_conceded")).intValue();

        return new Standing(team, points, wins, draws, losses, goalsScored, goalsConceded);
    }

    /**
     * Converts a JSONArray of events to an IEventManager containing all events.
     * @param jsonArray The JSONArray representing events.
     * @return An IEventManager object.
     */
    private IEventManager IEventManagerJSONtoArray(JSONArray jsonArray) {
        IEventManager eventManager = new EventManager();

        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject eventJson = (JSONObject) jsonArray.get(i);
            IEvent event = this.IEventJSONtoObject(eventJson);
            eventManager.addEvent(event);
        }

        return eventManager;
    }

    /**
     * Converts a JSONObject to an IEvent object, using the event type to instantiate the correct event class.
     * @param eventJson The JSONObject representing an event.
     * @return An IEvent object.
     * @throws IllegalStateException If the event type is unknown.
     */
    private IEvent IEventJSONtoObject(JSONObject eventJson) {
        String type = (String) eventJson.get("type");
        int minute = ((Long) eventJson.get("minute")).intValue();
        String description = (String) eventJson.get("description");
        Player player = null;
        if(eventJson.containsKey("player")){
            player = (Player) this.IPlayerJSONtoObject((JSONObject) eventJson.get("player"));
        }

        switch(type){
            case "Corner":
                return new CornerEvent(player, minute);
            case "Corner Kick":
                return new CornerKickEvent(player, minute);
            case "Defense":
                return new DefenseEvent(player, minute);
            case "End Event":
                return new EndEvent(minute);
            case "Foul":
                return new FoulEvent(player, minute);
            case "Goal":
                return new GoalEvent(player, minute);
            case "Goal Kick":
                return new GoalKickEvent(player, minute);
            case "OffSide":
                return new OffSideEvent(player, minute);
            case "Pass":
                return new PassingEvent(player, minute);
            case "Penalty":
                return new PenaltyEvent(player, minute);
            case "Red Card":
                return new RedCardEvent(player, minute);
            case "Shot":
                return new ShotEvent(player, minute);
            case "Start Event":
                return new StartEvent(minute);
            case "Turnover":
                return new TurnoverEvent(player, minute);
            case "Yellow Card":
                return new YellowCardEvent(player, minute);
            default:
                throw new IllegalStateException("Unknown Event: " + type);
        }
    }
}

