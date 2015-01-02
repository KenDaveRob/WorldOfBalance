package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import networking.response.ResponseExitGame;
import utility.DataReader;

/**
 * The RequestExitGame class is used to alert the server that the user wants to
 * disconnect.
 */
public class RequestExitGame extends GameRequest {

    private short type;
    // Responses
    private ResponseExitGame responseExitGame;

    public RequestExitGame() {
    }

    @Override
    public void parse() throws IOException {
        type = DataReader.readShort(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        short status = 0;

        responseExitGame = new ResponseExitGame();
        responseExitGame.setType(type);
        responseExitGame.setStatus(status);

        client.send(responseExitGame);

        if (status == 0) {
            if (type == 0) {
                client.end();
            } else if (type == 1) {
                client.removePlayerData();
                client.newSession();
            }
        }
    }
}
