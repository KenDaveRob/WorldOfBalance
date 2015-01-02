package networking.response;

// Other Imports
import utility.GamePacket;
import metadata.Constants;

/**
 * The ResponseRegister class is used to inform the client whether the
 * registration was successful.
 */
public class ResponseRegister extends GameResponse {

    private short status;

    public ResponseRegister() {
        responseCode = Constants.SMSG_REGISTER;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(status);

        return packet.getBytes();
    }
}