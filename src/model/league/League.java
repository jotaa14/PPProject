package model.league;
import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;

import java.io.IOException;

public class League implements ILeague {
    private String name;
    private ISeason[] seasons = new ISeason[10];
    private int seasonCount = 0;

    public League(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ISeason[] getSeasons() {
        ISeason[] result = new ISeason[seasonCount];
        for (int i = 0; i < seasonCount; i++) {
            result[i] = seasons[i];
        }
        return result;
    }

    @Override
    public boolean createSeason(ISeason season) {
        if (seasonCount == seasons.length) {
            ISeason[] newSeasons = new ISeason[seasons.length * 2];
            for (int i = 0; i < seasons.length; i++) {
                newSeasons[i] = seasons[i];
            }
            seasons = newSeasons;
        }
        seasons[seasonCount++] = season;
        return true;
    }

    @Override
    public ISeason removeSeason(int i) {
        return null;
    }

    @Override
    public ISeason getSeason(int i) {
        return null;
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
