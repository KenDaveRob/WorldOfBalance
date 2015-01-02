package model;

// Java Imports
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.TimerTask;

// Other Imports
import core.GameClient;
import dataAccessLayer.PlayerDAO;
import dataAccessLayer.UserLogDAO;
import metadata.Constants;
import utility.GameTimer;

/**
 * The Player class holds important information about the player including, most
 * importantly, the account. Such information includes the username, password,
 * email, and the player ID.
 */
public class Player {

    private int player_id;
    private String username;
    private String password;
    private String email;
    private String first_name;
    private String last_name;
    private GameClient client; // References GameClient instance
    private Avatar avatar;
    private long play_time; // Total time the account has ever been active
    private long active_time;
    private long last_saved; // Last time saved to the database
    private GameTimer saveTimer;
    private String last_logout; // Last time the account has been logged in
    private int contextPosition; // 0 = Main Lobby, 1 = PvP Lobby, 2 = PvE Lobby
    private long lastActivity;

    public Player(int player_id) {
        this.player_id = player_id;

        last_saved = System.currentTimeMillis();
        saveTimer = new GameTimer();
    }

    public int getID() {
        return player_id;
    }

    public int setID(int player_id) {
        return this.player_id = player_id;
    }

    public String getEmail() {
        return email;
    }

    public String setEmail(String email) {
        return this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String setPassword(String password) {
        return this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String setUsername(String username) {
        return this.username = username;
    }

    public String getFirstName() {
        return first_name;
    }

    public String setFirstName(String first_name) {
        return this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String setLastName(String last_name) {
        return this.last_name = last_name;
    }

    public GameClient getClient() {
        return client;
    }

    public GameClient setClient(GameClient client) {
        return this.client = client;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public Avatar setAvatar(Avatar avatar) {
        return this.avatar = avatar;
    }

    public long getPlayTime() {
        return play_time;
    }
    
    public long setPlayTime(long play_time) {
        return this.play_time = play_time;
    }
    
    public long getActiveTime() {
        return active_time;
    }
    
    public long setActiveTime(long active_time) {
        return this.active_time = active_time;
    }
    
    public long getLastSaved() {
        return last_saved;
    }

    public long setLastSaved(long last_saved) {
        return this.last_saved = last_saved;
    }

    public void startSaveTimer() {
        final Player player_f = this;
        saveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                long current = System.currentTimeMillis();
                long seconds = (current - player_f.getLastSaved()) / 1000;

                player_f.setPlayTime(player_f.getPlayTime() + seconds);
                player_f.setLastSaved(current);

                try {
                    PlayerDAO.updatePlayTime(player_f.getID(), player_f.getPlayTime(), player_f.getActiveTime());
                    UserLogDAO.updateTimeLog(player_id, (int) seconds);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }, Constants.SAVE_INTERVAL, Constants.SAVE_INTERVAL);
    }

    public GameTimer getSaveTimer() {
        return saveTimer;
    }

    public String getLastLogout() {
        return last_logout;
    }

    public String setLastLogout(String last_logout) {
        return this.last_logout = last_logout;
    }
    
    public int getContextPosition() {
        return contextPosition;
    }
    
    public int setContextPosition(int contextPosition) {
        return this.contextPosition = contextPosition;
    }

    public void updateActiveTime() {
        if (lastActivity != 0) {
            active_time += (System.currentTimeMillis() - lastActivity) / 1000;
        }

        lastActivity = System.currentTimeMillis();
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
