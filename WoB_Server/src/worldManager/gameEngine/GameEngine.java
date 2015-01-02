package worldManager.gameEngine;

// Java Imports
import core.EventTypes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Other Imports
import core.GameResources;
import core.GameServer;
import core.LobbyManager;
import core.NetworkManager;
import core.Species;
import core.SpeciesGroup;
import utility.Vector3;
import dataAccessLayer.ShopDAO;
import dataAccessLayer.StatsDAO;
import dataAccessLayer.SpeciesChangeListDAO;
import dataAccessLayer.WorldDAO;
import dataAccessLayer.ZoneDAO;
import dataAccessLayer.ZoneSpeciesDAO;
import java.util.Map.Entry;
import metadata.Constants;
import model.Player;
import model.ShopItem;
import model.SpeciesType;
import model.World;
import networking.response.ResponsePrediction;
import networking.response.ResponseShop;
import networking.response.ResponseSpeciesCreate;
import networking.response.ResponseSpeciesKill;
import networking.response.ResponseUpdateTime;
import simulationEngine.PredictionRunnable;
import simulationEngine.SpeciesZoneType;
import utility.Log;
import utility.SpeciesComparator;
import worldManager.gameEngine.species.Animal;
import worldManager.gameEngine.species.Plant;
import worldManager.gameEngine.species.Organism;

/**
 * The GameEngine class is used to control the in-game time as well as
 * performing certain actions at specific time intervals for its assigned World.
 * Actions such as performing predictions and species interpolation. Other
 * methods contained in this class decides how an organism of a particular
 * species gets created and handled.
 */
public class GameEngine {

    private World world;
    private long gameScaleTime; //how many ingame seconds have passed
    private float gameScale; //how much _gameScaleTime passes in one real second
    private long lastRunTime;
    private int startMonth;
    private int currentMonth; // current gameScale day
    private Map<Integer, Zone> zones;
    private boolean isActive;
    private ExecutorService predictionThreadPool;
    private long lastSimulationTime;
    private Queue<PredictionRunnable> waitList;
    private int prevDay;

    public GameEngine(World world) {
        this.world = world;

        lastRunTime = System.currentTimeMillis();
        zones = new HashMap<Integer, Zone>();

        gameScaleTime = world.getSeconds();
        startMonth = world.getMonth();
        currentMonth = startMonth;
        prevDay = (int) (gameScaleTime / (Constants.MONTH_DURATION / 30)) + 1;

        gameScale = world.getTimeRate() * Constants.TIME_MODIFIER;

        predictionThreadPool = Executors.newCachedThreadPool();
        waitList = new LinkedList<PredictionRunnable>();
    }

    public int getWorldID() {
        return world.getID();
    }

    public World getWorld() {
        return world;
    }

    public Zone getZone(int zone_id) {
        return zones.get(zone_id);
    }
    
    public Map<Integer, Zone> getZones() {
        return zones;
    }
    
    public float getGameScale() {
        return gameScale;
    }
    
    public float setGameScale(float gameScale) {
        return this.gameScale = gameScale;
    }

    public long getGameScaleTime() {
        return gameScaleTime;
    }

    public void setGameScaleTime(long gameScaleTime) {
        this.gameScaleTime = gameScaleTime;
    }
    
    public List<Zone> getZoneList() {
        return new ArrayList<Zone>(zones.values());
    }

    public void addZone(Zone zone) {
        zone.setLastSimulationTime(gameScaleTime);
        zone.setGameEngine(this);

        zones.put(zone.getID(), zone);
    }
    
    public int getCurrentMonth() {
        return currentMonth;
    }

    public void start() {
        lastRunTime = System.currentTimeMillis();
        isActive = true;
    }

    public void run() {
        if (isActive) {
            long currentRunTime = System.currentTimeMillis();
            long seconds = (currentRunTime - lastRunTime) / 1000;
            world.setPlayTime(world.getPlayTime() + seconds);

            long time_diff = (long) (seconds * gameScale);

            if (time_diff > 0) {
                gameScaleTime += time_diff;

                int currentDay = (int) (gameScaleTime / (Constants.MONTH_DURATION / 30)) + 1;

                while (prevDay <= currentDay) {
                    int prevMonth = currentMonth;
                    currentMonth = startMonth + (int) (gameScaleTime / Constants.MONTH_DURATION);

                    if (currentMonth != prevMonth) {
                        LobbyManager.getInstance().getLobby(world).getEventHandler().execute(EventTypes.NEW_MONTH, world, currentMonth);
                        // Monthly Rewards
                        GameResources.updateCredits(world, 350);
                    }
                    
                    prevDay++;
                }

                world.setMonth(currentMonth);
                world.setYear((currentMonth - 1) / 12 + 1);
                world.setDays(currentDay % 30 + 1);
                world.setSeconds(gameScaleTime % Constants.MONTH_DURATION);

                try {
                    WorldDAO.updateTime(world);
                } catch (SQLException ex) {
                    Log.println_e(ex.getMessage());
                }

                lastRunTime = currentRunTime;
            }
        }
    }

