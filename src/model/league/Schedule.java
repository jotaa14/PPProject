package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class Schedule implements ISchedule {
    private IMatch[] matches = new IMatch[10];
    private int matchCount = 0;
    private int currentRound = 1;

    @Override
    public IMatch[] getMatchesForRound(int round) {
        int count = 0;
        for (int i = 0; i < matchCount; i++) {
            if (matches[i].getRound() == round) {
                count++;
            }
        }

        IMatch[] result = new IMatch[count];
        int index = 0;
        for (int i = 0; i < matchCount; i++) {
            if (matches[i].getRound() == round) {
                result[index++] = matches[i];
            }
        }
        return result;
    }

    @Override
    public IMatch[] getMatchesForTeam(ITeam iTeam) {
        return new IMatch[0];
    }

    @Override
    public int getNumberOfRounds() {
        return 0;
    }

    public void addMatch(IMatch match) {
        if (matchCount == matches.length) {
            IMatch[] newMatches = new IMatch[matches.length * 2];
            for (int i = 0; i < matches.length; i++) {
                newMatches[i] = matches[i];
            }
            matches = newMatches;
        }
        matches[matchCount++] = match;
    }

    @Override
    public IMatch[] getAllMatches() {
        IMatch[] result = new IMatch[matchCount];
        for (int i = 0; i < matchCount; i++) {
            result[i] = matches[i];
        }
        return result;
    }

    @Override
    public void setTeam(ITeam iTeam, int i) {

    }

    @Override
    public void exportToJson() throws IOException {

    }
}
