package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Gary
 */
public final class ScoreCSVDAO {

    private ScoreCSVDAO() {
    }

    public static void createCSV(int zone_id, String csv) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query_1 = "SELECT * FROM `score_csv` WHERE `zone_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query_1);
            pstmt.setInt(1, zone_id);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                pstmt.close();

                String query_2 = "INSERT INTO `score_csv` (`zone_id`, `csv`) VALUES (?, ?)";
                pstmt = connection.prepareStatement(query_2);
                pstmt.setInt(1, zone_id);
                pstmt.setString(2, csv);
            } else {
                pstmt.close();

                String query_3 = "UPDATE `score_csv` SET `csv` = ? WHERE `zone_id` = ?";
                pstmt = connection.prepareStatement(query_3);
                pstmt.setString(1, csv);
                pstmt.setInt(2, zone_id);
            }

            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static String getCSV(int zone_id) throws SQLException {
        String csv = null;

        String query = "SELECT * FROM `score_csv` WHERE `zone_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, zone_id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                csv = rs.getString("csv");
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return csv;
    }
}
