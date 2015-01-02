using UnityEngine;

using System;

public class RequestTutorial : NetworkRequest {

	public RequestTutorial() {
		packet = new GamePacket(request_id = Constants.CMSG_TUTORIAL_DATA);
	}
	
	public void Send(int user_id) {
		packet.addInt32(user_id);
		//packet.addInt32(cur_tut);
		//packet.addInt32(milestone);
	}
}
