package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import model.player.Player;

import java.io.IOException;

public class Club implements IClub {

    private boolean isNationalTeam;
    private String name;
    private String code;
    private String country;
    private int foundedYear;
    private String stadiumName;
    private String logo;

    private IPlayer[] players;
    private int playerCount;

    public Club(String name, String code, String country, int foundedYear, boolean isNationalTeam, String logo, String stadiumName) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.foundedYear = foundedYear;
        this.stadiumName = stadiumName;
        this.logo = logo;
        this.players = new IPlayer[100];
        this.playerCount = 0;
        this.isNationalTeam = isNationalTeam;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IPlayer[] getPlayers() {
        IPlayer[] currentPlayers = new IPlayer[playerCount];
        for (int i = 0; i < playerCount; i++) {
            currentPlayers[i] = getPlayerClone(players[i]);
        }
        return currentPlayers;
    }

    public IPlayer getPlayerClone(IPlayer player) {
        try {
            return (IPlayer) ((Player) player).clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println("Error while cloning Player");
            return null;
        }
    }

    public boolean isNationalTeam() {
        return isNationalTeam;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public int getFoundedYear() {
        return foundedYear;
    }

    @Override
    public String getStadiumName() {
        return stadiumName;
    }

    @Override
    public String getLogo() {
        return logo;
    }

    @Override
    public void addPlayer(IPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (isPlayer(player)) {
            throw new IllegalArgumentException("Player is already in the club.");
        }
        if (playerCount >= players.length) {
            throw new IllegalStateException("The club is full.");
        }
        players[playerCount++] = player;
    }

    @Override
    public boolean isPlayer(IPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        for (int i = 0; i < playerCount; i++) {
            if (players[i].equals(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removePlayer(IPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        boolean found = false;
        for (int i = 0; i < playerCount; i++) {
            if (players[i].equals(player)) {
                for (int j = i; j < playerCount - 1; j++) {
                    players[j] = players[j + 1];
                }
                players[--playerCount] = null;
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Player is not in the club.");
        }
    }

    @Override
    public int getPlayerCount() {
        return playerCount;
    }

    @Override
    public IPlayer selectPlayer(IPlayerSelector selector, IPlayerPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }
        if (playerCount == 0) {
            throw new IllegalStateException("The club is empty.");
        }

        IPlayer selected = selector.selectPlayer(this, position);
        if (selected == null) {
            throw new IllegalStateException("No player found for the specified position.");
        }

        return selected;
    }

    @Override
    public boolean isValid() {
        if (players == null || players.length == 0) {
            throw new IllegalStateException("The club has no players array initialized.");
        }
        if (playerCount == 0) {
            throw new IllegalStateException("The club has no players. Please add players.");
        }
        if (playerCount < 16) {
            throw new IllegalStateException("The club has only " + playerCount + " players. At least 16 are required.");
        }

        boolean hasGoalkeeper = false;
        for (int i = 0; i < playerCount; i++) {
            if (players[i].getPosition().getDescription().equalsIgnoreCase("GOALKEEPER")) {
                hasGoalkeeper = true;
            }
        }
        if (!hasGoalkeeper) {
            throw new IllegalStateException("The club must have at least one goalkeeper.");
        }
        return true;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
        this.playerCount = players.length;
    }

    public int getClubStrength() {
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
    public String toString() {
        String result = "Club: " + name + "\n";
        result += "Code: " + code + "\n";
        result += "Club Strength: " +  getClubStrength() + "\n";
        result += "Country: " + country + "\n";
        result += "Founded Year: " + foundedYear + "\n";
        result += "Stadium: " + stadiumName + "\n";
        result += "Logo: " + logo + "\n";
        result += "Is National Team: " + isNationalTeam + "\n";
        result += "Players info: " + playerCount + "\n";

        if (playerCount > 0) {
            result += "Player(s): \n";
            for (int i = 0; i < playerCount; i++) {
                if (players[i] != null) {
                    result += players[i].toString() + "\n";
                }
            }
        } else {
            result += "No players found.\n";
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Club other = (Club) obj;
        return this.code.equals(other.code);
    }

    @Override
    public void exportToJson() throws IOException {
        // Implement the export to JSON logic here
    }
}
