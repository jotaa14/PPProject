package model.team;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;

public class Formation implements IFormation {
    private int formation;
    private String displayName;

    public Formation(int formation, String displayName) {
        this.formation = formation;
        this.displayName = displayName;
    }

    @Override
    public int getTacticalAdvantage(IFormation formation) {
        if(formation == null){
        throw new NullPointerException("Opponent formation is not set.");
        }
        if(!(formation instanceof Formation)){
        throw new ClassCastException("Unknown formation type");
        }

        Formation opponent = (Formation) formation;
        return this.formation - opponent.formation;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}