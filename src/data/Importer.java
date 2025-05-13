package data;

import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import model.player.Player;
import model.player.PlayerPosition;
import model.player.PlayerPositionType;
import model.team.Club;
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
                long numberLong = (Long) p.get("number");
                int number = (int) numberLong;

                float height = (Float) p.get("height");
                float weight = (Float) p.get("weight");
                int stamina = (int) p.get("stamina");
                int speed = (int) p.get("speed");
                int shooting = (int) p.get("shooting");
                int passing = (int) p.get("passing");
                PreferredFoot preferredFoot;
                if (p.containsKey("preferredFoot")) {
                    preferredFoot = PreferredFoot.fromString((String) p.get("preferredFoot"));
                } else {
                    preferredFoot = generateRandomPreferredFoot();
                }

                PlayerPosition playerPosition = new PlayerPosition(position);

                if (p.containsKey("height")) {
                    height = ((Number) p.get("height")).floatValue();
                } else {
                    height = generateRandomHeight();
                }

                if (p.containsKey("weight")) {
                    weight = ((Number) p.get("weight")).floatValue();
                } else {
                    weight = generateRandomWeight();
                }

                if (p.containsKey("stamina")) {
                    stamina = ((Number) p.get("stamina")).intValue();
                } else {
                    stamina = generateRandomStamina(playerPosition);
                }

                if (p.containsKey("speed")) {
                    speed = ((Number) p.get("speed")).intValue();
                } else {
                    speed = generateRandomSpeed(playerPosition);
                }

                if (p.containsKey("shooting")) {
                    shooting = ((Number) p.get("shooting")).intValue();
                } else {
                    shooting = generateRandomShooting(playerPosition);
                }

                if (p.containsKey("passing")) {
                    passing = ((Number) p.get("passing")).intValue();
                } else {
                    passing = generateRandomPassing(playerPosition);
                }


                players[i] = new Player(name, birthDate, nationality, new PlayerPosition(position), photo, number, shooting, passing, stamina, speed, height, weight, preferredFoot);
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
                return random.nextInt(20) + 30;
            case "DEFENDER":
                return random.nextInt(40) + 30;
            case "MIDFIELDER":
                return random.nextInt(60) + 30;
            case "FORWARD":
                return random.nextInt(70) + 30;
            default:
                return 0;
        }
    }

    private static int generateRandomStamina(PlayerPosition position) {
        Random random = new Random();
        switch (position.getDescription()) {
            case "GOALKEEPER":
                return random.nextInt(30) + 30;
            case "DEFENDER":
                return random.nextInt(60) + 30;
            case "MIDFIELDER":
                return random.nextInt(70) + 30;
            case "FORWARD":
                return random.nextInt(70) + 30;
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
                return random.nextInt(70) + 30;
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

    private static PreferredFoot generateRandomPreferredFoot() {
        PreferredFoot[] options = PreferredFoot.values();
        return options[new Random().nextInt(options.length)];
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
}

