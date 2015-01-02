package dataAccessLayer;

// Java Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * The UserLogDAO class contain methods used to track player progress such as
 * the amount of time spent per day of the week.
 */
public class UserLogDAO {

    private UserLogDAO() {
    }

    /**
     * Updates the database with the amount of time spent every day of the week.
     *
     * @param user_id used to identify the specific account
     * @param delta_time holds the amount of time passed
     * @throws SQLException
     */
    public static void updateTimeLog(int user_id, int delta_time) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();

            Calendar now = Calendar.getInstance();
            int day = now.get(Calendar.DAY_OF_WEEK) - 1;

            now.add(Calendar.DAY_OF_MONTH, -now.get(Calendar.DAY_OF_WEEK) + 1);
            String date_start = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);

            now.add(Calendar.DAY_OF_MONTH, 6);
            String date_end = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);

            pstmt = connection.prepareStatement("SELECT * FROM `user_log_tpd` WHERE `user_id` = ? AND `date_start` = ?");
            pstmt.setInt(1, user_id);
            pstmt.setString(2, date_start);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int play_time = rs.getInt("day_" + day);

                pstmt.close();

                pstmt = connection.prepareStatement("UPDATE `user_log_tpd` SET `day_" + day + "` = ?"
                        + " WHERE `user_id` = ? AND `date_start` = ?");
                pstmt.setInt(1, play_time + delta_time);
                pstmt.setInt(2, user_id);
                pstmt.setString(3, date_start);
            } else {
                pstmt.close();

                pstmt = connection.prepareStatement("INSERT INTO `user_log_tpd`"
                        + " (`user_id`, `day_" + day + "`, `date_start`, `date_end`) VALUES (?, ?, ?, ?)");
                pstmt.setInt(1, user_id);
                pstmt.setInt(2, delta_time);
                pstmt.setString(3, date_start);
                pstmt.setString(4, date_end);
            }

            pstmt.executeUpdate();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
