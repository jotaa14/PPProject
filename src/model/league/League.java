package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;

import java.io.IOException;

public class League implements ILeague {
    private String name;
    private ISeason[] seasons = new ISeason[1];

    public League(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ISeason[] getSeasons() {
        ISeason[] copy = new ISeason[seasons.length];
        for (int i = 0; i < seasons.length; i++) {
            copy[i] = seasons[i];
        }
        return copy;
    }

    @Override
    public boolean createSeason(ISeason season) {
        if (season == null) {
            throw new IllegalArgumentException("Season Cannot Be Null.");
        }
        if (seasonExists(season.getYear())) {
            throw new IllegalArgumentException("Season For Year " + season.getYear() + " Already Exists.");
        }

        ISeason[] newSeasons = new ISeason[seasons.length + 1];
        for (int i = 0; i < seasons.length; i++) {
            newSeasons[i] = seasons[i];
        }
        newSeasons[seasons.length] = season;
        seasons = newSeasons;

        return true;
    }

    @Override
    public ISeason getSeason(int year) {
        int index = indexOfSeason(year);
        if (index == -1) {
            throw new IllegalArgumentException("Season For Year " + year + " Not Found.");
        }
        return seasons[index];
    }

    @Override
    public ISeason removeSeason(int year) {
        int index = indexOfSeason(year);
        if (index == -1) {
            throw new IllegalArgumentException("Season For Year " + year + " Not Found.");
        }

        ISeason removed = seasons[index];
        ISeason[] newSeasons = new ISeason[seasons.length - 1];
        int newIndex = 0;
        for (int i = 0; i < seasons.length; i++) {
            if (i != index) {
                newSeasons[newIndex++] = seasons[i];
            }
        }
        seasons = newSeasons;
        return removed;
    }


    private boolean seasonExists(int year) {
        return indexOfSeason(year) != -1;
    }

    private int indexOfSeason(int year) {
        for (int i = 0; i < seasons.length; i++) {
            if (seasons[i].getYear() == year) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void exportToJson() throws IOException {
        String json = "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"seasons\": [\n";
    }
}