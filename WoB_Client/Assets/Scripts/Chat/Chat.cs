using UnityEngine;
using System.Collections;

public class Chat : MonoBehaviour {
	
	private GameObject mainObject;
	// Window Properties
	private float width = 453;
	private float height = 200;
	// Other
	private Rect windowRect;
	private Rect buttonRect;
	private Vector2 scrollViewVector;
	private string innerText = "";
	private string message = "";
	private Vector2 lastQuadrant;
	private Vector2 lastPosition;
	private bool isHidden;
	public GUISkin skin;
	
	void Awake() {
		mainObject = GameObject.Find("MainObject");
		mainObject.GetComponent<MessageQueue>().AddCallback(Constants.SMSG_CHAT, ResponseChat);
	}
	
	// Use this for initialization
	void Start () {
		if (!isHidden) {
			windowRect = new Rect(10, Screen.height - height - 10, width, height);
		}

		scrollViewVector = Vector2.zero;
	}
	
	// Update is called once per frame
	void Update () {
	}

	void OnGUI() {
		GUI.skin = skin;
		windowRect = GUI.Window((int) Constants.GUI_ID.Chat, windowRect, MakeWindow, "Chat");
		
		if (Event.current.type == EventType.KeyUp && Event.current.keyCode == KeyCode.Return) {
			SendMessage();
		}
	}
	
	public void MakeWindow(int id) {
		scrollViewVector = GUI.BeginScrollView(new Rect(30, 30, width - 60, height - 110), scrollViewVector, new Rect(0, 0, 300, 400));
//			GUI.contentColor = Color.black;
			GUI.Label(new Rect (0, 0, 400, 400), innerText);
//			GUI.contentColor = Color.white;
		GUI.EndScrollView();
		
		message = GUI.TextField(new Rect(30, height - 50, 278, 20), message, 100);
		
		buttonRect = new Rect(320, height - 50, 60, 20);
		if (GUI.Button(buttonRect, "Send")) {
			SendMessage();
		}

		GUI.DragWindow();
	}

	public void SendMessage() {
		ConnectionManager cManager = mainObject.GetComponent<ConnectionManager>();
		
		if (cManager) {
			cManager.Send(RequestChat(0, message));
		}
	}
	
	public void SetMessage(string message) {
		innerText += "\n" + message;
	}
	
	public RequestChat RequestChat(short type, string message) {
		RequestChat request = new RequestChat();
		request.Send(type, message);
		
		return request;
	}
	
	public void ResponseChat(ExtendedEventArgs eventArgs) {
		ResponseChatEventArgs args = eventArgs as ResponseChatEventArgs;

		if (args.status == 0) {
			string message = "";
			
			if (args.type == 0) {
				message += "[" + args.username + "] says: ";
			}
			
			message += args.message;
			
			SetMessage(message);
		}
	}
}
