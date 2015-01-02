using UnityEngine;

using System;

public class ResponseReadyEventArgs : ExtendedEventArgs {
	public bool status { get; set; }
	public string username { get; set; }
	
	public ResponseReadyEventArgs() {
		event_id = Constants.SMSG_READY;
	}
}

public class ResponseReady : NetworkResponse {
	
	private bool status;
	private string username;
	
	public ResponseReady() {
	}
	
	public override void parse() {
		status = DataReader.ReadBool(dataStream);
		username = DataReader.ReadString(dataStream);
	}
	
	public override ExtendedEventArgs process() {
		ResponseReadyEventArgs args = new ResponseReadyEventArgs();
		args.status = status;
		args.username = username;
		
		return args;
	}
}