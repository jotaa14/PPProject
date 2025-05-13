package model.player;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;

import java.io.IOException;
import java.time.LocalDate;

/**
 *
 * @author diogo
 */
public class Player implements IPlayer {
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
    private float height;
    private float weight;
    private PreferredFoot preferredFoot;

    public Player(String name, LocalDate birthDate, String nationality,
                  IPlayerPosition position, String photo, int number, int shooting, int passing,
                  int stamina, int speed, float height, float weight, PreferredFoot preferredFoot) {
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
        this.height = height;
        this.weight = weight;
        this.preferredFoot = preferredFoot;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LocalDate getBirthDate() {
        return birthDate;
    }

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

    @Override
    public String getNationality() {
        return nationality;
    }

    @Override
    public void setPosition(IPlayerPosition playerPosition) {
        if (playerPosition == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }
        this.position = playerPosition;
    }

    @Override
    public String getPhoto() {
        return photo;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int getShooting() {
        return shooting;
    }

    @Override
    public int getPassing() {
        return passing;
    }

    @Override
    public int getStamina() {
        return stamina;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public IPlayerPosition getPosition() {

        return position;
    }

    @Override
    public float getHeight() {

        return height;
    }

    @Override
    public float getWeight() {

        return weight;
    }

    @Override
    public PreferredFoot getPreferredFoot() {

        return preferredFoot;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Player)) return false;
        Player player = (Player) obj;
        return this.name.equals(player.name) && this.birthDate.equals(player.birthDate);
    }

    @Override
    public String toString() {
        return "Player: " + name + ", Age: " + getAge() + ", Nationality: " + nationality +
                ", Position: " + position + ", Number: " + number +
                ", Skills - Shooting: " + shooting + ", Passing: " + passing +
                ", Stamina: " + stamina + ", Speed: " + speed +
                ", Height: " + height + "m, Weight: " + weight + "kg, Preferred Foot: " + preferredFoot + "Strength" + getStrength();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getStrength() {
        return(shooting+speed+stamina+passing) / 4;
    }

    @Override
    public void exportToJson()
            throws IOException {

    }
}