using UnityEngine;

using System;

public class RequestHeartbeat : NetworkRequest {

	public RequestHeartbeat() {
		packet = new GamePacket(request_id = Constants.CMSG_HEARTBEAT);
	}
}