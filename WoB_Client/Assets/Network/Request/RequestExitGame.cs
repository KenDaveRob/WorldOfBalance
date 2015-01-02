using UnityEngine;

using System;

public class RequestExitGame : NetworkRequest {

	public RequestExitGame() {
		packet = new GamePacket(request_id = Constants.CMSG_SAVE_EXIT_GAME);
	}
	
	public void Send(short type) {
		packet.addShort16(type);
	}
}