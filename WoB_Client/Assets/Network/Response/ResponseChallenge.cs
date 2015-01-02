//Written by Kenneth Robertson
using UnityEngine;

using System;

public class ResponseChallengeEventArgs : ExtendedEventArgs {
	public int requiredBiomass { get; set; }
	public int creditsGiven { get; set; }
	public int requiredEnvironmentScore { get; set; }
	public int timeLimit { get; set; }
	public int minSpecies { get; set; }

	public ResponseChallengeEventArgs() {
		event_id = Constants.SMSG_TUTORIAL_CHALLENGE;
	}
}

public class ResponseChallenge : NetworkResponse {
	private int requiredBiomass;
	private int creditsGiven;
	private int requiredEnvironmentScore;
	private int timeLimit;
	private int minSpecies;

	public ResponseChallenge() {
	}
	
	public override void parse() {
		requiredBiomass = DataReader.ReadInt(dataStream);
		creditsGiven = DataReader.ReadInt(dataStream);
		requiredEnvironmentScore = DataReader.ReadInt(dataStream);
		timeLimit = DataReader.ReadInt(dataStream);
		minSpecies = DataReader.ReadInt(dataStream);
	}
	
	public override ExtendedEventArgs process() {
		ResponseChallengeEventArgs args = null;
		
		args = new ResponseChallengeEventArgs();
		args.requiredBiomass = requiredBiomass;
		args.creditsGiven = creditsGiven;
		args.requiredEnvironmentScore = requiredEnvironmentScore;
		args.timeLimit = timeLimit;
		args.minSpecies = minSpecies;
		
		return args;
	}
}

