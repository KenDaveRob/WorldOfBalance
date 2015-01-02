using UnityEngine;

using System.Collections;
using System.Collections.Generic;

public class World : MonoBehaviour {

	public static Dictionary<string, Object> speciesPrefabs = new Dictionary<string, Object>();
	private Dictionary<int, Species> speciesList = new Dictionary<int, Species>();
	private int size = 100;
	private Vector3 startPos = new Vector3(-1000, 100, -1000);
	private List<GameObject> tileList = new List<GameObject>();

	void Awake() {

	}
	
	// Use this for initialization
	void Start () {
		// Generate "Tiles"
//		PlacementManager p = TerrainController.Instance.GetPlacementManager("");
//		
//		GameObject tileParent = new GameObject("Tiles");
//		
//		for (int i = 0; i < 20; i++) {
//			for (int j = 0; j < 20; j++) {
//				Vector3 position = p.RequestPlacement(new Vector3(startPos[0] + i * (size + 1f), 0, startPos[2] + j * (size + 1f)));
//				GameObject cube = CreateCube(position);
//				cube.name = "Tile_" + (i * 20 + j).ToString();
//				tileList.Add(cube);
//				cube.transform.parent = tileParent.transform;
//				
//			}
//		}

		speciesPrefabs.Add("African Elephant", Resources.Load(Constants.PREFAB_RESOURCES_PATH + "African Elephant"));

		ConnectionManager cManager = GameObject.Find("MainObject").GetComponent<ConnectionManager>();
		
		if (cManager) {
			RequestReady request = new RequestReady();
			request.Send(true);
			
			cManager.Send(request);
		}
	}

	// Update is called once per frame
	void Update () {
		if (Input.GetKeyDown(KeyCode.Escape)) {
			Debug.Log("Exit Menu");
		}
	}

	public GameObject CreateCube(Vector3 position) {
		// Cube = "Tiles"
		GameObject cube = GameObject.CreatePrimitive(PrimitiveType.Cube);
		cube.collider.enabled = true;
		cube.collider.isTrigger = true; // Ignore Physics Collisions
		cube.transform.localScale = new Vector3(size, size, size);
		cube.transform.position = position;
		// Transparent Cubes (Visual Purposes Only)
    	cube.renderer.material = new Material(Shader.Find("Transparent/Diffuse"));
		cube.renderer.material.color = new Color(1, 1, 1, 0.3f);

		cube.GetComponent<MeshRenderer>().enabled = false;

		return cube;
	}
	
	public void UpdateSpeciesTile(int species_id, int tile_id) {
		if (speciesList.ContainsKey(species_id)) {
			Species species = speciesList[species_id];
			
			species.updateTerritoryPos(tileList[tile_id].transform.position);
		}
	}
}
