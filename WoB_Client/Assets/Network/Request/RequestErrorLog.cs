using UnityEngine;

using System;

public class RequestErrorLog : NetworkRequest {

	public RequestErrorLog() {
		packet = new GamePacket(request_id = Constants.CMSG_ERROR_LOG);
	}

	public void Send(string message) {
		packet.addString(message);
	}
}