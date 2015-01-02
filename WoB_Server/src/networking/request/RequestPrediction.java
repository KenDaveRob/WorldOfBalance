package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import model.World;

public class RequestPrediction extends GameRequest {

    public RequestPrediction() {
    }

    @Override
    public void parse() throws IOException {
    }

    @Override
    public void doBusiness() throws Exception {
        World world = client.getActiveObject(World.class);

        if (world != null) {
            world.getGameEngine().forceSimulation();
        }
    }
}