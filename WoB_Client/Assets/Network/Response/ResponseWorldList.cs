using UnityEngine;

using System;
using System.Collections.Generic;

public class ResponseWorldListEventArgs : ExtendedEventArgs {
	public short status { get; set; }
	public Dictionary<int, WorldData> worldList { get; set; }
	
	public ResponseWorldListEventArgs() {
		event_id = Constants.SMSG_WORLD_LIST;
	}
}

public class ResponseWorldList : NetworkResponse {
	
	private short status;
	private Dictionary<int, WorldData> worldList = new Dictionary<int, WorldData>();

	public ResponseWorldList() {
	}
	
	public override void parse() {
		status = DataReader.ReadShort(dataStream);
		
		if (status == 0) {
			short size = DataReader.ReadShort(dataStream);

			for (int i = 0; i < size; i++) {
				int world_id = DataReader.ReadInt(dataStream);
				
				WorldData world = new WorldData(world_id);
				world.name = DataReader.ReadString(dataStream);
				world.type = DataReader.ReadShort(dataStream);
				world.credits = DataReader.ReadInt(dataStream);
				
				world.isNew = DataReader.ReadBool(dataStream);
				
				if (!world.isNew) {
					world.year = DataReader.ReadShort(dataStream);
					world.month = DataReader.ReadShort(dataStream);
					world.play_time = DataReader.ReadInt(dataStream);
					world.score = DataReader.ReadInt(dataStream);
				}
				
				worldList.Add(world_id, world);
			}
		}
	}
	
	public override ExtendedEventArgs process() {
		ResponseWorldListEventArgs args = null;

		if (status == 0) {
			args = new ResponseWorldListEventArgs();
			args.worldList = worldList;
		}

		return args;
	}
}