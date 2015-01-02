package core;

// Java Imports
import utility.Vector3;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

// Other Imports
import dataAccessLayer.BiomassCSVDAO;
import dataAccessLayer.EnvironmentDAO;
import dataAccessLayer.LogDAO;
import dataAccessLayer.NewsDAO;
import dataAccessLayer.ScoreCSVDAO;
import dataAccessLayer.SpeciesChangeListDAO;
import dataAccessLayer.WorldDAO;
import dataAccessLayer.ZoneDAO;
import dataAccessLayer.ZoneNodeAddDAO;
import dataAccessLayer.ZoneSpeciesDAO;
import dataAccessLayer.ZoneTypeDAO;
import java.util.Map.Entry;
import metadata.Constants;
import model.Environment;
import model.SpeciesType;
import model.World;
import networking.response.ResponseChat;
import networking.response.ResponseCreateEnv;
import networking.response.ResponseGetPlayers;
import networking.response.ResponseSpeciesCreate;
import networking.response.ResponseWorldMenuAction;
import simulationEngine.SimulationEngine;
import simulationEngine.SpeciesZoneType;
import utility.CSVParser;
import utility.GameTimer;
import utility.Log;
import worldManager.gameEngine.GameEngine;
import worldManager.gameEngine.Zone;
import worldManager.gameEngine.species.Animal;
import worldManager.gameEngine.species.Organism;
import worldManager.gameEngine.species.Plant;

public class WorldManager {

    // Singleton Instance
    public static WorldManager manager;
    // Reference Tables
    private Map<Integer, World> worlds = new HashMap<Integer, World>(); // World ID -> World

    public WorldManager() {
    }

    public static WorldManager getInstance() {
        if (manager == null) {
            manager = new WorldManager();
        }

        return manager;
    }

    public World add(World world) {
        return worlds.put(world.getID(), world);
    }

    public World get(int world_id) {
        return worlds.get(world_id);
    }

    public World remove(int world_id) {
        return worlds.remove(world_id);
    }

    /**
     * Get the threads of all the players in the world
     *
     * @param world_id The id of the world
     * @return The list of threads of the players in the world
     */
    public List<GameClient> getClientsByWorld(int world_id) {
        List<GameClient> clients = new ArrayList<GameClient>();

        for (GameClient client : GameServer.getInstance().getActiveThreads().values()) {
            if (client.getActiveObject(World.class) != null) {
                if (client.getActiveObject(World.class).getID() == world_id) {
                    clients.add(client);
                }
            }
        }

        return clients;
    }

    /**
     * Create World.
     * Uses: RequestCreateNewWorld
     * 
     * @param world
     * @throws SQLException 
     */
    public static void createWorld(World world) throws SQLException {
        // World into Database
        int world_id = WorldDAO.createWorld(world);
        world.setID(world_id);
        // Create Environment
        Environment env = new Environment(-1);
        env.setOwnerID(world.getCreatorID());
        env.setWorldID(world_id);
        // Environment into Database
        int env_id = EnvironmentDAO.createEnvironment(env);
        env.setID(env_id);
        // Create Zone
        Zone zone = new Zone(-1);
        zone.setOrder(env.getZones().size());
        zone.setEnvironment(env);
        zone.setEnvID(env_id);
        zone.setType(new Random().nextInt(3) + 1);
        // Zone into Database
        int zone_id = ZoneDAO.createZone(zone);
        zone.setID(zone_id);
        // Set References
        env.setZone(zone);
        world.setEnvironment(env);
    }

    public static Map<Integer, Integer> convertSpeciesToNodes(Map<Integer, Integer> speciesBiomassList) {
        Map<Integer, Integer> nodeBiomassList = new HashMap<Integer, Integer>();

        for (Entry<Integer, Integer> entry : speciesBiomassList.entrySet()) {
            int species_id = entry.getKey(), biomass = entry.getValue();

            SpeciesType speciesType = GameServer.getInstance().getSpecies(species_id);

            if (speciesType != null) {
                for (Entry<Integer, Float> nodeDistribution : speciesType.getNodeDistribution().entrySet()) {
                    int node_id = nodeDistribution.getKey();
                    float distribution = nodeDistribution.getValue();

                    if (nodeBiomassList.containsKey(node_id)) {
                        nodeBiomassList.put(node_id, nodeBiomassList.get(node_id) + Math.round(biomass * distribution));
                    } else {
                        nodeBiomassList.put(node_id, Math.round(biomass * distribution));
                    }
                }
            }
        }

        return nodeBiomassList;
    }

