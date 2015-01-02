package networking.response;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseUpdateTime extends GameResponse {

    private int current;
    private int duration;
    private int month;
    private float rate;

    public ResponseUpdateTime() {
        responseCode = Constants.SMSG_UPDATE_TIME;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16((short) month);
        packet.addInt32(duration);
        packet.addInt32(current);
        packet.addFloat(rate);
        return packet.getBytes();
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
