using UnityEngine;

using System;

public class RequestSpeciesList : NetworkRequest {

	public RequestSpeciesList() {
		packet = new GamePacket(request_id = Constants.CMSG_SPECIES_LIST);
	}
}