package model.player;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import data.Exporter;

import java.io.FileWriter;
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
    private int defense;
    private int goalkeeping;
    private float height;
    private float weight;
    private PreferredFoot preferredFoot;
    private String clubCode;

    public Player(String name, LocalDate birthDate, String nationality,
                  IPlayerPosition position, String photo, int number, int shooting, int passing,
                  int stamina, int speed, int defense, int goalkeeping,  float height, float weight, PreferredFoot preferredFoot,  String clubCode) {
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

    public int getDefense() {
        return defense;
    }
    public int getGoalkeeping() {
        return goalkeeping;
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

    public  String getClub() {
        return this.clubCode;
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
        return "Player: " + name + " \nAge: " + getAge() + " \nNationality: " + nationality +
                " \nPosition: " + position.getDescription() + " \nNumber: " + number +
                " \nSkills: \n-Shooting: " + shooting + " \n-Passing: " + passing +
                " \n-Stamina: " + stamina + " \n-Speed: " + speed + "\n-Defense: " + defense + " \n-Goalkeeping: " + goalkeeping +
                " \n-Height: " +  String.format("%.2f", height) + "m \n-Weight: " +  String.format("%.2f", weight) +
                "kg \n-Preferred Foot: " + preferredFoot + "  \n-Strength: " + getStrength() + "\n";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getStrength() {
        return(shooting+speed+stamina+passing+defense+goalkeeping) / 5;
    }

    @Override
    public void exportToJson() throws IOException {
        Exporter.exportPlayer(this);
    }
}