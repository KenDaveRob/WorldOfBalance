using UnityEngine;

using System;
using System.Collections.Generic;

public class RequestShopAction : NetworkRequest {

	public RequestShopAction() {
		packet = new GamePacket(request_id = Constants.CMSG_SHOP_ACTION);
	}
	
	public void Send(short type, Dictionary<int, int> cartList) {
		packet.addShort16(type);
		packet.addShort16((short) cartList.Count);
		
		foreach (KeyValuePair<int, int> entry in cartList) {
			packet.addInt32(entry.Key);
			packet.addInt32(entry.Value);
		}
	}
}