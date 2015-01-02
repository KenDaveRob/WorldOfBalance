using UnityEngine;

using System;

public class RequestPrediction : NetworkRequest {

	public RequestPrediction() {
		packet = new GamePacket(request_id = Constants.CMSG_PREDICTION);
	}
}