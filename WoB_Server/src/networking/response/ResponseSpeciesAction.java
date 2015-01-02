package networking.response;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseSpeciesAction extends GameResponse {

    private short action;
    private short status;
    private short type;
    private String[] settings;
    private String selectionList;

    public ResponseSpeciesAction() {
        responseCode = Constants.SMSG_SPECIES_ACTION;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(action);
        packet.addShort16(status);

        if (action == 0) {
            packet.addShort16(type);

            if (type == 0) {
                packet.addString(selectionList);
            } else if (type == 1) {
                packet.addShort16((short) settings.length);

                for (String value : settings) {
                    packet.addString(value);
                }

                packet.addString(selectionList);
            }
        } else if (action == 1) {
            packet.addString(selectionList);
        }

        return packet.getBytes();
    }

    public void setAction(short action) {
        this.action = action;
    }

    public void setStatus(short status) {
        this.status = status;
    }
    
    public void setType(short type) {
        this.type = type;
    }

    public void setSettings(String[] settings) {
        this.settings = settings;
    }

    public void setSelectionList(String selectionList) {
        this.selectionList = selectionList;
    }
}
