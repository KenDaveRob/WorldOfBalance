using UnityEngine;

using System;

public class ResponseUpdateResourcesEventArgs : ExtendedEventArgs {
	public short type { get; set; }
	public int amount { get; set; }
	public int target { get; set; }
	
	public ResponseUpdateResourcesEventArgs() {
		event_id = Constants.SMSG_UPDATE_RESOURCES;
	}
}

public class ResponseUpdateResources : NetworkResponse {
	
	private short type;
	private int amount;
	private int target;

	public ResponseUpdateResources() {
	}
	
	public override void parse() {
		type = DataReader.ReadShort(dataStream);
		amount = DataReader.ReadInt(dataStream);
		target = DataReader.ReadInt(dataStream);
	}
	
	public override ExtendedEventArgs process() {
		ResponseUpdateResourcesEventArgs args = null;
		
		args = new ResponseUpdateResourcesEventArgs();
		args.type = type;
		args.amount = amount;
		args.target = target;
		
		return args;
	}
}