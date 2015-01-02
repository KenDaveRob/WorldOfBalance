package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import worldManager.gameEngine.Zone;

/**
 *
 * @author Partap Aujla
 */
public final class ZoneDAO {

    private ZoneDAO() {
    }

    /**
     * The function saves the passed zone to the database. However,
     * zonePidPk(in zone) does not get stored  because this field is
     * automatically generated in database. Also, the function checks to see
     * the passed argument is valid if not prints an appropriate message. 
     * @param zone which is ZoneType
     * @throws SQLException
     */
    public static int createZone(Zone zone) throws SQLException {
        int zone_id = -1;

        String query = "INSERT INTO `zone` (`env_id`, `type`, `order`, `row`, `column`, `manipulation_id`, `carrying_capacity`) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, zone.getEnvID());
            pstmt.setInt(2, zone.getType());
            pstmt.setInt(3, zone.getOrder());
            pstmt.setInt(4, zone.getRow());
            pstmt.setInt(5, zone.getColumn());
            pstmt.setString(6, zone.getManipulationID());
            pstmt.setInt(7, 0);
            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                zone_id = rs.getInt(1);
            }

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return zone_id;
    }

    /**
     * The function checks to make sure the passed argument is valid if not
     * prints an appropriate message.
     * @param env_id 
     * @return Returns list of Zones in an environment by matching the passed
     * environment id. If no zone is found in the database returns null.
     * @throws SQLException
     */
    public static List<Zone> getZoneByEnvironmentId(int env_id) throws SQLException {
        List<Zone> returnZoneList = new ArrayList<Zone>();

        String query = "SELECT * FROM `zone` WHERE `env_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, env_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Zone zone = new Zone(rs.getInt("zone_id"));
                zone.setOrder(rs.getInt("order"));
                zone.setEnvID(rs.getInt("env_id"));
                zone.setRow(rs.getInt("row"));
                zone.setColumn(rs.getInt("column"));
                zone.setManipulationID(rs.getString("manipulation_id"));
                zone.setMaxBiomass(rs.getFloat("max_biomass"));
                zone.setPrevBiomass(rs.getFloat("prev_biomass"));

                returnZoneList.add(zone);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return returnZoneList;
    }

    /**
     * The function checks the passed argument to make sure it is valid.  If not
     * prints an appropriate message.
     * @param environment which is EnvironmentType.  Extracts in_envID_fk.
     * @return Returns list of Zones in an environment by matching the
     * environment id which is extracted from environment.  If no zone is found
     * in the database returns null.
     * @throws SQLException
     */
    public static List<Zone> getZoneByEnvironmentID(int env_id) throws SQLException {
        List<Zone> zoneList = new ArrayList<Zone>();

        String query = "SELECT * FROM `zone` WHERE `env_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, env_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Zone zone = new Zone(rs.getInt("zone_id"));
                zone.setOrder(rs.getInt("order"));
                zone.setEnvID(rs.getInt("env_id"));
                zone.setType(rs.getInt("type"));
                zone.setRow(rs.getInt("row"));
                zone.setColumn(rs.getInt("column"));
                zone.setManipulationID(rs.getString("manipulation_id"));
                zone.setMaxBiomass(rs.getFloat("max_biomass"));
                zone.setPrevBiomass(rs.getFloat("prev_biomass"));

                zoneList.add(zone);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return zoneList;
    }

    /**
     * The function checks if the passed argument is valid if it is not then
     * prints an appropriate message otherwise matches the passed argument in
     * the database and returns the information.
     * @param zoneID
     * @return A zone type which also contains an animal list, plant list, and
     * water sources.
     * @throws SQLException 
     */
    public static Zone getZoneByZoneId(int zoneID) throws SQLException {
        Zone zone = null;

        String query = "SELECT * FROM `zone` WHERE `zone_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, zoneID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                zone = new Zone(rs.getInt("zone_id"));
                zone.setOrder(rs.getInt("order"));
                zone.setEnvID(rs.getInt("env_id"));
                zone.setRow(rs.getInt("row"));
                zone.setColumn(rs.getInt("column"));
                zone.setManipulationID(rs.getString("manipulation_id"));
                zone.setMaxBiomass(rs.getFloat("max_biomass"));
                zone.setPrevBiomass(rs.getFloat("prev_biomass"));
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return zone;
    }

    public static void updateManipulationID(int zone_id, String manipulation_id) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `zone` SET `manipulation_id` = ? WHERE `zone_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, manipulation_id);
            pstmt.setInt(2, zone_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateTimeStep(int zone_id, int time_step) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `zone` SET `current_time_step` = ? WHERE `zone_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, time_step);
            pstmt.setInt(2, zone_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateBiomass(int zone_id, float max_biomass, float prev_biomass, float total_biomass) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `zone` SET `max_biomass` = ?, `prev_biomass` = ?, `total_biomass` = ? WHERE `zone_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setFloat(1, max_biomass);
            pstmt.setFloat(2, prev_biomass);
            pstmt.setFloat(3, total_biomass);
            pstmt.setInt(4, zone_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}