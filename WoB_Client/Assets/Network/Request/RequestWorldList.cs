using UnityEngine;

using System;

public class RequestWorldList : NetworkRequest {

	public RequestWorldList() {
		packet = new GamePacket(request_id = Constants.CMSG_WORLD_LIST);
	}
	
	public void Send(int user_id) {
		packet.addInt32(user_id);
	}
}