    /**
     * Run a simulation for a given zone at the specific timestep.
     * 
     * @param world
     * @param currentTimeStep 
     */
    private void runSimulation(World world, int currentTimeStep) {
        Zone zone = world.getEnvironment().getZones().get(0);

        zone.updateScore();

        Map<Integer, Species> speciesList = zone.getSpeciesList();
        Map<Integer, Integer> newSpeciesNodeList = zone.getAddSpeciesList();

        PredictionRunnable runnable = new PredictionRunnable(this, zone, world.getSimulationEngine(), zone.getManipulationID(), currentTimeStep,
                speciesList, newSpeciesNodeList);
        waitList.add(runnable);

        if (waitList.size() == 1) {
            lastSimulationTime = runnable.initialize();
            predictionThreadPool.submit(runnable);
        }
    }

    /**
     * Run a simulation at the same timestep.
     */
    public void forceSimulation() {
        runSimulation(world, currentMonth);
    }
    
    /**
     * 
     * @param runnable 
     */
    public void updatePrediction(PredictionRunnable runnable) {
        long milliseconds = System.currentTimeMillis();

        Zone zone = runnable.getZone();
        // Remove species nodes that were just used
        for (Entry<Integer, Integer> entry : runnable.getNewSpeciesNodeList().entrySet()) {
            int node_id = entry.getKey(), biomass = entry.getValue();
            zone.removeNewSpeciesNode(node_id, biomass);
        }
        // Execute the most recent Prediction request; drop all before it.
        waitList.remove(runnable);
        if (!waitList.isEmpty()) {
            for (int i = 0; i < waitList.size() - 1; i++) {
                PredictionRunnable r = waitList.poll();
                Log.printf("Dropped Prediction Step [%d]", r.getID());
            }

            PredictionRunnable nextRunnable = waitList.poll();
            lastSimulationTime = nextRunnable.initialize();
            predictionThreadPool.submit(nextRunnable);
        }

        Log.printf("Running Prediction Step...");

        Map<Integer, Integer> nodeDifference = new HashMap<Integer, Integer>();
        Map<Integer, SpeciesZoneType> nextSpeciesNodeList = runnable.getNextSpeciesNodeList();

        runnable.createCSVs();

        try {
            Log.println("Interpreting Biomass Results...");
            // Determine the positive and negative change in biomass of species.
            Map<Integer, Integer> currentSpeciesNodeList = runnable.getCurrentSpeciesNodeList();

            for (SpeciesZoneType species : nextSpeciesNodeList.values()) {
                int node_id = species.getNodeIndex();
                int nextBiomass = (int) species.getCurrentBiomass();

                if (currentSpeciesNodeList.containsKey(node_id)) {
                    int currentBiomass = currentSpeciesNodeList.get(node_id);
                    nodeDifference.put(node_id, nextBiomass - currentBiomass);
                } else {
                    nodeDifference.put(node_id, nextBiomass);
                }
            }

            Map<Integer, Integer> speciesChangeList = new HashMap<Integer, Integer>();
            
            // Shuffle the order at when each species get processed.
            List<Integer> speciesList = new ArrayList<Integer>(runnable.getCurrentSpeciesList().keySet());
            Collections.shuffle(speciesList);
            // Adjust the number of species by creating or reducing the existing amount
            for (int species_id : speciesList) {
                SpeciesType speciesType = GameServer.getInstance().getSpecies(species_id);

                int gDiff = 0, rDiff = 0;
                boolean hasGrowth = true, hasReduced = true;

                for (int node_id : speciesType.getNodeList()) {
                    int diff = nodeDifference.get(node_id);

                    // Check Growth
                    if (diff > 0) {
                        gDiff = gDiff == 0 ? diff : Math.min(diff, gDiff);
                    } else {
                        hasGrowth = false;
                    }

                    // Check Reduction
                    if (diff < 0) {
                        rDiff = rDiff == 0 ? diff : Math.max(diff, rDiff);
                    } else {
                        hasReduced = false;
                    }
                }

                if (hasGrowth) {
                    Log.printf("  %s Species[%d] increased by %d", speciesType.getName(), speciesType.getID(), gDiff);

                    for (Entry<Integer, Float> entry : speciesType.getNodeDistribution().entrySet()) {
                        int node_id = entry.getKey();
                        float distribution = entry.getValue();

                        int biomass = (int) (gDiff * distribution);
                        nodeDifference.put(node_id, nodeDifference.get(node_id) - biomass);

                        Log.printf("    Node[%d] increased by %d", node_id, biomass);
                    }
                } else if (hasReduced) {
                    Log.printf("  %s Species[%d] decreased by %d", speciesType.getName(), speciesType.getID(), Math.abs(rDiff));

                    for (Entry<Integer, Float> entry : speciesType.getNodeDistribution().entrySet()) {
                        int node_id = entry.getKey();
                        float distribution = entry.getValue();

                        int biomass = (int) (rDiff * distribution);
                        nodeDifference.put(node_id, nodeDifference.get(node_id) - biomass);

                        Log.printf("    Node[%d] decreased by %d", node_id, Math.abs(biomass));
                    }
                }

                if (gDiff + rDiff != 0) {
                    speciesChangeList.put(species_id, gDiff + rDiff);
                    SpeciesChangeListDAO.createEntry(zone.getID(), species_id, gDiff + rDiff);
                }
            }

            zone.setSpeciesChangeList(speciesChangeList);
            
            ResponsePrediction responsePrediction = new ResponsePrediction();
            responsePrediction.setResults(speciesChangeList);
            NetworkManager.addResponseForLobby(LobbyManager.getInstance().getLobby(world).getID(), responsePrediction);

            ZoneDAO.updateBiomass(zone.getID(), zone.getMaxBiomass(), zone.getPrevBiomass(), zone.getTotalBiomass());

            zone.getEnvironment().updateEnvironmentScore();
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
            ex.printStackTrace();
        }

        Log.printf("Total Time (Prediction Step): %.2f seconds", Math.round((System.currentTimeMillis() - milliseconds) / 10.0) / 100.0);
    }

