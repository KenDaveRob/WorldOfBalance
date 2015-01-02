using UnityEngine;

using System;
using System.Collections.Generic;

public class RequestSpeciesAction : NetworkRequest {

	public RequestSpeciesAction() {
		packet = new GamePacket(request_id = Constants.CMSG_SPECIES_ACTION);
	}
	
	public void Send(short action, short type) {
		packet.addShort16(action);
		packet.addShort16(type);
	}

	public void Send(short action, Dictionary<int, int> speciesList) {
		packet.addShort16(action);
		packet.addShort16((short) speciesList.Count);

		foreach (KeyValuePair<int, int> entry in speciesList) {
			packet.addInt32(entry.Key);
			packet.addInt32(entry.Value);
		}
	}
}