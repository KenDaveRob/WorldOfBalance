package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ZoneType;

/**
 *
 * @author Gary
 */
public final class ZoneTypeDAO {

    private ZoneTypeDAO() {
    }

    public static ZoneType getZoneType(int zone_type_id) throws SQLException {
        ZoneType zoneType = null;

        String query = "SELECT * FROM `zone_type` WHERE `zone_type_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, zone_type_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                zoneType = new ZoneType(zone_type_id, rs.getBoolean("contains_water"));
            }

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return zoneType;
    }
}