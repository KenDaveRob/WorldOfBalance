using UnityEngine;

using System;

public class RequestHighScore : NetworkRequest {

	public RequestHighScore() {
		packet = new GamePacket(request_id = Constants.CMSG_HIGH_SCORE);
	}

	public void Send(short type, string message) {
		packet.addShort16(type);
		packet.addString(message);
	}
}