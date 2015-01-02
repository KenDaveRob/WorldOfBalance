using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using System.Text.RegularExpressions;

public class Graph : MonoBehaviour {
	
	private bool isHidden;
	
	Dictionary<String, Dictionary<int, float>> AnimalDictionary = new Dictionary<String, Dictionary<int, float>>();
	Dictionary<int, float> AntsDictionary = new Dictionary<int, float>();
	Dictionary<int, float> FliesDictionary = new Dictionary<int, float>();
	Dictionary<int, float> AphidDictionary = new Dictionary<int, float>();
	Dictionary<int, float> RoachDictionary = new Dictionary<int, float>();
	Dictionary<int, float> Roach2Dictionary = new Dictionary<int, float>();
	Dictionary<int, float> Roach3Dictionary = new Dictionary<int, float>();
	Dictionary<int, float> Roach4Dictionary = new Dictionary<int, float>();
	Dictionary<int, float> Roach5Dictionary = new Dictionary<int, float>();
	Dictionary<int, float> Roach6Dictionary = new Dictionary<int, float>();
	Dictionary<int, float> Roach7Dictionary = new Dictionary<int, float>();
	
	Dictionary<string, Color> colorDictionary = new Dictionary<string, Color>();
	Color[] colors;
	Color defaultColor;
	List<String> AnimalList = new List<String>();
	List<String> SelectedAnimalsList = new List<String>();
	int startMonth, endMonth;
	Rect GraphWindow = new Rect(20, 20, Screen.width *.9f, Screen.height * .8f);
	Rect scrollWindow;
	Vector2 scrollWindowPos;
	Texture2D graphLines;
	float maxBiomass = 0.0f;
	
	string barGraphMonthText, lineGraphStartMonthText, lineGraphEndMonthText, scrollBarSearchText;

	
	GUIStyle style = new GUIStyle();
	Texture2D texture;
	// Use this for initialization
	void Start () {
		colors = new Color[13] {Color.red, Color.blue, Color.magenta, Color.green, Color.cyan, Color.yellow, Color.white, Color.gray, Color.black,Color.red,Color.red,Color.red,Color.red};
		texture = new Texture2D(1, 1);
		graphLines = new Texture2D(1,1);
		graphLines.SetPixel(0,0,Color.white);
		AntsDictionary.Add(1, 1000f);
		AntsDictionary.Add(2, 2000f);
		AntsDictionary.Add(3, 1500f);
		AntsDictionary.Add(4, 2200f);
		AnimalDictionary.Add("Elephant", AntsDictionary);
		
		barGraphMonthText = "1";
		lineGraphStartMonthText = "1";
		lineGraphEndMonthText = "4";
		scrollBarSearchText = "";
		
		
		scrollWindow = new Rect(GraphWindow.width - 140, 50, 130, GraphWindow.height - 50);
		scrollWindowPos = new Vector2(0, 0);
		
		foreach(KeyValuePair<String, Dictionary<int, float>> entry in AnimalDictionary)
		{
			AnimalList.Add(entry.Key);
			SelectedAnimalsList.Add(entry.Key);
			//foreach(KeyValuePair<int, float>> entry
		}
				
		isHidden = true;
	}
	
	
    void OnGUI() {
		if (GUI.Button(new Rect(Screen.width - 240, Screen.height - 40, 100, 30), "Graph")) {
			isHidden = !isHidden;
		}

		if (!isHidden) {
        	GraphWindow = GUI.Window(0, GraphWindow, DoWindow, "Graph");
		}
    }
    void DoWindow(int windowID) {
		GUI.DragWindow(new Rect(0, 0, 10000, 20));
		int count = 0;
		int displayCount = 0;
		defaultColor = GUI.color;
		colorDictionary = new Dictionary<string, Color>();
		
		GUI.Label(new Rect(GraphWindow.width - 180,20, 50, 25), "Search");
		scrollBarSearchText = GUI.TextField(new Rect(GraphWindow.width - 130,20, 60, 25), scrollBarSearchText);
		
		scrollWindowPos = GUI.BeginScrollView (scrollWindow, scrollWindowPos, new Rect (0, 0, 110, 35 * AnimalList.Count));
		
		foreach(string animal in AnimalList)
		{
			
				if(count< colors.Length)
				{
					colorDictionary.Add(animal, colors[count]);
					GUI.color = colors[count];
					if(animal.ToLower().Contains(scrollBarSearchText.ToLower()))
					{
						if(GUI.Button(new Rect(2, 30*(displayCount),80,30), animal))
						{
							if(SelectedAnimalsList.Contains(animal))
							{
								SelectedAnimalsList.Remove(animal);
							}else{
								SelectedAnimalsList.Add(animal);
							}
						}
					displayCount++;
					}
				}
			
			count++;
			GUI.color = defaultColor;
		}
		
		GUI.EndScrollView ();
		
		GUIUtility.RotateAroundPivot(-90, new Vector2(55, 115));
		GUI.Label(new Rect(5,100, 100, 30),"Biomass");
		GUIUtility.RotateAroundPivot(90, new Vector2(55, 115));
		
		DrawGraphSkeleton();
		//DrawBarGraph();
		DrawLineGraph();
    }
	void DrawGraphSkeleton()
	{
		
		foreach(string animal in SelectedAnimalsList)
		{
			Dictionary<int, float> animalBiomasses;
			AnimalDictionary.TryGetValue(animal, out animalBiomasses);
			foreach(KeyValuePair<int, float> entry in animalBiomasses)
			{
				if(entry.Value > maxBiomass)
				{
					maxBiomass = entry.Value;
				}
			}
		}
		float maxGraphEntry = maxBiomass;
		int graphDraw = 10;
		for(float i = 0f; i <= 1.2; i = i + .1f)
		{
			//GUI.Label(new Rect(100, 30 * graphDraw + 60, 100, 30), "" + maxGraphEntry * i);
			//GUI.DrawTexture(new Rect(150, 30 * graphDraw + 70, GraphWindow.width - 300, 1f), graphLines);
			GUI.Label(new Rect(100, (GraphWindow.height / 16) * graphDraw + 70, 100, 30), "" + maxGraphEntry * i);
			GUI.DrawTexture(new Rect(150, (GraphWindow.height / 16) * graphDraw + 80, GraphWindow.width - 300, 1f), graphLines);
			graphDraw --;
		}
		
	}
	
