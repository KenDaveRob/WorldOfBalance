using UnityEngine;

using System;
using System.Collections.Generic;
using System.Reflection;

public class NetworkRequestTable {

	public static Dictionary<short, Type> requestTable { get; set; }
	
	public static void init() {
		requestTable = new Dictionary<short, Type>();
		
		add(Constants.CMSG_AUTH, "RequestLogin");
		add(Constants.CMSG_REGISTER, "RequestRegister");
		add(Constants.CMSG_CHAT, "RequestChat");
		add(Constants.CMSG_SHOP, "RequestShop");
		add(Constants.CMSG_SPECIES_LIST, "RequestSpeciesList");
		add(Constants.CMSG_HEARTBEAT, "RequestHeartbeat");
		add(Constants.CMSG_WORLD_LIST, "RequestWorldList");
		add(Constants.CMSG_PREDICTION, "RequestPrediction");
		add(Constants.CMSG_TUTORIAL_CHALLENGE_SPECIES, "RequestChallengeShopSpecies");
		add(Constants.CMSG_TUTORIAL_CHALLENGE, "RequestChallenge");
		add(Constants.CMSG_TUTORIAL_REMOVE_SPECIES, "RequestTutorialRemoveSpecies");
		add(Constants.CMSG_TUTORIAL_DATA, "RequestTutorial");
		add(Constants.CMSG_TUTORIAL_UPDATE_CUR_TUT, "RequestUpdateCurTut");
	}
	
	public static void add(short request_id, string name) {
		requestTable.Add(request_id, Type.GetType(name));
	}
	
	public static NetworkRequest get(short request_id) {
		NetworkRequest request = null;
		
		if (requestTable.ContainsKey(request_id)) {
			request = (NetworkRequest) Activator.CreateInstance(requestTable[request_id]);
			request.request_id = request_id;
		} else {
			Debug.Log("Request [" + request_id + "] Not Found");
		}
		
		return request;
	}
}
