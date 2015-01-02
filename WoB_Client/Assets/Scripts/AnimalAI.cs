using UnityEngine;
using System.Collections;

public class AnimalAI : MonoBehaviour {
	
	private bool isMoving;
	public Vector3 destination;
	public float speed = 0.5f;
	private NavMeshAgent agent;
	public GameObject alphaLeader {get; set;}
	public Vector3 territoryPos {get; set;}
    private Vector3 moveDirection = Vector3.zero;
	private float degree = 0;
	
	// Use this for initialization
	void Start () {
		agent = GetComponent<NavMeshAgent>();

		alphaLeader = gameObject;
		StartCoroutine(ChooseDestination(0.0f));
	}
	
	// Update is called once per frame
	void Update () {
		if (isMoving) {
			// Flip Texture Direction
			Transform textureTransform = transform.GetChild(0);

			if ((destination - transform.position).x > 0) {
				textureTransform.localScale = new Vector3(Mathf.Abs(textureTransform.localScale.x), textureTransform.localScale.y, textureTransform.localScale.z);
			} else {
				textureTransform.localScale = new Vector3(-Mathf.Abs(textureTransform.localScale.x), textureTransform.localScale.y, textureTransform.localScale.z);
			}
			
			if (agent.remainingDistance < 3) {
				isMoving = false;
				StartCoroutine(ChooseDestination(5f));
			}
		}
		
		transform.localEulerAngles = new Vector3(0, 0, 0);
	}

	void OnTriggerEnter(Collider other) {
		string name = other.gameObject.name;

		if (name.StartsWith("Tile")) {
			int species_id = int.Parse(gameObject.name.Split('_')[1]);
			int tile_id = int.Parse(name.Split('_')[1]);
			
//			Debug.Log(gameObject.name + " enters Tile #" + tile_id);

			// Entered "Tile" (Visual Purposes Only)
			other.renderer.material.color = new Color(1, 0, 0, 0.3f);
		}
	}
	
	void OnTriggerExit(Collider other) {
		// Exited "Tile" (Visual Purposes Only)
		other.renderer.material.color = new Color(1, 1, 1, 0.3f);
	}

	public IEnumerator ChooseDestination(float time) {
		return ChooseDestination(time, Vector3.one);
	}

    public IEnumerator ChooseDestination(float time, Vector3 faceDirection) {
		yield return new WaitForSeconds(time);

		isMoving = true;
	
		faceDirection.Scale(new Vector3(Random.Range(-1.0f, 1.0f), Random.Range(-1.0f, 1.0f), 0));

		if (gameObject == alphaLeader) {
			faceDirection *= Random.Range(10, 30);
			destination = transform.position + faceDirection;
		}  else {
			faceDirection *= Random.Range(30, 100);
			destination = alphaLeader.transform.position + faceDirection;
		}
		
		destination[1] = 100;
		
//		NavMeshHit hit;
//		
//		if (NavMesh.SamplePosition(destination, out hit, 100, 1)) {
//			destination = hit.position;
//			agent.SetDestination(destination);
//
//			//CreateDestinationSphere(destination);
//		}
		RaycastHit hit;
		Debug.DrawRay(destination, Vector3.down * 100);

		if (Physics.Raycast(destination, Vector3.down, out hit, 100)) {
			destination = hit.point;
			agent.SetDestination(destination);
		}

//		if ((new Vector3(-12, transform.position.y, 60) - transform.position).magnitude) {
//		} else if ((new Vector3(50, transform.position.y, 65) - transform.position).magnitude) {
//		}

//		CreateDestinationSphere(destination);
	}

	public void CreateDestinationSphere(Vector3 destination) {
		GameObject sphere = GameObject.CreatePrimitive(PrimitiveType.Sphere);
		sphere.collider.enabled = false;
		sphere.transform.localScale = new Vector3(5f, 5f, 5f);
		sphere.transform.position = destination;
	}
}

