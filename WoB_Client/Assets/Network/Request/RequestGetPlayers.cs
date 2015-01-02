using UnityEngine;

using System;

public class RequestGetPlayers : NetworkRequest {

	public RequestGetPlayers() {
		packet = new GamePacket(request_id = Constants.CMSG_GET_PLAYERS);
	}

	public void Send() {
	}
}