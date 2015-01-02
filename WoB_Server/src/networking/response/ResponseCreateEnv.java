package networking.response;

// Other Imports
import metadata.Constants;
import model.Environment;
import utility.GamePacket;
import worldManager.gameEngine.Zone;

/**
 * The ResponseCreateEnv class is used to send information to the client about
 * what is needed to create an environment.
 */
public class ResponseCreateEnv extends GameResponse {

    private Environment env;

    public ResponseCreateEnv() {
        responseCode = Constants.SMSG_CREATE_ENV;
    }

    @Override
    public byte[] constructResponseInBytes() {;
        GamePacket packet = new GamePacket(responseCode);
        packet.addInt32(env.getID());
        packet.addInt32(env.getOwnerID());
        packet.addInt32(env.getEnvironmentScore());

        packet.addShort16((short) env.getZones().size());

        for (Zone zone : env.getZones()) {
            packet.addBoolean(zone.isEnable());
            packet.addShort16((short) zone.getID());
            packet.addShort16((short) zone.getType());
        }

        return packet.getBytes();
    }

    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
