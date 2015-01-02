using UnityEngine;

using System;
using System.Collections;
using System.IO;
using System.Net.Sockets;

public class Login : MonoBehaviour {
	
	private GameObject mainObject;
	// Window Properties
	private float width = 280;
	private float height = 100;
	// Other
	public Texture background;
	private string user_id = "";
	private string password = "";
	private Rect windowRect;
	private bool isHidden;
	
	void Awake() {
		mainObject = GameObject.Find("MainObject");

		mainObject.GetComponent<MessageQueue>().AddCallback(Constants.SMSG_AUTH, ResponseLogin);
		mainObject.GetComponent<MessageQueue>().AddCallback(Constants.SMSG_SPECIES_LIST, ResponseSpeciesList);
	}
	
	// Use this for initialization
	void Start() {
		ConnectionManager cManager = mainObject.GetComponent<ConnectionManager>();
		
		if (cManager) {
			cManager.Send(new RequestSpeciesList());
		}
	}
	
	void OnGUI() {
		// Background
		GUI.DrawTexture(new Rect(0, 0, Screen.width, Screen.height), background);
		
		// Client Version Label
		GUI.Label(new Rect(Screen.width - 75, Screen.height - 30, 65, 20), "v" + Constants.CLIENT_VERSION + " Beta");
		
		// Login Interface
		if (!isHidden) {
			windowRect = new Rect((Screen.width - width) / 2, Screen.height / 2 - height, width, height);
			windowRect = GUILayout.Window((int) Constants.GUI_ID.Login, windowRect, MakeWindow, "Login");
		
			if (Event.current.type == EventType.KeyUp && Event.current.keyCode == KeyCode.Return) {
				Submit();
			}
		}
	}
	
	void MakeWindow(int id) {
		GUILayout.Label("User ID (Display Name or Email)");
		GUI.SetNextControlName("username_field");
		user_id = GUI.TextField(new Rect(10, 45, windowRect.width - 20, 25), user_id, 25);

		GUILayout.Space(30);
		
		GUILayout.Label("Password");
		GUI.SetNextControlName("password_field");
		password = GUI.PasswordField(new Rect(10, 100, windowRect.width - 20, 25), password, "*"[0], 25);
		
		GUILayout.Space(75);

		if (GUI.Button(new Rect(windowRect.width / 2 - 110, 135, 100, 30), "Log In")) {
			Submit();
		}
		
		if (GUI.Button(new Rect(windowRect.width / 2 + 10, 135, 100, 30), "Register")) {
			SwitchToRegister();
		}
	}
	
	public void Submit() {
		user_id = user_id.Trim();
		password = password.Trim();
		
		if (user_id.Length == 0) {
			mainObject.GetComponent<Main>().CreateMessageBox("User ID Required");
			GUI.FocusControl("username_field");
		} else if (password.Length == 0) {
			mainObject.GetComponent<Main>().CreateMessageBox("Password Required");
			GUI.FocusControl("password_field");
		} else {
			ConnectionManager cManager = mainObject.GetComponent<ConnectionManager>();
			
			if (cManager) {
				cManager.Send(RequestLogin(user_id, password));
			}
		}
	}
	
	public RequestLogin RequestLogin(string username, string password) {
		RequestLogin request = new RequestLogin();
		request.Send(username, password);
		
		return request;
	}
	
	public void ResponseLogin(ExtendedEventArgs eventArgs) {
		ResponseLoginEventArgs args = eventArgs as ResponseLoginEventArgs;
		
		if (args.status == 0) {
			SwitchToWorldSelection();
			Constants.USER_ID = args.user_id;

			gameObject.GetComponent<WorldMenu>().name = args.username;
			gameObject.GetComponent<WorldMenu>().last_logout = args.last_logout;
		} else {
			mainObject.GetComponent<Main>().CreateMessageBox("Login Failed");
		}
	}
	
	public void ResponseSpeciesList(ExtendedEventArgs eventArgs) {
		ResponseSpeciesListEventArgs args = eventArgs as ResponseSpeciesListEventArgs;

//		SpeciesTable.Update(args.speciesList);
	}
	
	public void SwitchToRegister() {
		isHidden = true;
		gameObject.AddComponent("Register");
	}
	
	public void SwitchToWorldSelection() {
		isHidden = true;
		gameObject.AddComponent("WorldMenu");
	}
	
	public void Show() {
		isHidden = false;
	}
	
	public void Hide() {
		isHidden = true;
	}
	
	// Update is called once per frame
	void Update() {
	}
}
