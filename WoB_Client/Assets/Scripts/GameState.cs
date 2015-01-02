using UnityEngine;

using System.Collections;
using System.Collections.Generic;

public class GameState : MonoBehaviour {

	public static AvatarData avatar;
	public static WorldData world;
	private int month;
	public Dictionary<int, Species> speciesList { get; set; }
	
	// Use this for initialization
	void Start () {
		speciesList = new Dictionary<int, Species>();

		GameObject.Find("MainObject").GetComponent<MessageQueue>().AddCallback(Constants.SMSG_CREATE_ENV, ResponseCreateEnv);
		GameObject.Find("MainObject").GetComponent<MessageQueue>().AddCallback(Constants.SMSG_SPECIES_CREATE, ResponseSpeciesCreate);
	}
	
	// Update is called once per frame
	void Update () {

	}

	public int getSpeciesCount()
	{
		return speciesList.Count;
	}

	public List<int> getSpeciesIDArray()
	{
		return new List<int>( speciesList.Keys );
	}
	
	public Species GetSpeciesGroup(int group_id) {
		return speciesList.ContainsKey(group_id) ? speciesList[group_id] : null;
	}

	public void CreateSpecies(int species_id, string type, string organism_type, int biomass) {
		if (speciesList.ContainsKey(species_id)) {
			UpdateSpecies(species_id, biomass);
		} else {
			Species species = gameObject.AddComponent<Species>();
			species.species_id = species_id;
			species.name = type;
			species.organism_type = organism_type;
			species.biomass = biomass;
	
			speciesList.Add(species_id, species);
		}
	}
	
	public void UpdateSpecies(int species_id, int size) {
		Species species = speciesList[species_id];
		species.UpdateSize(size);
	}
	
	public void ResponseCreateEnv(ExtendedEventArgs eventArgs) {
		ResponseCreateEnvEventArgs args = eventArgs as ResponseCreateEnvEventArgs;

		GetComponent<EnvironmentScore>().SetScore(args.score);
	}
	
	public void ResponseSpeciesCreate(ExtendedEventArgs eventArgs) {
		ResponseSpeciesCreateEventArgs args = eventArgs as ResponseSpeciesCreateEventArgs;
		
		if (args.species_id < 1000) {
			CreateSpecies(args.species_id, SpeciesTable.speciesList[args.species_id].name, "Animal", 500);
		} else {
			CreateSpecies(args.species_id, SpeciesTable.speciesList[args.species_id].name, "Plant", 500);
		}

		UpdateSpecies(args.species_id, args.biomass * 1);
	}

	public void resetSpecies() {
		Species[] species = FindObjectsOfType(typeof(Species)) as Species[];

		foreach (Species specie in species) {
			specie.resetSpecies();
		}

		foreach (Species specie in species) {
			specie.removeSpecie();
		}

		speciesList = new Dictionary<int, Species>();
	}

	public int getTotalBiomass() 
	{
		Species[] species = FindObjectsOfType(typeof(Species)) as Species[];
		int totalBiomass = 0;
		/*
		foreach(Species specie in species)
		{
			totalBiomass += specie.biomass;
		}
		*/

		foreach(Species specie in species)
		{
			totalBiomass += specie.size;
		}

		return totalBiomass;
	}
}
