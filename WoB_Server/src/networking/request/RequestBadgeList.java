package networking.request;

// Java Imports
import java.io.IOException;
import java.util.ArrayList;

// Other Imports
import core.Badge;
import core.BadgeManager;
import dataAccessLayer.BadgeDataDAO;
import networking.response.ResponseBadgeList;
import utility.DataReader;

public class RequestBadgeList extends GameRequest {

    // Data
    private int user_id;
    // Responses
    private ResponseBadgeList responseBadgeList;

    public RequestBadgeList() {
        responses.add(responseBadgeList = new ResponseBadgeList());
    }

    @Override
    public void parse() throws IOException {
        user_id = DataReader.readInt(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        BadgeManager badgeManager = new BadgeManager(user_id, null);
        badgeManager.initialize(BadgeDataDAO.getBadgeData(user_id));

        responseBadgeList.setBadgeList(new ArrayList<Badge>(badgeManager.getBadgeList().values()));
    }
}
