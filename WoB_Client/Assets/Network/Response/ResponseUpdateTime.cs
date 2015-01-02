using UnityEngine;

using System;

public class ResponseUpdateTimeEventArgs : ExtendedEventArgs {
	public short month { get; set; }
	public int duration { get; set; }
	public int current { get; set; }
	public float rate { get; set; }
	
	public ResponseUpdateTimeEventArgs() {
		event_id = Constants.SMSG_UPDATE_TIME;
	}
}

public class ResponseUpdateTime : NetworkResponse {
	
	private short month;
	private int duration;
	private int current;
	private float rate;
	
	public ResponseUpdateTime() {
	}
	
	public override void parse() {
		month = DataReader.ReadShort(dataStream);
		duration = DataReader.ReadInt(dataStream);
		current = DataReader.ReadInt(dataStream);
		rate = DataReader.ReadFloat(dataStream);
	}
	
	public override ExtendedEventArgs process() {
		ResponseUpdateTimeEventArgs args = new ResponseUpdateTimeEventArgs();
		args.month = month;
		args.duration = duration;
		args.current = current;
		args.rate = rate;
		
		return args;
	}
}