	void DrawLineGraph()
	{
		GUI.Label(new Rect(5,15, 80, 25), "Start Month");
		GUI.Label(new Rect(5,GraphWindow.height - 60, 80, 25), "End Month");
		lineGraphStartMonthText = GUI.TextField(new Rect(5,40, 40, 25), lineGraphStartMonthText);
		lineGraphStartMonthText = Regex.Replace(lineGraphStartMonthText, @"[^0-9 ]", "");
		lineGraphEndMonthText = GUI.TextField(new Rect(5,GraphWindow.height - 30, 40, 25), lineGraphEndMonthText);
		lineGraphEndMonthText = Regex.Replace(lineGraphEndMonthText, @"[^0-9 ]", "");
		
		Rect GraphArea = new Rect(155, (GraphWindow.height / 16) * -1 + 80, GraphWindow.width - 300, (GraphWindow.height / 16)* 11 + 2);
		
		float pointSpace = (GraphArea.width - 10) / (Int32.Parse(lineGraphEndMonthText) - Int32.Parse(lineGraphStartMonthText));
		GUI.BeginGroup(GraphArea);
	
		
		Dictionary<int, float> animalBiomasses;
		float tempBiomass = 0f;
		
		foreach(string animal in SelectedAnimalsList)
		{
			int count = 1;
			int month = Int32.Parse(lineGraphStartMonthText);
			AnimalDictionary.TryGetValue(animal, out animalBiomasses);
			while( month <= Int32.Parse(lineGraphEndMonthText))
			{
				if(animalBiomasses.TryGetValue(month, out tempBiomass))
				{
					tempBiomass = tempBiomass / maxBiomass;
					texture.SetPixel(0,0,colorDictionary[animal]);
					texture.Apply();
					style.normal.background = texture;
					Drawing.DrawLine(new Vector2((int)(pointSpace * (month - Int32.Parse(lineGraphStartMonthText))+1), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass))-1), new Vector2((int)(pointSpace * (month - Int32.Parse(lineGraphStartMonthText))+1), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass))+5), colorDictionary[animal], 5);
					//GUI.Box(new Rect((pointSpace * (month - Int32.Parse(lineGraphStartMonthText))), (GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass) - 1 ), 2, 2),texture);
					float tempBiomass2;
					if(animalBiomasses.TryGetValue(month + 1, out tempBiomass2))
					{
						tempBiomass2 = tempBiomass2 / maxBiomass;
						//Drawing.DrawLine(new Vector2((int)(pointSpace * month), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass))), new Vector2((int)(pointSpace * (month + 1)), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass2))));
						Drawing.DrawLine(new Vector2((int)(pointSpace * (month - Int32.Parse(lineGraphStartMonthText))), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass))), new Vector2((int)(pointSpace * (count)), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass2))), colorDictionary[animal],3);
					}
				}
				month++;
				count++;

			}
			
		}
		GUI.EndGroup();
	}
	void DrawBarGraph()
	{
		
		GUI.Label(new Rect(5,15, 40, 25), "Month");
		barGraphMonthText = GUI.TextField(new Rect(5,40, 40, 25), barGraphMonthText);
		barGraphMonthText = Regex.Replace(barGraphMonthText, @"[^0-9 ]", "");
		
		Rect GraphArea = new Rect(155, (GraphWindow.height / 16) * -1 + 80, GraphWindow.width - 300, (GraphWindow.height / 16)* 11 + 2);
		float RectWidth = (GraphArea.width - 10) / SelectedAnimalsList.Count;
		GUI.BeginGroup(GraphArea);
		
		int count = 0;
		Dictionary<int, float> animalBiomasses;
		float tempBiomass = 0f;
		foreach(string animal in SelectedAnimalsList)
		{
			if(count < colors.Length)
			{
				AnimalDictionary.TryGetValue(animal, out animalBiomasses);
				animalBiomasses.TryGetValue(Int32.Parse(barGraphMonthText), out tempBiomass);//change to specify month
				tempBiomass = tempBiomass / maxBiomass;
				texture.SetPixel(0,0,colorDictionary[animal]);
				texture.Apply();
				style.normal.background = texture;
				GUI.Box(new Rect(5+ RectWidth * count,GraphArea.height - (GraphArea.height * tempBiomass) + (GraphWindow.height / 16), RectWidth - 5, (GraphArea.height * tempBiomass)), "", style);
			}
			count++;
			GUI.color = defaultColor;
			
		}
		GUI.EndGroup();
		count = 0;
		foreach(string animal in SelectedAnimalsList)
		{					
			AnimalDictionary.TryGetValue(animal, out animalBiomasses);
			GUIUtility.RotateAroundPivot(-45, new Vector2(160 + RectWidth * count, GraphWindow.height - 40));
			GUI.Label(new Rect(160 + RectWidth * count, GraphWindow.height - 40,100,30), animal);
			GUIUtility.RotateAroundPivot(45, new Vector2(160 + RectWidth * count, GraphWindow.height - 40));
			count++;
			
		}
	}

	
	
	// Update is called once per frame
	void Update () {
	
	}
}