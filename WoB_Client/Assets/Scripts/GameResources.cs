using UnityEngine;

using System;
using System.Collections;

public class GameResources : MonoBehaviour {

	public GUISkin skin;
	
	private int credits;
	private int coins;

	// Use this for initialization
	void Start () {

	}
	
	// Update is called once per frame
	void Update () {
		try {
			credits = GameState.world.credits;
			coins = GameState.avatar.coins;
		} catch (NullReferenceException e) {
		}
	}
	
	void OnGUI() {
		GUI.BeginGroup(new Rect(Screen.width * 0.75f - 200, 10, 400, 100));

			GUIStyle style = new GUIStyle(skin.label);
			style.font = skin.font;
			style.fontSize = 20;
			style.alignment = TextAnchor.UpperRight;
			
			//ExtraMethods.DrawOutline(new Rect(-170, 0, 400, 50), credits.ToString("n0") + " Credits", style, Color.black, Color.green);
	
			style = new GUIStyle(skin.label);
			style.font = skin.font;
			style.fontSize = 20;
			style.alignment = TextAnchor.UpperRight;
	
			//ExtraMethods.DrawOutline(new Rect(-170, 20, 400, 50), coins.ToString("n0") + " Coins", style, Color.black, Color.yellow);

		GUI.EndGroup();
	}
	
	public void SetCredits(int credits) {
		this.credits = credits;
	}
	
	public void SetCoins(int coins) {
		this.coins = coins;
	}
}
