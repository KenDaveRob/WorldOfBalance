using UnityEngine;

using System.Collections;
using System.Collections.Generic;

public class Species : MonoBehaviour {

	public int species_id { get; set; }
	public string name { get; set; }
	public string organism_type { get; set; }
	public int biomass { get; set; }
	public int size { get; set; }
	public List<GameObject> speciesList = new List<GameObject>();
	
	// Use this for initialization
	void Start () {
		UpdateSize(biomass);
	}

	public void UpdateSize(int size) {
		this.size = size;

		int numChange = Mathf.RoundToInt(size / biomass) - speciesList.Count;

		if (numChange > 0) {
			for (int i = 0; i < numChange; i++) {
				if (organism_type.Equals("Plant")) {
//					createPlant();
					createAnimal();
				} else {
					createAnimal();
				}
			}
		} else if (numChange < 0) {
			for (int i = 0; i > numChange; i--) {
				GameObject organism = speciesList[speciesList.Count - 1];
				Destroy (organism);
				speciesList.RemoveAt(speciesList.Count - 1);
			}
		}
	}

	public void createPlant() {
		Vector3 position = new Vector3(Random.Range(-30.0f, 30.0f), 100, 60);

		GameObject organism = createOrganism(position);
		speciesList.Add(organism);
	}

	public void createAnimal() {
		Vector3 position;

		if (speciesList.Count == 0) {
			position = new Vector3(Random.Range(-30.0f, 30.0f), 100, 60);
			
			if (organism_type.Equals("Plant")) {
				position = new Vector3(Random.Range(-30.0f, 30.0f), 100, Random.Range(65, 70));
			}
		} else {
			position = new Vector3(Random.Range(-1.0f, 1.0f), 0, Random.Range(-1.0f, 1.0f));
			position *= Random.Range(5, 20);
			position += speciesList[0].transform.position;
		}

		RaycastHit hit;
		Debug.DrawRay(position, Vector3.down * 100);

		if (Physics.Raycast(position, Vector3.down, out hit, 100)) {
			position = hit.point;
		}
		
		position[1] += 1;
		
		GameObject organism = createOrganism(position);
		
		// Assign Leader
		if (speciesList.Count == 0) {
//			organism.GetComponent<AI>().alphaLeader = organism;
			organism.transform.localScale *= 1.25f;
//			organism.GetComponent<CapsuleCollider>().enabled = true;
			organism.name = name + " (Alpha)_" + species_id;
		} else {
//			organism.GetComponent<AI>().alphaLeader = speciesList[0];
			organism.name = name + "_" + species_id;
		}

		speciesList.Add(organism);
	}
	
	public GameObject createOrganism(Vector3 position) {
		GameObject organism = Instantiate(World.speciesPrefabs["African Elephant"]) as GameObject;
		organism.transform.GetChild(0).renderer.material.mainTexture = Resources.Load(Constants.TEXTURE_RESOURCES_PATH + "Species/" + name) as Texture;
		organism.transform.position = position;

		if (organism_type.Equals("Animal")) {
			organism.AddComponent<AnimalAI>();
		}

		if (name.Equals("Acacia")) {
			organism.transform.localScale *= 1.75f;
		}

		return organism;
	}
	
	public void updateTerritoryPos(Vector3 position) {
		GameObject organism = speciesList[0];
		organism.GetComponent<AI>().territoryPos = position;
	}
	
	public void resetSpecies() {
		foreach (GameObject organism in speciesList) {
			Destroy (organism);
		}
		speciesList = new List<GameObject>();
	}

	public void removeSpecie() {
		Destroy(this);
	}

}
