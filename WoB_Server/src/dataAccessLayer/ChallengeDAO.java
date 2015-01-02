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

import model.ChallengeData;


/**
 *
 * @author Robin Pennock
 */
public class ChallengeDAO {
    private ChallengeDAO()
    {
    }
    public static ChallengeData initializeChallenge(int challenge_id)throws SQLException {
        
        ChallengeData challenge = null;
        
        //query by p_id
        String query = "SELECT * FROM `tutorial_challenges` WHERE (`c_id` = ?)";
        
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, challenge_id);
            //borrowed from playerDAO
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                challenge = new ChallengeData(rs.getInt("c_id"), 
                        rs.getString("animal_ids"), rs.getString("plant_ids"),
                        rs.getInt("enviro_score"), rs.getInt("biomass_score"),
                        rs.getInt("time"), rs.getInt("credits"),
                        rs.getInt("min_species_num"));
                
            }

            rs.close();
            pstmt.close();
        }
        finally {
            if (connection != null) {
                connection.close();
            }
        }
        
        return challenge;
    }
         public static void updateChalData(ChallengeData challenge) throws SQLException {
        
         
        //String query = "UPDATE `tutorial_data` SET `cur_tut` = ?, `milestone` = ?, `cur_challenge` = ?, chal_milestone = ? WHERE `p_id` = ?";
        String query = "UPDATE `tutorial_challenges` SET `animal_ids`=?,`plant_ids`=?,`enviro_score`=?,`biomass_score`=?,`time`=?,`credits`=?,`min_species_num`=? WHERE `c_id`=?";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, challenge.getAnimalID());
            pstmt.setString(2, challenge.getPlantID());
            pstmt.setInt(3, challenge.getEnviroScore());
            pstmt.setInt(4, challenge.getBioScore());
            pstmt.setInt(5, challenge.getTime());
            pstmt.setInt(6, challenge.getCredits());
            pstmt.setInt(7, challenge.getMinSpecies());
            pstmt.setInt(8, challenge.getCID());
            pstmt.execute();
            pstmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
    public static ArrayList<Integer> getSpeciesList(int challenge)throws SQLException {
        String input_string = null;
        String[] animalTemp = null;
        String[] plantTemp = null;
        ArrayList<Integer> output = new ArrayList();
                
        String query = "SELECT `animal_ids` FROM `tutorial_challenges` WHERE `c_id` = ?";
        String query2 = "SELECT `plant_ids` FROM `tutorial_challenges` WHERE `c_id` = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try{
            ResultSet rs;
            
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, challenge);
            rs = pstmt.executeQuery();
            if(rs.next()){
                input_string = rs.getString("animal_ids");
                //remove all whitespaces from input_string
                input_string = input_string.replace(" " , "");
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
            if(rs.next()){
                input_string = rs.getString("plant_ids");
                input_string = input_string.replace(" " , "");
                plantTemp = input_string.split(";");
            }
            rs.close();
            pstmt.close();
      
            int counter;
            for(counter=0; counter<animalTemp.length; counter++){
                //System.out.println("animalTemp[" + counter +"] = " + animalTemp[counter]);
                output.add(Integer.parseInt(animalTemp[counter]));
                //System.out.println("outputArray[" + counter +"] = " + output.get(counter));
            }
            for(int i=0; i<plantTemp.length; i++){
                //System.out.println("plantTemp[" + i +"] = " + plantTemp[i]);
                output.add(Integer.parseInt(plantTemp[i]));                
                //System.out.println("outputArray[" + i +"] = " + output.get(counter));
                counter++;
            }
        }
        finally{
            if (connection != null) {
                connection.close();
            }
        }
        return output;
    }
    public static void insertChalData(int c_id, String animal_ids, String plant_ids, 
            int enviro_score, int biomass_score, int time, int credits, 
            int min_species_num) throws SQLException
    {
        
        String query = " INSERT INTO `tutorial_challenges`(`c_id`, `animal_ids`, `plant_ids`, `enviro_score`, `biomass_score`, `time`, `credits`, `min_species_num`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DAO.getDataSource().getConnection();
            pstmt = connection.prepareStatement(query);
            //prepare insert variables
            pstmt.setInt(1, c_id);
            pstmt.setString(2, animal_ids);
            pstmt.setString(3, plant_ids);
            pstmt.setInt(4, enviro_score);
            pstmt.setInt(5, biomass_score);
            pstmt.setInt(6, time);
            pstmt.setInt(7, credits);
            pstmt.setInt(8, min_species_num);
            
            //run query
            pstmt.execute();
            pstmt.close();
        } 
        finally {
            if (connection != null) {
                connection.close();
            }
        }
    }//end insert
    
}
