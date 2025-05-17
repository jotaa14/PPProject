package main;

import data.Importer;
import model.team.Club;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Importer importer = new Importer();

        Club[] clubs = importer.importData();

        for(Club club : clubs){
            System.out.println(club.toString());
        }
    }
}


