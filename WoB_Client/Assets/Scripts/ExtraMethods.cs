using UnityEngine;

using System.Text;

public class ExtraMethods : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}
	
	public static void DrawOutline(Rect position, string text, GUIStyle style, Color outColor, Color inColor) {
	    var backupStyle = style;
	    style.normal.textColor = outColor;
	    position.x--;
	    GUI.Label(position, text, style);
	    position.x += 2;
	    GUI.Label(position, text, style);
	    position.x--;
	    position.y--;
	    GUI.Label(position, text, style);
	    position.y += 2;
	    GUI.Label(position, text, style);
	    position.y--;
	    style.normal.textColor = inColor;
	    GUI.Label(position, text, style);
	    style = backupStyle;
	}
}
