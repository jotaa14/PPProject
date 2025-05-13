package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class Team implements ITeam {

   private IClub club;
   private IFormation formation;
   private IPlayer[] players;
   private int playerCount;

    public Team(IClub club){
        if(club == null){
            throw new NullPointerException("Club can't be null!");
        }
        this.club = club;
        this.players = new IPlayer[100];
        this.playerCount = 0;
    }

    @Override
    public IClub getClub() {
        return null;
    }

    @Override
    public IFormation getFormation() {
        if(formation == null){
            throw new IllegalArgumentException("Formation is not set!");
        }
        return formation;
    }

    @Override
    public IPlayer[] getPlayers() {
        IPlayer[] temp = new IPlayer[playerCount];
        for(int i = 0; i < playerCount; i++){
            temp[i] = players[i];
        }
        return temp;
    }

    @Override
    public void addPlayer(IPlayer iPlayer) {
        if(iPlayer == null){
            throw new IllegalArgumentException("Player can't be null!");
        }
        if(formation == null){
            throw new IllegalStateException("Formation is not set!");
        }
        if(!club.isPlayer(iPlayer)){
            throw new IllegalStateException("Player is not belong in the Club!");
        }

    }

    @Override
    public int getPositionCount(IPlayerPosition iPlayerPosition) {
        return 0;
    }

    @Override
    public boolean isValidPositionForFormation(IPlayerPosition iPlayerPosition) {
        return false;
    }

    @Override
    public int getTeamStrength() {
        return 0;
    }

    @Override
    public void setFormation(IFormation iFormation) {

    }

    @Override
    public void exportToJson() throws IOException {

    }
}
