package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import metadata.Constants;
import model.World;

/**
 *
 * @author Partap Aujla
 */
public final class WorldDAO {

    private WorldDAO() {
    }

    /**
     * The function saves the passed world to the database. However,
     * worldIdPk(in world) does not get stored because this field is
     * automatically generated in database. Before the value is stored in
     * database the function checks to see if any numeric value is less than 0
     * and if any world is null. If that is the case then the function prints
     * appropriate message. Also the function checks to make sure that if
     * gameAccessType is equal to private then password is not null.
     *
     * @param world which is WorldType. Extracts numEnvironments, gameName,
     * seconds, days, maxPlayers, envType, accessType, gameMode, creatorPlayer
     * and password.
     * @throws SQLException
     */
    public static int createWorld(World world) throws SQLException {
        int world_id = -1;

        String query = "INSERT INTO `world` (`game_name`, `credits`, `seconds`, `days`, `max_players`, `env_type`, `access_type`, `game_mode`, `creator_id`, `password`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, world.getGameName());
            pstmt.setInt(2, world.getCredits());
            pstmt.setLong(3, world.getSeconds());
            pstmt.setInt(4, world.getDays());
            pstmt.setInt(5, world.getMaxPlayers());
            pstmt.setString(6, world.getEnvType());
            pstmt.setShort(7, world.getAccessType());
            pstmt.setShort(8, world.getGameMode());
            pstmt.setInt(9, world.getCreatorID());
            pstmt.setString(10, world.getPassword());
            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                world_id = rs.getInt(1);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return world_id;
    }

    /**
     *
     * @return Returns a list of all PvE Worlds. Each of these world contains
     * PlayerType, the person who created the World, and list of EnvironmentType
     * which is to say a list of environments in the world.
     * @throws SQLException
     */
    public static List<World> getAllPvEWorlds() throws SQLException {
        List<World> worldList = new ArrayList<World>();

        String query = "SELECT * FROM `world` WHERE `game_mode` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, Constants.GAME_TYPE_PVE);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                World world = createWorld(rs);
                worldList.add(world);
            }

            rs.close();
            pstmt.close();

            for (World world : worldList) {
                world.setEnvironment(EnvironmentDAO.getEnvironmentByWorldID(world.getID()));
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return worldList;
    }

    /**
     *
     * @return Returns a list of all PvP Worlds. Each of these world contains
     * PlayerType, the person who created the World, and list of EnvironmentType
     * which is to say a list of environments in the world.
     * @throws SQLException
     */
    public static List<World> getAllPvPWorlds() throws SQLException {
        List<World> worldList = new ArrayList<World>();

        String query = "SELECT * FROM `world` WHERE `game_mode` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, Constants.GAME_TYPE_PVP);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                World world = createWorld(rs);
                worldList.add(world);
            }

            rs.close();
            pstmt.close();

            for (World world : worldList) {
                world.setEnvironment(EnvironmentDAO.getEnvironmentByWorldID(world.getID()));
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return worldList;
    }

    public static List<World> getPlayerWorlds(int player_id) throws SQLException {
        List<World> worldList = new ArrayList<World>();

        String query = "SELECT * FROM `world` WHERE `creator_id` = ? ORDER BY `last_played` DESC";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, player_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                World world = createWorld(rs);
                worldList.add(world);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return worldList;
    }

    private static World createWorld(ResultSet rs) throws SQLException {
        World world = new World(rs.getInt("world_id"));
        world.setGameName(rs.getString("game_name"));
        world.setCredits(rs.getInt("credits"));
        world.setPlayTime(rs.getInt("play_time"));
        world.setTimeRate(rs.getFloat("time_rate"));
        world.setSeconds(rs.getLong("seconds"));
        world.setYear(rs.getInt("year"));
        world.setMonth(rs.getInt("month"));
        world.setDays(rs.getInt("days"));
        world.setMaxPlayers(rs.getInt("max_players"));
        world.setEnvType(rs.getString("env_type"));
        world.setAccessType(rs.getShort("access_type"));
        world.setGameMode(rs.getShort("game_mode"));
        world.setPassword(rs.getString("password"));
        world.setCreatorID(rs.getInt("creator_id"));
        world.setLastPlayed(rs.getString("last_played"));

        return world;
    }

    public static World getWorld(int world_id) throws SQLException {
        World world = null;

        String query = "SELECT * FROM `world` WHERE `world_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, world_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                world = createWorld(rs);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return world;
    }

