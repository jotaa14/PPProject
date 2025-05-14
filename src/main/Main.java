package main;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import data.Importer;
import model.player.Player;
import model.player.PlayerPosition;
import model.team.Club;

import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IOException {
        Club[] clubs = (new Importer().importClubs("./JSON/clubs.json"));

        for (Club club : clubs) {
            System.out.println(club);
        }
    }
}


