package networking.request;

// Java Imports
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Other Imports
import core.Badge;
import core.BadgeManager;
import core.Lobby;
import core.NetworkManager;
import core.WorldManager;
import dataAccessLayer.BadgeDataDAO;
import metadata.Constants;
import model.World;
import networking.response.ResponseReady;
import networking.response.ResponseStartWorld;
import networking.response.ResponseUpdateTime;
import utility.DataReader;
import utility.Log;

public class RequestReady extends GameRequest {

    // Data
    private boolean status;

    public RequestReady() {
    }

    @Override
    public void parse() throws IOException {
        status = DataReader.readBoolean(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        Lobby lobby = client.getActiveObject(Lobby.class);

        if (lobby != null) {
            lobby.setReady(client.getPlayer().getID(), status);

            ResponseReady responseReady = new ResponseReady();
            responseReady.setStatus(status);
            responseReady.setUsername(client.getPlayer().getUsername());
            NetworkManager.addResponseForLobby(lobby.getID(), responseReady);

            if (lobby.isReady()) {
                Map<Integer, BadgeManager> badgeManagerList = new HashMap<Integer, BadgeManager>();

                for (int user_id : lobby.getUserList().keySet()) {
                    try {
                        BadgeManager badgeManager = new BadgeManager(user_id, lobby.getEventHandler());

                        List<Badge> badgeData = BadgeDataDAO.getBadgeData(user_id);
                        badgeManager.initialize(badgeData);

                        badgeManagerList.put(user_id, badgeManager);
                    } catch (SQLException ex) {
                        Log.println_e(ex.getMessage());
                    }
                }

                lobby.setBadgeManagerList(badgeManagerList);

                World world = lobby.getWorld();

                WorldManager.startWorld(world);

                ResponseStartWorld responseStartWorld = new ResponseStartWorld();
                responseStartWorld.setStatus(true);
                NetworkManager.addResponseForWorld(world.getID(), responseStartWorld);

                ResponseUpdateTime responseUpdateTime = new ResponseUpdateTime();
                responseUpdateTime.setMonth(world.getMonth());
                responseUpdateTime.setDuration(Constants.MONTH_DURATION);
                responseUpdateTime.setCurrent((int) world.getSeconds());
                responseUpdateTime.setRate(world.getGameEngine().getGameScale());
                NetworkManager.addResponseForWorld(world.getID(), responseUpdateTime);
            }
        }
    }
}