    /**
     * Function checks if passed argument is valid. If not prints out an
     * appropriate message.
     *
     * @param name which is StringType.
     * @return Returns a world from database which matches the worldName passed.
     * The returned world has a PlayerType which is the Player that created the
     * world and list of EnvironmentType that is to say list of environments
     * belonging to the world. If no world found in database returns null.
     * @throws SQLException
     */
    public static World getWorldByName(String name) throws SQLException {
        World world = null;

        String query = "SELECT * FROM `world` WHERE `game_name` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                world = createWorld(rs);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return world;
    }

    public static World getWorldByPlayerID(int player_id) throws SQLException {
        World world = null;

        String query = "SELECT * FROM `world` WHERE `creator_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, player_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                world = createWorld(rs);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return world;
    }

    /**
     * Function checks if passed argument is valid. If not prints out an
     * appropriate message.
     *
     * @param world which is WorldType. Extracts accessType, gameName, and
     * password.
     * @return Returns a world from database which matches all three accessType,
     * gameName, and password (Extracted from passed world). The returned world
     * has a PlayerType which is the Player that created the world and list of
     * EnvironmentType that is to say list of environments belonging to the
     * world. If no world found in database returns null.
     * @throws SQLException
     */
    public static World getPrivateWorldByWorldNameAndPassword(short access_type, String name, String password) throws SQLException {
        World world = null;

        String query = "SELECT * FROM `world` WHERE `access_type` = ? AND `game_name` = ? AND `password` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setShort(1, access_type);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                world = createWorld(rs);
            }

            rs.close();
            pstmt.close();

            world.setEnvironment(EnvironmentDAO.getEnvironmentByWorldID(world.getID()));
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return world;
    }

    /**
     * The function updates the passed world to the database. However,
     * worldIdPk(in world) does not get updated because this field is
     * automatically generated in database. Before the value is updated in
     * database the function checks to see if any numeric value is less than 0
     * and if any Object value is null. If that is the case then the function
     * prints out Invalid Values. Also the function checks to make sure that if
     * gameAccessType is equal to private then password is not null. The
     * function also checks to make sure that the world being updated exists. If
     * it does not then prints out appropriate message. Also, note this function
     * only updates the properties of the World. For example the Environments do
     * not get updated. If needed this will be implemented in the future.
     *
     * @param world which is WorldType. Extracts gameName.
     * @throws SQLException
     */
    public static void updateWorldProperties(World world) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `world` SET `game_name` = ?, `credits` = ?, `play_time` = ?, `time_rate` = ?, `seconds` = ?, `year` = ?, `month` = ?, `days` = ?, `max_players` = ?, `env_type` = ?, `access_type` = ?, `game_mode` = ?, `creator_id` = ?, `password` = ? WHERE `world_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, world.getGameName());
            pstmt.setInt(2, world.getCredits());
            pstmt.setLong(3, world.getPlayTime());
            pstmt.setFloat(4, world.getTimeRate());
            pstmt.setLong(5, world.getSeconds());
            pstmt.setInt(6, world.getYear());
            pstmt.setInt(7, world.getMonth());
            pstmt.setInt(8, world.getDays());
            pstmt.setInt(9, world.getMaxPlayers());
            pstmt.setString(10, world.getEnvType());
            pstmt.setShort(11, world.getAccessType());
            pstmt.setShort(12, world.getGameMode());
            pstmt.setInt(13, world.getCreatorID());
            pstmt.setString(14, world.getPassword());
            pstmt.setInt(15, world.getID());
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateTime(World world) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `world` SET `play_time` = ?, `seconds` = ?, `year` = ?, `month` = ?, `days` = ? WHERE `world_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, world.getPlayTime());
            pstmt.setLong(2, world.getSeconds());
            pstmt.setInt(3, world.getYear());
            pstmt.setInt(4, world.getMonth());
            pstmt.setInt(5, world.getDays());
            pstmt.setInt(6, world.getID());
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateLastPlayed(int world_id) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `world` SET `last_played` = ? WHERE `world_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            pstmt.setInt(2, world_id);
            pstmt.execute();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void removeWorld(int world_id) throws SQLException {
        String query = "DELETE FROM `world` WHERE `world_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, world_id);
            pstmt.executeUpdate();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static boolean containsWorldName(String name) throws SQLException {
        boolean status = false;

        String query = "SELECT * FROM `world` WHERE `game_name` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
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

    public static void updateCredits(int world_id, int amount) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `world` SET `credits` = ? WHERE `world_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, world_id);
            pstmt.setInt(2, amount);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
