/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package networking.request;

/**
 *
 * @author Kenneth
 */

// Java Imports
import dataAccessLayer.ChallengeDAO;
import java.io.IOException;
import utility.DataReader;

// Other Imports
import model.ChallengeData;
import networking.response.ResponseChallenge;

public class RequestChallenge extends GameRequest
{
    private int challengeID;

    public RequestChallenge() {
    }
    
    
    @Override
    public void parse() throws IOException {
        challengeID = DataReader.readShort(dataInput);
        System.out.println("RequestChallenge.parse(): challengeID = "+challengeID);
    }

    @Override
    public void doBusiness() throws Exception {
        ResponseChallenge responseChallenge = 
                new ResponseChallenge();
        //NOTE TO ROB: The code below is generating the error ||     <------------------------------------------------ 
        //                                                    \/
        responseChallenge.
                setChallenge(ChallengeDAO.initializeChallenge(challengeID));
        
        responses.add(responseChallenge);
    }
    
}
