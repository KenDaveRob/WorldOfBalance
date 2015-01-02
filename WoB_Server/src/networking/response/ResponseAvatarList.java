package networking.response;

// Java Imports
import java.util.List;

// Other Imports
import metadata.Constants;
import model.Avatar;
import utility.GamePacket;

public class ResponseAvatarList extends GameResponse {

    private short status;
    private List<Avatar> avatarList;

    public ResponseAvatarList() {
        responseCode = Constants.SMSG_AVATAR_LIST;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(status);

        if (status == 0) {
            packet.addShort16((short) avatarList.size());

            for (Avatar avatar : avatarList) {
                packet.addInt32(avatar.getID());
                packet.addString(avatar.getName());
                packet.addShort16((short) avatar.getLevel());
                packet.addInt32(avatar.getCurrency());
                packet.addString(avatar.getLastPlayed());
            }
        }

        return packet.getBytes();
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public void setAvatarList(List<Avatar> avatarList) {
        this.avatarList = avatarList;
    }
}
