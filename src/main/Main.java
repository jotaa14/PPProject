package main;

import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import data.Importer;
import model.match.Match;
import model.team.Club;
import model.team.Team;
import model.event.GoalEvent;
import model.player.Player;

import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {

        Importer importer = new Importer();

        Club[] clubs = importer.importData();

        for(Club club : clubs){
            System.out.println(club.toString());
        }
    }
}


