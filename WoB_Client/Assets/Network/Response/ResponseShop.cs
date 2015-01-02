using UnityEngine;

using System;
using System.Collections.Generic;

public class ResponseShopEventArgs : ExtendedEventArgs {
	public short action { get; set; }
	public short status { get; set; }
	public short type { get; set; }
	public string[] config { get; set; }
	public int[] speciesList { get; set; }
	
	public ResponseShopEventArgs() {
		event_id = Constants.SMSG_SHOP;
	}
}

public class ResponseShop : NetworkResponse {

	private string[] config;
	private int[] speciesList;

	public ResponseShop() {
	}
	
	public override void parse() {
		short action = DataReader.ReadShort(dataStream);
		short status = DataReader.ReadShort(dataStream);
		short type = DataReader.ReadShort(dataStream);
		
		short size = DataReader.ReadShort(dataStream);
		config = new string[size];
		
		for (int i = 0; i < size; i++) {
			config[i] = DataReader.ReadString(dataStream);
		}
		
		string species = DataReader.ReadString(dataStream);
		speciesList = Array.ConvertAll(species.Split(','), new Converter<string, int>(int.Parse));
	}
	
	public override ExtendedEventArgs process() {
		ResponseShopEventArgs args = null;

		args = new ResponseShopEventArgs();
		args.config = config;
		args.speciesList = speciesList;

		return args;
	}
}