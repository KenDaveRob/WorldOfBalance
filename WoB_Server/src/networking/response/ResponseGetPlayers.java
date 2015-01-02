package networking.response;

// Java Imports
import java.util.List;

// Other Imports
import metadata.Constants;
import model.Player;
import utility.GamePacket;

public class ResponseGetPlayers extends GameResponse {

    private List<Player> playerList;

    public ResponseGetPlayers() {
        responseCode = Constants.SMSG_GET_PLAYERS;
    }

    public void setPlayers(List<Player> playerList) {
        this.playerList = playerList;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16((short) playerList.size());

        for (Player player : playerList) {
            packet.addInt32(player.getID());
            packet.addString(player.getUsername());
        }

        return packet.getBytes();
    }
}
