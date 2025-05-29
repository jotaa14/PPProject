package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

/**
 * Represents a football match schedule organized into rounds, implementing the {@link ISchedule} interface.
 * Manages match assignments, retrieval, and provides queries for match data.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Stores matches in a round-based 2D array structure</li>
 *   <li>Supports queries by round, team, or all matches</li>
 *   <li>Validates round numbers and team assignments</li>
 *   <li>Throws meaningful exceptions for invalid operations</li>
 * </ul>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Schedule implements ISchedule {

    /**
     * 2D array storing matches in format [round][match index].
     * Null entries represent unused match slots.
     */
    private final IMatch[][] rounds;
    /** Total number of rounds in the schedule. */
    private final int numberOfRounds;
    /** Maximum matches allowed per round. */
    private final int maxMatchesPerRound;

    /**
     * Constructs a Schedule with specified dimensions.
     *
     * @param numberOfRounds      Total rounds (must be > 0)
     * @param maxMatchesPerRound  Maximum matches per round (must be > 0)
     * @throws IllegalArgumentException if parameters are non-positive
     */
    public Schedule(int numberOfRounds, int maxMatchesPerRound) {
        if (numberOfRounds <= 0 || maxMatchesPerRound <= 0) {
            throw new IllegalArgumentException("Number of rounds and matches per round must be positive.");
        }
        this.numberOfRounds = numberOfRounds;
        this.maxMatchesPerRound = maxMatchesPerRound;
        this.rounds = new IMatch[numberOfRounds][maxMatchesPerRound];
    }

    /**
     * {@inheritDoc}
     * @param round Round number (0-based index)
     * @return Array of non-null matches in specified round
     * @throws IllegalArgumentException if round is invalid
     */
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

    /**
     * {@inheritDoc}
     * @param team Team to search for
     * @return All played matches involving the specified team
     * @throws IllegalArgumentException if team is null
     */
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

    /**
     * {@inheritDoc}
     * @return Total number of rounds in the schedule
     */
    @Override
    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    /**
     * {@inheritDoc}
     * @return Array of all non-null matches in the schedule
     */
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

    /**
     * Attempts to assign a team to an existing match in the specified round.
     * Prioritizes matches missing one team. Fails if:
     * <ul>
     *   <li>All matches already have both teams</li>
     *   <li>No empty match slots exist</li>
     * </ul>
     *
     * @param team  Team to assign
     * @param round Target round number (0-based)
     * @throws IllegalArgumentException if team is null or round is invalid
     * @throws IllegalStateException if no valid match slot is found
     */
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

    /**
     * Adds a complete match to the specified round.
     *
     * @param round Target round number (0-based)
     * @param match Match to add (must contain both teams)
     * @throws IllegalArgumentException if match is null or round is invalid
     * @throws IllegalStateException if round is full
     */
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

    /**
     * Clears all matches from the schedule by setting all slots to null.
     */
    public void clearSchedule() {
        for (int i = 0; i < numberOfRounds; i++) {
            for (int j = 0; j < maxMatchesPerRound; j++) {
                rounds[i][j] = null;
            }
        }
    }

    /**
     * Validates that a round number is within valid range.
     * @param round Round number to validate
     * @throws IllegalArgumentException if round < 0 or >= numberOfRounds
     */
    private void validateRound(int round) {
        if (round < 0 || round >= numberOfRounds) {
            throw new IllegalArgumentException("Invalid round number: " + round);
        }
    }

    /**
     * Gets the maximum number of matches allowed per round.
     * @return Maximum matches per round
     */
    public int getMaxMatchesPerRound() {
        return maxMatchesPerRound;
    }

    /**
     * {@inheritDoc}
     *
     * <p><b>Note:</b> This method is intentionally left unimplemented in this class,
     * as JSON export is handled centrally by a component responsible for exporting
     * the complete state of the application.</p>
     *
     * <p>This implementation exists solely to satisfy the requirements of the
     * {@code Exportable} interface and has no practical use in this specific class.</p>
     *
     * @throws IOException Not applicable in this implementation
     */
    @Override
    public void exportToJson() throws IOException {
        // Not applicable in this class
    }
}
