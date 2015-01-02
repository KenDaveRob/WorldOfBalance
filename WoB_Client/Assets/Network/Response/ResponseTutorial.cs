using UnityEngine;

using System;

public class ResponseTutorialEventArgs : ExtendedEventArgs {
	public int currentTutorial { get; set; }
	
	public ResponseTutorialEventArgs() {
		event_id = Constants.SMSG_TUTORIAL_DATA;
	}
}

public class ResponseTutorial : NetworkResponse {
	
	private int currentTutorial;
	
	public ResponseTutorial() {
	}
	
	public override void parse() {
		currentTutorial = DataReader.ReadInt(dataStream);
	}
	
	public override ExtendedEventArgs process() {
		ResponseTutorialEventArgs args = null;
		
		args = new ResponseTutorialEventArgs();
		args.currentTutorial = currentTutorial;
		
		return args;
	}
}