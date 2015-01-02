using UnityEngine;

using System;
using System.Collections.Generic;

public class ResponseSpeciesActionEventArgs : ExtendedEventArgs {
	public short action { get; set; }
	public short status { get; set; }
	public short type { get; set; }
	public string selectionList { get; set; }
	
	public ResponseSpeciesActionEventArgs() {
		event_id = Constants.SMSG_SPECIES_ACTION;
	}
}

public class ResponseSpeciesAction : NetworkResponse {
	
	private short action;
	
	public ResponseSpeciesAction() {
	}
	
	public override void parse() {
	}
	
	public override ExtendedEventArgs process() {
		ResponseSpeciesActionEventArgs args = new ResponseSpeciesActionEventArgs();
		args.action = DataReader.ReadShort(dataStream);
		args.status = DataReader.ReadShort(dataStream);

		if (args.action == 0) {
			args.type = DataReader.ReadShort(dataStream);
			args.selectionList = DataReader.ReadString(dataStream);
		}

		return args;
	}
}