package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import dataAccessLayer.StatsDAO;
import model.World;
import networking.response.ResponseStats;
import utility.DataReader;

public class RequestStats extends GameRequest {

    private short month_start;
    private short month_end;

    public RequestStats() {
    }

    @Override
    public void parse() throws IOException {
        month_start = DataReader.readShort(dataInput);
        month_end = DataReader.readShort(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        if (client.getActiveObject(World.class) != null) {
            int player_id = client.getUserID();
            int zone_id = client.getActiveObject(World.class).getEnvironment().getZones().get(0).getID();

            ResponseStats responseStats = new ResponseStats();
            responseStats.setStats(StatsDAO.getStats(month_start, month_end, player_id, zone_id));
            responses.add(responseStats);
        }
    }
}
