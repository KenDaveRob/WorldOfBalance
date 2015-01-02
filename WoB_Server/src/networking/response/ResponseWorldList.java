package networking.response;

// Java Imports
import java.util.List;

// Other Imports
import metadata.Constants;
import model.World;
import utility.GamePacket;

public class ResponseWorldList extends GameResponse {

    private short status;
    private List<World> worldList;
    private List<Integer> scoreList;

    public ResponseWorldList() {
        responseCode = Constants.SMSG_WORLD_LIST;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(status);

        if (status == 0) {
            packet.addShort16((short) worldList.size());

            for (int i = 0; i < worldList.size(); i++) {
                World world = worldList.get(i);

                packet.addInt32(world.getID());
                packet.addString(world.getGameName());
                packet.addShort16(world.getGameMode());
                packet.addInt32(world.getCredits());

                boolean isNew = world.getPlayTime() == 0;
                packet.addBoolean(isNew);

                if (!isNew) {
                    packet.addShort16((short) world.getYear());
                    packet.addShort16((short) world.getMonth());
                    packet.addInt32((int) world.getPlayTime());
                    packet.addInt32(scoreList.get(i));
                }
            }
        }

        return packet.getBytes();
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public void setWorldList(List<World> worldList, List<Integer> scoreList) {
        this.worldList = worldList;
        this.scoreList = scoreList;
    }
}
