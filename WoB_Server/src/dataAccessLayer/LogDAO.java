package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogDAO {

    private LogDAO() {
    }

    public static void createMessage(int player_id, String message) throws SQLException {
        String query = "INSERT INTO `log_chat` (`player_id`, `message`) VALUES (?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, player_id);
            pstmt.setString(2, message);
            pstmt.execute();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void createInitialSpecies(int player_id, int zone_id, String species) throws SQLException {
        String query = "INSERT INTO `log_initial_species` (`player_id`, `zone_id`, `species`) VALUES (?, ?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, player_id);
            pstmt.setInt(2, zone_id);
            pstmt.setString(3, species);
            pstmt.execute();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void createErrorLog(int user_id, String message) throws SQLException {
        String query = "INSERT INTO `log_error` (`user_id`, `message`) VALUES (?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setString(2, message);
            pstmt.execute();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
