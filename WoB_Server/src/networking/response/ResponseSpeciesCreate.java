package networking.response;

// Other Imports
import core.SpeciesGroup;
import metadata.Constants;
import utility.GamePacket;

public class ResponseSpeciesCreate extends GameResponse {

    private short status;
    private int zone_id;
    private SpeciesGroup group;

    public ResponseSpeciesCreate(short status, int zone_id, SpeciesGroup group) {
        responseCode = Constants.SMSG_SPECIES_CREATE;
        
        this.status = status;
        this.group = group;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(status);
        packet.addInt32(zone_id);
        packet.addInt32(group.getID());
        packet.addInt32(group.getSpecies().getID());
        packet.addString(group.getSpecies().getSpeciesType().getName());
        packet.addInt32(group.getSpecies().getSpeciesType().getModelID());
        packet.addInt32(group.getBiomass());
        packet.addFloat(group.getPosition().getX());
        packet.addFloat(group.getPosition().getY());
        packet.addFloat(group.getPosition().getZ());
        packet.addInt32(group.getUserID());

        return packet.getBytes();
    }
}
