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
    public int getTacticalAdvantage(IFormation iFormation) {

        return formation;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
