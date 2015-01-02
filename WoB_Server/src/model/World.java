package model;

// Java Imports
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

// Other Imports
import core.EventTypes;
import core.GameResources;
import core.GameServer;
import core.LobbyManager;
import core.NetworkManager;
import core.WorldManager;
import dataAccessLayer.WorldDAO;
import metadata.Constants;
import networking.response.ResponseShopAction;
import simulationEngine.SimulationEngine;
import utility.GameTimer;
import worldManager.gameEngine.GameEngine;
import worldManager.gameEngine.Zone;

public class World {

    private int world_id;
    private int creatorPlayerID;
    private String gameName;
    private int credits;
    private long seconds;
    private int year;
    private int month;
    private int days;
    private int maxPlayers;
    private String envType;
    private short accessType;
    private short gameMode;
    private String password;
    private Environment environment;
    private boolean hasStarted;
    private List<Player> playerList;
    private GameEngine gameEngine;
    private SimulationEngine simulationEngine;
    private float time_rate;
    private long play_time;
    private String last_played;
    private Map<Integer, Integer> shopList;
    private GameTimer shopTimer;

    public World(int world_id) {
        this.world_id = world_id;

        seconds = 1;
        year = 1;
        month = 1;
        days = 1;
        password = "";

        time_rate = 1.0f;

        playerList = new ArrayList<Player>();

        simulationEngine = new SimulationEngine();
        
        shopList = new HashMap<Integer, Integer>();
        shopTimer = new GameTimer();
    }

    public Integer getID() {
        return world_id;
    }

