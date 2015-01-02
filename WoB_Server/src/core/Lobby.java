package core;

// Java Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Other Imports
import model.Player;
import model.World;

public class Lobby {

    private String lobby_id;
    private String name;
    private String password;
    private int host_id;
    private int max_players;
    private Map<Integer, Player> userList;
    private List<Player> readyList;
    private Map<String, String> settings;
    private World world;
    private EventHandler eventHandler;
    private ObjectiveManager objectiveManager;
    private Map<Integer, BadgeManager> badgeManagerList;
    private boolean isReady;

    public Lobby(String lobby_id) {
        this.lobby_id = lobby_id;

        max_players = 10;
        
        userList = new HashMap<Integer, Player>();
        readyList = new ArrayList<Player>();
        
        eventHandler = new EventHandler();
        objectiveManager = new ObjectiveManager(this, eventHandler);
        
        badgeManagerList = new HashMap<Integer, BadgeManager>();
    }

    public String getID() {
        return lobby_id;
    }

    public int getHostID() {
        return host_id;
    }

    public int setHostID(int host_id) {
        return this.host_id = host_id;
    }

    public boolean hasUser(int user_id) {
        return userList.containsKey(user_id);
    }

    public Map<Integer, Player> getUserList() {
        return userList;
    }

    public boolean add(Player player) {
        boolean status = userList.size() < max_players;

        if (status) {
            userList.put(player.getID(), player);
        }

        return status;
    }

    public void remove(int user_id) {
        if (userList.containsKey(user_id)) {
            userList.remove(user_id);
        }
    }

    public boolean setReady(int user_id, boolean status) {
        Player player = userList.get(user_id);

        if (status) {
            if (!readyList.contains(player)) {
                readyList.add(player);
            }
        } else {
            if (readyList.contains(player)) {
                readyList.remove(player);
            }
        }

        return isReady = readyList.size() == userList.size();
    }
    
    public boolean isReady() {
        return isReady;
    }

    public void setBadgeManagerList(Map<Integer, BadgeManager> badgeManagerList) {
        this.badgeManagerList = badgeManagerList;
    }
    
    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getSize() {
        return userList.size();
    }

    public int getCapacity() {
        return max_players;
    }
    
    public EventHandler getEventHandler() {
        return eventHandler;
    }
    
    public ObjectiveManager getObjectiveManager() {
        return objectiveManager;
    }

    public void destroy() {
        objectiveManager.stopNextObjectiveTimer();
    }
}
