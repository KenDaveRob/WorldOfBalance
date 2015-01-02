package worldManager.gameEngine;

/**
 *
 * @author Nathan Greco, Partap Aujla
 */
public class Disease {
    //disease ID
    //reference to diseaseType

    //Assuming this is suppose to be Disease "model"
    private int disease_id;
    private String name;
    private String description;
    private float infect_chance;
    private float spread_chance;
    private float death_rate;
    private float heal_chance;

    public Disease(int disease_id) {
        this.disease_id = disease_id;
    }

    public int getID() {
        return disease_id;
    }

    public void setID(int disease_id) {
        this.disease_id = disease_id;
    }

    public float getDeathRate() {
        return death_rate;
    }

    public void setDeathRate(float death_rate) {
        this.death_rate = death_rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getHealChance() {
        return heal_chance;
    }

    public void setHealChance(float heal_chance) {
        this.heal_chance = heal_chance;
    }

    public float getInfectChance() {
        return infect_chance;
    }

    public void setInfectChance(float infect_chance) {
        this.infect_chance = infect_chance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSpreadChance() {
        return spread_chance;
    }

    public void setSpreadChance(float spread_chance) {
        this.spread_chance = spread_chance;
    }
}
