using UnityEngine;

using System;
using System.Collections.Generic;

public class ResponseSpeciesListEventArgs : ExtendedEventArgs {
	public Dictionary<int, SpeciesData> speciesList { get; set; }
	
	public ResponseSpeciesListEventArgs() {
		event_id = Constants.SMSG_SPECIES_LIST;
	}
}

public class ResponseSpeciesList : NetworkResponse {
	
	private Dictionary<int, SpeciesData> speciesList;

	public ResponseSpeciesList() {
	}
	
	public override void parse() {
		speciesList = new Dictionary<int, SpeciesData>();
		
		int size = DataReader.ReadShort(dataStream);
		for (int i = 0; i < size; i++) {
			SpeciesData species = new SpeciesData(DataReader.ReadInt(dataStream));
			species.name = DataReader.ReadString(dataStream);
			species.description = DataReader.ReadString(dataStream);
			species.cost = DataReader.ReadInt(dataStream);
			
			short numArgs = DataReader.ReadShort(dataStream);
			string[] extraArgs = new string[numArgs];
	
			for (int j = 0; j < numArgs; j++) {
				string arg = DataReader.ReadString(dataStream);
				extraArgs[j] = arg;
			}

			species.biomass = int.Parse(extraArgs[0]);
			species.diet_type = short.Parse(extraArgs[1]);
			species.trophic_level = float.Parse(extraArgs[2]);

			Dictionary<int, string> predatorList = species.predatorList;
			foreach (string predator_id in extraArgs[3].Split(new string[]{","}, StringSplitOptions.None)) {
				if (predator_id != "") {
					predatorList.Add(int.Parse(predator_id), "");
				}
			}

			Dictionary<int, string> preyList = species.preyList;
			foreach (string prey_id in extraArgs[4].Split(new string[]{","}, StringSplitOptions.None)) {
				if (prey_id != "") {
					preyList.Add(int.Parse(prey_id), "");
				}
			}

			species.categoryList = DataReader.ReadString(dataStream).Split(new string[]{", "}, StringSplitOptions.None);
			
			speciesList.Add(species.species_id, species);
		}
	}
	
	public override ExtendedEventArgs process() {
		ResponseSpeciesListEventArgs args = null;

		args = new ResponseSpeciesListEventArgs();
		args.speciesList = speciesList;

		return args;
	}
}