package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class Schedule implements ISchedule {

    @Override
    public IMatch[] getMatchesForRound(int i) {
        return new IMatch[0];
    }

    @Override
    public IMatch[] getMatchesForTeam(ITeam iTeam) {
        return new IMatch[0];
    }

    @Override
    public int getNumberOfRounds() {
        return 0;
    }

    @Override
    public IMatch[] getAllMatches() {
        return new IMatch[0];
    }

    @Override
    public void setTeam(ITeam iTeam, int i) {

    }

    @Override
    public void exportToJson() throws IOException {

    }
}
