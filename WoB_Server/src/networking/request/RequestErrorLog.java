package networking.request;

// Java Imports
import dataAccessLayer.LogDAO;
import java.io.IOException;

// Other Imports
import utility.DataReader;

public class RequestErrorLog extends GameRequest {

    // Data
    private String message;

    public RequestErrorLog() {
    }

    @Override
    public void parse() throws IOException {
        message = DataReader.readString(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        LogDAO.createErrorLog(client.getPlayer() == null ? 0 : client.getUserID(), message);
    }
}