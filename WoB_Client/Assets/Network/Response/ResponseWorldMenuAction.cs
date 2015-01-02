using UnityEngine;

using System;

public class ResponseWorldMenuActionEventArgs : ExtendedEventArgs {
	public short action { get; set; }
	public short status { get; set; }
	public int world_id { get; set; }
	public string name { get; set; }
	public short game_mode { get; set; }
	public int credits { get; set; }
	public string env_type { get; set; }
	public short max_players { get; set; }
	public float time_rate { get; set; }
	public short month { get; set; }
	
	public ResponseWorldMenuActionEventArgs() {
		event_id = Constants.SMSG_WORLD_MENU_ACTION;
	}
}

public class ResponseWorldMenuAction : NetworkResponse {
	
	private short action;
	private short status;
	private int world_id;
	private string name;
	private short game_mode;
	private int credits;
	private string env_type;
	private short max_players;
	private float time_rate;
	private short month;

	public ResponseWorldMenuAction() {
	}
	
	public override void parse() {
		action = DataReader.ReadShort(dataStream);
		
		switch (action) {
			case 1:
				status = DataReader.ReadShort(dataStream);

				if (status == 0) {
					world_id = DataReader.ReadInt(dataStream);
					name = DataReader.ReadString(dataStream);
					game_mode = DataReader.ReadShort(dataStream);
					credits = DataReader.ReadInt(dataStream);
					env_type = DataReader.ReadString(dataStream);
					max_players = DataReader.ReadShort(dataStream);
					time_rate = DataReader.ReadFloat(dataStream);
				}
				break;
			case 2:
				status = DataReader.ReadShort(dataStream);

				if (status == 0) {
					world_id = DataReader.ReadInt(dataStream);
					name = DataReader.ReadString(dataStream);
					month = DataReader.ReadShort(dataStream);
					game_mode = DataReader.ReadShort(dataStream);
					credits = DataReader.ReadInt(dataStream);
					env_type = DataReader.ReadString(dataStream);
					max_players = DataReader.ReadShort(dataStream);
				}
				break;
			case 3:
				status = DataReader.ReadShort(dataStream);
				world_id = DataReader.ReadInt(dataStream);
				break;
			default:
				break;
		}
	}

	public override ExtendedEventArgs process() {
		ResponseWorldMenuActionEventArgs args = null;

		if (status == 0) {
			args = new ResponseWorldMenuActionEventArgs();
			args.action = action;
			args.status = status;
			args.world_id = world_id;
			args.name = name;
			args.game_mode = game_mode;
			args.credits = credits;
			args.env_type = env_type;
			args.max_players = max_players;
			args.time_rate = time_rate;
			args.month = month;
		}

		return args;
	}
}