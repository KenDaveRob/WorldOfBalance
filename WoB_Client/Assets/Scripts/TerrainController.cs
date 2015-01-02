using UnityEngine;
using System.Collections;

public class TerrainController : MonoBehaviour {

	public GameObject[] terrainList = new GameObject[3]; // Will be set from GUI
	
	private static TerrainController instance;
	
	private static int XZ_OFFSET = -1000;
	private static int PROXIMITY_RESOLUTION = 100;
	private float[,] waterProximity = new float[PROXIMITY_RESOLUTION,PROXIMITY_RESOLUTION];
	
	private static float MAXIMUM_WATER_Y =  0;
	private static float MINIMUM_WATER_Y = -15;
	
	// Ideal y value for object types, relative to high water line
	private static float IDEAL_PLANT_Y = 10;
	private static float IDEAL_WILDFIRE_Y = IDEAL_PLANT_Y * 2;
	
	private float initialWaterY = 0;
	private float waterLevel = (0-MINIMUM_WATER_Y) / (MAXIMUM_WATER_Y - MINIMUM_WATER_Y);
	
	private DefaultPlacementManager defaultPlacementManager;
	private DefaultPlacementManager plantPlacementManager;
	private DefaultPlacementManager wildfirePlacementManager;

	private GameObject activeObject;
	
	// Use this for initialization
	void Start () {
		activeObject = gameObject;
		
		defaultPlacementManager = new DefaultPlacementManager(this);
		
		GameObject water = GameObject.Find("Waterline");		
		if (water != null) {
			initialWaterY = water.transform.position.y;
		}
		
		if (GetComponent<Terrain>() != null) {		
			ComputeWaterProximity(); // Precompute water proximity table
			
			plantPlacementManager = new PlantPlacementManager(this); //new HeightBiasedPlacementManager(initialWaterY + IDEAL_PLANT_Y);
			
			wildfirePlacementManager = new WildfirePlacementManager(this, initialWaterY + IDEAL_WILDFIRE_Y);
			wildfirePlacementManager.controller = this;
		}
	}
	
	// Update is called once per frame
	void Update () {
		//SetWaterLevel (Random.value); // Used to test SetWaterLevel 
	}
	
	public static TerrainController Instance {
		get {
			if (instance == null) {
				instance = new GameObject("TerrainController").AddComponent<TerrainController>();
			}
			
			return instance;
		}
	}
	
	/**
	 * Set the active terrain, by index.
	 */
	public void SetTerrain(int terrainIndex) {
		if (terrainIndex >= 0 && terrainIndex < terrainList.Length) {
			GameObject terrain = terrainList[terrainIndex];
			Destroy (activeObject); // Remove the old game object
			activeObject = (GameObject) Instantiate (terrain, new Vector3(XZ_OFFSET,0,XZ_OFFSET), Quaternion.identity);
			activeObject.AddComponent<TerrainController>();
		}
	}
	
	/**
	 * Set the active terrain, by index.
	 */
	public void setTerrain(int terrainIndex) {
		SetTerrain (terrainIndex);
	}
	
	/**
	 * Expects a value between 0 (extremely dry) and 1 (high water mark)
	 */
	public void SetWaterLevel ( float level ) {
		if (level < 0) SetWaterLevel (0);
		if (level > 1) SetWaterLevel (1);
		
		float y = MINIMUM_WATER_Y + level * (MAXIMUM_WATER_Y - MINIMUM_WATER_Y);
		y += initialWaterY; // Adjust relative to initial water position
		
		GameObject water = GameObject.Find("Waterline");
		
		if (water != null) {
		    //Debug.Log("Setting " + level + " becomes " + y + " from " + water.transform.position.y);	
			y = y - water.transform.position.y;
			water.transform.Translate(new Vector3(0,y,0));
		}		
	}

    /**
	 * Get the current water level, as a value between 0 and 1
	 */
	public float GetWaterLevel ( ) {
		return waterLevel;
	}
	
	/**
	 * Find the entry to any bridges accessible from this point 
	 */
	public Vector3[] FindBridgeEntrances(Vector3 start) {
		GameObject[] bridges = GameObject.FindGameObjectsWithTag("Bridge");
		Vector3[] candidates = new Vector3[bridges.Length * 2];
		
		// First, identify bridge endpoints
		for (int i = 0; i < bridges.Length; i++) {
			Vector3 origin = bridges[i].transform.position;		
			float yRot = Mathf.PI * bridges[i].transform.eulerAngles.y / 180f;
			candidates[i*2  ] = VectorAt (origin.x + Mathf.Sin(yRot) * 100f,				
				origin.z + Mathf.Cos (yRot) * 100f);
			candidates[i*2+1] = VectorAt (origin.x - Mathf.Sin(yRot) * 100f,				
				origin.z - Mathf.Cos (yRot) * 100f);
		}
		
		// Check to see if you would have to cross water to get to endpoint
		bool[] crossesWater = new bool[candidates.Length];
		int count = 0;
		for (int i = 0; i < candidates.Length; i++) {
			crossesWater[i] = CrossesWater(start, candidates[i]);
			if (!crossesWater[i]) {
				count++;
			}			
		}
		
		// Prepare array of reachable candidates
		Vector3[] result = new Vector3[count];
		int j = 0;
		for (int i = 0; i < candidates.Length; i++) {
			if (!crossesWater[i]) {
				result[j++] = candidates[i];
			}
		}
		
		return result;
	}
	