    /**
     * Create Ecosystem
     * Uses: RequestSpeciesAction
     * 
     * @param world
     * @param speciesList
     * @throws SQLException 
     */
    public static void createEcosystem(World world, Map<Integer, Integer> speciesList) throws SQLException {
        // Grab Default Main Zone
        Zone zone = world.getEnvironment().getZones().get(0);
        // Map Species IDs to Node IDs
        Map<Integer, Integer> nodeBiomassList = WorldManager.convertSpeciesToNodes(speciesList);
        // Perform Web Services
        createWebServices(world, zone, nodeBiomassList);
        // Update Environment Score
        double biomass = 0;

        for (Map.Entry<Integer, Integer> entry : speciesList.entrySet()) {
            SpeciesType speciesType = GameServer.getInstance().getSpecies(entry.getKey());
            biomass += speciesType.getAvgBiomass() * Math.pow(entry.getValue() / speciesType.getAvgBiomass(), speciesType.getTrophicLevel());
        }

        if (biomass > 0) {
            biomass = Math.round(Math.log(biomass) / Math.log(2)) * 5;
        }

        int env_score = (int) Math.round(Math.pow(biomass, 2) + Math.pow(speciesList.size(), 2));
        EnvironmentDAO.updateEnvironmentScore(world.getEnvironment().getID(), env_score, env_score);
        // Generate CSVs from Web Services
        createCSVs(world);
        // Logging Purposes Only
        {
            String tempList = "";
            for (Entry<Integer, Integer> entry : speciesList.entrySet()) {
                tempList += entry.getKey() + ":" + entry.getValue() + ",";
            }
            LogDAO.createInitialSpecies(zone.getEnvironment().getOwnerID(), zone.getID(), tempList);
        }
    }

    /**
     * Create Web Services
     * Uses: WorldManager, createEcosystem()
     * 
     * @param world
     * @param zone
     * @param nodeBiomassList
     * @throws SQLException 
     */
    private static void createWebServices(World world, Zone zone, Map<Integer, Integer> nodeBiomassList) throws SQLException {
        Log.println("Creating Web Services...");
        // Prepare Web Services
        SimulationEngine se = world.getSimulationEngine();
        String networkName = "WoB-" + zone.getEnvID() + "." + zone.getOrder() + "-" + System.currentTimeMillis() % 100000;
        // Create Sub-Foodweb
        int[] nodeList = new int[nodeBiomassList.size()];
        int i = 0;
        for (int node_id : nodeBiomassList.keySet()) {
            nodeList[i++] = node_id;
        }
        zone.setManipulationID(se.createAndRunSeregenttiSubFoodweb(nodeList, networkName, 0, 0, false));
        // Update Zone Database
        ZoneDAO.updateManipulationID(zone.getID(), zone.getManipulationID());
        // Initialize Biomass and Additional Parameters
        List<SpeciesZoneType> mSpecies = new ArrayList<SpeciesZoneType>();
        for (Entry<Integer, Integer> entry : nodeBiomassList.entrySet()) {
            int node_id = entry.getKey(), biomass = entry.getValue();
            mSpecies.add(se.createSpeciesZoneType(node_id, biomass));
        }
        se.setParameters2(mSpecies, 1, zone.getManipulationID());
        // First Month Logic
        for (SpeciesZoneType szt : mSpecies) {
            int species_id = GameServer.getInstance().getSpeciesTypeByNodeID(szt.getNodeIndex()).getID();
            ZoneSpeciesDAO.createSpecies(zone.getID(), species_id, (int) szt.getCurrentBiomass(), Vector3.zero);
        }
    }

