package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Gary
 */
public final class BiomassCSVDAO {

    private BiomassCSVDAO() {
    }

    public static void createCSV(String manipulation_id, String csv) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query_1 = "SELECT * FROM `biomass_csv` WHERE `manipulation_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query_1);
            pstmt.setString(1, manipulation_id);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                pstmt.close();

                String query_2 = "INSERT INTO `biomass_csv` (`manipulation_id`, `csv`) VALUES (?, ?)";
                pstmt = connection.prepareStatement(query_2);
                pstmt.setString(1, manipulation_id);
                pstmt.setString(2, csv);
            } else {
                pstmt.close();

                String query_3 = "UPDATE `biomass_csv` SET `csv` = ? WHERE `manipulation_id` = ?";
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

    public static String getCSV(String manipulation_id) throws SQLException {
        String csv = null;

        String query = "SELECT * FROM `biomass_csv` WHERE `manipulation_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, manipulation_id);
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
