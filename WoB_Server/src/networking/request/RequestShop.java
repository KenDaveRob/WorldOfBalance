package networking.request;

// Java Imports
import java.io.IOException;

// Other Imports
import dataAccessLayer.ShopDAO;
import networking.response.ResponseShop;

public class RequestShop extends GameRequest {

    public RequestShop() {
    }

    @Override
    public void parse() throws IOException {
    }

    @Override
    public void doBusiness() throws Exception {
        ResponseShop responseShop = new ResponseShop();
        responseShop.setShopList(ShopDAO.getItems("level:0,99"));
        responses.add(responseShop);
    }
}
