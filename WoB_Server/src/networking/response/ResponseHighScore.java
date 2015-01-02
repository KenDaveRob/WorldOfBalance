package networking.response;

import java.util.List;
import metadata.Constants;
import utility.GamePacket;

/**
 *
 * @author Gary
 */
public class ResponseHighScore extends GameResponse {

    private short type;
    private List<String[]> scoreList;
    private List<String[]> totalScoreList;
    private List<String[]> currentScoreList;

    public ResponseHighScore() {
        responseCode = Constants.SMSG_HIGH_SCORE;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(type);
        packet.addShort16((short) scoreList.size());

        for (String[] score : scoreList) {
            packet.addString(score[0]);
            packet.addInt32(Integer.parseInt(score[1]));
        }

        packet.addShort16((short) totalScoreList.size());

        for (String[] score : totalScoreList) {
            packet.addString(score[0]);
            packet.addInt32(Integer.parseInt(score[1]));
        }

        packet.addShort16((short) currentScoreList.size());

        for (String[] score : currentScoreList) {
            packet.addString(score[0]);
            packet.addInt32(Integer.parseInt(score[1]));
        }

        return packet.getBytes();
    }

    public void setType(short type) {
        this.type = type;
    }

    public void setEnvScore(List<String[]> scoreList) {
        this.scoreList = scoreList;
    }

    public void setTotalEnvScore(List<String[]> totalScoreList) {
        this.totalScoreList = totalScoreList;
    }

    public void setCurrentEnvScore(List<String[]> currentScoreList) {
        this.currentScoreList = currentScoreList;
    }
}
