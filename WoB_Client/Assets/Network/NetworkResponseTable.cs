using UnityEngine;

using System;
using System.Collections.Generic;
using System.Reflection;

public class NetworkResponseTable {

	public static Dictionary<short, Type> responseTable { get; set; }
	
	public static void init() {
		responseTable = new Dictionary<short, Type>();
		
		add(Constants.SMSG_AUTH, "ResponseLogin");
		add(Constants.SMSG_REGISTER, "ResponseRegister");
		add(Constants.SMSG_CHAT, "ResponseChat");
		add(Constants.SMSG_SHOP, "ResponseShop");
		add(Constants.SMSG_SPECIES_LIST, "ResponseSpeciesList");
		add(Constants.SMSG_WORLD_LIST, "ResponseWorldList");
		add(Constants.SMSG_SPECIES_CREATE, "ResponseSpeciesCreate");
		add(Constants.SMSG_WORLD_MENU_ACTION, "ResponseWorldMenuAction");
		add(Constants.SMSG_AVATAR_LIST, "ResponseAvatarList");
		add(Constants.SMSG_CREATE_ENV, "ResponseCreateEnv");
		add(Constants.SMSG_SHOP_ACTION, "ResponseShopAction");
		add(Constants.SMSG_UPDATE_RESOURCES, "ResponseUpdateResources");
		add(Constants.SMSG_SPECIES_ACTION, "ResponseSpeciesAction");
		add(Constants.SMSG_PREDICTION, "ResponsePrediction");
		add(Constants.SMSG_READY, "ResponseReady");
		add(Constants.SMSG_UPDATE_TIME, "ResponseUpdateTime");
		add(Constants.SMSG_UPDATE_ENV_SCORE, "ResponseUpdateEnvironmentScore");
		add(Constants.SMSG_TUTORIAL_CHALLENGE_SPECIES, "ResponseChallengeShopSpecies");
		add(Constants.SMSG_TUTORIAL_CHALLENGE, "ResponseChallenge");
		add(Constants.SMSG_TUTORIAL_REMOVE_SPECIES, "ResponseTutorialRemoveSpecies");
		add(Constants.SMSG_TUTORIAL_DATA, "ResponseTutorial");
		add(Constants.SMSG_TUTORIAL_UPDATE_CUR_TUT, "ResponseUpdateCurTut");
	}
	
	public static void add(short response_id, string name) {
		responseTable.Add(response_id, Type.GetType(name));
	}
	
	public static NetworkResponse get(short response_id) {
		NetworkResponse response = null;
		
		if (responseTable.ContainsKey(response_id)) {
			response = (NetworkResponse) Activator.CreateInstance(responseTable[response_id]);
			response.response_id = response_id;
		} else {
			Debug.Log("Response [" + response_id + "] Not Found");
		}
		
		return response;
	}
}