    /**
     * Create CSVs
     * Uses: WorldManager, createEcosystem()
     * 
     * @param zone 
     */
    private static void createCSVs(final World world) {
        new GameTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                Zone zone = world.getEnvironment().getZones().get(0);
                String csv = world.getSimulationEngine().getBiomassCSVString(zone.getManipulationID());

                if (!csv.isEmpty()) {
                    try {
                        BiomassCSVDAO.createCSV(zone.getManipulationID(), CSVParser.removeNodesFromCSV(csv));
                        // Generate Environment Score CSV
                        List<List<String>> envScoreList = new ArrayList<List<String>>(2);
                        envScoreList.add(new ArrayList<String>(Arrays.asList(new String[]{"", "1"})));
                        envScoreList.add(new ArrayList<String>(Arrays.asList(new String[]{"\"Environment Score\"", "0"})));
                        ScoreCSVDAO.createCSV(zone.getID(), CSVParser.createCSV(envScoreList));
                    } catch (SQLException ex) {
                        Log.println_e(ex.getMessage());
                    }

                    cancel();
                    Log.printf("CSV [%s] Retrieval Success!", zone.getManipulationID());
                } else {
                    Log.printf_e("Error: CSV [%s] Retrieval Failed!", zone.getManipulationID());
                }
            }
        }, 1000, 3000);
    }

    /**
     * Create Existing World
     * Uses: RequestJoinWorld
     * 
     * @param world_id
     * @return 
     */
    public World createExistingWorld(int world_id) {
        World world = null;

        try {
            world = WorldDAO.getWorld(world_id);

            if (world != null) {
                world.setEnvironment(EnvironmentDAO.getEnvironmentByWorldID(world.getID()));

                add(world);
            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        }

        return world;
    }

    /**
     * Enter World
     * Uses: RequestJoinWorld
     * 
     * @param world
     * @param client
     * @throws SQLException 
     */
    public static void enterWorld(World world, GameClient client) throws SQLException {
        // Create Game Engine
        GameEngine engine = new GameEngine(world);
        world.setGameEngine(engine);
        // Initialize Game Engine
        for (Zone zone : world.getEnvironment().getZones()) {
            engine.addZone(zone);
        }
        // Switch Client to World
        ResponseWorldMenuAction responseJoinWorld = new ResponseWorldMenuAction();
        responseJoinWorld.setAction((short) 2);
        responseJoinWorld.setStatus((short) 0);
        responseJoinWorld.setWorld(world);
        client.addResponseForUpdate(responseJoinWorld);
        // Enter World
        world.setPlayer(client.getPlayer());
        client.putActiveObject(World.class, world);
        WorldDAO.updateLastPlayed(world.getID());
        // Additional Features
        getServerAnnouncement(client);
        getOnlineMessage(client);
        getOnlinePlayers(client);
    }

    /**
     * Initialize Environment
     * Uses: RequestJoinWorld
     * 
     * @param world
     * @param client
     * @throws SQLException 
     */
    public static void initializeEnvironment(World world, GameClient client) throws SQLException {
        ResponseCreateEnv responseCreateEnv = new ResponseCreateEnv();
        responseCreateEnv.setEnvironment(world.getEnvironment());
        client.addResponseForUpdate(responseCreateEnv);

        for (Zone zone : world.getEnvironment().getZones()) {
            zone.setScoreCSV(CSVParser.convertCSVtoArrayList(ScoreCSVDAO.getCSV(zone.getID())));

            for (Species species : ZoneSpeciesDAO.getSpecies(zone.getID())) {
                world.getGameEngine().initializeSpecies(species, zone);
            }

            zone.setSpeciesChangeList(SpeciesChangeListDAO.getList(zone.getID()));
            zone.setAddNodeList(ZoneNodeAddDAO.getList(zone.getID()));
        }
    }
    
    /**
     * Start World
     * Uses: RequestReady
     * 
     * @param world 
     */
    public static void startWorld(World world) {
        world.getGameEngine().forceSimulation();
        world.getGameEngine().start();

        for (Zone zone : world.getGameEngine().getZoneList()) {
            if (zone.isEnable()) {
                zone.startTimeActiveTimer();
            }
        }
    }

    private static void executeMultiplayerFeatures(World world, GameClient client) {
        // Retrieve Species from other Players' Environments
        for (Zone zone : world.getEnvironment().getZones()) {
            if (zone.isEnable()) {
                for (Organism organism : zone.getOrganisms()) {
//                    ResponseSpeciesCreate responseSpeciesCreate = new ResponseSpeciesCreate();
//                    responseSpeciesCreate.setSpeciesGroup(organism);
//                    client.addResponseForUpdate(responseSpeciesCreate);
                }
            }
        }

        // Replicate Environment for Other Players
        ResponseCreateEnv responseCreateEnv = new ResponseCreateEnv();
        responseCreateEnv.setEnvironment(world.getEnvironment());
        NetworkManager.addResponseToOtherPeopleInTheSameWorld(client.getPlayer().getID(), world.getID(), responseCreateEnv);
    }

    public static void getServerAnnouncement(GameClient client) {
        try {
            ResponseChat responseChat = new ResponseChat();
            responseChat.setMessage(""
                    + "Server Announcement" + "\n"
                    + "- - - - - - - - - - - - - - - - -" + "\n"
                    + NewsDAO.getLatestNews().getText() + "\n"
                    + "- - - - - - - - - - - - - - - - -");
            responseChat.setType((short) 1);

            client.addResponseForUpdate(responseChat);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void getOnlineMessage(GameClient client) {
        ResponseChat responseChat = new ResponseChat();
        responseChat.setMessage("[" + client.getPlayer().getUsername() + "] has logged on.");

        NetworkManager.addResponseForAllOnlinePlayers(client.getPlayer().getID(), responseChat);
    }

    public static void getOnlinePlayers(GameClient client) {
        ResponseGetPlayers responsePlayers = new ResponseGetPlayers();
        responsePlayers.setPlayers(GameServer.getInstance().getActivePlayers());

        NetworkManager.addResponseForAllOnlinePlayers(client.getPlayer().getID(), responsePlayers);

        //Add the response to all the players in the same world.
//            GameServer.getInstance().addResponseForWorld(world.getID(), response);
    }
}
