using UnityEngine;
using System.Collections;

public class EnvironmentScore : MonoBehaviour {

	public GUISkin skin;
	
	public int score { get; set; }

	// Use this for initialization
	void Start () {
		GameObject.Find("MainObject").GetComponent<MessageQueue>().AddCallback(Constants.SMSG_UPDATE_ENV_SCORE, ResponseUpdateEnvironmentScore);
	}
	
	// Update is called once per frame
	void Update () {
	
	}
	
	void OnGUI() {
		GUI.BeginGroup(new Rect(Screen.width / 2 - 100, 85, 200, 100));
			GUIStyle style = new GUIStyle(skin.label);
			style.font = skin.font;
			style.fontSize = 20;
			style.alignment = TextAnchor.UpperCenter;
			
			Color color = new Color(1.0f, 0.93f, 0.73f, 1.0f);
	
			ExtraMethods.DrawOutline(new Rect(0, 0, 200, 50), "Environment Score", style, Color.black, color);
	
			style = new GUIStyle(skin.label);
			style.font = skin.font;
			style.fontSize = 24;
			style.alignment = TextAnchor.UpperCenter;
	
			ExtraMethods.DrawOutline(new Rect(0, 25, 200, 50), score.ToString("n0"), style, Color.black, Color.white);
		GUI.EndGroup();
	}
	
	public void SetScore(int score) {
		this.score = score;
	}

	public int GetScore() {
		return this.score;
	}
	
	public void ResponseUpdateEnvironmentScore(ExtendedEventArgs eventArgs) {
		ResponseUpdateEnvironmentScoreEventArgs args = eventArgs as ResponseUpdateEnvironmentScoreEventArgs;

		SetScore(args.score);
	}
}
