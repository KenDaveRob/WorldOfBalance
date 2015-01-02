package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import core.GameServer;
import networking.response.ResponseGetPlayers;

public class RequestGetPlayers extends GameRequest {

    // Responses
    private ResponseGetPlayers responseGetPlayers;

    public RequestGetPlayers() {
        responses.add(responseGetPlayers = new ResponseGetPlayers());
    }

    @Override
    public void parse() throws IOException {
    }

    @Override
    public void doBusiness() throws Exception {
        responseGetPlayers.setPlayers(GameServer.getInstance().getActivePlayers());
    }
}