	public bool CrossesWater(Vector3 start, Vector3 end) {
		Vector3 delta = (end - start) / Vector3.Distance(start, end);
		Vector3 current = start + delta;
		for (int i = 0 ;  i < ((int)Vector3.Distance(start, end)) ; i++) {
			if (IsWater(current.x, current.z)) {
				return true;
			}
			current = current + delta;
		}
		return false;
	}
	
	private Vector3 VectorAt(float x, float z) {
		return new Vector3(x, GetHeight(x,z), z);
	}
	
	/**
	 * Get the height of the terrain at a given x,z location
	 */
	public float GetHeight(float x, float z) {		
		Terrain t = (Terrain) GetComponent (typeof(Terrain));
		if (t != null) {
			return t.SampleHeight(new Vector3 (x, 0, z)) + t.transform.position.y;
		} else {
			return 0;
		}
	}
	
	/**
	 * Get a PlacementManager for use with the given type. A PlacementManager may 
	 * be used to get appropriate locations for adding objects to the scene.
	 */
	public PlacementManager GetPlacementManager(System.Type t) {		
		return new TypedPlacementManager(this, t, GetPlacementManager(t.Name));
	}
	
	public PlacementManager GetPlacementManager(string name) {
		if (name.ToLower().EndsWith("plant")) {
			if (plantPlacementManager == null) {
				plantPlacementManager = new PlantPlacementManager(this);
			}
			return plantPlacementManager;
		} else if (name.ToLower().EndsWith("wildfire")) {
			if (wildfirePlacementManager == null) {
				wildfirePlacementManager = new WildfirePlacementManager(this, initialWaterY + IDEAL_WILDFIRE_Y);
			}
			return wildfirePlacementManager;
		} else {
			if (defaultPlacementManager == null) {
				defaultPlacementManager = new DefaultPlacementManager(this);
			}
			return defaultPlacementManager;
		}
	}
	
	/**
	 * Determine if the given x,z location on the map is water
	 */
	public bool IsWater(float x, float z) {
		return IsUnderwater (x, GetHeight(x,z), z);
	}
	
	/**
	 * Check if a given point is under water.
	 * 
	 * NOTE: To provide consistent results, this compares to the highest water 
	 * level possible, not the current water level.
	 */
	public bool IsUnderwater (float x, float y, float z) {
		return y < (initialWaterY + MAXIMUM_WATER_Y);
	}	
	
	/**
	 * Check if a given point is under water.
	 * 
	 * NOTE: To provide consistent results, this compares to the highest water 
	 * level possible, not the current water level.
	 */
	public bool IsUnderwater(Vector3 v) {
		return IsUnderwater (v.x, v.y, v.z);
	}

	private void ComputeWaterProximity() {
		for (int u = 0; u < PROXIMITY_RESOLUTION; u++) {
			for (int v = 0; v < PROXIMITY_RESOLUTION; v++) {
				waterProximity[u,v] = PROXIMITY_RESOLUTION * PROXIMITY_RESOLUTION; 
			}
		}
		for (int u = 0; u < PROXIMITY_RESOLUTION; u++) {
			for (int v = 0; v < PROXIMITY_RESOLUTION; v++) {
				if (IsWater(FromIndex (u), FromIndex (v))) {
					waterProximity[u,v] = 0f;	
					for (int uu = 0; uu < PROXIMITY_RESOLUTION; uu++) {
						for (int vv = 0; vv < PROXIMITY_RESOLUTION; vv++) {
							float distSq = (uu-u)*(uu-u) + (vv-v)*(vv-v);
							if (waterProximity[uu,vv] > distSq) {
								waterProximity[uu,vv] = distSq;
							}
						}
					}
				}
			}
		}
	}
	
	private float GetWaterProximity(Vector3 v) {
		return waterProximity[ToIndex (v.x), ToIndex(v.z)];		
	}
	
	private float FromIndex(int index) {
			return ((float) index / (float) PROXIMITY_RESOLUTION) * 2000f - 999.5f;
		}
		
	private int ToIndex(float coord) {
		int index = (int) (((coord + 999.5f) / 2000f) * PROXIMITY_RESOLUTION);
		if (index < 0) index = 0;
		if (index > PROXIMITY_RESOLUTION - 1) index = PROXIMITY_RESOLUTION - 1;
		return index;
	}
	
	private class DefaultPlacementManager : PlacementManager {
		public TerrainController controller;
		
		public DefaultPlacementManager(TerrainController tc) {
			controller = tc;
		}
		
