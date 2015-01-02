package networking.response;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseBadgeUpdate extends GameResponse {

    private int badge_id;
    private int amount;
    private int progress;
    private boolean isDone;

    public ResponseBadgeUpdate() {
        responseCode = Constants.SMSG_BADGE_UPDATE;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addInt32(badge_id);
        packet.addShort16((short) amount);
        packet.addShort16((short) progress);
        packet.addBoolean(isDone);

        return packet.getBytes();
    }

    public void setBadgeID(int badge_id) {
        this.badge_id = badge_id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}