    public void initializeSpecies(Species species, Zone zone) {
        for (SpeciesGroup group : species.getGroups().values()) {
            ResponseSpeciesCreate response = new ResponseSpeciesCreate(Constants.CREATE_STATUS_DEFAULT, zone.getID(), group);
            NetworkManager.addResponseForWorld(world.getID(), response);
        }

        zone.addSpecies(species);
    }

    public void createSpeciesByPurchase(Player player, Map<Integer, Integer> speciesList, Zone zone) {
        for (Entry<Integer, Integer> entry : speciesList.entrySet()) {
            int species_id = entry.getKey(), biomass = entry.getValue();
            SpeciesType speciesType = GameServer.getInstance().getSpecies(species_id);

            for (int node_id : speciesType.getNodeList()) {
                zone.setNewSpeciesNode(node_id, biomass);
            }

            Species species = null;

            if (zone.containsSpecies(species_id)) {
                species = zone.getSpecies(species_id);

                for (SpeciesGroup group : species.getGroups().values()) {
                    group.setBiomass(group.getBiomass() + biomass / species.getGroups().size());

                    try {
                        ZoneSpeciesDAO.updateBiomass(group.getID(), group.getBiomass());
                    } catch (SQLException ex) {
                        Log.println_e(ex.getMessage());
                    }

                    ResponseSpeciesCreate response = new ResponseSpeciesCreate(Constants.CREATE_STATUS_DEFAULT, zone.getID(), group);
                    NetworkManager.addResponseForWorld(world.getID(), response);
                }
                
            } else {
                try {
                    int group_id = ZoneSpeciesDAO.createSpecies(zone.getID(), species_id, biomass, Vector3.zero);

                    species = new Species(species_id, speciesType);
                    SpeciesGroup group = new SpeciesGroup(species, group_id, biomass, Vector3.zero);
                    species.add(group);

                    ResponseSpeciesCreate response = new ResponseSpeciesCreate(Constants.CREATE_STATUS_DEFAULT, zone.getID(), group);
                    NetworkManager.addResponseForWorld(world.getID(), response);
                } catch (SQLException ex) {
                    Log.println_e(ex.getMessage());
                }
            }

            if (species != null) {
                zone.addSpecies(species);

                // Logging Purposes
                int player_id = player.getID(), zone_id = zone.getID();

                try {
                    StatsDAO.createStat(species_id, currentMonth, "Purchase", biomass, player_id, zone_id);
                } catch (SQLException ex) {
                    Log.println_e(ex.getMessage());
                }
            }
        }
    }

    public void end() {
        for (Zone zone : zones.values()) {
            if (zone.isEnable()) {
                zone.stopTimeActiveTimer();
            }
        }
    }
}
