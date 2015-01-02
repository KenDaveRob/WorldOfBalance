using UnityEngine;

using System.Collections;
using System.Collections.Generic;

public class TutorialShop : MonoBehaviour {

  private GameObject worldObject;
  
  public Dictionary<int, SpeciesData> itemList { get; set; }
  public SpeciesData selectedSpecies { get; set; }

  private GameObject mainObject;
  // Window Properties
  private float width = 800;
  private float height = 600;
  // Other
  private Rect windowRect;
  private Rect avatarRect;
  private Texture avatar;
  private Rect[] buttonRectList;
  private GameObject messageBox;
  private Vector2 scrollPosition = Vector2.zero;
  public bool isHidden { get; set; }

  public Dictionary<int, SpeciesData> entireSpeciesList { get; set; }
  
  void Awake() {
    mainObject = GameObject.Find("MainObject");
    buttonRectList = new Rect[3];
    
    itemList = new Dictionary<int, SpeciesData>();
    
    gameObject.AddComponent("TutorialShopPanel");
    gameObject.AddComponent("TutorialShopInfoPanel");
    gameObject.AddComponent("TutorialShopCartPanel");
    
    isHidden = true;
    mainObject.GetComponent<MessageQueue>().AddCallback(Constants.SMSG_SHOP, ResponseShop);
    mainObject.GetComponent<MessageQueue>().AddCallback(Constants.SMSG_SPECIES_LIST, ResponseSpeciesList);

  }

  // Use this for initialization
  void Start () {
    windowRect = new Rect (0, 0, width, height);
    windowRect.x = (Screen.width - windowRect.width) / 2;
    windowRect.y = (Screen.height - windowRect.height) / 2;

    worldObject = GameObject.Find("WorldObject");
    entireSpeciesList = new Dictionary<int, SpeciesData>();

    ConnectionManager cManager = mainObject.GetComponent<ConnectionManager>();
    
    if (cManager) {
      cManager.Send(RequestShop(1));
      cManager.Send(new RequestSpeciesList());
    }

  }
  
  // Update is called once per frame
  void Update () {

  }
  
  void OnGUI() {
    GUIStyle btnStyle = new GUIStyle(GUI.skin.button);
    btnStyle.font = (Font)Resources.Load("coopbl", typeof(Font));

    if (!isHidden) {
      GUI.backgroundColor = new Color(0.0f , 0.0f , 0.0f , 1.0f);
      windowRect = GUI.Window((int) Constants.GUI_ID.Shop, windowRect, MakeWindow, "Shop");
    }

    if (GUI.Button(new Rect(Screen.width - 120, Screen.height - 40, 100, 30), "Shop", btnStyle)) {
      isHidden = !isHidden;
    }
  }
  
  void MakeWindow(int id) {
    GUIStyle style = new GUIStyle(GUI.skin.label);
    style.fontSize = 18;

    GUI.Label(new Rect(width / 2 - 100, 30, 200, 50), "<b>Choose Your Species</b>", style);
    
    gameObject.GetComponent<TutorialShopPanel>().MakeWindow();
    gameObject.GetComponent<TutorialShopInfoPanel>().MakeWindow();
    gameObject.GetComponent<TutorialShopCartPanel>().MakeWindow();
    
    GUI.DragWindow();
  }

  /*
  public void Initialize(string[] config, int[] speciesList) {
    foreach (int species_id in speciesList) {
      SpeciesData species = new SpeciesData(SpeciesTable.speciesList[species_id]);

      species.image = Resources.Load(Constants.IMAGE_RESOURCES_PATH + species.name) as Texture;
      
      if (!itemList.ContainsKey(species_id)) {
        itemList.Add(species_id, species);
      }
    }
  }
  */

  public void Initialize(string[] config, int[] challenge_species) {
    itemList.Clear();

    //int[] speciesList = new int[SpeciesTable.speciesList.Count];
    int[] speciesList = new int[entireSpeciesList.Count];
    int i = 0;
    foreach (KeyValuePair<int, SpeciesData> s in entireSpeciesList) {
      speciesList[i++] = s.Key;
    }

    foreach (int species_id in speciesList) {
      if (System.Array.IndexOf(challenge_species, species_id) != -1) {
        SpeciesData species = new SpeciesData(entireSpeciesList[species_id]);

        species.image = Resources.Load(Constants.IMAGE_RESOURCES_PATH + species.name) as Texture;

        if (!itemList.ContainsKey(species_id)) {
          itemList.Add(species_id, species);
        }
      }
    }
  }


	public void InitializeAll()
	{
		itemList.Clear();
		
		//int[] speciesList = new int[SpeciesTable.speciesList.Count];
    int[] speciesList = new int[entireSpeciesList.Count];
		int i = 0;
		foreach (KeyValuePair<int, SpeciesData> s in entireSpeciesList) {
			speciesList[i++] = s.Key;
		}

		foreach (int species_id in speciesList) {

				//SpeciesData species = new SpeciesData(SpeciesTable.speciesList[species_id]);
        SpeciesData species = new SpeciesData(entireSpeciesList[species_id]);
				
				species.image = Resources.Load(Constants.IMAGE_RESOURCES_PATH + species.name) as Texture;
				
				if (!itemList.ContainsKey(species_id)) {
					itemList.Add(species_id, species);
				}

		}
	}



  public RequestShop RequestShop(short type) {
    RequestShop request = new RequestShop();
    request.Send(type);
    
    return request;
  }
  
  public void ResponseShop(ExtendedEventArgs eventArgs) {
    ResponseShopEventArgs args = eventArgs as ResponseShopEventArgs;
    
    //Initialize(args.config, args.speciesList);
  }

  public void ResponseSpeciesList(ExtendedEventArgs eventArgs) {
    ResponseSpeciesListEventArgs args = eventArgs as ResponseSpeciesListEventArgs;
    entireSpeciesList = args.speciesList;
  }
}
