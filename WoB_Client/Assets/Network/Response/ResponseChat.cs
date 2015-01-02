using UnityEngine;

using System;

public class ResponseChatEventArgs : ExtendedEventArgs {
	public short status { get; set; }
	public short type { get; set; }
	public string username { get; set; }
	public string message { get; set; }
	
	public ResponseChatEventArgs() {
		event_id = Constants.SMSG_CHAT;
	}
}

public class ResponseChat : NetworkResponse {
	
	private short status;
	private short type;
	private string username;
	private string message;

	public ResponseChat() {
	}
	
	public override void parse() {
		status = DataReader.ReadShort(dataStream);
		
		if (status == 0) {
			type = DataReader.ReadShort(dataStream);
			
			if (type == 0) {
				username = DataReader.ReadString(dataStream);
			}
			
			message = DataReader.ReadString(dataStream);
		}
	}
	
	public override ExtendedEventArgs process() {
		ResponseChatEventArgs args = null;

		if (status == 0) {
			args = new ResponseChatEventArgs();
			args.status = status;
			args.type = type;
			args.username = username;
			args.message = message;
		}
		
		return args;
	}
}