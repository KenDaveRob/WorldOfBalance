using UnityEngine;

using System;

public class RequestRegister : NetworkRequest {

	public RequestRegister() {
		packet = new GamePacket(request_id = Constants.CMSG_REGISTER);
	}
	
	public void Send(string fname, string lname, string email, string password, string name) {
		packet.addString(fname);
		packet.addString(lname);
		packet.addString(email);
		packet.addString(password);		
		packet.addString(name);
	}
}