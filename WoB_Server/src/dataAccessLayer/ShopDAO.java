package dataAccessLayer;

// Java Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Other Imports
import core.GameServer;
import metadata.Constants;
import model.ShopItem;
import model.SpeciesType;

public final class ShopDAO {

    private ShopDAO() {
    }

    public static List<ShopItem> getItems(String... filters) throws SQLException {
        List<ShopItem> shopList = new ArrayList<ShopItem>();

        String query = "SELECT * FROM `shop`";

        if (filters.length > 0) {
            query += " WHERE ";
        }

        for (int i = 0; i < filters.length; i++) {
            String[] var = filters[i].split(":");

            String filter = var[0];
            String[] value = var[1].split(",");

            if (filter.equalsIgnoreCase("level")) {
                if (value.length > 1) {
                    String min = value[0], max = value[1];

                    query += min.equals("") ? "`level` >= 0" : "`level` >= " + min;
                    query += max.equals("") ? "" : " AND `level` <= " + max;
                } else {
                    query += "`level` = " + value[0];
                }
            } else if (filter.equalsIgnoreCase("type")) {
                query += "`type` IN (" + value[0];
                
                for (int j = 0; j < value.length; i++) {
                    query += value[j];
                    
                    if (j < value.length - 1) {
                        query += ", ";
                    } else {
                        query += ")";
                    }
                }
            }

            if (i < filters.length - 1) {
                query += " AND ";
            }
        }

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int itemLevel = rs.getInt("level");
                String[] tempList = rs.getString("items").split(",");

                ShopItem item = null;

                for (String item_id : tempList) {
                    SpeciesType data = GameServer.getInstance().getSpecies(Integer.parseInt(item_id));

                    if (data != null) {
                        String type = "Unknown";

                        if (data.getOrganismType() == Constants.ORGANISM_TYPE_ANIMAL) {
                            type = "Animal";
                        } else if (data.getOrganismType() == Constants.ORGANISM_TYPE_PLANT) {
                            type = "Plant";
                        }

                        if (type.equals("Animal") || type.equals("Plant")) {
                            item = new ShopItem(data.getID(), itemLevel, data.getName(), data.getDescription(), data.getCost());

                            String predatorList = data.getPredatorListAsString();
                            String preyList = data.getPreyListAsString();
                            item.setExtraArgs(Arrays.asList(String.valueOf((int) data.getAvgBiomass()), String.valueOf(data.getDietType()), String.valueOf(data.getTrophicLevel()), predatorList, preyList));

                            item.setCategoryList(Arrays.asList(type, data.getCategory()));
                        }

                        if (item != null) {
                            shopList.add(item);
                        }
                    }
                }
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return shopList;
    }
}
