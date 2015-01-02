/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import model.ChallengeData;
public class ResponseChallenge extends GameResponse
{
    private ChallengeData challengeData;
    
    public ResponseChallenge() {
        responseCode = Constants.SMSG_TUTORIAL_CHALLENGE;
    }
    
    public void setChallenge(ChallengeData challengeData)
    {
        this.challengeData = challengeData;
    }
    
    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        
        packet.addInt32(challengeData.getBioScore());
        packet.addInt32(challengeData.getCredits());
        packet.addInt32(challengeData.getEnviroScore());
        packet.addInt32(challengeData.getTime());
        packet.addInt32(challengeData.getMinSpecies());
        
        //We can use the RequestChallengeShopSpecies for this data
        //challengeData.getAnimalID();
        //challengeData.getPlantID();
        /*
        packet.addShort16((short) speciesList.size());
        
        for(Integer speciesID : speciesList)
            packet.addInt32(speciesID.intValue());
        */
        return packet.getBytes();
    }
    
}
