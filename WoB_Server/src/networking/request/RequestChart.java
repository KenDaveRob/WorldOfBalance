package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import dataAccessLayer.BiomassCSVDAO;
import dataAccessLayer.ScoreCSVDAO;
import model.World;
import networking.response.ResponseChart;
import utility.DataReader;
import worldManager.gameEngine.Zone;

public class RequestChart extends GameRequest {

    private short type;

    public RequestChart() {
    }

    @Override
    public void parse() throws IOException {
        type = DataReader.readShort(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {

        World world = client.getActiveObject(World.class);

        if (world != null) {
            for (Zone zone : world.getGameEngine().getZoneList()) {
                if (zone.isEnable()) {
                    String csv = "";
                    if (type == 0) {
                        csv = BiomassCSVDAO.getCSV(zone.getManipulationID());
                    } else {
                        csv = ScoreCSVDAO.getCSV(zone.getID());
                    }

                    if (csv != null) {
                        ResponseChart responseChart = new ResponseChart();
                        responseChart.setType(type);
                        responseChart.setCSV(csv);

                        responses.add(responseChart);
                    }
                }
            }
        }
    }
}
