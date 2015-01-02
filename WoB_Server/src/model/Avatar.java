package model;

import java.lang.reflect.Field;

/**
 *
 * @author Xuyuan
 */
public class Avatar {

    private int avatar_id;
    private String name;
    private int avatarType;
    private int experience;
    private int abilityPoints;
    private int currency;
    private int teamNo;
    private int level;
    private int player_id;
    private String last_played;

    public Avatar(int avatar_id) {
        this.avatar_id = avatar_id;
    }

    public int getID() {
        return avatar_id;
    }

    public int setID(int avatar_id) {
        return this.avatar_id = avatar_id;
    }
    
    public String getName() {
        return name;
    }
    
    public String setName(String name) {
        return this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public int setLevel(int level) {
        return this.level = level;
    }

    public int getPlayerID() {
        return player_id;
    }

    public int setPlayerID(int player_id) {
        return this.player_id = player_id;
    }

    public int getAbilityPoints() {
        return abilityPoints;
    }

    public int setAbilityPoints(int abilityPoints) {
        return this.abilityPoints = abilityPoints;
    }

    public int getAvatarType() {
        return avatarType;
    }

    public int setAvatarType(int avatarType) {
        return this.avatarType = avatarType;
    }

    public int getCurrency() {
        return currency;
    }

    public int setCurrency(int currency) {
        return this.currency = currency;
    }

    public int getExperience() {
        return experience;
    }

    public int setExperience(int experience) {
        return this.experience = experience;
    }

    public int getTeamNo() {
        return teamNo;
    }

    public int setTeamNo(int teamNo) {
        return this.teamNo = teamNo;
    }

    public String getLastPlayed() {
        return last_played;
    }

    public String setLastPlayed(String last_played) {
        return this.last_played = last_played;
    }

    public boolean spendCash(int value) {
        if (value <= currency) {
            currency = Math.max(0, currency - value);
            return true;
        }

        return false;
    }

    public boolean spendAbilityPoints(int value) {
        if (value <= abilityPoints) {
            abilityPoints = Math.max(0, abilityPoints - value);
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String str = "";

        str += "-----" + "\n";
        str += getClass().getName() + "\n";
        str += "\n";

        for (Field field : getClass().getDeclaredFields()) {
            try {
                str += field.getName() + " - " + field.get(this) + "\n";
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        str += "-----";

        return str;
    }
}
