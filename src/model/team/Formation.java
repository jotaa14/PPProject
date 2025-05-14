package model.team;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;

public class Formation implements IFormation {
    private int defenders;
    private int midfielders;
    private int forwards;

    public Formation(int defenders, int midfielders, int forwards) {
        this.defenders = defenders;
        this.midfielders = midfielders;
        this.forwards = forwards;
    }


    public int getDefenders() {
        return this.defenders;
    }

    public int getMidfielders() {
        return this.midfielders;
    }

    public int getForwards() {
        return this.forwards;
    }


    @Override
    public int getTacticalAdvantage(IFormation formation) {
        if(formation == null){
        throw new IllegalStateException("Opponent formation is not set.");
        }

        if(!(formation instanceof Formation)){
        throw new IllegalStateException("Unknown formation type");
        }

        Formation opponent  = (Formation) formation;
        return this.calculateTacticalValue() - opponent.calculateTacticalValue();
    }

    public int calculateTacticalValue() {
        int totalTaticalValue = 0;
        int defendersValue = 4;
        int midfieldersValue = 3;
        int forwardsValue = 4;

        totalTaticalValue = defenders *defendersValue + midfielders *midfieldersValue + forwards *forwardsValue;

        if(defenders > 4 || forwards > 3){
            totalTaticalValue -= 10;
        }

        return totalTaticalValue;
    }

    @Override
    public String getDisplayName() {
        return defenders + "-" + midfielders + "-" + forwards;
    }
}