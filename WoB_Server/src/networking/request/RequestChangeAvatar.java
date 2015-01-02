package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import dataAccessLayer.AvatarDAO;
import model.Avatar;
import networking.response.ResponseChangeAvatar;
import utility.DataReader;

public class RequestChangeAvatar extends GameRequest {

    // Data
    private int avatar_id;
    // Responses
    private ResponseChangeAvatar responseChangeAvatar;

    public RequestChangeAvatar() {
        responses.add(responseChangeAvatar = new ResponseChangeAvatar());
    }

    @Override
    public void parse() throws IOException {
        avatar_id = DataReader.readInt(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        Avatar avatar = client.getAvatar();

        if (avatar != null) {
        } else {
//            avatar = AvatarDAO.getAvatar(avatar_id);
            avatar = AvatarDAO.getAvatars(client.getPlayer().getID()).get(0);
            client.setAvatar(avatar);
            client.getPlayer().setAvatar(avatar);

            AvatarDAO.updateLastPlayed(avatar_id);

            responseChangeAvatar.setStatus((short) 0);
        }

        responseChangeAvatar.setAvatar(avatar);
        responseChangeAvatar.setPlayerUsername(client.getPlayer().getUsername());
    }
}
