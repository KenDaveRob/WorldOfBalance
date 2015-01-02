using UnityEngine;

using System;
using System.Collections;
using System.Text.RegularExpressions;

public class Register : MonoBehaviour {
	
	private GameObject mainObject;
	// Window Properties
	private float width = 300;
	private float height = 100;
	// Other
	private string fname = "";
	private string lname = "";
	private string email = "";
	private string password = "";
	private string confirm = "";
	private string name = "";
	private Rect windowRect;
	
	void Awake() {
		mainObject = GameObject.Find("MainObject");

		mainObject.GetComponent<MessageQueue>().AddCallback(Constants.SMSG_REGISTER, ResponseRegister);
	}
	
	// Use this for initialization
	void Start() {
		windowRect = new Rect((Screen.width - width) / 2, 200, width, height);
	}
	
	void OnGUI() {
		windowRect = GUILayout.Window((int) Constants.GUI_ID.Register, windowRect, MakeWindow, "Register");

		if (Event.current.type == EventType.KeyUp && Event.current.keyCode == KeyCode.Return) {
			Submit();
		}
	}
	
	void MakeWindow(int id) {
		GUILayout.Label("First Name");
		GUI.SetNextControlName("fname_field");
		fname = GUILayout.TextField(fname, 25);

		GUILayout.Label("Last Name");
		GUI.SetNextControlName("lname_field");
		lname = GUILayout.TextField(lname, 25);

		GUILayout.Label("Email Address");
		GUI.SetNextControlName("email_field");
		email = GUILayout.TextField(email, 25);
		
		GUILayout.Label("Password");
		GUI.SetNextControlName("password_field");
		password = GUILayout.PasswordField(password, "*"[0], 25);
		
		GUILayout.Label("Confirm Password");
		GUI.SetNextControlName("confirm_field");
		confirm = GUILayout.PasswordField(confirm, "*"[0], 25);

		GUILayout.Label("Display Name");
		GUI.SetNextControlName("name_field");
		name = GUILayout.TextField(name, 25);
		
		GUILayout.Space(40);

		if (GUI.Button(new Rect(windowRect.width / 2 - 110, windowRect.height - 40, 100, 30), "Submit")) {
			Submit();
		}
		
		if (GUI.Button(new Rect(windowRect.width / 2 + 10, windowRect.height - 40, 100, 30), "Cancel")) {
			SwitchToLogin();
		}
	}
	
	public bool CheckEmail(string email) {
		return Regex.IsMatch(email, @"\A(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?)\Z");
	}
	
	public void Submit() {
		fname = fname.Trim();
		lname = lname.Trim();
		email = email.Trim();
		password = password.Trim();
		confirm = confirm.Trim();
		name = name.Trim();

		if (fname.Length == 0) {
			mainObject.GetComponent<Main>().CreateMessageBox("First Name Required");
			GUI.FocusControl("fname_field");
		} else if (lname.Length == 0) {
			mainObject.GetComponent<Main>().CreateMessageBox("Last Name Required");
			GUI.FocusControl("lname_field");
		} else if (email.Length == 0) {
			mainObject.GetComponent<Main>().CreateMessageBox("Email Required");
			GUI.FocusControl("email_field");
		} else if (!CheckEmail(email)) {
			mainObject.GetComponent<Main>().CreateMessageBox("Invalid Email");
			GUI.FocusControl("email_field");
		} else if (password.Length == 0) {
			mainObject.GetComponent<Main>().CreateMessageBox("Password Required");
			GUI.FocusControl("password_field");
		} else if (!password.Equals(confirm)) {
			mainObject.GetComponent<Main>().CreateMessageBox("Passwords do not match");
			GUI.FocusControl("confirm_field");
		} else if (name.Length == 0) {
			mainObject.GetComponent<Main>().CreateMessageBox("Display Name Required");
			GUI.FocusControl("name_field");
		} else {
			ConnectionManager cManager = mainObject.GetComponent<ConnectionManager>();
			
			if (cManager) {
				cManager.Send(RequestRegister(fname, lname, email, password, name));
			}
		}
	}
	
	public RequestRegister RequestRegister(string fname, string lname, string email, string password, string name) {
		RequestRegister request = new RequestRegister();
		request.Send(fname, lname, email, password, name);
		
		return request;
	}
	
	public void ResponseRegister(ExtendedEventArgs eventArgs) {
		ResponseRegisterEventArgs args = eventArgs as ResponseRegisterEventArgs;
		
		switch (args.status) {
			case 0:
				SwitchToLogin();
				break;
			case 1:
				mainObject.GetComponent<Main>().CreateMessageBox("Email Taken");
				break;
			case 2:
				mainObject.GetComponent<Main>().CreateMessageBox("Display Name Taken");
				break;
		}
	}

	public void SwitchToLogin() {
		Destroy(this);
		GameObject.Find("LoginObject").GetComponent<Login>().Show();
	}
	
	// Update is called once per frame
	void Update() {
	}
}
