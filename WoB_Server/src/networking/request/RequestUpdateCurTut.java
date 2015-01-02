/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package networking.request;

/**
 *
 * @author Aleck
 */
import java.io.IOException;

// Java Imports
import dataAccessLayer.TutorialDAO;
import java.io.IOException;
import utility.DataReader;
import model.TutorialData;
import networking.response.ResponseUpdateCurTut;

public class RequestUpdateCurTut extends GameRequest{
    private int p_id;
    private int cur_tut;
    
   private ResponseUpdateCurTut responseUpdateCurTut;
    
    public RequestUpdateCurTut() 
    {
        responses.add(responseUpdateCurTut = new ResponseUpdateCurTut());
    }
    
    @Override
    public void parse() throws IOException 
    {
        p_id = DataReader.readInt(dataInput);
        cur_tut = DataReader.readInt(dataInput);
        //milestone = DataReader.readInt(dataInput);
        //cur_challenge = DataReader.readInt(dataInput);
    }
    
    @Override
    public void doBusiness() throws Exception 
    {
        TutorialDAO.updateCurTut(p_id, cur_tut);
        responseUpdateCurTut.setTutorial(TutorialDAO.initializeTutorial(p_id));
        responses.add(responseUpdateCurTut);
    }
}
