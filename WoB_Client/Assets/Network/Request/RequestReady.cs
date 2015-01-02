using UnityEngine;

using System;

public class RequestReady : NetworkRequest {
	
	public RequestReady() {
		packet = new GamePacket(request_id = Constants.CMSG_READY);
	}

	public void Send(bool status) {
		packet.addBool(status);
	}
}