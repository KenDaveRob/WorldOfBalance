using UnityEngine;

using System;

public class ResponseCreateEnvEventArgs : ExtendedEventArgs {
	public int env_id { get; set; }
	public int user_id { get; set; }
	public int score { get; set; }
	
	public ResponseCreateEnvEventArgs() {
		event_id = Constants.SMSG_CREATE_ENV;
	}
}

public class ResponseCreateEnv : NetworkResponse {
	
	private int env_id;
	private int user_id;
	private int score;

	public ResponseCreateEnv() {
	}
	
	public override void parse() {
		env_id = DataReader.ReadInt(dataStream);
		user_id = DataReader.ReadInt(dataStream);
		score = DataReader.ReadInt(dataStream);
	}

	public override ExtendedEventArgs process() {
		ResponseCreateEnvEventArgs args = new ResponseCreateEnvEventArgs();
		args.env_id = env_id;
		args.user_id = user_id;
		args.score = score;

		return args;
	}
}