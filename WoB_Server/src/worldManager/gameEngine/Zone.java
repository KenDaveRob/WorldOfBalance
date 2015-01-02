package worldManager.gameEngine;

// Java Imports
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

// Other Imports
import core.GameResources;
import core.GameServer;
import core.NetworkManager;
import core.Species;
import core.SpeciesGroup;
import dataAccessLayer.ScoreCSVDAO;
import dataAccessLayer.ZoneNodeAddDAO;
import metadata.Constants;
import model.Environment;
import model.SpeciesType;
import networking.response.ResponseChart;
import utility.CSVParser;
import utility.GameTimer;
import worldManager.gameEngine.species.Animal;
import worldManager.gameEngine.species.Plant;
import worldManager.gameEngine.species.Organism;

/**
 * The Zone class is used to store information about a specific section of an
 * Environment such as species residing in this zone.
 */
public class Zone {

    private int zone_id;
    private int order;
    private int env_id;
    private int row;
    private int column;
    private int type;
    private int environmentScore;
    private String manipulationID;
    private long lastSimulationTime;
    private Map<Integer, Animal> animals = new HashMap<Integer, Animal>();
    private Map<Integer, Plant> plants = new HashMap<Integer, Plant>();
    private GameEngine gameEngine;
    private boolean isEnable;
    private float max_biomass;
    private GameTimer timeActiveTimer = new GameTimer();
    private float totalBiomass;
    private Environment env;
    private Map<Short, Float> parametersList = new HashMap<Short, Float>();
    private float prevBiomass;
    // Species ID -> Species
    private Map<Integer, Species> speciesList = new HashMap<Integer, Species>();
    // Node ID -> Count
    private Map<Integer, Integer> nodeList = new HashMap<Integer, Integer>();
    // Node ID -> Biomass
    private Map<Integer, Integer> addNodeList = new HashMap<Integer, Integer>();
    private List<List<String>> score_csv;
    private Map<Integer, Integer> speciesChangeList = new HashMap<Integer, Integer>();
    private Random random;

    public Zone(int zone_id) {
        this.zone_id = zone_id;
        manipulationID = "";
    }

    public int getID() {
        return zone_id;
    }

    public int setID(int zone_id) {
        return this.zone_id = zone_id;
    }

    public int getOrder() {
        return order;
    }

    public int setOrder(int order) {
        return this.order = order;
    }

    public int getEnvID() {
        return env_id;
    }

    public int setEnvID(int env_id) {
        return this.env_id = env_id;
    }

    public Environment getEnvironment() {
        return this.env;
    }

    public Environment setEnvironment(Environment env) {
        return this.env = env;
    }

    public long getLastSimulationTime() {
        return lastSimulationTime;
    }

