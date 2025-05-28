package model.player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import data.Exporter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Represents a football player with physical, technical, and club information.
 * Implements the IPlayer interface, supports cloning, and can export to JSON.
 * This class encapsulates all necessary attributes and methods to manage a player's data and behavior.
 *
 * @author Diogo Fernando Águia Costa
 * @author João Pedro Martins Ribeiro
 */
public class Player implements IPlayer, Cloneable {
    private String name;
    private LocalDate birthDate;
    private String nationality;
    private IPlayerPosition position;
    private String photo;
    private int number;
    private int shooting;
    private int passing;
    private int stamina;
    private int speed;
    private int defense;
    private int goalkeeping;
    private float height;
    private float weight;
    private PreferredFoot preferredFoot;
    private String clubCode;
    private int yellowCards = 0;
    private boolean sentOff = false;

    /**
     * Full constructor to create a player.
     *
     * @param name Player's name
     * @param birthDate Date of birth
     * @param nationality Nationality
     * @param position Main playing position (implements IPlayerPosition)
     * @param photo Path or URL to the player's photo
     * @param number Jersey number
     * @param shooting Shooting skill (0-99)
     * @param passing Passing skill (0-99)
     * @param stamina Stamina (0-99)
     * @param speed Speed (0-99)
     * @param defense Defensive skill (0-99)
     * @param goalkeeping Goalkeeping skill (0-99)
     * @param height Height in meters
     * @param weight Weight in kg
     * @param preferredFoot Preferred foot (LEFT/RIGHT/BOTH)
     * @param clubCode Current club code
     */
    public Player(String name, LocalDate birthDate, String nationality,
                  IPlayerPosition position, String photo, int number, int shooting, int passing,
                  int stamina, int speed, int defense, int goalkeeping, float height, float weight,
                  PreferredFoot preferredFoot, String clubCode) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.position = position;
        this.photo = photo;
        this.number = number;
        this.shooting = shooting;
        this.passing = passing;
        this.stamina = stamina;
        this.speed = speed;
        this.defense = defense;
        this.goalkeeping = goalkeeping;
        this.height = height;
        this.weight = weight;
        this.preferredFoot = preferredFoot;
        this.clubCode = clubCode;
    }

    /**
     * Gets the player's name.
     * @return Name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the player's date of birth.
     * @return Date of birth
     */
    @Override
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Calculates the player's age.
     * @return Age in years
     */
    @Override
    public int getAge() {
        LocalDate now = LocalDate.now();
        int years = now.getYear() - birthDate.getYear();
        if (now.getMonthValue() < birthDate.getMonthValue() ||
                (now.getMonthValue() == birthDate.getMonthValue() && now.getDayOfMonth() < birthDate.getDayOfMonth())) {
            years--;
        }
        return years;
    }

    /**
     * Gets the player's nationality.
     * @return Nationality
     */
    @Override
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets the player's main position.
     * @param playerPosition New position (cannot be null)
     * @throws IllegalArgumentException if position is null
     */
    @Override
    public void setPosition(IPlayerPosition playerPosition) {
        if (playerPosition == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }
        this.position = playerPosition;
    }

    /**
     * Gets the path or URL to the player's photo.
     * @return Photo
     */
    @Override
    public String getPhoto() {
        return photo;
    }

    /**
     * Gets the jersey number.
     * @return Number
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Gets the shooting skill.
     * @return Shooting (0-99)
     */
    @Override
    public int getShooting() {
        return shooting;
    }

    /**
     * Gets the passing skill.
     * @return Passing (0-99)
     */
    @Override
    public int getPassing() {
        return passing;
    }

    /**
     * Gets the stamina value.
     * @return Stamina (0-99)
     */
    @Override
    public int getStamina() {
        return stamina;
    }

    /**
     * Gets the speed value.
     * @return Speed (0-99)
     */
    @Override
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the defensive skill.
     * @return Defense (0-99)
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Gets the goalkeeping skill.
     * @return Goalkeeping (0-99)
     */
    public int getGoalkeeping() {
        return goalkeeping;
    }

    /**
     * Gets the player's main position.
     * @return Position (IPlayerPosition)
     */
    @Override
    public IPlayerPosition getPosition() {
        return position;
    }

    /**
     * Gets the player's height.
     * @return Height in meters
     */
    @Override
    public float getHeight() {
        return height;
    }

    /**
     * Gets the player's weight.
     * @return Weight in kg
     */
    @Override
    public float getWeight() {
        return weight;
    }

    /**
     * Gets the player's preferred foot.
     * @return PreferredFoot (LEFT/RIGHT/BOTH)
     */
    @Override
    public PreferredFoot getPreferredFoot() {
        return preferredFoot;
    }

    /**
     * Gets the current club code.
     * @return Club code
     */
    public String getClub() {
        return this.clubCode;
    }

    /**
     * Compares players by name and date of birth.
     * @param obj Object to compare
     * @return true if name and birth date are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Player)) return false;
        Player player = (Player) obj;
        return this.name.equals(player.name) && this.birthDate.equals(player.birthDate);
    }

    /**
     * Returns a formatted string with the player's main attributes.
     * @return String with player details
     */
    @Override
    public String toString() {
        return "Player: " + name + " \nAge: " + getAge() + " \nNationality: " + nationality +
                " \nPosition: " + position.getDescription() + " \nNumber: " + number +
                " \nSkills: \n-Shooting: " + shooting + " \n-Passing: " + passing +
                " \n-Stamina: " + stamina + " \n-Speed: " + speed + "\n-Defense: " + defense + " \n-Goalkeeping: " + goalkeeping +
                " \n-Height: " +  String.format("%.2f", height) + "m \n-Weight: " +  String.format("%.2f", weight) +
                "kg \n-Preferred Foot: " + preferredFoot + "  \n-Strength: " + getStrength() + "\n";
    }

    /**
     * Creates a shallow copy of the Player object.
     * @return New cloned Player object
     * @throws CloneNotSupportedException if cloning fails
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Calculates the player's strength for a specific position, using different weights for each attribute.
     * <ul>
     *   <li>GOALKEEPER: 2×goalkeeping + defense + stamina + speed + shooting + 2×passing</li>
     *   <li>DEFENDER: goalkeeping + 2×defense + stamina + speed + shooting + 2×passing</li>
     *   <li>MIDFIELDER: goalkeeping + defense + 2×stamina + speed + shooting + 2×passing</li>
     *   <li>FORWARD: goalkeeping + defense + stamina + 2×speed + 2×shooting + passing</li>
     * </ul>
     * @param position Position for calculation
     * @return Strength (0-99)
     */
    public int getStrengthByType(IPlayerPosition position) {
        int playerStatus = 0;
        String desc = position.getDescription();
        switch (desc) {
            case "GOALKEEPER":
                playerStatus = ((goalkeeping * 2 + defense + stamina + speed + shooting + passing * 2) / 7);
                break;
            case "DEFENDER":
                playerStatus = ((goalkeeping + defense * 2 + stamina+ speed + shooting + passing * 2) / 7);
                break;
            case "MIDFIELDER":
                playerStatus = ((goalkeeping + defense + stamina * 2 + speed + shooting+ passing * 2) / 7);
                break;
            case "FORWARD":
                playerStatus = ((goalkeeping + defense + stamina + speed * 2 + shooting * 2 + passing) / 7);
                break;
            default:
                playerStatus = 0;
                break;
        }
        return playerStatus;
    }

    /**
     * Calculates the player's strength in their main position.
     * @return Strength (0-99)
     */
    public int getStrength() {
        return getStrengthByType(this.position);
    }

    /**
     * Increments the number of yellow cards for the player.
     */
    public void addYellowCard() {
        yellowCards++;
    }

    /**
     * Gets the number of yellow cards the player has received.
     * @return Number of yellow cards
     */
    public int getYellowCards() {
        return yellowCards;
    }

    /**
     * Checks if the player has been sent off.
     * @return true if sent off, false otherwise
     */
    public boolean isSentOff() {
        return sentOff;
    }

    /**
     * Marks the player as sent off.
     */
    public void sendOff() {
        sentOff = true;
    }

    /**
     * Exports the player's data to a JSON file using the Exporter class.
     * @throws IOException if writing fails
     */
    @Override
    public void exportToJson() throws IOException {
        String player = "Player :{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"birthdate\": \"" + birthDate + "\",\n" +
                "  \"nationality\": \"" + nationality + "\",\n" +
                "  \"photo\": \"" + photo + "\",\n" +
                "  \"number\": " + number + ",\n" +
                "  \"passing\": " + passing + ",\n" +
                "  \"shooting\": " + shooting + ",\n" +
                "  \"speed\": " + speed + ",\n" +
                "  \"stamina\": " + stamina + ",\n" +
                "  \"defense\": " + defense + ",\n" +
                "  \"goalkeeping\": " + goalkeeping + ",\n" +
                "  \"weight\": " + weight + ",\n" +
                "  \"height\": " + height + ",\n" +
                "  \"position\": \"" + position.getDescription() + "\",\n" +
                "  \"foot\": \"" + preferredFoot.getPreferredFoot() + "\",\n" +
                "  \"clubCode\": \"" + clubCode + "\"\n" +
                "}";
        System.out.println(player);
    }
}
