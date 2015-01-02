/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package networking.response;

/**
 *
 * @author Kenneth
 */

// Java Imports
import java.util.ArrayList;
import java.util.List;

// Other Imports
import metadata.Constants;
import utility.GamePacket;

public class ResponseChallengeShopSpecies extends GameResponse
{
    private ArrayList<Integer> speciesList;
    
    public ResponseChallengeShopSpecies() 
    {
        responseCode = Constants.SMSG_TUTORIAL_CHALLENGE_SPECIES;
    }
    
    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16((short) speciesList.size());
        
        for(Integer speciesID : speciesList)
            packet.addInt32(speciesID.intValue());
        
        return packet.getBytes();
    }
    
    public void setChallenge(ArrayList<Integer> speciesList)
    {
        this.speciesList = speciesList;
    }
}
