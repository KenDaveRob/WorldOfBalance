package dataAccessLayer;

// Java Imports
import core.GameServer;
import core.Species;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Other Imports
import core.SpeciesGroup;
import utility.Vector3;
import model.SpeciesType;

public final class ZoneSpeciesDAO {

    private ZoneSpeciesDAO() {
    }

    public static int createSpecies(int zone_id, int species_id, int biomass, Vector3<Float> position) throws SQLException {
        int group_id = -1;

        String query = "INSERT INTO `zone_species` (`zone_id`, `species_id`, `biomass`, `pos_x`, `pos_y`, `pos_z`) VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, zone_id);
            pstmt.setInt(2, species_id);
            pstmt.setInt(3, biomass);
            pstmt.setFloat(4, position.getX());
            pstmt.setFloat(5, position.getY());
            pstmt.setFloat(6, position.getZ());
            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                group_id = rs.getInt(1);
            }

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        
        return group_id;
    }

    public static List<Species> getSpecies(int zone_id) throws SQLException {
        List<Species> speciesList = new ArrayList<Species>();

        String query = "SELECT * FROM `zone_species` WHERE `zone_id` = ? ORDER BY `species_id`";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, zone_id);

            ResultSet rs = pstmt.executeQuery();

            int prev_id = -1;

            Species species = null;

            while (rs.next()) {
                int species_id = rs.getInt("species_id");

                if (species_id != prev_id) {
                    SpeciesType speciesType = GameServer.getInstance().getSpecies(species_id);

                    if (speciesType != null) {
                        species = new Species(species_id, speciesType);
                        speciesList.add(species);
                    } else {
                        continue;
                    }
                }

                int group_id = rs.getInt("group_id"), biomass = rs.getInt("biomass");
                Vector3<Float> position = new Vector3<Float>(rs.getFloat("pos_x"), rs.getFloat("pos_y"), rs.getFloat("pos_z"));

                species.add(new SpeciesGroup(species, group_id, biomass, position));
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return speciesList;
    }

    public static void removeSpecies(int group_id) throws SQLException {
        String query = "DELETE FROM `zone_species` WHERE `group_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, group_id);
            pstmt.executeUpdate();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updateBiomass(int group_id, int biomass) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `zone_species` SET `biomass` = ? WHERE `group_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, biomass);
            pstmt.setInt(2, group_id);
            pstmt.execute();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void updatePosition(int group_id, Vector3<Float> position) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE `zone_species` SET `pos_x` = ?, `pos_y` = ?, `pos_z` = ? WHERE `group_id` = ?";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setFloat(1, position.getX());
            pstmt.setFloat(2, position.getY());
            pstmt.setFloat(3, position.getZ());
            pstmt.setInt(4, group_id);
            pstmt.execute();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
