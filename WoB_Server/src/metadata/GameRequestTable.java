package metadata;

// Java Imports
import java.util.HashMap;

// Other Imports
import networking.request.GameRequest;
import utility.Log;

/**
 * The GameRequestTable class stores a mapping of unique request code numbers
 * with its corresponding request class.
 */
public class GameRequestTable {

    private static HashMap<Short, Class> requestTable; // Request Code -> Class

    /**
     * Initialize the hash map by populating it with request codes and classes.
     */
    public static void init() {
        requestTable = new HashMap<Short, Class>();

        // Populate the table using request codes and class names
        add(Constants.CMSG_AUTH, "RequestLogin");
        add(Constants.CMSG_REGISTER, "RequestRegister");
        add(Constants.CMSG_GET_PLAYERS, "RequestGetPlayers");
        add(Constants.CMSG_WORLD_MENU_ACTION, "RequestWorldMenuAction");
        add(Constants.CMSG_READY, "RequestReady");
        add(Constants.CMSG_HEARTBEAT, "RequestHeartbeat");
//        add(Constants.CMSG_START_GAME, "RequestStartWorld");
        add(Constants.CMSG_CHANGE_AVATAR, "RequestChangeAvatar");
        add(Constants.CMSG_CHAT, "RequestChat");
        add(Constants.CSMG_SHOP, "RequestShop");
        add(Constants.CSMG_SHOP_ACTION, "RequestShopAction");
        add(Constants.CMSG_SAVE_EXIT_GAME, "RequestExitGame");
        add(Constants.CMSG_WORLD_LIST, "RequestWorldList");
//        add(Constants.CMSG_CHANGE_PARAMETERS, "RequestChangeParameters");
        add(Constants.CMSG_LEAVE_WORLD, "RequestLeaveWorld");
        add(Constants.CMSG_STATISTICS, "RequestStats");
//        add(Constants.CMSG_PARAMS, "RequestParams");
//        add(Constants.CMSG_CHANGE_FUNCTIONAL_PARAMETERS, "RequestChangeFunctionalParams");
        add(Constants.CMSG_HIGH_SCORE, "RequestHighScore");
//        add(Constants.CMSG_GET_FUNCTIONAL_PARAMETERS, "RequestGetFunctionalParameters");
        add(Constants.CMSG_CHART, "RequestChart");
        add(Constants.CMSG_ACTIVITY, "RequestActivity");
        add(Constants.CMSG_SPECIES_LIST, "RequestSpeciesList");
        add(Constants.CMSG_SPECIES_ACTION, "RequestSpeciesAction");
        add(Constants.CMSG_AVATAR_LIST, "RequestAvatarList");
        add(Constants.CMSG_BADGE_LIST, "RequestBadgeList");
        add(Constants.CMSG_ERROR_LOG, "RequestErrorLog");
        add(Constants.CMSG_PREDICTION, "RequestPrediction");
        add(Constants.CMSG_TUTORIAL_CHALLENGE_SPECIES, "RequestChallengeShopSpecies");
        add(Constants.CMSG_TUTORIAL_REMOVE_SPECIES, "RequestTutorialRemoveSpecies");
        add(Constants.CMSG_TUTORIAL_DATA, "RequestTutorial");
        add(Constants.CMSG_TUTORIAL_CHALLENGE, "RequestChallenge");
        add(Constants.CMSG_TUTORIAL_UPDATE_CUR_TUT, "RequestUpdateCurTut");
    }

    /**
     * Map the request code number with its corresponding request class, derived
     * from its class name using reflection, by inserting the pair into the
     * table.
     *
     * @param code a value that uniquely identifies the request type
     * @param name a string value that holds the name of the request class
     */
    public static void add(short code, String name) {
        try {
            requestTable.put(code, Class.forName("networking.request." + name));
        } catch (ClassNotFoundException e) {
            Log.println_e(e.getMessage());
        }
    }

    /**
     * Get the instance of the request class by the given request code.
     *
     * @param requestID a value that uniquely identifies the request type
     * @return the instance of the request class
     */
    public static GameRequest get(short request_code) {
        GameRequest request = null;

        try {
            Class name = requestTable.get(request_code);

            if (name != null) {
                request = (GameRequest) name.newInstance();
                request.setID(request_code);
            } else {
                Log.printf_e("Request Code [%d] does not exist!\n", request_code);
            }
        } catch (Exception e) {
            Log.println_e(e.getMessage());
        }

        return request;
    }
}
