package networking.response;

/**
 *
 * @author Aleck
 */
import java.io.IOException;

// Java Imports
import dataAccessLayer.TutorialDAO;
import java.util.ArrayList;
import java.util.List;
import model.TutorialData;
import metadata.Constants;
import utility.GamePacket;

public class ResponseTutorial extends GameResponse
{
    private TutorialData tutorial;
    
    public ResponseTutorial() 
    {
        responseCode = Constants.SMSG_TUTORIAL_DATA;
    }
    
    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addInt32(tutorial.getCurTut());
        
        return packet.getBytes();
    }
    
    public void setTutorial( TutorialData tutorial) {
        this.tutorial = tutorial;
    }
}
