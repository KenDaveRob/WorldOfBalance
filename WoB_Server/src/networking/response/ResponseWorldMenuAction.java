package networking.response;

// Other Imports
import metadata.Constants;
import model.World;
import utility.GamePacket;

public class ResponseWorldMenuAction extends GameResponse {

    private short action;
    private short status;
    private World world;
    private int world_id;

    public ResponseWorldMenuAction() {
        responseCode = Constants.SMSG_WORLD_MENU_ACTION;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(action);

        switch (action) {
            case 1: // Create
                packet.addShort16(status);

                if (status == 0) {
                    packet.addInt32(world.getID());
                    packet.addString(world.getGameName());
                    packet.addShort16(world.getGameMode());
                    packet.addInt32(world.getCredits());
                    packet.addString(world.getEnvType());
                    packet.addShort16((short) world.getMaxPlayers());

                    packet.addFloat(world.getTimeRate());
                }
                break;
            case 2: // Join
                packet.addShort16((short) status);

                if (status == 0) {
                    packet.addInt32(world.getID());
                    packet.addString(world.getGameName());
                    packet.addShort16((short) world.getMonth());
                    packet.addShort16(world.getGameMode());
                    packet.addInt32(world.getCredits());
                    packet.addString(world.getEnvType());
                    packet.addShort16((short) world.getMaxPlayers());
                }
                break;
            case 3: // Delete
                packet.addShort16(status);
                packet.addInt32(world_id);
                break;
            default: break;
        }

        return packet.getBytes();
    }
    
    public void setAction(short action) {
        this.action = action;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public void setWorld(World world) {
        this.world = world;
    }
    
    public void setWorldID(int world_id) {
        this.world_id = world_id;
    }
}
