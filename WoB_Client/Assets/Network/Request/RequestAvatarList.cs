using UnityEngine;

using System;

public class RequestAvatarList : NetworkRequest {

	public RequestAvatarList() {
		packet = new GamePacket(request_id = Constants.CMSG_AVATAR_LIST);
	}
	
	public void Send(int user_id) {
		packet.addInt32(user_id);
	}
}