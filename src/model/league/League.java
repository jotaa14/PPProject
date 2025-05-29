package model.league;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;

import java.io.IOException;

/**
 * Represents a football league, managing its name and a list of seasons.
 * Implements the {@link ILeague} interface and provides methods for
 * creating, retrieving, and removing seasons, as well as exporting league data.
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Stores league name and manages an array of seasons</li>
 *   <li>Allows creation, retrieval, and removal of seasons</li>
 *   <li>Prevents duplicate seasons for the same year</li>
 *   <li>Supports exporting league data to JSON format</li>
 * </ul>
 *
 * <b>Note:</b> The exportToJson method should be implemented to complete JSON serialization.
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class League implements ILeague {
    /** Name of the league. */
    private String name;
    /** Array of league seasons. */
    private ISeason[] seasons;

    /**
     * Constructs a League with the specified name and initializes with no seasons.
     * @param name Name of the league
     */
    public League(String name) {
        this.name = name;
        this.seasons = new ISeason[0];
    }

    /**
     * Constructs a League with the specified name and an array of seasons.
     * @param name Name of the league
     * @param seasons Array of seasons for the league
     */
    public League(String name, ISeason[] seasons) {
        this.name = name;
        if (seasons != null) {
            this.seasons = new ISeason[seasons.length];
            System.arraycopy(seasons, 0, this.seasons, 0, seasons.length);
        } else {
            this.seasons = new ISeason[0];
        }
    }

    /**
     * {@inheritDoc}
     * @return Name of the league
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * @return A copy of the array of seasons for this league
     */
    @Override
    public ISeason[] getSeasons() {
        ISeason[] copy = new ISeason[seasons.length];
        System.arraycopy(seasons, 0, copy, 0, seasons.length);
        return copy;
    }

    /**
     * {@inheritDoc}
     * Adds a season to the league if it does not already exist for the given year.
     *
     * @param season The season to add
     * @return true if successfully added
     * @throws IllegalArgumentException if season is null or already exists for the year
     */
    @Override
    public boolean createSeason(ISeason season) {
        if (season == null) {
            throw new IllegalArgumentException("Season Cannot Be Null.");
        }
        if (seasonExists(season.getYear())) {
            throw new IllegalArgumentException("Season For Year " + season.getYear() + " Already Exists.");
        }

        ISeason[] newSeasons = new ISeason[seasons.length + 1];
        System.arraycopy(seasons, 0, newSeasons, 0, seasons.length);
        newSeasons[seasons.length] = season;
        seasons = newSeasons;

        return true;
    }

    /**
     * {@inheritDoc}
     * Retrieves a season for the given year.
     *
     * @param year The year of the season to retrieve
     * @return The season for the given year
     * @throws IllegalArgumentException if the season is not found
     */
    @Override
    public ISeason getSeason(int year) {
        int index = indexOfSeason(year);
        if (index == -1) {
            throw new IllegalArgumentException("Season For Year " + year + " Not Found.");
        }
        return seasons[index];
    }

    /**
     * {@inheritDoc}
     * Removes a season for the given year.
     *
     * @param year The year of the season to remove
     * @return The removed season
     * @throws IllegalArgumentException if the season is not found
     */
    @Override
    public ISeason removeSeason(int year) {
        int index = indexOfSeason(year);
        if (index == -1) {
            throw new IllegalArgumentException("Season For Year " + year + " Not Found.");
        }

        ISeason removed = seasons[index];
        ISeason[] newSeasons = new ISeason[seasons.length - 1];
        int newIndex = 0;
        for (int i = 0; i < seasons.length; i++) {
            if (i != index) {
                newSeasons[newIndex++] = seasons[i];
            }
        }
        seasons = newSeasons;
        return removed;
    }

    /**
     * Checks if a season exists for the specified year.
     * @param year Year to check
     * @return true if season exists, false otherwise
     */
    private boolean seasonExists(int year) {
        return indexOfSeason(year) != -1;
    }

    /**
     * Returns the index of the season for the specified year, or -1 if not found.
     * @param year Year to search
     * @return Index of the season, or -1 if not found
     */
    private int indexOfSeason(int year) {
        for (int i = 0; i < seasons.length; i++) {
            if (seasons[i].getYear() == year) {
                return i;
            }
        }
        return -1;
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
