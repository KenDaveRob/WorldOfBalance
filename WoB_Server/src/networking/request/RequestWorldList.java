package networking.request;

// Java Imports
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Other Imports
import dataAccessLayer.EnvironmentDAO;
import dataAccessLayer.WorldDAO;
import model.Environment;
import model.World;
import networking.response.ResponseWorldList;
import utility.DataReader;
import utility.Log;

public class RequestWorldList extends GameRequest {

    // Data
    private int user_id;
    // Responses
    private ResponseWorldList responseWorldList;

    public RequestWorldList() {
        responses.add(responseWorldList = new ResponseWorldList());
    }

    @Override
    public void parse() throws IOException {
        user_id = DataReader.readInt(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        List<World> worldList = WorldDAO.getPlayerWorlds(user_id);
        List<Integer> scoreList = new ArrayList<Integer>();

        for (World world : worldList) {
            try {
                Environment env = EnvironmentDAO.getEnvironmentByWorldID(world.getID());

                if (env != null) {
                    scoreList.add(env.getEnvironmentScore());
                } else {
                    scoreList.add(0);
                }
            } catch (SQLException ex) {
                Log.println_e(ex.getMessage());
            }
        }

        responseWorldList.setWorldList(worldList, scoreList);
    }
}