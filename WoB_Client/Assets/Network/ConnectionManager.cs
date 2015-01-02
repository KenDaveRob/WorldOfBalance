using UnityEngine;
using System.Collections;

using System;
using System.IO;
using System.Net.Sockets;
using System.Security.Cryptography;
using System.Text;

public class ConnectionManager : MonoBehaviour {
	
	private GameObject mainObject;
	private TcpClient mySocket;
	private NetworkStream theStream;
	private bool socketReady = false;
	
	void Awake() {
		mainObject = GameObject.Find("MainObject");
		
		NetworkRequestTable.init();
		NetworkResponseTable.init();
	}
	
	// Use this for initialization
	void Start () {
		SetupSocket();
	}

	public void SetupSocket() {
		if (socketReady) {
			Debug.Log("Already Connected");
			return;
		}

		try {
			mySocket = new TcpClient(Constants.REMOTE_HOST, Constants.REMOTE_PORT);
			theStream = mySocket.GetStream();
			socketReady = true;
			
			Debug.Log("Connected");

			StartCoroutine(RequestHeartbeat(Constants.HEARTBEAT_RATE));
		} catch (Exception e) {
			Debug.Log("Socket error: " + e);
		}
	}

	public void ReadSocket() {
		if (!socketReady) {
			return;
		}
		
		if (theStream.DataAvailable) {
			byte[] buffer = new byte[2];
			theStream.Read(buffer, 0, 2);
			short bufferSize = BitConverter.ToInt16(buffer, 0);

			buffer = new byte[bufferSize];
			theStream.Read(buffer, 0, bufferSize);
			MemoryStream dataStream = new MemoryStream(buffer);

			short response_id = DataReader.ReadShort(dataStream);
			
			NetworkResponse response = NetworkResponseTable.get(response_id);
			
			if (response != null) {
				response.dataStream = dataStream;
				
				response.parse();
				ExtendedEventArgs args = response.process();
				
				if (args != null) {
					MessageQueue msgQueue = mainObject.GetComponent<MessageQueue>();
					msgQueue.AddMessage(args.event_id, args);
				}
			}

			Debug.Log("Received Response No. " + response_id + " [" + response.ToString() + "]");
		}
	}

	public void CloseSocket() {
		if (!socketReady) {
			return;
		}

		mySocket.Close();
		socketReady = false;
	}
	
	public void Send(NetworkRequest request) {
		if (!socketReady) {
			SetupSocket();
		}

		GamePacket packet = request.packet;

		byte[] bytes = packet.getBytes();
		theStream.Write(bytes, 0, bytes.Length);

		if (request.request_id != Constants.CMSG_HEARTBEAT) {
			Debug.Log("Sent Request No. " + request.request_id + " [" + request.ToString() + "]");
		}
	}
	
	public IEnumerator RequestHeartbeat(float time) {
		yield return new WaitForSeconds(time);

		Send(new RequestHeartbeat());
		StartCoroutine(RequestHeartbeat(Constants.HEARTBEAT_RATE));
	}

	// Update is called once per frame
	void Update () {
		try {
			ReadSocket();
		} catch (Exception e) {
			Debug.Log("Read Socket Error");
		}
	}
}
