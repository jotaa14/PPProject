package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.player.Player;
import model.player.PlayerPosition;

import java.io.IOException;

/**
 * Represents a football club or national team, implementing the {@link IClub} interface.
 * Manages club information, player roster, and provides operations for player management
 * and team validation.
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Storing club metadata (name, code, country, founding year)</li>
 *   <li>Managing a player roster with add/remove operations</li>
 *   <li>Validating team composition requirements</li>
 *   <li>Calculating team strength based on player skills</li>
 *   <li>Selecting players using a strategy pattern via {@link IPlayerSelector}</li>
 * </ul>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Club implements IClub {

    /** Flag indicating if this is a national team */
    private boolean isNationalTeam;
    /** Official name of the club */
    private String name;
    /** Unique 3-letter club code */
    private String code;
    /** Home country of the club */
    private String country;
    /** Year the club was founded */
    private int foundedYear;
    /** Name of the home stadium */
    private String stadiumName;
    /** Path/URL to club logo image */
    private String logo;
    /** Associated team management object */
    private ITeam team;

    /** Array storing club players */
    private IPlayer[] players;
    /** Current number of players in the club */
    private int playerCount;

    /**
     * Constructs a Club instance with specified details.
     *
     * @param name Official club name
     * @param code Unique 3-letter code (e.g., "FCB")
     * @param country Home country
     * @param foundedYear Year of foundation
     * @param isNationalTeam True for national teams
     * @param logo Path/URL to club logo
     * @param stadiumName Home stadium name
     */
    public Club(String name, String code, String country, int foundedYear,
                boolean isNationalTeam, String logo, String stadiumName) {
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

    public Club(String name, String code, String stadium, String logo, String country, int founded, boolean isNationalTeam, IPlayer[] players) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.foundedYear = founded;
        this.stadiumName = stadium;
        this.logo = logo;
        this.players = players;
        this.playerCount = players.length;
        this.isNationalTeam = isNationalTeam;

        if (this.players == null) {
            this.players = new IPlayer[100];
            this.playerCount = 0;
        }
    }


    /**
     * {@inheritDoc}
     * @return Club's official name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * @return Array of cloned {@link IPlayer} objects to prevent external modification
     */
    @Override
    public IPlayer[] getPlayers() {
        IPlayer[] currentPlayers = new IPlayer[playerCount];
        for (int i = 0; i < playerCount; i++) {
            currentPlayers[i] = getPlayerClone(players[i]);
        }
        return currentPlayers;
    }

    /**
     * Creates a defensive copy of a player object.
     *
     * @param player Player to clone
     * @return Cloned player instance
     */
    public IPlayer getPlayerClone(IPlayer player) {
        try {
            return (IPlayer) ((Player) player).clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println("Error while cloning Player");
            return null;
        }
    }

    /**
     * Checks if this club represents a national team.
     * @return True if national team, false otherwise
     */
    public boolean isNationalTeam() {
        return isNationalTeam;
    }

    /**
     * {@inheritDoc}
     * @return Club's 3-letter code
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * {@inheritDoc}
     * @return Club's home country
     */
    @Override
    public String getCountry() {
        return country;
    }

    /**
     * {@inheritDoc}
     * @return Year of foundation
     */
    @Override
    public int getFoundedYear() {
        return foundedYear;
    }

    /**
     * {@inheritDoc}
     * @return Name of home stadium
     */
    @Override
    public String getStadiumName() {
        return stadiumName;
    }

    /**
     * {@inheritDoc}
     * @return Path/URL to club logo
     */
    @Override
    public String getLogo() {
        return logo;
    }

    /**
     * Gets the associated team management object.
     * @return ITeam instance
     */
    public ITeam getTeam() {
        return team;
    }

    /**
     * Sets the team management object.
     * @param team ITeam instance to associate
     */
    public void setTeam(ITeam team) {
        this.team = team;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if the player is null or already in the club
     * @throws IllegalStateException if the club is at capacity (max 100 players)
     */
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

    /**
     * {@inheritDoc}
     * @return True if a player exists in the club
     * @throws IllegalArgumentException if the player is null
     */
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

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if the player is null or not found
     */
    @Override
    public void removePlayer(IPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        boolean found = false;
        for (int i = 0; i < playerCount; i++) {
            if (players[i].equals(player)) {
                System.arraycopy(players, i + 1, players, i, playerCount - i - 1);
                players[--playerCount] = null;
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Player is not in the club.");
        }
    }

    /**
     * {@inheritDoc}
     * @return Current number of players in the club
     */
    @Override
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * {@inheritDoc}
     * @return Selected player for a specified position
     * @throws IllegalArgumentException if the position is null
     * @throws IllegalStateException if a club has no players or selection fails
     */
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

    /**
     * Validates club configuration:
     * <ul>
     *   <li>Players array initialized</li>
     *   <li>Minimum 16 players</li>
     *   <li>At least one goalkeeper</li>
     * </ul>
     * @return True if valid
     * @throws IllegalStateException with a descriptive message for validation failures
     */
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
                break;
            }
        }
        if (!hasGoalkeeper) {
            throw new IllegalStateException("The club must have at least one goalkeeper.");
        }
        return true;
    }

    /**
     * Directly sets the player array (for testing/initialization).
     * @param players Array of Player objects
     */
    public void setPlayers(Player[] players) {
        this.players = players;
        this.playerCount = players.length;
    }

    /**
     * Calculates average player strength for the club.
     * @return Average strength (0-99) or 0 if no players.
     */
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

    /**
     * Returns formated string with club details and player roster.
     * @return Multi-line string containing:
     *         - Club metadata
     *         - Team strength
     *         - Player list with details
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Club: ").append(name).append("\n")
                .append("Code: ").append(code).append("\n")
                .append("Club Strength: ").append(getClubStrength()).append("\n")
                .append("Country: ").append(country).append("\n")
                .append("Founded Year: ").append(foundedYear).append("\n")
                .append("Stadium: ").append(stadiumName).append("\n")
                .append("Logo: ").append(logo).append("\n")
                .append("Is National Team: ").append(isNationalTeam).append("\n")
                .append("Players info: ").append(playerCount).append("\n");

        if (playerCount > 0) {
            result.append("Player(s): \n");
            for (int i = 0; i < playerCount; i++) {
                if (players[i] != null) {
                    result.append(players[i].toString()).append("\n");
                }
            }
        } else {
            result.append("No players found.\n");
        }
        return result.toString();
    }

    /**
     * Compares clubs by their unique code.
     * @param obj Club to compare
     * @return True if same code.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Club other = (Club) obj;
        return this.code.equals(other.code);
    }

    /**
     * {@inheritDoc}
     *
     * <p><b>Note:</b> This method is intentionally left unimplemented in this class,
     * as JSON export is handled centrally by a component responsible for exporting
     * the complete state of the application.</p>
     *
     * <p>This implementation exists solely to satisfy the requirements of the
     * {@code Exportable} interface and has no practical use in this specific class.</p>
     *
     * @throws IOException Not applicable in this implementation
     */
    @Override
    public void exportToJson() throws IOException {
        // Not applicable in this class
    }
}
