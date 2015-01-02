package dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import worldManager.gameEngine.Disease;

/**
 *
 * @author Partap Aujla
 */
public final class DiseaseDAO {

    private DiseaseDAO() {
    }

    /**
     * Function checks if the passed argument is valid.  If not prints an
     * appropriate message and returns null.
     * @param disease_id which is intType.
     * @return Returns the Disease from database by matching disease_id.
     * If none found returns null.
     * @throws SQLException
     */
    public static Disease getByDiseaseID(int disease_id) throws SQLException {
        Disease returnDisease = null;

        String query = "SELECT * FROM disease WHERE disease_id = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, disease_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                returnDisease = new Disease(rs.getInt("disease_id"));
                returnDisease.setName(rs.getString("name"));
                returnDisease.setDescription(rs.getString("description"));
                returnDisease.setInfectChance(rs.getFloat("infect_chance"));
                returnDisease.setSpreadChance(rs.getFloat("spread_chance"));
                returnDisease.setDeathRate(rs.getFloat("death_rate"));
                returnDisease.setHealChance(rs.getFloat("heal_chance"));
            }

            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DiseaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return returnDisease;
    }
}
