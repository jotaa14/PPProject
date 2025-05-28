package model.team;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;

/**
 * Represents a football team formation configuration and provides tactical analysis capabilities.
 * Implements the {@link IFormation} interface to support formation comparisons and advantage calculations.
 *
 * <h2>Key Functionality:</h2>
 * <ul>
 *   <li>Stores defender/midfielder/forward counts (e.g., 4-3-3)</li>
 *   <li>Calculates tactical validity scores</li>
 *   <li>Compares formations with opponents</li>
 *   <li>Combines club strength with formation quality</li>
 * </ul>
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Formation implements IFormation {
    /** Number of defenders in formation */
    private int defenders;
    /** Number of midfielders in formation */
    private int midfielders;
    /** Number of forwards in formation */
    private int forwards;
    /** Associated club for strength calculations */
    private Club club;

    /**
     * Constructs a formation with specified player counts.
     *
     * @param defenders Number of defenders (3-5 recommended)
     * @param midfielders Number of midfielders (3-4 recommended)
     * @param forwards Number of forwards (2-3 recommended)
     */
    public Formation(int defenders, int midfielders, int forwards) {
        this.defenders = defenders;
        this.midfielders = midfielders;
        this.forwards = forwards;
    }

    /**
     * {@inheritDoc}
     * @return Number of defenders
     */
    public int getDefenders() {
        return this.defenders;
    }

    /**
     * {@inheritDoc}
     * @return Number of midfielders
     */
    public int getMidfielders() {
        return this.midfielders;
    }

    /**
     * {@inheritDoc}
     * @return Number of forwards
     */
    public int getForwards() {
        return this.forwards;
    }

    /**
     * {@inheritDoc}
     * @param formation Opponent formation to compare against
     * @return Tactical advantage score (positive = advantage, negative = disadvantage)
     * @throws IllegalStateException if input is null or wrong type
     */
    @Override
    public int getTacticalAdvantage(IFormation formation) {
        if(formation == null) {
            throw new IllegalStateException("Away formation is not set.");
        }
        if(!(formation instanceof Formation)) {
            throw new IllegalStateException("Unknown formation type");
        }
        return calculateTacticalAdvantage((Formation) formation);
    }

    /**
     * Calculates base tactical validity score (100 = ideal).
     * Deducts 10 points for:
     * <ul>
     *   <li>Defenders &lt; 3 or &gt; 5</li>
     *   <li>Midfielders &lt; 3 or &gt; 4</li>
     *   <li>Forwards &lt; 2 or &gt; 3</li>
     * </ul>
     * @return Tactical score between 90-100
     */
    public int calculateTacticalValue() {
        int totalTacticalValue = 100;
        if(defenders > 4 || forwards > 3 || defenders < 3 ||
                forwards < 2 || midfielders < 3 || midfielders > 4) {
            totalTacticalValue -= 10;
        }
        return totalTacticalValue;
    }

    /**
     * Combines tactical value with club strength.
     * @return Average of tactical value and club strength
     * @throws IllegalStateException if club is not set
     */
    public int calculateOverAllValue() {
        if (club == null) {
            throw new IllegalStateException("Club is not set.");
        }
        return (this.calculateTacticalValue() + club.getClubStrength()) / 2;
    }

    /**
     * Compares overall value with opponent's formation.
     * @param opponent Opponent formation
     * @return Advantage score (this formation's value - opponent's value)
     * @throws IllegalStateException if either formation lacks club association
     */
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

    /**
     * Associates a club with this formation for strength calculations.
     * @param club Club to associate
     */
    public void setClub(Club club) {
        this.club = club;
    }

    /**
     * {@inheritDoc}
     * @return Formation string in "D-M-F" format (e.g., "4-3-3")
     */
    @Override
    public String getDisplayName() {
        return defenders + "-" + midfielders + "-" + forwards;
    }
}
