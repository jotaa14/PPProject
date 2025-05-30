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

/**
 * Represents a football team configuration with players and formation,
 * implementing the {@link ITeam} interface. Manages team lineup validation,
 * strength calculation, and automatic player selection based on formation.
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Maintaining team composition according to formation rules</li>
 *   <li>Validating player positions against formation requirements</li>
 *   <li>Calculating overall team strength</li>
 *   <li>Automatic selection of starting eleven</li>
 * </ul>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Team implements ITeam {

    /** Associated football club */
    private IClub club;
    /** Current team formation */
    private IFormation formation;
    /** Array of selected players */
    private IPlayer[] players;
    /** Current number of selected players */
    private int playerCount;
    /** Starting lineup players */
    private IPlayer[] startingPlayers;

    /**
     * Constructs a team instance associated with a club.
     *
     * @param club The club this team belongs to (cannot be null)
     * @throws NullPointerException if club is null
     */
    public Team(IClub club) {
        if (club == null) {
            throw new NullPointerException("Club can't be null!");
        }
        this.club = club;
        this.players = new IPlayer[100];
        this.playerCount = 0;
    }

    /**
     * Constructs a team instance with a specified formation, club, and player array.
     *
     * @param formation The formation to use for this team (cannot be null)
     * @param club The club this team belongs to (cannot be null)
     * @param players The array of players in this team (cannot be null; length determines player count)
     */
    public Team(IFormation formation, IClub club, IPlayer[] players) {
        this.club = club;
        this.formation = formation;
        this.players = players;
        this.playerCount = players.length;
    }

    /**
     * {@inheritDoc}
     * @return Associated club instance
     */
    @Override
    public IClub getClub() {
        return club;
    }

    /**
     * {@inheritDoc}
     * @return Current formation
     * @throws IllegalArgumentException if formation is not set
     */
    @Override
    public IFormation getFormation() {
        if (formation == null) {
            throw new IllegalArgumentException("Formation is not set!");
        }
        return formation;
    }

    /**
     * {@inheritDoc}
     * @return Copy of selected players array
     */
    @Override
    public IPlayer[] getPlayers() {
        IPlayer[] temp = new IPlayer[playerCount];
        System.arraycopy(players, 0, temp, 0, playerCount);
        return temp;
    }

    /**
     * {@inheritDoc}
     * @param iPlayer Player to add
     * @throws IllegalArgumentException if player is null
     * @throws IllegalStateException if:
     *         - Formation not set
     *         - Player not in club
     *         - Player already in team
     *         - Team is full
     */
    @Override
    public void addPlayer(IPlayer iPlayer) {
        if (iPlayer == null) {
            throw new IllegalArgumentException("Player can't be null!");
        }
        if (formation == null) {
            throw new IllegalStateException("Formation is not set!");
        }
        if (!club.isPlayer(iPlayer)) {
            throw new IllegalStateException("Player does not belong to the Club!");
        }
        for (int i = 0; i < playerCount; i++) {
            if (players[i].equals(iPlayer)) {
                throw new IllegalStateException("Player is already in the Team!");
            }
        }

        if (playerCount >= players.length) {
            throw new IllegalStateException("Team is full!");
        }
        players[playerCount++] = iPlayer;
    }

    /**
     * {@inheritDoc}
     * @param position Position to count
     * @return Number of players in specified position
     * @throws IllegalArgumentException if position is null
     */
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

    /**
     * {@inheritDoc}
     * @param iPlayerPosition Position to validate
     * @return True if position is allowed in current formation
     */
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
                return ((Formation) formation).getDefenders() > 0;
            case MIDFIELDER:
                return ((Formation) formation).getMidfielders() > 0;
            case FORWARD:
                return ((Formation) formation).getForwards() > 0;
            case GOALKEEPER:
                return true;
            default:
                return false;
        }
    }

    /**
     * {@inheritDoc}
     * @return Average player strength (0-99) or 0 if no players
     */
    @Override
    public int getTeamStrength() {
        if (playerCount == 0) {
            return 0;
        }

        int teamStrength = 0;
        for (int i = 0; i < playerCount; i++) {
            teamStrength += ((Player) players[i]).getStrength();
        }
        return teamStrength / playerCount;
    }

    /**
     * {@inheritDoc}
     * @param formation Formation to set
     * @throws IllegalArgumentException if formation is null
     */
    @Override
    public void setFormation(IFormation formation) {
        if (formation == null) {
            throw new IllegalArgumentException("Formation cannot be null.");
        }
        this.formation = formation;
    }

    /**
     * Automatically selects starting eleven based on formation requirements.
     *
     * @param players Available players
     * @param formation Formation to use
     * @throws IllegalArgumentException if inputs are null
     */
    public void setAutomaticTeam(IPlayer[] players, Formation formation) {
        if (players == null || formation == null) {
            throw new IllegalArgumentException("Players and formation must be set!");
        }
        startingPlayers = new IPlayer[11];
        int keepercount = 0, defcount = 0, midcount = 0, forcount = 0, index = 0;

        for (int i = 0; i < players.length && index < 11; i++) {
            IPlayer player = players[i];
            if (player == null || player.getPosition() == null) continue;

            String pos = player.getPosition().getDescription();
            if (pos.equals("GOALKEEPER") && keepercount < 1) {
                addPlayer(player);
                startingPlayers[index++] = player;
                keepercount++;
            } else if (pos.equals("DEFENDER") && defcount < formation.getDefenders()) {
                addPlayer(player);
                startingPlayers[index++] = player;
                defcount++;
            } else if (pos.equals("MIDFIELDER") && midcount < formation.getMidfielders()) {
                addPlayer(player);
                startingPlayers[index++] = player;
                midcount++;
            } else if (pos.equals("FORWARD") && forcount < formation.getForwards()) {
                addPlayer(player);
                startingPlayers[index++] = player;
                forcount++;
            }
        }
    }

    /**
     * Prints the starting lineup grouped by positions.
     *
     * @param formation Formation used for position counts
     */
    public void printStartingElevenByPosition(Formation formation) {
        if (startingPlayers == null || startingPlayers.length != 11) {
            System.out.println("Starting eleven has not been set correctly.");
            return;
        }

        System.out.print("Goalkeeper: ");
        for (int i = 0; i < startingPlayers.length; i++) {
            IPlayer player = startingPlayers[i];
            if (player != null && player.getPosition() != null &&
                    player.getPosition().getDescription().equals("GOALKEEPER")) {
                System.out.println(player.getName());
                break;
            }
        }

        System.out.print("Defenders (" + formation.getDefenders() + "): ");
        boolean first = true;
        for (IPlayer player : startingPlayers) {
            if (player != null && player.getPosition() != null &&
                    player.getPosition().getDescription().equals("DEFENDER")) {
                if (!first) System.out.print(", ");
                System.out.print(player.getName());
                first = false;
            }
        }
        System.out.println();

        System.out.print("Midfielders (" + formation.getMidfielders() + "): ");
        first = true;
        for (IPlayer player : startingPlayers) {
            if (player != null && player.getPosition() != null &&
                    player.getPosition().getDescription().equals("MIDFIELDER")) {
                if (!first) System.out.print(", ");
                System.out.print(player.getName());
                first = false;
            }
        }
        System.out.println();

        System.out.print("Forwards (" + formation.getForwards() + "): ");
        first = true;
        for (IPlayer player : startingPlayers) {
            if (player != null && player.getPosition() != null &&
                    player.getPosition().getDescription().equals("FORWARD")) {
                if (!first) System.out.print(", ");
                System.out.print(player.getName());
                first = false;
            }
        }
        System.out.println();
    }

    /**
     * Sets the associated club for this team.
     * @param club The club to associate
     */
    public void setClub(IClub club) {
        this.club = club;
    }

    /**
     * Compares teams by their associated club.
     * @param obj Object to compare
     * @return True if the associated clubs are equal
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Team other = (Team) obj;
        return this.club.equals(other.club);
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
