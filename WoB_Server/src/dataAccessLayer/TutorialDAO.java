/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessLayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.TutorialData;

import model.AnimalType;
import model.PlantType;

/**
 *
 * @author Robin Penock
 */
public class TutorialDAO {

    private TutorialDAO() {
    }

    //initializes a TutorialData object by p_id
    public static TutorialData initializeTutorial(int p_id) throws SQLException {

        TutorialData tutorial = null;

        //query by p_id
        String query = "SELECT * FROM `tutorial_data` WHERE (`p_id` = ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            //borrowed from playerDAO
            pstmt.setInt(1, p_id);
            //pstmt.setInt(2, cur_tut);
            //pstmt.setInt(3, milestone);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                tutorial = new TutorialData(rs.getInt("p_id"), rs.getInt("cur_tut"),
                        rs.getInt("milestone"), rs.getInt("cur_challenge"), rs.getInt("chal_milestone"));

            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return tutorial;
    }

    //pulls the content strings from the DB
    public static ArrayList<String[]> initializeContent() throws SQLException {

        ArrayList<String[]> tutorialContent = new ArrayList<String[]>();
        //need to double check
        String query = "SELECT `content` FROM `tutorial_content`";

        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            //will this grab all the content strings?
            ResultSet rs = pstmt.executeQuery();
            //from environmentDAO
            String[] newstring;
            while (rs.next()) {
                newstring = new String[]{rs.getString("content")};
                tutorialContent.add(newstring);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return tutorialContent;
    }
    public static ArrayList<Boolean> initializeNextable() throws SQLException {

        ArrayList<Boolean> nextableAL = new ArrayList<Boolean>();
        //need to double check
        String query = "SELECT `nextable` FROM `tutorial_content`";

        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            //will this grab all the content strings?
            ResultSet rs = pstmt.executeQuery();
            //from environmentDAO
            boolean new_bool = false;
            while (rs.next()) {
                new_bool = rs.getBoolean("nextable");
                nextableAL.add(new_bool);
            }

            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return nextableAL;
    }

    //allows for updates to the tutorial_data BD
    public static void updateTutData(TutorialData tutorial) throws SQLException {

        String query = "UPDATE `tutorial_data` SET `cur_tut` = ?, `milestone` = ?, `cur_challenge` = ?, chal_milestone = ? WHERE `p_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, tutorial.getCurTut());
            pstmt.setInt(2, tutorial.getMilestone());
            pstmt.setInt(3, tutorial.getCurChallenge());
            pstmt.setInt(4, tutorial.getChalMilestone());
            pstmt.setInt(5, tutorial.getPlayerID());
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
    public static void updateCurTut(int p_id, int cur_tut) throws SQLException {

        String query = "UPDATE `tutorial_data` SET `cur_tut` = ? WHERE `p_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, cur_tut);
            pstmt.setInt(2, p_id);
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
     //ANTIQUATED IGNORE
    //this function creates an list of animal objects based on the species_ids 
    //     field in tutorial challenges
    /*
    public static List<AnimalType> getAnimals(TutorialData tutorial) throws SQLException {
        List<AnimalType> animalList = new ArrayList<AnimalType>();

        String query = "SELECT `animal_ids` FROM `tutorial_challenges` WHERE `c_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, tutorial.getCurChallenge());
            ResultSet rs = pstmt.executeQuery();

            //next 2 lines parse species_ids string
            String animal = rs.getString("animal_ids");
            String[] requiredAnimals = animal.split(";");

            rs.close();
            pstmt.close();
            //now grab species
            int species_id = -1;
            AnimalType animalType = null;

            query = "SELECT * FROM `species`";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            //below modified from speciesDAO
            int i = 0;
            int cur_animal = Integer.parseInt(requiredAnimals[i]);
            while ((rs.next()) && (i < requiredAnimals.length)) {
                if (cur_animal != species_id) {
                    cur_animal = Integer.parseInt(requiredAnimals[i]);
                    species_id = cur_animal;

                    animalType = new AnimalType(species_id);
                    animalType.setName(rs.getString("name"));
                    animalType.setOrganismType(rs.getInt("organism_type"));
                    animalType.setCost(rs.getInt("cost"));
                    animalType.setDescription(rs.getString("description"));
                    animalType.setCategory(rs.getString("category"));
                    animalType.setAvgBiomass(rs.getInt("max_biomass"));
                    animalType.setWaterBiomassLoss(rs.getInt("water_biomass_loss"));
                    animalType.setDietType(rs.getShort("diet_type"));
                    animalType.setWaterNeedFrequency(rs.getInt("water_need_frequency"));
                    animalType.setMetabolism(rs.getFloat("metabolism"));
                    animalType.setTrophicLevel(rs.getFloat("trophic_level"));
                    animalType.setModelID(rs.getInt("model_id"));
                    animalType.setHealChance(rs.getFloat("heal_chance"));
                    animalType.setGroupCapacity(rs.getInt("group_capacity"));

                    animalList.add(animalType);
                    i++;
                }
            }
            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return animalList;
    }//end species stuff
    */

    //antiquated ignore
    /*
    public static List<PlantType> getPlants(TutorialData tutorial) throws SQLException {
        List<PlantType> plantList = new ArrayList<PlantType>();

        String query = "SELECT `plant_ids` FROM `tutorial_challenges` WHERE `c_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, tutorial.getCurChallenge());
            ResultSet rs = pstmt.executeQuery();

            //next 2 lines parse species_ids string
            String plants = rs.getString("plant_ids");
            String[] requiredPlants = plants.split(";");

            rs.close();
            pstmt.close();
            //now grab species
            int species_id = -1;
            PlantType plantType = null;

            query = "SELECT * FROM `species`";

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            //below modified from speciesDAO
            int i = 0;
            int cur_plant = Integer.parseInt(requiredPlants[i]);
            while ((rs.next()) && (i < requiredPlants.length)) {
                if (cur_plant != species_id) {
                    cur_plant = Integer.parseInt(requiredPlants[i]);
                    species_id = cur_plant;

                    plantType = new PlantType(species_id);
                    plantType.setName(rs.getString("name"));
                    plantType.setOrganismType(rs.getInt("organism_type"));
                    plantType.setCost(rs.getInt("cost"));
                    plantType.setDescription(rs.getString("description"));
                    plantType.setCategory(rs.getString("category"));
                    plantType.setAvgBiomass(rs.getInt("max_biomass"));
                    plantType.setWaterNeedFrequency(rs.getInt("water_need_frequency"));
                    plantType.setLightNeedFrequency(rs.getInt("light_need_frequency"));
                    plantType.setGrowRadius(rs.getFloat("grow_radius"));
                    plantType.setCarryingCapacity(rs.getFloat("carrying_capacity"));
                    plantType.setTrophicLevel(rs.getFloat("trophic_level"));
                    plantType.setGrowthRate(rs.getFloat("growth_rate"));
                    plantType.setModelID(rs.getInt("model_id"));
                    plantType.setHealChance(rs.getFloat("heal_chance"));
                    plantType.setGroupCapacity(rs.getInt("group_capacity"));

                    plantList.add(plantType);
                    i++;
                }
            }
            rs.close();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return plantList;
    }
    */

    //provides a simple integer ArrayList containing the species_ids of need
    //  species for challenges
    public static ArrayList<Integer> getSpeciesList(int challenge) throws SQLException {
        String input_string = null;
        String[] animalTemp = null;
        String[] plantTemp = null;
        ArrayList<Integer> output = new ArrayList();

        String query = "SELECT `animal_ids` FROM `tutorial_challenges` WHERE `c_id` = ?";
        String query2 = "SELECT `plant_ids` FROM `tutorial_challenges` WHERE `c_id` = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            ResultSet rs;

            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, challenge);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                input_string = rs.getString("animal_ids");
                //remove all whitespaces from input_string
                input_string = input_string.replace(" ", "");
                //break up incoming string using ; as delim and adds
                //each element to the animalTemp array
                animalTemp = input_string.split(";");
            }
            rs.close();
            pstmt.close();

            //now get plant stuff
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query2);
            pstmt.setInt(1, challenge);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                input_string = rs.getString("plant_ids");
                input_string = input_string.replace(" ", "");
                plantTemp = input_string.split(";");
            }
            rs.close();
            pstmt.close();

            int counter;
            for (counter = 0; counter < animalTemp.length; counter++) {
                //System.out.println("animalTemp[" + counter +"] = " + animalTemp[counter]);
                output.add(Integer.parseInt(animalTemp[counter]));
                //System.out.println("outputArray[" + counter +"] = " + output.get(counter));
            }
            for (int i = 0; i < plantTemp.length; i++) {
                //System.out.println("plantTemp[" + i +"] = " + plantTemp[i]);
                output.add(Integer.parseInt(plantTemp[i]));
                //System.out.println("outputArray[" + i +"] = " + output.get(counter));
                counter++;
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return output;
    }

    //allows for others to insert into the DB
    public static void insertTutData(int p_id, int cut_tut, int milestone,
            int cur_challenge, int chal_milestone) throws SQLException {

        String query = "INSERT INTO `tutorial_data` (`p_id`, `cur_tut`, `milestone`, `cur_challenge`, `chal_milestone`) VALUES (? ,?, ? , ?, ?)";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            //prepare insert variables
            pstmt.setInt(1, p_id);
            pstmt.setInt(2, cut_tut);
            pstmt.setInt(3, milestone);
            pstmt.setInt(4, cur_challenge);
            pstmt.setInt(5, chal_milestone);

            //run query
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }//end insertTutData

     public static void removeAllSpecies(int zone_id) throws SQLException {
        String query1 = "DELETE FROM `zone_species` WHERE `zone_id` = ?";
        String query2 = "DELETE FROM `zone_node_add` WHERE `zone_id` = ?";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query1);
            pstmt.setInt(1, zone_id);
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement(query2);
            pstmt.setInt(1, zone_id);
            pstmt.executeUpdate();

            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
