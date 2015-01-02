package networking.request;

// Java Imports
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.World;
import utility.DataReader;

// Other Imports
import networking.response.ResponseTutorialRemoveSpecies;
import worldManager.gameEngine.GameEngine;
import worldManager.gameEngine.Zone;
import dataAccessLayer.TutorialDAO;

public class RequestTutorialRemoveSpecies extends GameRequest {
    
    private GameEngine gameEngine;
    
    public RequestTutorialRemoveSpecies() {
    }
    
    @Override
    public void parse() throws IOException {
    }
    
    @Override
    public void doBusiness() throws Exception {        
        World world = client.getActiveObject(World.class);
        gameEngine = world.getGameEngine();
        List<Integer> zones = new ArrayList<Integer>(gameEngine.getZones().keySet());
        Collections.sort(zones);
        
        Zone zone = gameEngine.getZone(zones.get(0));
        zone.resetAllSpecies();
        
        TutorialDAO.removeAllSpecies(zone.getID());
        
        gameEngine.forceSimulation();
        
        ResponseTutorialRemoveSpecies responseRemoveSpecies = new ResponseTutorialRemoveSpecies();
        
        responses.add(responseRemoveSpecies);
    }
}

