package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class Schedule implements ISchedule {

    private IMatch[][] rounds;
    private int numberOfRounds;
    private int maxMatchesPerRound;

    public Schedule(int numberOfRounds, int maxMatchesPerRound) {
        this.numberOfRounds = numberOfRounds;
        this.maxMatchesPerRound = maxMatchesPerRound;
        this.rounds = new IMatch[numberOfRounds][maxMatchesPerRound];
    }

    @Override
    public IMatch[] getMatchesForRound(int round) {
        if (round < 0 || round >= numberOfRounds) {
            throw new IllegalArgumentException("Invalid round number");
        }

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
            throw new IllegalArgumentException("Team Cannot Be Null");
        }
        int totalCount = 0;
        for (int r = 0; r < numberOfRounds; r++) {
            for (int m = 0; m < maxMatchesPerRound; m++) {
                IMatch match = rounds[r][m];
                if (match != null && (match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team))) {
                    totalCount++;
                }
            }
        }
        IMatch[] result = new IMatch[totalCount];
        int idx = 0;
        for (int r = 0; r < numberOfRounds; r++) {
            for (int m = 0; m < maxMatchesPerRound; m++) {
                IMatch match = rounds[r][m];
                if (match != null && (match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team))) {
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
        for (int r = 0; r < numberOfRounds; r++) {
            for (int m = 0; m < maxMatchesPerRound; m++) {
                if (rounds[r][m] != null) {
                    totalCount++;
                }
            }
        }
        IMatch[] result = new IMatch[totalCount];
        int idx = 0;
        for (int r = 0; r < numberOfRounds; r++) {
            for (int m = 0; m < maxMatchesPerRound; m++) {
                if (rounds[r][m] != null) {
                    result[idx++] = rounds[r][m];
                }
            }
        }
        return result;
    }

    @Override
    public void setTeam(ITeam team, int round) {
        if (team == null) {
            throw new IllegalArgumentException("Team Cannot Be Null");
        }
        if (round < 0 || round >= numberOfRounds) {
            throw new IllegalArgumentException("Invalid Round Number");
        }

        for (int m = 0; m < maxMatchesPerRound; m++) {
            IMatch match = rounds[round][m];
            if (match != null) {
                try {
                    match.setTeam(team);
                    return; // Atualizou o time em uma partida da rodada
                } catch (IllegalStateException e) {
                    // Não faz parte da partida, continue procurando
                }
            }
        }

        // Se não encontrou uma partida correspondente para atualizar, tenta inserir em uma vaga vazia
        for (int m = 0; m < maxMatchesPerRound; m++) {
            if (rounds[round][m] == null) {
                // Criação parcial de um novo match não é possível sem outro time
                throw new IllegalStateException("Cannot add new match with only one team. Match must be created with both teams.");
            }
        }

        throw new IllegalStateException("No available match to update with this team in the given round.");
    }

    @Override
    public void exportToJson() throws IOException {
        throw new UnsupportedOperationException("Export Not Implemented");
    }
}
