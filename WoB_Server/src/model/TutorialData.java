/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;
//imports
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.TimerTask;
import java.util.ArrayList;

import core.GameClient;
import dataAccessLayer.PlayerDAO;
import dataAccessLayer.UserLogDAO;
import dataAccessLayer.TutorialDAO;
import metadata.Constants;
import utility.GameTimer;

import model.AnimalType;
/**
 *
 * @author Robin Pennock
 */
public class TutorialData {
    
    private int player_id;
    private int cur_tut;
    private int milestone;
    private int cur_challenge;
    private int chal_milestone;
    private ArrayList<String[]> content;
    
    public TutorialData(){}//allows for null object
    
    public TutorialData(int player_id, int cur_tut, int milestone, 
            int cur_challenge, int chal_milestone) {
        this.player_id = player_id;
        this.cur_tut = cur_tut;    
        this.milestone = milestone;
        this.cur_challenge=cur_challenge;
        this.chal_milestone=chal_milestone;
    }
    
    public int getPlayerID(){
        return player_id;
    }
    
    public int setPlayerID(int player_id) {
        return this.player_id = player_id;
    }
    public int getMilestone(){
        return milestone;
    }
    public int setMilestone(int milestone){
        return this.milestone = milestone;
    }
    public int getCurTut(){
        return cur_tut;
    }
    public int setCutTut(int cur_tut){
        return this.cur_tut = cur_tut;
    }
    public int getCurChallenge(){
        return cur_challenge;
    }
    public int setCutChallenge(int cur_challenge){
        return this.cur_challenge = cur_challenge;
    }
    public int getChalMilestone(){
        return chal_milestone;
    }
    public int setChalMilestone(int chal_milestone){
        return this.chal_milestone = chal_milestone;
    }
    public ArrayList<String[]> getContent(){
        return this.content;
    }
    
}
