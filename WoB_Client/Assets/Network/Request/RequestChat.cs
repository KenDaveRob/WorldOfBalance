using UnityEngine;

using System;

public class RequestChat : NetworkRequest {

	public RequestChat() {
		packet = new GamePacket(request_id = Constants.CMSG_CHAT);
	}

	public void Send(short type, string message) {
		packet.addShort16(type);
		packet.addString(message);
	}
}