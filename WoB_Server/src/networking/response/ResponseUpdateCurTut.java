package networking.response;

import model.TutorialData;
import metadata.Constants;
import utility.GamePacket;

/**
 *
 * @author Aleck
 */
public class ResponseUpdateCurTut extends GameResponse{
    private TutorialData tutorial;
    
    public ResponseUpdateCurTut() 
    {
        responseCode = Constants.SMSG_TUTORIAL_UPDATE_CUR_TUT;
    }
    
    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addInt32(tutorial.getCurTut());
        
        return packet.getBytes();
    }
    
    public void setTutorial(TutorialData tutorial) {
        this.tutorial = tutorial;
    }
}
