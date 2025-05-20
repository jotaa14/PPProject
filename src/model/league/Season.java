package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;

import java.io.IOException;

public class Season implements ISeason {
    @Override
    public int getYear() {
        return 0;
    }

    @Override
    public boolean addClub(IClub iClub) {
        return false;
    }

    @Override
    public boolean removeClub(IClub iClub) {
        return false;
    }

    @Override
    public void generateSchedule() {

    }

    @Override
    public IMatch[] getMatches() {
        return new IMatch[0];
    }

    @Override
    public IMatch[] getMatches(int i) {
        return new IMatch[0];
    }

    @Override
    public void simulateRound() {

    }

    @Override
    public void simulateSeason() {

    }

    @Override
    public int getCurrentRound() {
        return 0;
    }

    @Override
    public boolean isSeasonComplete() {
        return false;
    }

    @Override
    public void resetSeason() {

    }

    @Override
    public String displayMatchResult(IMatch iMatch) {
        return "";
    }

    @Override
    public void setMatchSimulator(MatchSimulatorStrategy matchSimulatorStrategy) {

    }

    @Override
    public IStanding[] getLeagueStandings() {
        return new IStanding[0];
    }

    @Override
    public ISchedule getSchedule() {
        return null;
    }

    @Override
    public int getPointsPerWin() {
        return 0;
    }

    @Override
    public int getPointsPerDraw() {
        return 0;
    }

    @Override
    public int getPointsPerLoss() {
        return 0;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getMaxTeams() {
        return 0;
    }

    @Override
    public int getMaxRounds() {
        return 0;
    }

    @Override
    public int getCurrentMatches() {
        return 0;
    }

    @Override
    public int getNumberOfCurrentTeams() {
        return 0;
    }

    @Override
    public IClub[] getCurrentClubs() {
        return new IClub[0];
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
