package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import metadata.Constants;
import utility.DataReader;

public class RequestActivity extends GameRequest {

    // Data
    private short type;

    public RequestActivity() {
    }

    @Override
    public void parse() throws IOException {
        type = DataReader.readShort(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        if (type == Constants.ACTIVITY_MOUSE) {
            if (client.getPlayer() != null) {
                client.getPlayer().updateActiveTime();
            }
        }
    }
}