    public Integer setID(int world_id) {
        return this.world_id = world_id;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public int setCredits(int credits) {
        return this.credits = credits;
    }

    public float getTimeRate() {
        return time_rate;
    }

    public float setTimeRate(float time_rate) {
        return this.time_rate = time_rate;
    }

    public int getCreatorID() {
        return creatorPlayerID;
    }

    public int setCreatorID(int creatorPlayerID) {
        return this.creatorPlayerID = creatorPlayerID;
    }

    public short getAccessType() {
        return accessType;
    }

    public short setAccessType(short accessType) {
        return this.accessType = accessType;
    }

    public int getDays() {
        return days;
    }

    public int setDays(int days) {
        return this.days = days;
    }

    public String getEnvType() {
        return envType;
    }

    public String setEnvType(String envType) {
        return this.envType = envType;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public short getGameMode() {
        return gameMode;
    }

    public short setGameMode(short gameMode) {
        return this.gameMode = gameMode;
    }

    public String getGameName() {
        return gameName;
    }

    public String setGameName(String gameName) {
        return this.gameName = gameName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int setMaxPlayers(int maxPlayers) {
        return this.maxPlayers = maxPlayers;
    }

    public String getPassword() {
        return password;
    }

    public String setPassword(String password) {
        return this.password = password;
    }

    public long getPlayTime() {
        return play_time;
    }
    
    public long setPlayTime(long play_time) {
        return this.play_time = play_time;
    }

    public long getSeconds() {
        return seconds;
    }

    public long setSeconds(long seconds) {
        return this.seconds = seconds;
    }
    
    public int getYear() {
        return year;
    }
    
    public int setYear(int year) {
        return this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public int setMonth(int month) {
        return this.month = month;
    }

    public String getLastPlayed() {
        return last_played;
    }

    public String setLastPlayed(String last_played) {
        return this.last_played = last_played;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public boolean setHasStarted(boolean hasStarted) {
        return this.hasStarted = hasStarted;
    }

    public List<Player> getPlayers() {
        return playerList;
    }

    public Player getPlayer(int player_id) {
        for (Player player : playerList) {
            if (player.getID() == player_id) {
                return player;
            }
        }

        return null;
    }

    public boolean hasPlayer(int player_id) {
        return getPlayer(player_id) != null;
    }

    public boolean setPlayer(Player player) {
        return playerList.add(player);
    }

    public void removePlayer(int player_id) {
        for (Player player : playerList) {
            if (player.getID() == player_id) {
                playerList.remove(player);
                break;
            }
        }

        if (playerList.isEmpty()) {
            end();
            WorldManager.getInstance().remove(world_id);
        }

        try {
            WorldDAO.updateLastPlayed(world_id);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public GameEngine setGameEngine(GameEngine gameEngine) {
        return this.gameEngine = gameEngine;
    }
    
    public void removeGameEngine() {
        if (gameEngine != null) {
            gameEngine.end();
            gameEngine = null;
        }
    }

    public void end() {
        try {
            removeGameEngine();
            WorldDAO.updateTime(this);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public SimulationEngine getSimulationEngine() {
        return simulationEngine;
    }

    public boolean useCredits(int amount) {
        if (amount <= credits) {
            credits -= amount;
            GameResources.updateCredits(this, 0);

            LobbyManager.getInstance().getLobby(this).getEventHandler().execute(EventTypes.CREDITS_SPENT, amount);
            return true;
        }

        return false;
    }

    /**
     * Create new and merge existing purchases until a given time frame is up.
     * 
     * @param itemList
     * @return 
     */
    public int createShopOrder(Map<Integer, Integer> itemList, Player player) {
        int totalCost = 0;
        
        // Determine the total cost of purchase
        for (int item_id : itemList.keySet()) {
            SpeciesType species = GameServer.getInstance().getSpecies(item_id);
            
            if (species != null) {
                int biomass = itemList.get(item_id);
                totalCost += species.getCost() * Math.ceil(biomass / species.getAvgBiomass());
            } else {
                return -1;
            }
        }

        if (useCredits(totalCost)) {
            LobbyManager.getInstance().getLobby(this).getEventHandler().execute(EventTypes.SPECIES_BOUGHT, itemList.size());

            int totalBiomass = 0;
            for (int item_id : itemList.keySet()) {
                SpeciesType species = GameServer.getInstance().getSpecies(item_id);

                if (species != null) {
                    totalBiomass += itemList.get(item_id);
                }
            }
            LobbyManager.getInstance().getLobby(this).getEventHandler().execute(EventTypes.BIOMASS_BOUGHT, totalBiomass);

            // Create a new timer, if none exist.
            if (shopTimer.getTask() == null || shopTimer.getTimeRemaining() <= 0) {
                // Timer Declaration Start
                final World world_f = this;
                final Player player_f = player;
                shopTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        world_f.processShopOrder(player_f);
                    }
                }, Constants.SHOP_PROCESS_DELAY);
                // End
            }
            // Insert these item values into the hashmap
            for (int item_id : itemList.keySet()) {
                int amount = itemList.get(item_id);
                // New item
                if (shopList.containsKey(item_id)) {
                    amount += shopList.get(item_id);
                }

                shopList.put(item_id, amount);
            }
        } else {
            totalCost = -1;
        }

        return totalCost;
    }

    /**
     * Processes all pending purchases.
     */
    public void processShopOrder(Player player) {
        // Retrieve starting Zone
        List<Integer> zones = new ArrayList<Integer>(gameEngine.getZones().keySet());
        Collections.sort(zones);
        Zone zone = gameEngine.getZone(zones.get(0));

        gameEngine.createSpeciesByPurchase(player, shopList, zone);
        gameEngine.forceSimulation();

        String tempList = "";

        int index = 0;
        for (Entry<Integer, Integer> entry : shopList.entrySet()) {
            tempList += entry.getKey() + ":" + entry.getValue();

            if (index++ < shopList.size() - 1) {
                tempList += ",";
            }
        }

        ResponseShopAction response = new ResponseShopAction();
        response.setStatus((short) 2);
        response.setItems(tempList);
        NetworkManager.addResponseForUser(player.getID(), response);

        shopList.clear();
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
