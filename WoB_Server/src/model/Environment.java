package model;

// Java Imports
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Other Imports
import core.NetworkManager;
import dataAccessLayer.EnvironmentDAO;
import networking.response.ResponseUpdateEnvironmentScore;
import utility.Log;
import worldManager.gameEngine.Zone;

/**
 *
 * @author Xuyuan
 */
public class Environment {

    private int env_id;
    private int world_id;
    private List<Zone> zones;
    private int player_id;
    private int environmentScore;
    private int highEnvScore;
    private int accumEnvScore;

    public Environment(int env_id) {
        this.env_id = env_id;
        zones = new ArrayList<Zone>();
    }

    public int getID() {
        return env_id;
    }

    public int setID(int env_id) {
        return this.env_id = env_id;
    }

    public int getOwnerID() {
        return player_id;
    }

    public int setOwnerID(int player_id) {
        return this.player_id = player_id;
    }

    /**
     * Get the zone by its id.
     *
     * @param zoneID            The id of the zone
     * @return                  The target zone
     */
    public Zone getZoneByID(int zoneID) {
        for (Zone zone : zones) {
            if (zone.getID() == zoneID) {
                return zone;
            }
        }

        return null;
    }

    public int getWorldID() {
        return world_id;
    }

    public int setWorldID(int world_id) {
        return this.world_id = world_id;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public List<Zone> setZones(List<Zone> zones) {
        return this.zones = zones;
    }

    public boolean setZone(Zone zone) {
        return zones.add(zone);
    }

    public void setEnvironmentScore(int environmentScore) {
        this.environmentScore = environmentScore;
    }
    
    public int getHighEnvScore() {
        return highEnvScore;
    }
    
    public int setHighEnvScore(int score) {
        return this.highEnvScore = score;
    }

    public int getAccumulatedEnvScore() {
        return accumEnvScore;
    }

    public int setAccumulatedEnvScore(int score) {
        return this.accumEnvScore = score;
    }

    public void updateEnvironmentScore() {
        environmentScore = 0;

        for (Zone zone : zones) {
            environmentScore += zone.getEnvironmentScore();
        }
        
        if (environmentScore > highEnvScore) {
            highEnvScore = environmentScore;
        }

        try {
            EnvironmentDAO.updateEnvironmentScore(env_id, environmentScore, highEnvScore);
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        }

        ResponseUpdateEnvironmentScore responseUpdateEnvSc = new ResponseUpdateEnvironmentScore();
        responseUpdateEnvSc.setEnvID(env_id);
        responseUpdateEnvSc.setScore(environmentScore);
        NetworkManager.addResponseForWorld(world_id, responseUpdateEnvSc);
    }
    
    public void updateAccumEnvScore() {
        accumEnvScore += environmentScore;

        try {
            EnvironmentDAO.updateAccumEnvScore(env_id, accumEnvScore);
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        }
    }

    public int getEnvironmentScore() {
        return environmentScore;
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
