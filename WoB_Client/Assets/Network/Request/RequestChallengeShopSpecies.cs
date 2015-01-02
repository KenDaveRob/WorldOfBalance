//Written By Kenneth Robertson
using UnityEngine;

using System;

public class RequestChallengeShopSpecies : NetworkRequest {
	
	public RequestChallengeShopSpecies() {
		packet = new GamePacket(request_id = Constants.CMSG_TUTORIAL_CHALLENGE_SPECIES);
	}
	
	public void Send(short challengeIndex) {
		packet.addShort16(challengeIndex);
	}
}


