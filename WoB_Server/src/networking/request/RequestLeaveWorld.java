package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import core.WorldManager;
import model.World;

public class RequestLeaveWorld extends GameRequest {

    // Data
    // Responses
    public RequestLeaveWorld() {
    }

    @Override
    public void parse() throws IOException {
    }

    @Override
    public void doBusiness() throws Exception {
        World world = client.getActiveObject(World.class);

        if (world != null) {
            world.removePlayer(client.getPlayer().getID());

            if (world.getPlayers().isEmpty()) {
                WorldManager.getInstance().remove(world.getID());
            }
        }
    }
}
