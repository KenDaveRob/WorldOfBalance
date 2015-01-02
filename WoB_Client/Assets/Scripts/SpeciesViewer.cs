using UnityEngine;

using System.Collections;
using System.Collections.Generic;

public class SpeciesViewer : MonoBehaviour {
	
	private float width = Screen.width - 60;
	private float height = 200;
	private Rect windowRect;
	public bool isHidden { get; set; }
	private Vector2 scrollPosition = Vector2.zero;
	private float t = 0;
	private List<Species> speciesList;

	// Use this for initialization
	void Start () {
		windowRect = new Rect(0, 100, width, height);
		isHidden = true;
		
		speciesList = new List<Species>(GetComponent<GameState>().speciesList.Values);
	}
	
	// Update is called once per frame
	void Update () {
		if (isHidden) {
			windowRect.x = Mathf.Lerp(10, -width + 50, t);
		} else {
			windowRect.x = Mathf.Lerp(-width + 50, 10, t);
		}

		t += Time.deltaTime * 2;
	}

	void OnGUI() {
		windowRect = GUI.Window((int) Constants.GUI_ID.Species_Viewer, windowRect, MakeWindow, "Species Viewer");
	}
	
	public void MakeWindow(int id) {
        Matrix4x4 matrix = GUI.matrix;

		Vector2 pivotPoint = new Vector2(width - 25, height / 2);
//		GUIUtility.RotateAroundPivot(-90, pivotPoint);
		Rect btnRect = new Rect(pivotPoint.x - 15, pivotPoint.y - 65, 30, 130);
		if (GUI.Button(btnRect, "S\nP\nE\nC\nI\nE\nS")) {
			isHidden = !isHidden;
			t = 0;
		}

        GUI.matrix = matrix;

		List<int> shopList = new List<int>(GetComponent<Shop>().itemList.Keys);
		float viewRectWidth = shopList.Count * 120;
		
		scrollPosition = GUI.BeginScrollView(new Rect(10, 30, width - 80, 160), scrollPosition, new Rect(0, 0, viewRectWidth, 160));
			GUI.Box(new Rect(0, 0, width - 50, 160), "");

			speciesList = new List<Species>(GetComponent<GameState>().speciesList.Values);

			for (int i = 0; i < speciesList.Count; i++) {
				Species species = speciesList[i];

				GUI.BeginGroup (new Rect(10 + i % 10 * 90, 20, 160, 160));
					if (GUI.Button(new Rect(0, 0, 80, 120), "")) {
						GetComponent<GameState>().CreateSpecies(species.species_id, species.name, "Animal", species.biomass);
					}
		
					GUI.DrawTexture(new Rect(10, 10, 60, 60), Resources.Load(Constants.IMAGE_RESOURCES_PATH + species.name) as Texture);
	
					GUIStyle style = new GUIStyle(GUI.skin.button);
					style.alignment = TextAnchor.MiddleLeft;
					style.normal.textColor = Color.white;
	
					GUI.Label(new Rect(5, 80, 70, 30), species.name, style);
				GUI.EndGroup ();
			}
		GUI.EndScrollView();
	}
}
