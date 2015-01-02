package networking.response;

// Java Imports
import java.util.List;

// Other Imports
import metadata.Constants;
import model.Stat;
import utility.GamePacket;

public class ResponseStats extends GameResponse {

    private List<Stat> statList;

    public ResponseStats() {
        responseCode = Constants.SMSG_STATISTICS;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16((short) statList.size());

        for (Stat stat : statList) {
            packet.addShort16((short) stat.getMonth());
            packet.addString(stat.getSpeciesName());
            packet.addString(stat.getType());
            packet.addShort16((short) stat.getAmount());
        }

        return packet.getBytes();
    }

    public void setStats(List<Stat> statList) {
        this.statList = statList;
    }
}
