package networking.request;

// Java Imports
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// Other Imports
import core.WorldManager;
import dataAccessLayer.ShopDAO;
import model.ShopItem;
import model.World;
import networking.response.ResponseSpeciesAction;
import utility.DataReader;

public class RequestSpeciesAction extends GameRequest {

    private short action;
    private short type;
    private Map<Integer, Integer> speciesList;

    public RequestSpeciesAction() {
    }

    @Override
    public void parse() throws IOException {
        action = DataReader.readShort(dataInput);

        if (action == 0) {
            type = DataReader.readShort(dataInput);
        } else if (action == 1) {
            short size = DataReader.readShort(dataInput);
            speciesList = new HashMap<Integer, Integer>();

            int species_id, biomass;

            for (int i = 0; i < size; i++) {
                species_id = DataReader.readInt(dataInput);
                biomass = DataReader.readInt(dataInput);

                speciesList.put(species_id, biomass);
            }
        }
    }

    @Override
    public void doBusiness() throws Exception {
        short status = 0;

        ResponseSpeciesAction responseSpeciesAction = new ResponseSpeciesAction();
        responseSpeciesAction.setAction(action);
        responseSpeciesAction.setStatus(status);

        if (action == 0) {
            responseSpeciesAction.setType(type);

            if (type == 0) { // Get Default Species
                speciesList = new HashMap<Integer, Integer>();
                speciesList.put(13, 5000);
                speciesList.put(20, 5000);
                speciesList.put(31, 5000);

                String selectionList = "";

                int index = 0;
                for (Entry<Integer, Integer> entry : speciesList.entrySet()) {
                    selectionList += entry.getKey() + ":" + entry.getValue();

                    if (index++ < speciesList.size() - 1) {
                        selectionList += ",";
                    }
                }

                responseSpeciesAction.setSelectionList(selectionList);
            } else if (type == 1) { // Get Every Species
                String[] settings = new String[]{"30000", "2,10", "500,1000,2500"};

                List<ShopItem> shopList = ShopDAO.getItems("level:0,99");
                String selectionList = "";

                int index = 0;
                for (ShopItem item : shopList) {
                    selectionList += item.getID();

                    if (index++ < shopList.size() - 1) {
                        selectionList += ",";
                    }
                }

                responseSpeciesAction.setSettings(settings);
                responseSpeciesAction.setSelectionList(selectionList);
            }

            responses.add(responseSpeciesAction);
        } else if (action == 1) { // Create Ecosystem Using Species
            World world = client.getActiveObject(World.class);

            if (world != null) {
                WorldManager.createEcosystem(world, speciesList);
                client.removeActiveObject(World.class);

                String selectionList = "";

                int index = 0;
                for (Entry<Integer, Integer> entry : speciesList.entrySet()) {
                    selectionList += entry.getKey() + ":" + entry.getValue();

                    if (index++ < speciesList.size() - 1) {
                        selectionList += ",";
                    }
                }

                responseSpeciesAction.setSelectionList(selectionList);
                responses.add(responseSpeciesAction);
            }
        }
    }
}
