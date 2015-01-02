//Written by Kenneth Robertson
using UnityEngine;

using System;

public class ResponseChallengeShopSpeciesEventArgs : ExtendedEventArgs {
	public int[] speciesList { get; set; }
	
	public ResponseChallengeShopSpeciesEventArgs() {
		event_id = Constants.SMSG_TUTORIAL_CHALLENGE_SPECIES;
	}
}

public class ResponseChallengeShopSpecies : NetworkResponse {
	
	private int[] speciesList;
	
	public ResponseChallengeShopSpecies() {
	}
	
	public override void parse() {
		
		short size = DataReader.ReadShort(dataStream);
		speciesList = new int[size];
		
		for (int i = 0; i < size; i++) {
			speciesList[i] = DataReader.ReadInt(dataStream);
		}
	}
	
	public override ExtendedEventArgs process() {
		ResponseChallengeShopSpeciesEventArgs args = null;
		
		args = new ResponseChallengeShopSpeciesEventArgs();
		args.speciesList = speciesList;
		
		return args;
	}
}