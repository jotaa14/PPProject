package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.player.Player;

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
        for(int i = 0; i < playerCount; i++){
            if(players[i].equals(iPlayer)){
                throw new IllegalStateException("Player is already in the Club!");
            }
        }

        if(playerCount >= players.length){
            throw new IllegalStateException("Team is full!");

        }
        players[playerCount++] = iPlayer;

    }

    @Override
    public int getPositionCount(IPlayerPosition iPlayerPosition) {
       if(iPlayerPosition == null){
           throw new IllegalArgumentException("Player position can't be null!");
       }

        int counter = 0;
        for(int i = 0; i < playerCount; i++){
            if(players[i].getPosition().equals(iPlayerPosition)){
                counter++;
            }
        }
        return counter;
    }
    //fazerrrrrrrrr
    @Override
    public boolean isValidPositionForFormation(IPlayerPosition iPlayerPosition) {
        return false;

    }

    @Override
    public int getTeamStrength() {
        if(playerCount == 0){
            return 0;
        }

        int teamStrength = 0;
        for(int i = 0; i < playerCount; i++){
            teamStrength += ((Player)players[i]).getStrength();
        }
        return teamStrength / playerCount;
    }

    @Override
    public void setFormation(IFormation formation) {
        if (formation == null) {
            throw new IllegalArgumentException("Formation cannot be null.");
        }
        this.formation = formation;
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
