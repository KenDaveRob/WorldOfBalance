//Written by Kenneth Robertson
using UnityEngine;

using System;

public class RequestChallenge : NetworkRequest {
	
	public RequestChallenge() {
		packet = new GamePacket(request_id = Constants.CMSG_TUTORIAL_CHALLENGE);
	}
	
	public void Send(short challengeIndex) {
		packet.addShort16(challengeIndex);
	}
}

