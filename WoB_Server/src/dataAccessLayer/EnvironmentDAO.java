package dataAccessLayer;

// Java Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Other Imports
import model.Environment;
import worldManager.gameEngine.Zone;

public final class EnvironmentDAO {

    private EnvironmentDAO() {
    }

    /**
     * The function saves the passed environment to the database. However,
     * envIdPk(in environment) does not get stored  because this field is
     * automatically generated in database. Function checks if the passed
     * argument is valid.  If not prints an appropriate message.
     * @param environment which is EnvironmentType. Extracts inWorldIDFk,
     * envRow, and envColumn.
     * @throws SQLException
     */
    public static int createEnvironment(Environment environment) throws SQLException {
        int env_id = -1;

        String query = "INSERT INTO `environment` (`world_id`, `player_id`) VALUES (?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, environment.getWorldID());
            pstmt.setInt(2, environment.getOwnerID());
            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                env_id = rs.getInt(1);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return env_id;
    }

    public static Environment getEnvironment(int player_id, int world_id) throws SQLException {
        Environment environment = null;

        String query = "SELECT * FROM `environment` WHERE `world_id` = ? AND `player_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, world_id);
            pstmt.setInt(2, player_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                environment = new Environment(rs.getInt("env_id"));
                environment.setWorldID(rs.getInt("world_id"));
                environment.setOwnerID(rs.getInt("player_id"));
                environment.setEnvironmentScore(rs.getInt("score"));
                environment.setHighEnvScore(rs.getInt("high_score"));
                environment.setAccumulatedEnvScore(rs.getInt("accumulated_score"));

                List<Zone> zoneList = ZoneDAO.getZoneByEnvironmentID(environment.getID());

                for (Zone zone : zoneList) {
                    zone.setEnvironment(environment);
                }

                environment.setZones(zoneList);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return environment;
    }

    public static Environment getEnvironmentByWorldID(int world_id) throws SQLException {
        Environment environment = null;

        String query = "SELECT * FROM `environment` WHERE `world_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, world_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                environment = new Environment(rs.getInt("env_id"));
                environment.setWorldID(rs.getInt("world_id"));
                environment.setOwnerID(rs.getInt("player_id"));
                environment.setEnvironmentScore(rs.getInt("score"));
                environment.setHighEnvScore(rs.getInt("high_score"));
                environment.setAccumulatedEnvScore(rs.getInt("accumulated_score"));

                List<Zone> zoneList = ZoneDAO.getZoneByEnvironmentID(environment.getID());

                for (Zone zone : zoneList) {
                    zone.setEnvironment(environment);
                }

                environment.setZones(zoneList);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return environment;
    }

    /**
     * Function checks if the passed argument is valid.  If not prints an
     * appropriate message and returns null.  Note this function does not
     * return the zones in the environment.  (Might need to be implemented. 
     * Check other implementations on how to accomplish )
     * @param envID
     * @return Returns the Environment which matches the passed argument in the
     * database.  If none found returns null.
     * @throws SQLException
     */
    public static Environment getEnvironmentByEnvID(int envID) throws SQLException {
        Environment env = null;

        String query = "SELECT * FROM `environment` WHERE `env_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, envID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                env = new Environment(rs.getInt("env_id"));
                env.setWorldID(rs.getInt("world_id"));
                env.setOwnerID(rs.getInt("player_id"));
                env.setEnvironmentScore(rs.getInt("score"));
                env.setHighEnvScore(rs.getInt("high_score"));
                env.setAccumulatedEnvScore(rs.getInt("accumulated_score"));
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return env;
    }

    /**
     * Function checks if the passed argument is valid.  If not prints an
     * appropriate message.  The function matches the passed argument in the
     * database and then deletes it.
     * @throws SQLException
     */
    public static void deleteEnvironmentByID(int env_id) throws SQLException {
        String query = "DELETE FROM `avatar` WHERE `env_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, env_id);
            pstmt.executeUpdate();
            pstmt.close();

            String query1 = "DELETE FROM `environment` WHERE `env_id` = ?";
            pstmt = connection.prepareStatement(query1);
            pstmt.setInt(1, env_id);
            pstmt.executeUpdate();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static List<String[]> getBestEnvScore(int min_range, int max_range) throws SQLException {
        return getBestEnvScore(min_range, max_range, new ArrayList<String>());
    }

    public static List<String[]> getBestEnvScore(int min_range, int max_range, List<String> patternList) throws SQLException {
        List<String[]> scoreList = new ArrayList<String[]>();

        String query = "SELECT * FROM `environment` e JOIN `player` p ON e.`player_id` = p.`player_id`";

        if (!patternList.isEmpty()) {
            query += " WHERE p.`username` REGEXP '";

            for (int i = 0; i < patternList.size(); i++) {
                query += patternList.get(i);

                if (i < patternList.size() - 1) {
                    query += "|";
                }
            }

            query += "'";
        }

        query += " GROUP BY e.`player_id` ORDER BY e.`high_score` DESC LIMIT ?, ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, min_range);
            pstmt.setInt(2, max_range);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] score = new String[]{rs.getString("username"), rs.getString("high_score")};
                scoreList.add(score);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return scoreList;
    }
    
    public static List<String[]> getBestTotalEnvScore(int min_range, int max_range) throws SQLException {
        return getBestTotalEnvScore(min_range, max_range, new ArrayList<String>());
    }

    public static List<String[]> getBestTotalEnvScore(int min_range, int max_range, List<String> patternList) throws SQLException {
        List<String[]> scoreList = new ArrayList<String[]>();

        String query = "SELECT * FROM `environment` e JOIN `player` p ON e.`player_id` = p.`player_id`";

        if (!patternList.isEmpty()) {
            query += " WHERE p.`username` REGEXP '";

            for (int i = 0; i < patternList.size(); i++) {
                query += patternList.get(i);

                if (i < patternList.size() - 1) {
                    query += "|";
                }
            }

            query += "'";
        }

        query += " GROUP BY e.`player_id` ORDER BY e.`accumulated_score` DESC LIMIT ?, ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, min_range);
            pstmt.setInt(2, max_range);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] score = new String[]{rs.getString("username"), rs.getString("accumulated_score")};
                scoreList.add(score);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return scoreList;
    }