    public long setLastSimulationTime(long lastSimulationTime) {
        return this.lastSimulationTime = lastSimulationTime;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getType() {
        return type;
    }

    public int setType(int type) {
        return this.type = type;
    }

    public List<List<String>> getScoreCSV() {
        return this.score_csv;
    }

    public List<List<String>> setScoreCSV(List<List<String>> score_csv) {
        return this.score_csv = score_csv;
    }

    public String getManipulationID() {
        return manipulationID;
    }

    public String setManipulationID(String manipulationID) {
        isEnable = !manipulationID.isEmpty();
        return this.manipulationID = manipulationID;
    }

    public Map<Integer, Integer> getSpeciesChangeList() {
        return speciesChangeList;
    }

    public Map<Integer, Integer> setSpeciesChangeList(Map<Integer, Integer> speciesChangeList) {
        random = new Random();
        return this.speciesChangeList = speciesChangeList;
    }

    public void clearSpeciesChangeList() {
        speciesChangeList.clear();
    }

    public Random getRandom() {
        return random;
    }

    public boolean containsSpecies(int species_id) {
        return speciesList.containsKey(species_id);
    }

    public Species getSpecies(int species_id) {
        return speciesList.get(species_id);
    }

    public Map<Integer, Species> getSpeciesList() {
        return speciesList;
    }

    public Map<Integer, Integer> getAddSpeciesList() {
        return addNodeList;
    }

    public Map<Integer, Integer> removeAddSpeciesList() {
        Map<Integer, Integer> nodeList = addNodeList;
        addNodeList = new HashMap<Integer, Integer>();
        return nodeList;
    }

    public void setAddNodeList(HashMap<Integer, Integer> addNodeList) {
        this.addNodeList = addNodeList;
    }

    public void setNewSpeciesNode(int node_id, int amount) {
        try {
            if (addNodeList.containsKey(node_id)) {
                addNodeList.put(node_id, addNodeList.get(node_id) + amount);
                ZoneNodeAddDAO.updateAmount(zone_id, node_id, addNodeList.get(node_id));
            } else {
                addNodeList.put(node_id, amount);
                ZoneNodeAddDAO.createEntry(zone_id, node_id, amount);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void removeNewSpeciesNode(int node_id, int amount) {
        try {
            if (addNodeList.containsKey(node_id)) {
                int temp = Math.max(0, addNodeList.get(node_id) - amount);

                if (temp > 0) {
                    addNodeList.put(node_id, temp);
                    ZoneNodeAddDAO.updateAmount(zone_id, node_id, addNodeList.get(node_id));
                } else {
                    addNodeList.remove(node_id);
                    ZoneNodeAddDAO.removeEntry(zone_id, node_id);
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public boolean containsOrganism(int organism_id) {
        return plants.containsKey(organism_id) || animals.containsKey(organism_id);
    }

    public List<Animal> getAnimals() {
        return new ArrayList<Animal>(animals.values());
    }

    public List<Organism> getOrganisms() {
        List<Organism> organismList = new ArrayList<Organism>();
        organismList.addAll(plants.values());
        organismList.addAll(animals.values());

        return organismList;
    }

    public List<Organism> getOrganismsBySpecies(int species_id) {
        List<Organism> organismList = new ArrayList<Organism>();

        for (Animal animal : animals.values()) {
            if (animal.getSpeciesTypeID() == species_id) {
                organismList.add(animal);
            }
        }

        for (Plant plant : plants.values()) {
            if (plant.getSpeciesTypeID() == species_id) {
                organismList.add(plant);
            }
        }

        return organismList;
    }

    public void addSpecies(Species species) {
        speciesList.put(species.getID(), species);
        setTotalBiomass((float) (totalBiomass + species.getTotalBiomass()));

        updateEnvironmentScore();
    }

    public void updateScore() {
        updateEnvironmentScore();

        try {
            List<String> rowFirst = score_csv.get(0), rowSecond = score_csv.get(1);

            int lastMonth = Integer.valueOf(rowFirst.get(rowFirst.size() - 1));
            int currentMonth = gameEngine.getCurrentMonth();

            for (int i = lastMonth; i <= currentMonth; i++) {
                if (i == lastMonth && i == currentMonth) {
                    rowSecond.set(i, Integer.toString(environmentScore));
                } else if (i != lastMonth) {
                    rowFirst.add(Integer.toString(i));
                    rowSecond.add(Integer.toString(environmentScore));
                }
            }

            String csv = CSVParser.createCSV(score_csv);
            ScoreCSVDAO.createCSV(zone_id, csv);

            ResponseChart responseChart = new ResponseChart();
            responseChart.setType((short) 2);
            responseChart.setCSV(csv);

            NetworkManager.addResponseForUser(env.getOwnerID(), responseChart);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (env != null) {
            env.updateAccumEnvScore();
        }
    }

    private void updateEnvironmentScore() {
        double biomass = 0;

        for (Species species : speciesList.values()) {
            SpeciesType speciesType = species.getSpeciesType();
            biomass += speciesType.getAvgBiomass() * Math.pow(species.getTotalBiomass() / speciesType.getAvgBiomass(), speciesType.getTrophicLevel());
        }

        if (biomass > 0) {
            biomass = Math.round(Math.log(biomass) / Math.log(2)) * 5;
        }

        environmentScore = (int) Math.round(Math.pow(biomass, 2) + Math.pow(speciesList.size(), 2));

        if (env != null) {
            env.updateEnvironmentScore();
        }
    }

    public int getEnvironmentScore() {
        return environmentScore;
    }

    public float getPrevBiomass() {
        return prevBiomass;
    }

    public float setPrevBiomass(float prevBiomass) {
        return this.prevBiomass = prevBiomass;
    }

    public float getMaxBiomass() {
        return max_biomass;
    }

    public float setMaxBiomass(float max_biomass) {
        return this.max_biomass = max_biomass;
    }

    public float getTotalBiomass() {
        return totalBiomass;
    }

    public void setTotalBiomass(float totalBiomass) {
        this.totalBiomass = totalBiomass;

        max_biomass = Math.max(max_biomass, totalBiomass);

        if (totalBiomass >= max_biomass && totalBiomass >= 1000) {
            if (String.valueOf((int) totalBiomass).length() > String.valueOf((int) max_biomass).length()) {
                GameResources.updateCredits(gameEngine.getWorld(), 1000);
            }
        }
    }

    public Animal findPredator(Organism organism) {
        Animal predator = null;

        int species_id = organism.getSpeciesType().getID();
        double distance = -1;

        List<Animal> predatorList = getAnimals();
        Collections.shuffle(predatorList);

        for (Animal animal : predatorList) {
            if (animal.getID() != organism.getID() && (organism.getOrganismType() == Constants.ORGANISM_TYPE_PLANT || animal.getHungerLevel() < 1.0f)) {
                List<Integer> preyList = animal.getSpeciesType().getPreyIDs();

                if (preyList != null) {
                    for (int prey_id : preyList) {
                        if (prey_id == species_id) {
                            double temp = Math.sqrt(Math.pow(animal.getX() - organism.getX(), 2) + Math.pow(animal.getY() - organism.getY(), 2));

                            if (distance == -1 || temp < distance) {
                                distance = temp;
                                predator = animal;
                            }
                        }
                    }
                }
            }
        }

        return predator;
    }

    public Map<Short, Float> getParameters() {
        return parametersList;
    }

    public Map<Short, Float> setParameters(HashMap<Short, Float> parametersList) {
        return this.parametersList = parametersList;
    }

    public void updateAnimalTarget(int animalID, int xTarg, int yTarg) {
        Animal animal = animals.get(animalID);

        if (animal != null) {
            animal.setTargetPos(xTarg, yTarg, 0);
        }
    }

    public void updateAnimalCoors(int animalID, int xCoor, int yCoor) {
        Animal animal = animals.get(animalID);

        if (animal != null) {
            animal.setPos(xCoor, yCoor, 0);
        }
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public GameEngine setGameEngine(GameEngine gameEngine) {
        return this.gameEngine = gameEngine;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public boolean setEnable(boolean status) {
        return this.isEnable = status;
    }

    public void startTimeActiveTimer() {
        timeActiveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameResources.updateCredits(gameEngine.getWorld(), 75);
            }
        }, 60000, 60000);
    }

    public boolean stopTimeActiveTimer() {
        return timeActiveTimer.end();
    }

    public void resetAllSpecies() {
        speciesList = new HashMap<Integer, Species>();
		addNodeList = new HashMap<Integer, Integer>();
        setTotalBiomass(0);
        updateEnvironmentScore();
    }
}
