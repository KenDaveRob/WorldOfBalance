using UnityEngine;

using System;
using System.Collections.Generic;

public class ResponsePredictionEventArgs : ExtendedEventArgs {
	public short status { get; set; }
	public Dictionary<int, int> results { get; set; }
	
	public ResponsePredictionEventArgs() {
		event_id = Constants.SMSG_PREDICTION;
	}
}

public class ResponsePrediction : NetworkResponse {
	
	private short status;
	private Dictionary<int, int> results;

	public ResponsePrediction() {
		results = new Dictionary<int, int>();
	}
	
	public override void parse() {
		status = DataReader.ReadShort(dataStream);
		
		if (status == 0) {
			short size = DataReader.ReadShort(dataStream);
			
			for (int i = 0; i < size; i++) {
				int species_id = DataReader.ReadInt(dataStream);
				int change = DataReader.ReadInt(dataStream);
				
				results.Add(species_id, change);
			}
		}
	}

	public override ExtendedEventArgs process() {
		ResponsePredictionEventArgs args = new ResponsePredictionEventArgs();
		args.status = status;

		if (status == 0) {
			args.results = results;
		}
		
		return args;
	}
}