using UnityEngine;

using System;

public class RequestChart : NetworkRequest {

	public RequestChart() {
		packet = new GamePacket(request_id = Constants.CMSG_CHART);
	}
	
	public void Send(short type) {
		packet.addShort16(type);
	}
}