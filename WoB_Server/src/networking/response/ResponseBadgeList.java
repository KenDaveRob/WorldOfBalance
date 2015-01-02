package networking.response;

// Java Imports
import java.util.List;

// Other Imports
import core.Badge;
import core.Objective;
import metadata.Constants;
import utility.GamePacket;

public class ResponseBadgeList extends GameResponse {

    private short status;
    private List<Badge> badgeList;

    public ResponseBadgeList() {
        responseCode = Constants.SMSG_BADGE_LIST;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(status);

        if (status == 0) {
            packet.addShort16((short) badgeList.size());

            for (Badge badge : badgeList) {
                packet.addInt32(badge.getID());
                packet.addString(badge.getName());
                packet.addShort16((short) badge.getAmount());

                Objective objective = badge.getObjective();
                packet.addInt32(objective.getID());
                packet.addString(objective.getName());
                packet.addShort16((short) objective.getAmount());
                packet.addShort16((short) objective.getTarget());
            }
        }

        return packet.getBytes();
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public void setBadgeList(List<Badge> badgeList) {
        this.badgeList = badgeList;
    }
}
