using UnityEngine;
using System.Collections;

public class TileExample : MonoBehaviour {
	
	private int size = 2;
	private Vector3 startPos = new Vector3(-2, 0, -2);

	// Use this for initialization
	void Start () {
		// Grab Camera Instance
		GameObject camera = GameObject.Find("Main Camera");
		// Top View Camera (Optional)
		camera.transform.Rotate(45, 0, 0);
		camera.transform.position = new Vector3(0, camera.transform.position[1], 0);
		// Generate 3x3 "Tiles"
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				GameObject cube = CreateCube(new Vector3(startPos[0] + i * (size + 0.1f), 0, startPos[2] + j * (size + 0.1f)));
				// Tile "ID"
				cube.name = "Tile_" + (i * 3 + j).ToString();
			}
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
		
		return cube;
	}
	
	// Update is called once per frame
	void Update () {
	
	}
}
