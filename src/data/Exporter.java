package data;

import model.player.Player;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Exporter {

        public static void exportPlayer(Player player) throws IOException {
            JSONObject p = new JSONObject();

            p.put("name", player.getName());
            p.put("birthDate", player.getBirthDate().toString());
            p.put("nationality", player.getNationality());
            p.put("basePosition", player.getPosition().getDescription());
            p.put("photo", player.getPhoto());
            p.put("number", player.getNumber());

            p.put("height", player.getHeight());
            p.put("weight", player.getWeight());
            p.put("stamina", player.getStamina());
            p.put("speed", player.getSpeed());
            p.put("shooting", player.getShooting());
            p.put("passing", player.getPassing());
            p.put("defense", player.getDefense());
            p.put("goalkeeping", player.getGoalkeeping());

            p.put("preferredFoot", player.getPreferredFoot().toString());


            try (FileWriter file = new FileWriter("filePath")) {
                file.write(p.toJSONString());
            }
        }
    }

