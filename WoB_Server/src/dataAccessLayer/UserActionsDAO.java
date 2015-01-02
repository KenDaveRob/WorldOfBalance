package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Gary
 */
public final class UserActionsDAO {

    private UserActionsDAO() {
    }

    public static int createAction(String manipulation_id, int timestep, int event, int node_id, double biomass) throws SQLException {
        int action_id = -1;

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "INSERT INTO `user_actions` (`manipulation_id`, `timestep`, `event`, `node_id`, `biomass`) VALUES (?, ?, ?, ?, ?)";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, manipulation_id);
            pstmt.setInt(2, timestep);
            pstmt.setInt(3, event);
            pstmt.setInt(4, node_id);
            pstmt.setDouble(5, biomass);
            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                action_id = rs.getInt(1);
            }

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return action_id;
    }

    public static String getActions(String manipulation_id) throws SQLException {
        String actionsCSV = "";
        
        String query = "SELECT * FROM `user_actions` WHERE `manipulation_id` = ? ORDER BY `timestep` ASC";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, manipulation_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rs.getInt("timestep");
                rs.getShort("event");
                rs.getInt("node_id");
                rs.getDouble("biomass");
            }

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        
        return actionsCSV;
    }

    public static void createCSV(String manipulation_id, String csv) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query_1 = "SELECT * FROM `user_actions_csv` WHERE `manipulation_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query_1);
            pstmt.setString(1, manipulation_id);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                pstmt.close();

                String query_2 = "INSERT INTO `user_actions_csv` (`manipulation_id`, `csv`) VALUES (?, ?)";
                pstmt = connection.prepareStatement(query_2);
                pstmt.setString(1, manipulation_id);
                pstmt.setString(2, csv);
            } else {
                pstmt.close();

                String query_3 = "UPDATE `user_actions_csv` SET `csv` = ? WHERE `manipulation_id` = ?";
                pstmt = connection.prepareStatement(query_3);
                pstmt.setString(1, csv);
                pstmt.setString(2, manipulation_id);
            }

            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
