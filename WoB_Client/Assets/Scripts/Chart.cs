using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;

public class Chart : MonoBehaviour {

	// Window Properties
	private float width = 500;
	private float height = 300;
	// Other
	private Rect windowRect;
	private string[] xAxisLabels;
	private Dictionary<string, float[]> csvList;
	private string csv;
	private float maxRange = 15;
	
	void Awake() {
		csvList = new Dictionary<string, float[]>();
		// CSV Example
		csv = ",Month 1,Month 2,Month 3,Month 4,Month 5\nSpecies X,1,5,10,10,15\nSpecies Y,0,7,14,5,4\nSpecies Z,5,7,10,11,2";
	}

	// Use this for initialization
	void Start() {
		windowRect = new Rect ((Screen.width - width) / 2, (Screen.height - height) / 2, width, height);
		// Convert CSV string into iterable form
		parseCSV();
	}
	
	void OnGUI() {
		windowRect = GUILayout.Window((int) 1234567890, windowRect, MakeWindow, "Some Species Chart");
	}
	
	void MakeWindow(int id) {
		// Zero Origin Label
		GUIStyle style = new GUIStyle();
		style.alignment = TextAnchor.UpperRight;
		style.normal.textColor = Color.white;

		GUI.Label(new Rect(-80, height - 37, 100, 100), "0", style);

		// X-Axis Line
		Drawing.DrawLine(new Vector2(30, height - 30), new Vector2(width - 30, height - 30), Color.red, 3);
		// X-Axis Grid Lines
		float xAxisLength = width - 30 - 30 - 20;
		for (int i = 0; i < xAxisLabels.Length; i++) {
			float xPos = 30 + xAxisLength / xAxisLabels.Length * (i + 1);
			float yPos = height - 30 - 5;

			Drawing.DrawLine(new Vector2(xPos, yPos), new Vector2(xPos, yPos + 10), Color.red, 3);
			// Unit Label
			style = new GUIStyle();
			style.alignment = TextAnchor.UpperCenter;
			style.normal.textColor = Color.white;

			GUI.Label(new Rect(xPos - 50, yPos + 13, 100, 100), xAxisLabels[i], style);
		}

		// Y-Axis Line
		Drawing.DrawLine(new Vector2(30, height - 30), new Vector2(30, 30), Color.green, 3);
		// Y-Axis Grid Lines
		float yAxisLength = height - 30 - 30 - 20;
		for (int i = 0; i < 5; i++) {
			float xPos = 26;
			float yPos = height - 30 - yAxisLength / 5 * (i + 1);

			Drawing.DrawLine(new Vector2(xPos, yPos), new Vector2(xPos + 10, yPos), Color.green, 3);
			// Unit Label
			style = new GUIStyle();
			style.alignment = TextAnchor.UpperRight;
			style.normal.textColor = Color.white;

			GUI.Label(new Rect(-80, yPos - 7, 100, 100), ((i + 1) * maxRange / 5).ToString(), style);
		}
		
		// Some Nice Looking Colors
		Vector4[] colorList = new Vector4[]{Color.cyan, Color.yellow, Color.magenta};
		int colorIndex = 0;
		// Generate Line Graph
		foreach (KeyValuePair<string, float[]> entry in csvList) {
			for (int i = 0; i < entry.Value.Length - 1; i++) {
				// First Point
				float xPos = 30 + xAxisLength / xAxisLabels.Length * (i + 1);
				float yPos = height - 30 - (entry.Value[i] / maxRange * yAxisLength);
				// Next Point
				float xPosNext = 30 + xAxisLength / xAxisLabels.Length * (i + 2);
				float yPosNext = height - 30 - (entry.Value[i + 1] / maxRange * yAxisLength);
				// Connect the Dots
				Drawing.DrawLine(new Vector2(xPos, yPos), new Vector2(xPosNext, yPosNext), colorList[colorIndex], 3);
			}
			
			colorIndex++;
		}
		
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
