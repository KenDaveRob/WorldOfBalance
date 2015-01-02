package networking.response;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseStartWorld extends GameResponse {

    private boolean status;

    public ResponseStartWorld() {
        responseCode = Constants.SMSG_START_GAME;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addBoolean(status);

        return packet.getBytes();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
