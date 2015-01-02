package networking.response;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

/**
 * The ResponseExitGame class is used to inform the client whether it can allow
 * the user to exit the game safely.
 */
public class ResponseExitGame extends GameResponse {

    private short type;
    private int status;
    private int user_id;

    public ResponseExitGame() {
        responseCode = Constants.SMSG_SAVE_EXIT_GAME;
    }

    public void setType(short type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(type);
        packet.addShort16((short) status);
        packet.addInt32(user_id);
        return packet.getBytes();
    }
}
