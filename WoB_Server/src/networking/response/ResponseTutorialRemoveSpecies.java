package networking.response;

// Java Imports

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseTutorialRemoveSpecies extends GameResponse
{
    
    public ResponseTutorialRemoveSpecies() 
    {
        responseCode = Constants.SMSG_TUTORIAL_REMOVE_SPECIES;
    }
    
    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        return packet.getBytes();
    }
}
