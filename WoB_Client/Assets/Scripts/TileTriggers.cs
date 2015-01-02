using UnityEngine;
using System.Collections;

public class TileTriggers : MonoBehaviour {

	void OnTriggerEnter(Collider other) {
		string name = other.gameObject.name;

		if (name.StartsWith("Tile")) {
			int tile_id = int.Parse(name.Split('_')[1]);

			// Task: Send Animal ID and Tile ID to Server
			// Do Something...
			// ...
			// ..
			// End

			// Entered "Tile" (Visual Purposes Only)
			other.renderer.material.color = new Color(1, 0, 0, 0.3f);
		}
	}
	
	void OnTriggerExit(Collider other) {
		// Exited "Tile" (Visual Purposes Only)
		other.renderer.material.color = new Color(1, 1, 1, 0.3f);
	}
}
