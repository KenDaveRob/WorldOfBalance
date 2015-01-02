using UnityEngine;

using System;
using System.Collections.Generic;

public class ResponseAvatarListEventArgs : ExtendedEventArgs {
	public short status { get; set; }
	public Dictionary<int, AvatarData> avatarList { get; set; }
	
	public ResponseAvatarListEventArgs() {
		event_id = Constants.SMSG_AVATAR_LIST;
	}
}

public class ResponseAvatarList : NetworkResponse {
	
	private short status;
	private Dictionary<int, AvatarData> avatarList = new Dictionary<int, AvatarData>();

	public ResponseAvatarList() {
	}
	
	public override void parse() {
		status = DataReader.ReadShort(dataStream);
		
		if (status == 0) {
			short size = DataReader.ReadShort(dataStream);
			
			for (int i = 0; i < size; i++) {
				int avatar_id = DataReader.ReadInt(dataStream);

				AvatarData avatar = new AvatarData(avatar_id);
				avatar.name = DataReader.ReadString(dataStream);
				avatar.level = DataReader.ReadShort(dataStream);
				avatar.coins = DataReader.ReadInt(dataStream);
				avatar.last_played = DataReader.ReadString(dataStream);
				
				avatarList.Add(avatar_id, avatar);
			}
		}
	}
	
	public override ExtendedEventArgs process() {
		ResponseAvatarListEventArgs args = null;

		if (status == 0) {
			args = new ResponseAvatarListEventArgs();
			args.status = status;
			args.avatarList = avatarList;
		}
		
		return args;
	}
}