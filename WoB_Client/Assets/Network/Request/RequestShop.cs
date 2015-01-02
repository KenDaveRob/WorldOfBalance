using UnityEngine;

using System;

public class RequestShop : NetworkRequest {

	public RequestShop() {
		packet = new GamePacket(request_id = Constants.CMSG_SHOP);
	}
	
	public void Send(short type) {
		packet.addShort16(0);
		packet.addShort16(type);
	}
}