package model.team;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;

public class Formation implements IFormation {
    private int defenders;
    private int midfielders;
    private int forwards;
    private Club club;

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
        throw new IllegalStateException("Away formation is not set.");
        }

        if(!(formation instanceof Formation)){
        throw new IllegalStateException("Unknown formation type");
        }

        Formation away = (Formation) formation;
        return calculateTacticalAdvantage(away);

    }

    public int calculateTacticalValue() {
        int totalTaticalValue = 100;

        if(defenders > 4 || forwards > 3 || defenders < 3 || forwards < 2 || midfielders < 3 || midfielders > 4){
            totalTaticalValue -= 10;
        }

        return totalTaticalValue;
    }

    public int calculateOverAllValue() {
        if (club == null) {
            throw new IllegalStateException("Club is not set.");
        }
        return (this.calculateTacticalValue() + club.getClubStrength()) / 2;
    }

    public int calculateTacticalAdvantage(Formation opponent) {
        int advantage = this.calculateOverAllValue() - opponent.calculateOverAllValue();

        if (advantage < 0) {
            System.out.println("Tactical disadvantage: away formation is superior.");
        } else if (advantage > 0) {
            System.out.println("Tactical advantage: away formation is inferior.");
        } else {
            System.out.println("Tactical balance: formations are equally strong.");
        }

        return advantage;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    @Override
    public String getDisplayName() {
        return defenders + "-" + midfielders + "-" + forwards;
    }
}