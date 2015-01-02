/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.TimerTask;
import java.util.ArrayList;

import core.GameClient;
import dataAccessLayer.UserLogDAO;
import dataAccessLayer.ChallengeDAO;
import metadata.Constants;
import utility.GameTimer;

/**
 *
 * @author Robin Pennock
 */
public class ChallengeData {
    
    private int c_id;
    private String animal_ids;
    private String plant_ids;
    private int enviro_score;
    private int biomass_score;
    private int time;
    private int credits;
    private int min_species_num;
    
    public ChallengeData(){};
    
    public ChallengeData(int c_id, String animal_ids, String plant_ids, 
            int enviro_score, int biomass_score, int time, int credits, int min_species_num){
        
        this.c_id = c_id;
        this.animal_ids = animal_ids;        
        this.plant_ids = plant_ids;       
        this.enviro_score=enviro_score;
        this.biomass_score=biomass_score;
        this.time = time;
        this.credits = credits;
        this.min_species_num = min_species_num;
    }
    
    public int getCID(){
    return c_id;
    }
    
    public int setCID(int c_id) {
        return this.c_id = c_id;
    }
    public String getAnimalID(){
        return animal_ids;
    }
    
    public String setAnimalID(String animal_ids) {
        return this.animal_ids = animal_ids;
    }
    
    public String getPlantID(){
        return plant_ids;
    }
    
    public String setPlantID(String plant_ids) {
        return this.plant_ids = plant_ids;
    }
    public int getEnviroScore(){
    return enviro_score;
    }
    
    public int setEnviroScore(int enviro_score) {
        return this.enviro_score = enviro_score;
    }
    public int getBioScore(){
    return biomass_score;
    }
    
    public int setBioScore(int biomass_score) {
        return this.biomass_score = biomass_score;
    }
    public int getTime(){
    return time;
    }
    
    public int setTime(int time) {
        return this.time = time;
    }
    public int getCredits(){
    return credits;
    }
    
    public int setCredits(int credits) {
        return this.credits = credits;
    }
    
    public int getMinSpecies() {
        return min_species_num;
    }
    
    public void setMinSpecies(int num) {
        this.min_species_num = num;
    }
    
}
