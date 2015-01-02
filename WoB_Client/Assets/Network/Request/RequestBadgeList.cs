using UnityEngine;

using System;

public class RequestBadgeList : NetworkRequest {

	public RequestBadgeList() {
		packet = new GamePacket(request_id = Constants.CMSG_BADGE_LIST);
	}

	public void Send(int user_id) {
		packet.addInt32(user_id);
	}
}