		public virtual Vector3 RequestPlacement() {
			return RequestPlacement (Random.value * 1990f - 995f, Random.value * 1990f - 995f);
		}
		public virtual Vector3 RequestPlacement(Vector3 suggested) {
			// First, move point around til it's not on water
			while (controller.IsWater(suggested.x, suggested.z)) {
				// We're on water, so we need to move
				suggested = new Vector3(suggested.x + (Random.value - 0.5f) * 5f,
					suggested.y, suggested.z + (Random.value - 0.5f) * 5f);
			}
			
			// Now, make sure it's not underground
			if (suggested.y < controller.GetHeight(suggested.x, suggested.z)){
				// Not on water, but underground - push up!
				suggested = new Vector3(suggested.x, 
					controller.GetHeight (suggested.x, suggested.z), suggested.z);
			}
			
			// At this point, we're confident the position is OK
			return suggested;			
		}
		public Vector3 RequestPlacement(float x, float y, float z) {
			return RequestPlacement (new Vector3(x,y,z));
		}
		public Vector3 RequestPlacement(float x, float z) {
			return RequestPlacement (x, controller.GetHeight(x, z), z);
		}
	}
	
	private abstract class PreferentialPlacementManager : DefaultPlacementManager {
		private int candidateCount;
		
		public PreferentialPlacementManager(TerrainController tc, int bias) : base(tc) {
			candidateCount = bias;
		}
		
		public override Vector3 RequestPlacement() {
			Vector3[] candidates = new Vector3[candidateCount];
			for (int i = 0; i < candidateCount; i++) candidates[i] = base.RequestPlacement();
			return ChooseBestPlacement ( base.RequestPlacement(), candidates );
		}
		
		private Vector3 ChooseBestPlacement(Vector3 a, Vector3[] others) {
			Vector3 best = a;
			foreach (Vector3 other in others) {
				if (assess(other) > assess(best)) {
					best = other;
				}
			}
			return best;
		}

		abstract protected float assess(Vector3 placement);
	}
	
	private class PlantPlacementManager : PreferentialPlacementManager {
		public PlantPlacementManager(TerrainController tc) : base(tc, 2) {
		}
		
		override protected float assess(Vector3 placement) {
			return 3000f - controller.GetWaterProximity(placement);
		}		
	}

	private class TypedPlacementManager : DefaultPlacementManager {
		private System.Type placementType;
		private PlacementManager delegateManager;
		private float minDistance;
		
		public TypedPlacementManager(TerrainController tc, System.Type t, PlacementManager d) : base(tc) {
			placementType = t;
			delegateManager = d;
			minDistance = 5.0f;
		}
		
		public override Vector3 RequestPlacement() {
			Vector3 suggested;
  			while (tooClose (suggested = delegateManager.RequestPlacement()));
			return suggested;
		}
		
		public override Vector3 RequestPlacement(Vector3 suggested) {
			suggested = delegateManager.RequestPlacement(suggested);
			int i = 0;
  			while (tooClose (suggested)) {
				suggested = delegateManager.RequestPlacement(wiggle (suggested));
				i++;
			}
			return suggested;
		}
		
		private Vector3 wiggle(Vector3 v) {
			return new Vector3(wiggle (v.x), v.y, wiggle (v.z));
		}
		
		private float wiggle(float x) {
			return x + (Random.value - 0.5f) * minDistance * 10.0f;
		}
		
		private bool tooClose(Vector3 suggested) {
			Object[] objs = GameObject.FindObjectsOfType(placementType);			
			foreach (Object obj in objs) {
				GameObject gameObj = obj as GameObject;
				if (gameObj == null && obj is MonoBehaviour) {
					gameObj = (obj as MonoBehaviour).gameObject;
				}
				if (gameObj != null) {
					if (Vector3.Distance(gameObj.transform.position, suggested) < minDistance) {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	private class WildfirePlacementManager : HeightBiasedPlacementManager {
		public WildfirePlacementManager(TerrainController tc, float idealHeight) : base(tc, idealHeight, 2) {
		}
		
		override protected float assess(Vector3 placement) {
			// Prefer to be far from water, and near a certain height
			float heightAssessment = base.assess(placement);			
			return controller.GetWaterProximity(placement) * heightAssessment * heightAssessment;
		}		
	}
	
	private class HeightBiasedPlacementManager : PreferentialPlacementManager {
		private float idealHeight;
		
		public HeightBiasedPlacementManager(TerrainController tc, float ideal, int bias) : base(tc, bias) {
			idealHeight = ideal;
		}

		override protected float assess(Vector3 placement) {
			return 1 / (1 + Mathf.Abs(placement.y - idealHeight));
		}		
	}
}


public interface PlacementManager {
	Vector3 RequestPlacement();
	Vector3 RequestPlacement(Vector3 suggested);
	Vector3 RequestPlacement(float x, float y, float z);
	Vector3 RequestPlacement(float x, float z);
}