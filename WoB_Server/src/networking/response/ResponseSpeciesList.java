package networking.response;

// Java Imports
import java.util.List;

// Other Imports
import metadata.Constants;
import model.ShopItem;
import utility.GamePacket;

public class ResponseSpeciesList extends GameResponse {

    private List<ShopItem> speciesList;

    public ResponseSpeciesList() {
        responseCode = Constants.SMSG_SPECIES_LIST;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16((short) speciesList.size());

        for (ShopItem species : speciesList) {
            packet.addInt32(species.getID());
            packet.addString(species.getName());
            packet.addString(species.getDescription());

            packet.addShort16((short) species.getExtraArgs().size());
            for (String s : species.getExtraArgs()) {
                packet.addString(s);
            }

            packet.addString(species.getCategoryListAsString());
        }

        return packet.getBytes();
    }

    public void setSpeciesList(List<ShopItem> speciesList) {
        this.speciesList = speciesList;
    }
}
