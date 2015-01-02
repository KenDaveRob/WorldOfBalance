package networking.response;

import metadata.Constants;
import utility.GamePacket;

/**
 *
 * @author Xuyuan
 */
public class ResponseUpdateEnvironmentScore extends GameResponse {

    private int env_id;
    private int score;

    public ResponseUpdateEnvironmentScore() {
        responseCode = Constants.SMSG_UPDATE_ENV_SCORE;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addInt32(env_id);
        packet.addInt32(score);
        return packet.getBytes();
    }

    public void setEnvID(int env_id) {
        this.env_id = env_id;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
