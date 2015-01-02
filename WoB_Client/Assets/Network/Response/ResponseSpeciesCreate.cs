using UnityEngine;

using System;

public class ResponseSpeciesCreateEventArgs : ExtendedEventArgs {
	public short status { get; set; }
	public int zone_id { get; set; }
	public int group_id { get; set; }
	public int species_id { get; set; }
	public string name { get; set; }
	public int model_id { get; set; }
	public int biomass { get; set; }
	public float x { get; set; }
	public float y { get; set; }
	public float z { get; set; }
	public int user_id { get; set; }
	
	public ResponseSpeciesCreateEventArgs() {
		event_id = Constants.SMSG_SPECIES_CREATE;
	}
}

public class ResponseSpeciesCreate : NetworkResponse {
	
	private short status;
	private int zone_id;
	private int group_id;
	private int species_id;
	private string name;
	private int model_id;
	private int biomass;
	private float x;
	private float y;
	private float z;
	private int user_id;

	public ResponseSpeciesCreate() {
	}
	
	public override void parse() {
		status = DataReader.ReadShort(dataStream);
		
		if (status == 0) {
			zone_id = DataReader.ReadInt(dataStream);
			group_id = DataReader.ReadInt(dataStream);
			species_id = DataReader.ReadInt(dataStream);
			name = DataReader.ReadString(dataStream);
			model_id = DataReader.ReadInt(dataStream);
			biomass = DataReader.ReadInt(dataStream);
			x = DataReader.ReadFloat(dataStream);
			y = DataReader.ReadFloat(dataStream);
			z = DataReader.ReadFloat(dataStream);
			user_id = DataReader.ReadInt(dataStream);
		}
	}

	public override ExtendedEventArgs process() {
		ResponseSpeciesCreateEventArgs args = null;

		if (status == 0) {
			args = new ResponseSpeciesCreateEventArgs();
			args.status = status;
			args.zone_id = zone_id;
			args.group_id = group_id;
			args.species_id = species_id;
			args.name = name;
			args.model_id = model_id;
			args.biomass = biomass;
			args.x = x;
			args.y = y;
			args.z = z;
			args.user_id = user_id;
		}
		
		return args;
	}
}