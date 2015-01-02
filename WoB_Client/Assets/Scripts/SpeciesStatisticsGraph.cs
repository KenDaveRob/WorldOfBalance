using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using System.Text.RegularExpressions;

public class SpeciesStatisticsGraph : MonoBehaviour {
	
	private bool isHidden;
	
	Dictionary<String, Dictionary<int, float>> AnimalDictionary = new Dictionary<String, Dictionary<int, float>>();
	
	Dictionary<string, Color> colorDictionary = new Dictionary<string, Color>();
	Color[] colors;
	Color defaultColor;
	List<String> AnimalList = new List<String>();
	List<String> SelectedAnimalsList = new List<String>();
	int startMonth, endMonth;
	Rect GraphWindow = new Rect(25 ,25, Screen.width*.95f,  Screen.height*.9f);
	Rect scrollWindow;
	Vector2 scrollWindowPos;
	Texture2D graphLines;
	float maxBiomass = 0.0f;
	
	string barGraphMonthText, lineGraphStartMonthText, lineGraphEndMonthText, scrollBarSearchText;

	bool popup;
	Rect locationOfPopup;
	String contentOfPopup;

	GUIStyle style = new GUIStyle();
	Texture2D texture;
	// Use this for initialization
	void Start () {
		popup = false;

		colors = new Color[13] {Color.red, Color.blue, Color.magenta, Color.green, Color.cyan, Color.yellow, Color.white, Color.gray, Color.black,Color.red,Color.red,Color.red,Color.red};
		texture = new Texture2D(1, 1);
		graphLines = new Texture2D(1,1);
		graphLines.SetPixel(1,1,Color.white);

		Dictionary<int, float> tempDictionary = new Dictionary<int, float>();
		tempDictionary.Add(1, 1000f);
		tempDictionary.Add(2, 2000f);
		tempDictionary.Add(3, 1500f);
		tempDictionary.Add(4, 2200f);
		AnimalDictionary.Add("Elephant", tempDictionary);
		tempDictionary= new Dictionary<int, float>();
		tempDictionary.Add(1, 1500f);
		tempDictionary.Add(2, 1000f);
		tempDictionary.Add(3, 3000f);
		tempDictionary.Add(4, 1000f);
		AnimalDictionary.Add("Big Foot", tempDictionary);
		tempDictionary= new Dictionary<int, float>();
		tempDictionary.Add(1, 2750f);
		tempDictionary.Add(2, 2000f);
		tempDictionary.Add(3, 1800f);
		tempDictionary.Add(4, 1000f);
		AnimalDictionary.Add("Yeti", tempDictionary);
		tempDictionary= new Dictionary<int, float>();
		tempDictionary.Add(1, 1000f);
		tempDictionary.Add(2, 1000f);
		tempDictionary.Add(3, 3000f);
		tempDictionary.Add(4, 1000f);
		AnimalDictionary.Add("Aliens", tempDictionary);
		tempDictionary= new Dictionary<int, float>();
		tempDictionary.Add(1, 2000f);
		tempDictionary.Add(2, 1000f);
		tempDictionary.Add(3, 2000f);
		tempDictionary.Add(4, 1000f);
		AnimalDictionary.Add("Chupacabra", tempDictionary);
		tempDictionary= new Dictionary<int, float>();
		tempDictionary.Add(1, 1000f);
		tempDictionary.Add(2, 1000f);
		tempDictionary.Add(3, 1000f);
		tempDictionary.Add(4, 1000f);
		AnimalDictionary.Add("Loch Ness", tempDictionary);

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
		if (GUI.Button(new Rect(Screen.width - 240, Screen.height - 100, 100, 30), "Species")) {
			isHidden = !isHidden;
		}
		
		if (!isHidden) {
			GraphWindow = GUI.Window(0, GraphWindow, DoWindow, "Species Statistics Graph");
		}
	}
	void DoWindow(int windowID) {
		GUI.DragWindow(new Rect(0, 0, 10000, 20));
		int count = 0;
		int displayCount = 0;
		defaultColor = GUI.color;
		colorDictionary = new Dictionary<string, Color>();

		GUIStyle style = new GUIStyle();
		style.alignment = TextAnchor.UpperRight;
		style.normal.textColor = Color.white;
		style.fontSize = 16;
		GUI.Label(new Rect(GraphWindow.width - 140,20, 100, 100), "Species List" , style);

		
		scrollWindowPos = GUI.BeginScrollView (scrollWindow, scrollWindowPos, new Rect (0, 0, 110, 35 * AnimalList.Count));
		
		foreach(string animal in AnimalList)
		{
			
			if(count< colors.Length)
			{
				colorDictionary.Add(animal, colors[count]);
				GUI.color = colors[count];
				if(animal.ToLower().Contains(scrollBarSearchText.ToLower()))
				{
					if(GUI.Button(new Rect(20, 30*(displayCount),80,30), animal))
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
		
//		GUIUtility.RotateAroundPivot(-90, new Vector2(55, 115));
//		GUI.Label(new Rect(5,100, 100, 30),"Biomass");
//		GUIUtility.RotateAroundPivot(90, new Vector2(55, 115));
//		
//		GUI.Label(new Rect(5,15, 80, 25), "Start Month");
//		GUI.Label(new Rect(5,GraphWindow.height - 60, 80, 25), "End Month");
//		lineGraphStartMonthText = GUI.TextField(new Rect(5,40, 40, 25), lineGraphStartMonthText);
//		lineGraphStartMonthText = Regex.Replace(lineGraphStartMonthText, @"[^0-9 ]", "");
//		lineGraphEndMonthText = GUI.TextField(new Rect(5,GraphWindow.height - 30, 40, 25), lineGraphEndMonthText);
//		lineGraphEndMonthText = Regex.Replace(lineGraphEndMonthText, @"[^0-9 ]", "");
		
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

		GUIStyle style = new GUIStyle();
		style.alignment = TextAnchor.UpperRight;
		style.normal.textColor = Color.white;
		style.fontSize = 14;
		GUI.Label(new Rect(0, (GraphWindow.height / 16) * -1 + 60, 70, 70), "Biomass" , style);
		GUI.Label(new Rect(GraphWindow.width/4+50,GraphWindow.height - 30, 70, 70), "Time" , style);
		
		float maxGraphEntry = maxBiomass;
		int graphDraw = 11;
		for(float i = 0f; i <= 1.3; i = i + .1f)
		{
			//GUI.Label(new Rect(100, 30 * graphDraw + 60, 100, 30), "" + maxGraphEntry * i);
			//GUI.DrawTexture(new Rect(150, 30 * graphDraw + 70, GraphWindow.width - 300, 1f), graphLines);
			GUI.Label(new Rect(17, (GraphWindow.height / 16) * graphDraw + 100, 100, 30), "" + (int)(maxGraphEntry * i));
			GUI.DrawTexture(new Rect(50, (GraphWindow.height / 16) * graphDraw + 110, GraphWindow.width - 200, 1f), graphLines);
			graphDraw --;
		}
		
		GUI.DrawTexture(new Rect(55, (GraphWindow.height / 16) * -1 + 100, 1f, (GraphWindow.height / 16) * 11 + 70), graphLines);
		
	}
	
	void DrawLineGraph()
	{
		
		
		Rect GraphArea = new Rect(155, (GraphWindow.height / 16) * -1 + 80, GraphWindow.width - 300, (GraphWindow.height / 16)* 11 + 2);
		
		float pointSpace = (GraphArea.width - 10) / (Int32.Parse(lineGraphEndMonthText) - Int32.Parse(lineGraphStartMonthText));
		GUI.BeginGroup(GraphArea);
		
		
		Dictionary<int, float> animalBiomasses;
		float tempBiomass = 0f;
		Texture2D rgb_texture = new Texture2D(6, 6);
		GUIStyle generic_style = new GUIStyle();
		GUI.skin.box = generic_style;

		foreach(string animal in SelectedAnimalsList)
		{
			int count = 1;
			int month = Int32.Parse(lineGraphStartMonthText);
			AnimalDictionary.TryGetValue(animal, out animalBiomasses);
			while( month <= Int32.Parse(lineGraphEndMonthText))
			{
				if(animalBiomasses.TryGetValue(month, out tempBiomass))
				{
					int h, w;
					for(h = 0;h<6;h++)
					{
						for(w = 0;w<6;w++)
						{
							rgb_texture.SetPixel(h, w,colorDictionary[animal]);
						}
					}
					rgb_texture.Apply();

					tempBiomass = tempBiomass / maxBiomass;
					texture.SetPixel(0,0,colorDictionary[animal]);
					texture.Apply();
					style.normal.background = texture;
					//Drawing.DrawLine(new Vector2((int)(pointSpace * (month - Int32.Parse(lineGraphStartMonthText))+1), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass))-1), new Vector2((int)(pointSpace * (month - Int32.Parse(lineGraphStartMonthText))+1), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass))+5), colorDictionary[animal], 5);
					//GUI.Box (new Rect (xPos-3,yPos-3,6,6), rgb_texture);
					float tempBiomass2;
										if(animalBiomasses.TryGetValue(month + 1, out tempBiomass2))
										{
											tempBiomass2 = tempBiomass2 / maxBiomass;
											//Drawing.DrawLine(new Vector2((int)(pointSpace * month), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass))), new Vector2((int)(pointSpace * (month + 1)), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass2))));
											Drawing.DrawLine(new Vector2((int)(pointSpace * (month - Int32.Parse(lineGraphStartMonthText))), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass))), new Vector2((int)(pointSpace * (count)), (int)(GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass2))), colorDictionary[animal],3);
										}
										Rect newRect=new Rect((pointSpace * (month - Int32.Parse(lineGraphStartMonthText))), (GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass) - 1 ), 6, 6);

					GUI.Box(newRect,rgb_texture);

				}
				month++;
				count++;
				
			}
			
		}

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

					Rect newRect=new Rect((pointSpace * (month - Int32.Parse(lineGraphStartMonthText))), (GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass) - 1 ), 6, 6);
					if(newRect.Contains(Event.current.mousePosition)){
						//Debug.Log(animal);
						//popup=true;
						GUI.depth=10;
						GUI.skin.box = generic_style;
						GUIStyle popstyle = new GUIStyle();
						popstyle.alignment = TextAnchor.MiddleCenter;
						popstyle.normal.textColor = Color.black;
						popstyle.fontSize = 10;
						popstyle.normal.background = texture;
						locationOfPopup=new Rect((pointSpace * (month - Int32.Parse(lineGraphStartMonthText))), (GraphArea.height - ((GraphArea.height - GraphArea.height/11)  * tempBiomass) - 1 ),100,100);
						contentOfPopup=animal;
						GUI.Box(locationOfPopup,contentOfPopup,popstyle);
					}
					
					
				}
				month++;
				count++;
				
			}
			
		}
		GUI.EndGroup();
	}

	
	// Update is called once per frame
	void Update () {
		
	}
}