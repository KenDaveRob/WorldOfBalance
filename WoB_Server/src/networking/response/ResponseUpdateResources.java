package networking.response;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseUpdateResources extends GameResponse {

    private short type;
    private int amount;
    private int target;

    public ResponseUpdateResources() {
        responseCode = Constants.SMSG_UPDATE_RESOURCES;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(type);
        packet.addInt32(amount);
        packet.addInt32(target);
        return packet.getBytes();
    }

    public void setType(short type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