    public static List<String[]> getBestCurrentEnvScore(int min_range, int max_range) throws SQLException {
        return getBestCurrentEnvScore(min_range, max_range, new ArrayList<String>());
    }

    public static List<String[]> getBestCurrentEnvScore(int min_range, int max_range, List<String> patternList) throws SQLException {
        List<String[]> scoreList = new ArrayList<String[]>();

        String query = "SELECT * FROM `environment` e JOIN `player` p ON e.`player_id` = p.`player_id`";

        if (!patternList.isEmpty()) {
            query += " WHERE p.`username` REGEXP '";

            for (int i = 0; i < patternList.size(); i++) {
                query += patternList.get(i);

                if (i < patternList.size() - 1) {
                    query += "|";
                }
            }

            query += "'";
        }

        query += " GROUP BY e.`player_id` ORDER BY e.`score` DESC LIMIT ?, ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, min_range);
            pstmt.setInt(2, max_range);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] score = new String[]{rs.getString("username"), rs.getString("score")};
                scoreList.add(score);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return scoreList;
    }

    /**
     * Function checks if the passed argument is valid.  If not prints an
     * appropriate message.  The function extracts envIdPk, inWorldIDFk, 
     * envRow, envColumn, and envMapID.  After extracting the information the 
     * function matches the envIdPk in the database and then updates the 
     * information for that environment with data from passed argument.
     * @param env which is EnvironmentType.
     * @throws SQLException
     */
    public static void updateEnvironment(Environment env) throws SQLException {
        String query = "UPDATE `environment` SET `world_id` = ? WHERE `env_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, env.getWorldID());
            pstmt.setInt(2, env.getID());
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateEnvironmentScore(int env_id, int envScore, int highEnvScore) throws SQLException {
        String query = "UPDATE `environment` SET `score` = ?, `high_score` = ? WHERE `env_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, envScore);
            pstmt.setInt(2, highEnvScore);
            pstmt.setInt(3, env_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateAccumEnvScore(int env_id, int score) throws SQLException {
        String query = "UPDATE `environment` SET `accumulated_score` = ? WHERE `env_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, score);
            pstmt.setInt(2, env_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static List<Integer> getEnvironmentScores() throws SQLException {
        List<Integer> scoreList = new ArrayList<Integer>();

        String query = "SELECT * FROM `environment` ORDER BY `high_score` DESC";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                scoreList.add(rs.getInt("high_score"));
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return scoreList;
    }
}
