package networking.response;

import metadata.Constants;

import model.Avatar;

import utility.ExpTable;
import utility.GamePacket;

/**
 *
 * @author Xuyuan
 */
public class ResponseChangeAvatar extends GameResponse {

    private short status;
    private Avatar avatar;
    private String username;

    public ResponseChangeAvatar() {
        responseCode = Constants.SMSG_CHANGE_AVATAR;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(status);

        if (status == 0) {
            packet.addInt32(avatar.getID());
            packet.addShort16((short) avatar.getLevel());
            packet.addInt32(ExpTable.getExp(avatar.getLevel() - 1));
            packet.addInt32(ExpTable.getExpToAdvance(avatar.getLevel()));
            packet.addInt32(avatar.getExperience());
            packet.addInt32(avatar.getCurrency());
        }

        return packet.getBytes();
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setPlayerUsername(String username) {
        this.username = username;
    }

    public void setStatus(short status) {
        this.status = status;
    }
}
