package networking.response;

// Java Imports
import java.util.Map;
import java.util.Map.Entry;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponsePrediction extends GameResponse {

    private short status;
    private Map<Integer, Integer> results;

    public ResponsePrediction() {
        responseCode = Constants.SMSG_PREDICTION;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(status);
        packet.addShort16((short) results.size());

        for (Entry<Integer, Integer> entry : results.entrySet()) {
            packet.addInt32(entry.getKey());
            packet.addInt32(entry.getValue());
        }

        return packet.getBytes();
    }
    
    public void setStatus(short status) {
        this.status = status;
    }

    public void setResults(Map<Integer, Integer> results) {
        this.results = results;
    }
}
