using UnityEngine;

using System;

public class ResponseUpdateEnvironmentScoreEventArgs : ExtendedEventArgs {
	public int env_id { get; set; }
	public int score { get; set; }
	
	public ResponseUpdateEnvironmentScoreEventArgs() {
		event_id = Constants.SMSG_UPDATE_ENV_SCORE;
	}
}

public class ResponseUpdateEnvironmentScore : NetworkResponse {
	
	private int env_id;
	private int score;
	
	public ResponseUpdateEnvironmentScore() {
	}
	
	public override void parse() {
		env_id = DataReader.ReadInt(dataStream);
		score = DataReader.ReadInt(dataStream);
	}
	
	public override ExtendedEventArgs process() {
		ResponseUpdateEnvironmentScoreEventArgs args = new ResponseUpdateEnvironmentScoreEventArgs();
		args.env_id = env_id;
		args.score = score;
		
		return args;
	}
}