package dataAccessLayer;

// Java Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Other Imports
import core.Badge;

public final class BadgeDataDAO {

    private BadgeDataDAO() {
    }

    public static void createEntry(int player_id, int badge_id, int amount, int progress) throws SQLException {
        String query_1 = "SELECT * FROM `badge_data` WHERE `player_id` = ? AND `badge_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query_1);
            pstmt.setInt(1, player_id);
            pstmt.setInt(2, badge_id);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                pstmt.close();

                String query_2 = "INSERT INTO `badge_data` (`player_id`, `badge_id`, `amount`, `progress`) VALUES (?, ?, ?, ?)";
                pstmt = connection.prepareStatement(query_2);
                pstmt.setInt(1, player_id);
                pstmt.setInt(2, badge_id);
                pstmt.setInt(3, amount);
                pstmt.setInt(4, progress);
            } else {
                pstmt.close();

                String query_3 = "UPDATE `badge_data` SET `amount` = ?, `progress` = ? WHERE `player_id` = ? AND `badge_id` = ?";
                pstmt = connection.prepareStatement(query_3);
                pstmt.setInt(1, amount);
                pstmt.setInt(2, progress);
                pstmt.setInt(3, player_id);
                pstmt.setInt(4, badge_id);
            }

            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static List<Badge> getBadgeData(int user_id) throws SQLException {
        List<Badge> badgeList = new ArrayList<Badge>();

        String query = "SELECT * FROM `badge_data` WHERE `player_id` = ? ORDER BY `badge_id`";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Badge badge = new Badge(rs.getInt("badge_id"), rs.getInt("amount"), rs.getInt("progress"));
                badgeList.add(badge);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return badgeList;
    }
}
