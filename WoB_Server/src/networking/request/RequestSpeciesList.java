package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import dataAccessLayer.ShopDAO;
import networking.response.ResponseSpeciesList;

public class RequestSpeciesList extends GameRequest {

    public RequestSpeciesList() {
    }

    @Override
    public void parse() throws IOException {
    }

    @Override
    public void doBusiness() throws Exception {
        ResponseSpeciesList responseSpeciesList = new ResponseSpeciesList();
        responseSpeciesList.setSpeciesList(ShopDAO.getItems("level:0,99"));
        responses.add(responseSpeciesList);
    }
}
