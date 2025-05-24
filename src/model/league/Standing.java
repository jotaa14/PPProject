package model.league;

import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

public class Standing implements IStanding {
    private ITeam team;
    private int points = 0;
    private int wins = 0;
    private int draws = 0;
    private int losses = 0;
    private int goalsScored = 0;
    private int goalsConceded = 0;;

    public Standing(ITeam team) {
        this.team = team;
    }

    @Override
    public ITeam getTeam() {
        return team;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void addPoints(int i) {
        points += i;
    }

    @Override
    public void addWin(int i) {
        wins += i;
        points += 3 * i;
    }

    @Override
    public void addDraw(int i) {
        draws += i;
        points += 1 * i;
    }

    @Override
    public void addLoss(int i) {
        losses += i;
        points -= 0 * i;
    }

    @Override
    public int getWins() {
        return wins;
    }

    @Override
    public int getDraws() {
        return draws;
    }

    @Override
    public int getLosses() {
        return losses;
    }

    @Override
    public int getTotalMatches() {
        return wins + draws + losses;
    }

    @Override
    public int getGoalScored() {
        return goalsScored;
    }

    @Override
    public int getGoalsConceded() {
        return goalsConceded;
    }

    @Override
    public int getGoalDifference() {
        return goalsScored - goalsConceded;
    }

    public void addGoalsScored(int goals) {
        goalsScored += goals;
    }

    public void addGoalsConceded(int goals) {
        goalsConceded += goals;
    }

    public static void updateStandingsAfterMatch(Standing homeStanding, int homeGoals, Standing awayStanding, int awayGoals) {
        homeStanding.addGoalsScored(homeGoals);
        homeStanding.addGoalsConceded(awayGoals);

        awayStanding.addGoalsScored(awayGoals);
        awayStanding.addGoalsConceded(homeGoals);
        
        if (homeGoals > awayGoals) {
            homeStanding.addWin(1);
            awayStanding.addLoss(1);
        } else if (homeGoals < awayGoals) {
            awayStanding.addWin(1);
            homeStanding.addLoss(1);
        } else {
            homeStanding.addDraw(1);
            awayStanding.addDraw(1);
        }
    }
}