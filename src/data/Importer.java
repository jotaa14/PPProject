package data;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.event.Event;
import model.event.PlayerEvent;
import model.league.League;
import model.league.Season;
import model.match.Match;
import model.player.Player;
import model.player.PlayerPosition;
import model.player.PlayerPositionType;
import model.team.Club;
import model.team.Formation;
import model.team.Team;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.Random;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class Importer {

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
            throw new IOException("Error reading player file:" + e.getMessage());
        }
    }

    private static float generateRandomHeight() {
        Random random = new Random();
        return 1.50f + random.nextFloat() * (2.00f - 1.50f);
    }

    private static float generateRandomWeight() {
        Random random = new Random();
        return 60.00f + random.nextFloat() * (100.00f - 60.00f);
    }

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

    public Club[] importData() throws IOException {
        try {
            Club[] clubs = importClubs("./JSON/clubs.json");

            for (Club club : clubs) {
                Player[] players = importPlayers("./JSON/players/"+ club.getCode()+".json");
                    club.setPlayers(players);
            }
            return clubs;

        }catch (Exception e) {
            throw new IOException("Error reading club file: " + e.getMessage());
        }
    }

    public void importAllLeagues() {


    }

    private ILeague[] ILeagueJSONtoArray(JSONArray jsonArray){
        ILeague[] leagues = new ILeague[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            ILeague league = this.ILeagueJSONtoObject((JSONObject) jsonArray.get(i));
            leagues[i] = league;
        }

        return leagues;
    }

    private ILeague ILeagueJSONtoObject(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        ISeason[] seasons = ISeasonJSONtoArray((JSONArray) jsonObject.get("seasons"));

        return new League(name, seasons);
    }

    private ISeason[] ISeasonJSONtoArray(JSONArray jsonArray) {
        ISeason[] seasons = new ISeason[jsonArray.size()];

        for(int i = 0; i < jsonArray.size(); i++) {
            seasons[i] = this.ISeasonJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return seasons;
    }

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

    private IClub[] IClubJSONtoArray(JSONArray jsonArray) {
        IClub[] clubs = new IClub[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            clubs[i] = this.IClubJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return clubs;
    }

    private IClub IClubJSONtoObject(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        String code = (String) jsonObject.get("code");
        String stadium = (String) jsonObject.get("stadium");
        String logo = (String) jsonObject.get("logo");
        String country = (String) jsonObject.get("country");
        int founded = ((Long) jsonObject.get("foundedYear")).intValue();
        boolean isNationalTeam = (boolean) jsonObject.get("isNationalTeam");
        IPlayer[] players = IPlayerJSONtoArray((JSONArray) jsonObject.get("players"));

        return new Club(name, code, stadium, logo, country, founded, isNationalTeam, players);
    }

    private IMatch[] IMatchJSONtoArray(JSONArray jsonArray) {
        IMatch[] matches = new IMatch[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            matches[i] = this.IMatchJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return matches;
    }

    private IMatch IMatchJSONtoObject(JSONObject jsonObject) {
        IClub homeTeam = this.IClubJSONtoObject((JSONObject) jsonObject.get("home_team"));
        IClub awayTeam = this.IClubJSONtoObject((JSONObject) jsonObject.get("away_team"));

        int round = ((Long) jsonObject.get("round")).intValue();
        IEvent[] events = IEventJSONtoArray((JSONArray) jsonObject.get("events"));
        boolean isPlayed = (boolean) jsonObject.get("is_played");

        return new Match(homeTeam, awayTeam, round, events, isPlayed);
    }

    private ITeam[] ITeamJSONtoArray(JSONArray jsonArray) {
        ITeam[] teams = new ITeam[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            teams[i] = this.ITeamJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return teams;
    }

    private ITeam ITeamJSONtoObject(JSONObject jsonObject) {
        IFormation formation = IFormationStringToObject((String) jsonObject.get("formation"));
        IClub club = this.IClubJSONtoObject((JSONObject) jsonObject.get("club"));
        IPlayer[] players = IPlayerJSONtoArray((JSONArray) jsonObject.get("players"));

        return new Team(formation, club, players);
    }

    IPlayer[] IPlayerJSONtoArray(JSONArray jsonArray) {
        IPlayer[] players = new IPlayer[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            players[i] = this.IPlayerJSONtoObject((JSONObject) jsonArray.get(i));
        }
        return players;
    }

    IPlayer IPlayerJSONtoObject(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        String stringPosition = (String) jsonObject.get("position");
        int age = ((Long) jsonObject.get("age")).intValue();
        int number = ((Long) jsonObject.get("number")).intValue();
        int shooting = ((Long) jsonObject.get("shooting")).intValue();
        int passing = ((Long) jsonObject.get("passing")).intValue();
        int stamina = ((Long) jsonObject.get("stamina")).intValue();
        int speed = ((Long) jsonObject.get("speed")).intValue();
        int defense = ((Long) jsonObject.get("defense")).intValue();
        int goalkeeping = ((Long) jsonObject.get("goalkeeping")).intValue();
        int height = ((Long) jsonObject.get("height")).intValue();
        int weight = ((Long) jsonObject.get("weight")).intValue();
        String nationality = (String) jsonObject.get("nationality");
        PreferredFoot preferredFoot = PreferredFoot.fromString((String) jsonObject.get("preferredFoot"));
        String photo = (String) jsonObject.get("photo");
        LocalDate birthDate = LocalDate.parse((String) jsonObject.get("birthDate"));
        String clubCode = (String) jsonObject.get("clubCode");
        int strength = ((Long) jsonObject.get("strength")).intValue();

        PlayerPosition playerPosition = new PlayerPosition(stringPosition);
        return new Player(name, playerPosition, age, number, shooting, passing, stamina, speed,
                defense, goalkeeping, height, weight, nationality, preferredFoot, photo, birthDate, clubCode, strength);

    }


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
}

