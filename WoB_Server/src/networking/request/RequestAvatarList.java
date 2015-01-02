package networking.request;

// Java Imports
import java.io.IOException;
import java.util.List;

// Other Imports
import dataAccessLayer.AvatarDAO;
import model.Avatar;
import networking.response.ResponseAvatarList;
import utility.DataReader;

public class RequestAvatarList extends GameRequest {

    // Data
    private int user_id;
    // Responses
    private ResponseAvatarList responseAvatarList;

    public RequestAvatarList() {
        responses.add(responseAvatarList = new ResponseAvatarList());
    }

    @Override
    public void parse() throws IOException {
        user_id = DataReader.readInt(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        List<Avatar> avatarList = AvatarDAO.getAvatars(user_id);
        responseAvatarList.setAvatarList(avatarList);
    }
}
