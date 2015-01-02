package dataAccessLayer;

// Java Imports
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

// Other Imports
import model.Player;

/**
 * The PlayerDAO class hold methods that can execute a variety of different
 * queries for very specific purposes. For use with queries utilizing the
 * "player" table.
 */
public final class PlayerDAO {

    private PlayerDAO() {
    }
    
    private static String getMD5(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input);

        byte[] mdbytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return sb.toString();
    }

    /**
     * Creates an account profile using the information as provided to the
     * method into the database.
     *
     * @param email contains the email used for the account
     * @param password contains the MD5-hashed password
     * @param username is the display name describing the user
     * @param first_name is the user's first name
     * @param last_name is the user's last name
     * @param last_ip contains the location by IP used to create the account
     * @return the player ID generated from the database
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public static int createAccount(String email, String password, String username, String first_name, String last_name, String last_ip) throws SQLException, NoSuchAlgorithmException {
        int player_id = -1;

        String query = "INSERT INTO `player` (`username`, `email`, `password`, `salt`, `first_name`, `last_name`, `last_ip`) VALUES (?, ?, MD5(CONCAT(?, ?)), ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            String salt = PlayerDAO.getMD5(new Long(System.currentTimeMillis()).toString().getBytes());
            pstmt.setString(4, salt);
            pstmt.setString(5, salt);

            pstmt.setString(6, first_name);
            pstmt.setString(7, last_name);
            pstmt.setString(8, last_ip);
            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                player_id = rs.getInt(1);
            }

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return player_id;
    }

    /**
     * Retrieve account information from the database using both email and
     * password.
     *
     * @param user_id is used in combination with password to identify the account
     * @param password is used in combination with user_id to identify the account
     * @return a Player instance holding player information
     * @throws SQLException
     */
    public static Player getAccount(String user_id, String password) throws SQLException {
        Player player = null;

        String query = "SELECT * FROM `player` WHERE (`username` = ? OR `email` = ?) AND `password` = MD5(CONCAT(?, `salt`))";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user_id);
            pstmt.setString(2, user_id);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                player = new Player(rs.getInt("player_id"));
                player.setUsername(rs.getString("username"));
                player.setEmail(rs.getString("email"));
                player.setPassword(rs.getString("password"));
                player.setPlayTime(rs.getLong("play_time"));
                player.setActiveTime(rs.getLong("active_time"));
                player.setLastLogout(rs.getString("last_logout"));
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return player;
    }

    /**
     * Confirms if the email already exists in the "player" table.
     *
     * @param email is a string containing the email
     * @return true if the email already exists
     * @throws SQLException
     */
    public static boolean containsEmail(String email) throws SQLException {
        boolean status = false;

        String query = "SELECT * FROM `player` WHERE `email` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            status = rs.next();

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return status;
    }

    /**
     * Confirms if the username already exists in the "player" table.
     *
     * @param username is a string containing the username
     * @return true if the username already exists
     * @throws SQLException
     */
    public static boolean containsUsername(String username) throws SQLException {
        boolean status = false;

        String query = "SELECT * FROM `player` WHERE `username` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            status = rs.next();

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return status;
    }

    /**
     * Updates the database by marking the account active and recording at which
     * time, including location, the account was accessed.
     *
     * @param player_id used to identify the specific account
     * @param address holds the IP address in current use
     * @throws SQLException
     */
    public static void updateLogin(int player_id, String address) throws SQLException {
        String query = "UPDATE `player` SET `online` = 1, `last_login` = ?, `last_ip` = ?  WHERE `player_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            pstmt.setString(2, address);
            pstmt.setInt(3, player_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Updates the database by marking the account inactive, recording the total
     * time the account has been in use, and the time when the user logged off.
     *
     * @param player_id used to identify the specific account
     * @throws SQLException
     */
    public static void updateLogout(int player_id) throws SQLException {
        String query = "UPDATE `player` SET `online` = 0, `last_logout` = ? WHERE `player_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            pstmt.setInt(2, player_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Updates the database by recording the total time the account has been in
     * use.
     *
     * @param player_id used to identify the specific account
     * @param play_time holds the total time the account has been logged in
     * @param active_time holds the total time being active
     * @throws SQLException
     */
    public static void updatePlayTime(int player_id, long play_time, long active_time) throws SQLException {
        String query = "UPDATE `player` SET `play_time` = ?, `active_time` = ? WHERE `player_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, play_time);
            pstmt.setLong(2, active_time);
            pstmt.setInt(3, player_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
