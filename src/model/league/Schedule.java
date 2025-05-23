package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class Schedule implements ISchedule {

    private final IMatch[][] rounds;
    private final int numberOfRounds;
    private final int maxMatchesPerRound;

    public Schedule(int numberOfRounds, int maxMatchesPerRound) {
        if (numberOfRounds <= 0 || maxMatchesPerRound <= 0) {
            throw new IllegalArgumentException("Number of rounds and matches per round must be positive.");
        }
        this.numberOfRounds = numberOfRounds;
        this.maxMatchesPerRound = maxMatchesPerRound;
        this.rounds = new IMatch[numberOfRounds][maxMatchesPerRound];
    }

    @Override
    public IMatch[] getMatchesForRound(int round) {
        validateRound(round);

        int count = 0;
        for (int i = 0; i < maxMatchesPerRound; i++) {
            if (rounds[round][i] != null) {
                count++;
            }
        }

        IMatch[] result = new IMatch[count];
        int idx = 0;
        for (int i = 0; i < maxMatchesPerRound; i++) {
            if (rounds[round][i] != null) {
                result[idx++] = rounds[round][i];
            }
        }
        return result;
    }

    @Override
    public IMatch[] getMatchesForTeam(ITeam team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null.");
        }
        int totalCount = 0;
        for (int i = 0; i < numberOfRounds; i++) {
            for (int m = 0; m < maxMatchesPerRound; m++) {
                IMatch match = rounds[i][m];
                if (match != null
                        && (match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team))
                        && match.isPlayed()) {
                    totalCount++;
                }
            }
        }
        IMatch[] result = new IMatch[totalCount];
        int idx = 0;
        for (int i = 0; i < numberOfRounds; i++) {
            for (int j = 0; j < maxMatchesPerRound; j++) {
                IMatch match = rounds[i][j];
                if (match != null
                        && (match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team))
                        && match.isPlayed()) {
                    result[idx++] = match;
                }
            }
        }
        return result;
    }

    @Override
    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    @Override
    public IMatch[] getAllMatches() {
        int totalCount = 0;
        for (int i = 0; i < numberOfRounds; i++) {
            for (int j = 0; j < maxMatchesPerRound; j++) {
                if (rounds[i][j] != null) {
                    totalCount++;
                }
            }
        }
        IMatch[] result = new IMatch[totalCount];
        int idx = 0;
        for (int i = 0; i < numberOfRounds; i++) {
            for (int j = 0; j < maxMatchesPerRound; j++) {
                if (rounds[i][j] != null) {
                    result[idx++] = rounds[i][j];
                }
            }
        }
        return result;
    }

    @Override
    public void setTeam(ITeam team, int round) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null.");
        }
        validateRound(round);

        for (int m = 0; m < maxMatchesPerRound; m++) {
            IMatch match = rounds[round][m];
            if (match != null) {
                try {
                    match.setTeam(team);
                    return;
                } catch (IllegalStateException ignored) {}
            }
        }

        for (int m = 0; m < maxMatchesPerRound; m++) {
            if (rounds[round][m] == null) {
                throw new IllegalStateException("Cannot add new match with only one team. A match must be created with both teams.");
            }
        }
        throw new IllegalStateException("No available match to update with this team in the given round.");
    }

    public void addMatchToRound(int round, IMatch match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null.");
        }
        validateRound(round);

        for (int i = 0; i < maxMatchesPerRound; i++) {
            if (rounds[round][i] == null) {
                rounds[round][i] = match;
                return;
            }
        }
        throw new IllegalStateException("Round is already full.");
    }

    public void clearSchedule() {
        for (int i = 0; i < numberOfRounds; i++) {
            for (int j = 0; j < maxMatchesPerRound; j++) {
                rounds[i][j] = null;
            }
        }
    }

    @Override
    public void exportToJson() throws IOException {
        throw new UnsupportedOperationException("Export not implemented.");
    }

    private void validateRound(int round) {
        if (round < 0 || round >= numberOfRounds) {
            throw new IllegalArgumentException("Invalid round number: " + round);
        }
    }
}