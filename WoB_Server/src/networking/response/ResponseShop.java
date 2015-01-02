package networking.response;

// Java Imports
import java.util.List;

// Other Imports
import metadata.Constants;
import model.ShopItem;
import utility.GamePacket;

public class ResponseShop extends GameResponse {

    private List<ShopItem> shopList;

    public ResponseShop() {
        responseCode = Constants.SMSG_SHOP;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16((short) shopList.size());

        for (ShopItem item : shopList) {
            packet.addInt32(item.getID());
            packet.addShort16((short) item.getLevel());
            packet.addString(item.getName());
            packet.addShort16((short) item.getPrice());
            packet.addString(item.getDescription());

            packet.addShort16((short) item.getExtraArgs().size());
            for (String s : item.getExtraArgs()) {
                packet.addString(s);
            }

            packet.addString(item.getCategoryListAsString());
            packet.addString(item.getTagListAsString());
        }

        return packet.getBytes();
    }

    public void setShopList(List<ShopItem> shopList) {
        this.shopList = shopList;
    }
}
