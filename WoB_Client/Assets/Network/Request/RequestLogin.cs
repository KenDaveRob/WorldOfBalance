using UnityEngine;

using System;

public class RequestLogin : NetworkRequest {

	public RequestLogin() {
		packet = new GamePacket(request_id = Constants.CMSG_AUTH);
	}
	
	public void Send(string username, string password) {
		packet.addString(Constants.CLIENT_VERSION);
		packet.addString(username);
		packet.addString(password);		
	}
}