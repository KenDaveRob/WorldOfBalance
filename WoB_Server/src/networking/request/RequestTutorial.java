package networking.request;

/**
 *
 * @author Aleck
 *
 */
import java.io.IOException;

// Java Imports
import dataAccessLayer.TutorialDAO;
import dataAccessLayer.PlayerDAO;
import java.io.IOException;
import utility.DataReader;
import model.Avatar;
import model.TutorialData;
import networking.response.ResponseTutorial;

public class RequestTutorial extends GameRequest {

    private int p_id;
    private int cur_tut;
    private int milestone;
    private String message;
    private short tutorialindex;

    private ResponseTutorial responseTutorial;

    public RequestTutorial() {
        responses.add(responseTutorial = new ResponseTutorial());
    }

    @Override
    public void parse() throws IOException {
        p_id = DataReader.readInt(dataInput);
        //cur_tut = DataReader.readInt(dataInput);
        //milestone = DataReader.readInt(dataInput);
        //cur_challenge = DataReader.readInt(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        TutorialData data = TutorialDAO.initializeTutorial(p_id);
        if (data == null) {
            TutorialDAO.insertTutData(p_id, 0, 0, 0, 0);
            data = TutorialDAO.initializeTutorial(p_id);
        }
        responseTutorial.setTutorial(data);
        responses.add(responseTutorial);
    }

}
