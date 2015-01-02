package networking.response;

import metadata.Constants;

import utility.GamePacket;

/**
 *
 * @author Gary
 */
public class ResponseUpdateLevel extends GameResponse {

    private int amount;
    private int level;
    private String range;

    public ResponseUpdateLevel() {
        responseCode = Constants.SMSG_UPDATE_LEVEL;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16((short) amount);
        packet.addShort16((short) level);
        packet.addString(range);
        return packet.getBytes();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
