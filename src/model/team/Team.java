package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.player.Player;
import model.player.PlayerPosition;
import model.player.PlayerPositionType;

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
        return club;
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
        System.arraycopy(players, 0, temp, 0, playerCount);
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
            throw new IllegalStateException("Player does not belong to the Club!");
        }
        for(int i = 0; i < playerCount; i++){
            if(players[i].equals(iPlayer)){
                throw new IllegalStateException("Player is already in the Team!");
            }
        }

        if(playerCount >= players.length){
            throw new IllegalStateException("Team is full!");
        }
        players[playerCount++] = iPlayer;
    }

    @Override
    public int getPositionCount(IPlayerPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("Player position can't be null!");
        }

        int count = 0;
        for (int i = 0; i < playerCount; i++) {
            if (players[i].getPosition().getDescription().equalsIgnoreCase(position.getDescription())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isValidPositionForFormation(IPlayerPosition iPlayerPosition) {
        if (iPlayerPosition == null) {
            return false;
        }

        PlayerPositionType role;
        if (iPlayerPosition instanceof PlayerPosition) {
            role = ((PlayerPosition) iPlayerPosition).getType();
        } else {
            try {
                role = PlayerPositionType.fromString(iPlayerPosition.getDescription());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        switch (role) {
            case DEFENDER:
                return ((Formation)formation).getDefenders() > 0;
            case MIDFIELDER:
                return ((Formation)formation).getMidfielders() > 0;
            case FORWARD:
                return ((Formation)formation).getForwards() > 0;
            case GOALKEEPER:
                return true;
            default:
                return false;
        }
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
        // Implementar exportação para JSON aqui
    }
}
