package networking.response;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseChart extends GameResponse {

    private short type;
    private String csv;

    public ResponseChart() {
        responseCode = Constants.SMSG_CHART;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(type);
        packet.addString(csv);

        return packet.getBytes();
    }

    public void setCSV(String csv) {
        this.csv = csv;
    }

    public void setType(short type) {
        this.type = type;
    }
}
