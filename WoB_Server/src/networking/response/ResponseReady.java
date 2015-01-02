package networking.response;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseReady extends GameResponse {

    private boolean status;
    private String username;

    public ResponseReady() {
        responseCode = Constants.SMSG_READY;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addBoolean(status);
        packet.addString(username);

        return packet.getBytes();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
