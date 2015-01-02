using UnityEngine;

using System;

public class ResponseRegisterEventArgs : ExtendedEventArgs {
	public short status { get; set; }
	
	public ResponseRegisterEventArgs() {
		event_id = Constants.SMSG_REGISTER;
	}
}

public class ResponseRegister : NetworkResponse {
	
	private short status;

	public ResponseRegister() {
	}
	
	public override void parse() {
		status = DataReader.ReadShort(dataStream);
	}
	
	public override ExtendedEventArgs process() {
		ResponseRegisterEventArgs args = null;
		
		args = new ResponseRegisterEventArgs();
		args.status = status;
		
		return args;
	}
}