package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author Gary
 */
public final class ZoneNodeAddDAO {

    private ZoneNodeAddDAO() {
    }

    public static void createEntry(int zone_id, int node_id, int amount) throws SQLException {
        String query = "INSERT INTO `zone_node_add` (`zone_id`, `node_id`, `amount`) VALUES (?, ?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, zone_id);
            pstmt.setInt(2, node_id);
            pstmt.setInt(3, amount);
            pstmt.execute();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void removeEntry(int zone_id, int node_id) throws SQLException {
        String query = "DELETE FROM `zone_node_add` WHERE `zone_id` = ? AND `node_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, zone_id);
            pstmt.setInt(2, node_id);
            pstmt.executeUpdate();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static HashMap<Integer, Integer> getList(int zone_id) throws SQLException {
        HashMap<Integer, Integer> nodeList = new HashMap<Integer, Integer>();

        String query = "SELECT * FROM `zone_node_add` WHERE `zone_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, zone_id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int node_id = rs.getInt("node_id");
                int amount = rs.getInt("amount");

                nodeList.put(node_id, amount);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return nodeList;
    }

    public static void updateAmount(int zone_id, int node_id, int amount) throws SQLException {
        String query = "UPDATE `zone_node_add` SET `amount` = ? WHERE `zone_id` = ? AND `node_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, zone_id);
            pstmt.setInt(3, node_id);
            pstmt.executeUpdate();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
