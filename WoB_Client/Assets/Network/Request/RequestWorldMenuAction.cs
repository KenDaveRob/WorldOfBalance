using UnityEngine;

using System;

public class RequestWorldMenuAction : NetworkRequest {

	public RequestWorldMenuAction() {
		packet = new GamePacket(request_id = Constants.CMSG_WORLD_MENU_ACTION);
	}

	public void CreateAction(short game_mode, string name, short max_players, string env_type, short access_type, string password) {
		packet.addShort16(1);
		packet.addShort16(game_mode);
		packet.addString(name);
		packet.addShort16(max_players);
		packet.addString(env_type);
		packet.addShort16(access_type);
		
		if (access_type == 1) {
			packet.addString(password);
		}
	}

	public void JoinAction(int world_id) {
		packet.addShort16(2);
		packet.addInt32(world_id);
	}

	public void DeleteAction(int world_id) {
		packet.addShort16(3);
		packet.addInt32(world_id);
	}
}