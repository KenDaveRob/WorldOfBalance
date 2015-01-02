/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package networking.request;

/**
 *
 * @author Kenneth
 */

// Java Imports
import dataAccessLayer.TutorialDAO;
import java.io.IOException;
import utility.DataReader;

// Other Imports
import model.TutorialData;
import networking.response.ResponseChallengeShopSpecies;

public class RequestChallengeShopSpecies extends GameRequest
{
    private short challengeIndex; 

    public RequestChallengeShopSpecies() {
    }
    
    @Override
    public void parse() throws IOException 
    {
        challengeIndex = DataReader.readShort(dataInput);
    }
    
    @Override
    public void doBusiness() throws Exception {
        ResponseChallengeShopSpecies responseChallengeSpecies = 
                new ResponseChallengeShopSpecies();
        responseChallengeSpecies.
                setChallenge(TutorialDAO.getSpeciesList(challengeIndex));
        
        responses.add(responseChallengeSpecies);
    }
}

