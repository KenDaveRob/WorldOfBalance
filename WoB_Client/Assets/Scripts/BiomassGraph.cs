using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;

public class BiomassGraph : MonoBehaviour {

	private bool isHidden;
	// Window Properties
	private float width = 900;
	private float height = 600;
	// Other
	private Rect windowRect;
	private string[] xAxisLabels;
	private Dictionary<string, float[]> csvList;
	private string csv;
	private float maxRange = 15;
	
	void Awake() {
		isHidden = true;
		csvList = new Dictionary<string, float[]>();
		// CSV Example
		csv = ",Month 1,Month 2,Month 3,Month 4,Month 5\nSpecies X,1,5,10,10,15";
	}
	
	// Use this for initialization
	void Start() {
		windowRect = new Rect (25, 25, width, height);
		// Convert CSV string into iterable form
		parseCSV();
	}
	
	void OnGUI() {

		if (GUI.Button(new Rect(Screen.width - 120, Screen.height - 100, 100, 30), "Biomass")) {
			isHidden = !isHidden;
		}
		
		if (!isHidden) {
			windowRect = GUILayout.Window((int) 1234567890, windowRect, MakeWindow, "Total Biomass");
		}
	}
	
	void MakeWindow(int id) {
		// Zero Origin Label
		GUIStyle style = new GUIStyle();
		style.alignment = TextAnchor.UpperRight;
		style.normal.textColor = Color.white;
		
		GUI.Label(new Rect(-80, height - 66, 100, 100), "0", style);
		
		// X-Axis Line
		Drawing.DrawLine(new Vector2(30, height - 60), new Vector2(width - 30, height - 60), Color.white, 1);
		// X-Axis Grid Lines
		float xAxisLength = width - 30 - 30 - 20;
		for (int i = 0; i < xAxisLabels.Length; i++) {
			float xPos = 30 + xAxisLength / xAxisLabels.Length * (i + 1);
			float yPos = height - 60 - 5;
			
			Drawing.DrawLine(new Vector2(xPos, yPos), new Vector2(xPos, yPos + 10), Color.white, 1);
			// Unit Label
			style = new GUIStyle();
			style.alignment = TextAnchor.UpperCenter;
			style.normal.textColor = Color.white;
			
			GUI.Label(new Rect(xPos - 50, yPos + 13, 100, 100), xAxisLabels[i], style);
		}
	

		// Y-Axis Line
		Drawing.DrawLine(new Vector2(50, height - 40), new Vector2(50, 45), Color.white, 1);
		// Y-Axis Grid Lines
		float yAxisLength = height - 30 - 30 - 60;
		for (int i = 0; i < 6; i++) {
			float xPos = 43;
			float yPos = height - 60 - yAxisLength / 6 * (i + 1);
			
			Drawing.DrawLine(new Vector2(xPos, yPos), new Vector2(width - 30, yPos), Color.white, 1);
			// Unit Label
			style = new GUIStyle();
			style.alignment = TextAnchor.UpperRight;
			style.normal.textColor = Color.white;
			GUI.Label(new Rect(-80, yPos - 7, 100, 100), ((i + 1) * maxRange / 5).ToString(), style);
		}
		
		// Some Nice Looking Colors

		Texture2D rgb_texture = new Texture2D(6, 6);
		int h, w;
		for(h = 0;h<6;h++)
		{
			for(w = 0;w<6;w++)
			{
				rgb_texture.SetPixel(h, w, Color.white);
			}
		}
		rgb_texture.Apply();
		GUIStyle generic_style = new GUIStyle();
		GUI.skin.box = generic_style;

		//Vector4[] colorList = new Vector4[]{Color.cyan, Color.yellow, Color.magenta};
		//int colorIndex = 0;
		// Generate Line Graph
		foreach (KeyValuePair<string, float[]> entry in csvList) {
			for (int i = 0; i < entry.Value.Length - 1; i++) {
				// First Point
				float xPos = 30 + xAxisLength / xAxisLabels.Length * (i + 1);
				float yPos = height - 60 - (entry.Value[i] / maxRange * yAxisLength);
				// Next Point
				float xPosNext = 30 + xAxisLength / xAxisLabels.Length * (i + 2);
				float yPosNext = height - 60 - (entry.Value[i + 1] / maxRange * yAxisLength);


				// Connect the Dots
//				Drawing.DrawLine(new Vector2(xPos-1, yPos-1), new Vector2(xPos+1, yPos+1), Color.red, 3);
//				Drawing.DrawLine(new Vector2(xPosNext-1, yPosNext-1), new Vector2(xPosNext+1, yPosNext+1), Color.red, 3);
				GUI.Box (new Rect (xPos-3,yPos-3,6,6), rgb_texture);
				GUI.Box (new Rect (xPosNext-3,yPosNext-3,6,6), rgb_texture);
				Drawing.DrawLine(new Vector2(xPos, yPos), new Vector2(xPosNext, yPosNext), new Color(255, 255, 255), 4);
			}
			
//			colorIndex++;
		}

		//names for axies
		style.fontSize = 16;
		GUI.Label(new Rect(width/2 -70, height - 30, 100, 100), "Time" , style);
		GUI.Label(new Rect(-30, 20, 100, 100), "Biomass" , style);

		GUILayout.Space(190);
		
		GUI.DragWindow();
	}
	
	// Update is called once per frame
	void Update() {
	}
	
	public void parseCSV() {
		string[] rowList = csv.Split('\n');
		
		for (int i = 0; i < rowList.Length; i++) {
			string[] elements = rowList[i].Split(',');
			
			if (i == 0) {
				xAxisLabels = new string[elements.Length - 1];
				Array.Copy(elements, 1, xAxisLabels, 0, elements.Length - 1);
			} else {
				string seriesLabel = "Untitled";
				float[] values = new float[elements.Length - 1];
				
				for (int j = 0; j < elements.Length; j++) {
					if (j == 0) {
						seriesLabel = elements[0];
					} else {
						values[j - 1] = float.Parse(elements[j]);
					}
				}
				
				csvList.Add(seriesLabel, values);
			}
		}
	}
}
