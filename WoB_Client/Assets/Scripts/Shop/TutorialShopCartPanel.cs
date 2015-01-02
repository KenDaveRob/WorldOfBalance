using UnityEngine;

using System.Collections;
using System.Collections.Generic;

public class TutorialShopCartPanel : MonoBehaviour {

  private GameObject mainObject;
  private Vector2 scrollPosition = Vector2.zero;
  private TutorialShop shop;
  public Dictionary<int, int> cartList { get; set; }
  public ProgressBar biomassMeter;
  public int totalBiomass;
  public bool resetSpecies { get; set; }
  public int tutorialCreditsBeforePurchase { get; set; }
  public int totalCost = 0;

  void Awake () {
    mainObject = GameObject.Find("MainObject");
    mainObject.GetComponent<MessageQueue>().AddCallback(Constants.SMSG_SHOP_ACTION, ResponseShopAction);
  }
  
  // Use this for initialization
  void Start () {
    shop = GetComponent<TutorialShop>();
    cartList = new Dictionary<int, int>();
  }
  
  // Update is called once per frame
  void Update () {

  }

  public void ResetCartList() {
    cartList = new Dictionary<int, int>();
  }
  
  public void MakeWindow() {
    GUI.Label(new Rect(580, 30, 200, 30), "<b>Biomass Capacity:</b> ");
    if(gameObject.GetComponent<Tutorial>().currentlyOnChallenge) {
      GUI.Label(new Rect(580, 45, 200, 30), "<b>Remaining Credits:</b> ");
      GUI.Label(new Rect(580, 60, 200, 30), "<b>Total Cost:</b> ");
    }
    int height = 20 + cartList.Count * 90;

    totalBiomass = 0;

    scrollPosition = GUI.BeginScrollView(new Rect(580, 80, 200, 460), scrollPosition, new Rect(0, 0, 100, height));
    GUI.Box(new Rect(0, 0, 555, Mathf.Max(500, height)), "");

    List<int> items = new List<int>(cartList.Keys);

    for (int i = 0; i < cartList.Count; i++) {
      int species_id = items[i];
      SpeciesData species = shop.itemList[species_id];
      
      totalBiomass += cartList[species_id];

      GUI.BeginGroup (new Rect(10, 20 + i * 90, 160, 160));
      if (shop.selectedSpecies != null) {
        if (shop.selectedSpecies.species_id == species.species_id) {
          GUI.backgroundColor = Color.black;
          GUI.color = Color.yellow;
          } else if (shop.selectedSpecies.predatorList.ContainsKey(species.species_id)) {
            GUI.backgroundColor = Color.red;
            GUI.color = Color.red;
            } else if (shop.selectedSpecies.preyList.ContainsKey(species.species_id)) {
              GUI.backgroundColor = Color.green;
              GUI.color = Color.green;
            }
          }

          if (GUI.Button(new Rect(0, 0, 160, 80), "")) {
          }
          GUI.backgroundColor = Color.white;

          GUI.DrawTexture(new Rect(10, 10, 60, 60), species.image);

          {
            GUIStyle style = new GUIStyle(GUI.skin.label);
            style.alignment = TextAnchor.MiddleLeft;
            style.normal.textColor = Color.white;
            style.normal.background = null;
            style.wordWrap = false;

            GUI.Label(new Rect(75, 10, 70, 30), species.name, style);
          }

          {
            GUIStyle style = new GUIStyle(GUI.skin.label);
            style.alignment = TextAnchor.UpperLeft;
            
            GUI.Label(new Rect(75, 40, 100, 30), "Total B: " + cartList[species_id].ToString(), style);
          }
          GUI.EndGroup();
        }
        GUI.EndScrollView();

        GUI.Label(new Rect(700, 30, 200, 200), "<color=#00ff00ff>" + totalBiomass.ToString() + "</color>");

        if(gameObject.GetComponent<Tutorial>().currentlyOnChallenge) {
          GUI.Label(new Rect(705, 45, 200, 200), "<color=#00ff00ff>$" + tutorialCreditsBeforePurchase.ToString() + "</color>");
          GUI.Label(new Rect(695, 60, 200, 200), "<color=#00ff00ff>$" + totalCost.ToString() + "</color>");
        }

        {
          GUIStyle style = new GUIStyle(GUI.skin.label);
          style.alignment = TextAnchor.UpperCenter;

          //GUI.Label(new Rect(625, 555, 100, 100), cartList.Count.ToString() + " / 10", style);
        }

        {
          GUIStyle style = new GUIStyle(GUI.skin.button);
          style.alignment = TextAnchor.MiddleCenter;
          style.normal.textColor = Color.white;
          GUI.color = Color.white;

          if (GUI.Button(new Rect(700, 550, 80, 30), "Finish", style)) {

            ConnectionManager cManager = GameObject.Find("MainObject").GetComponent<ConnectionManager>();
            
            if (cManager) {
              cManager.Send(RequestShopAction(0, cartList));
            }
            foreach (int species_id in cartList.Keys) {
              GetComponent<GameState>().CreateSpecies(species_id, SpeciesTable.speciesList[species_id].name, "Animal", cartList[species_id]);
            }

            gameObject.GetComponent<Tutorial>().currentTutorialCredits -= totalCost;


            totalCost = 0;
            tutorialCreditsBeforePurchase = gameObject.GetComponent<Tutorial>().currentTutorialCredits;
            cartList = new Dictionary<int, int>();
            shop.isHidden = true;
          }

          if (GUI.Button(new Rect(600, 550, 80, 30), "Reset Cart", style)) {
            ConnectionManager cManager = GameObject.Find("MainObject").GetComponent<ConnectionManager>();
            
            ResetCartList();
            totalCost = 0;
            tutorialCreditsBeforePurchase = gameObject.GetComponent<Tutorial>().currentTutorialCredits;
          }
        }
      }
      
      public void Add(SpeciesData species) {
        if (!cartList.ContainsKey(species.species_id)) {
          cartList.Add(species.species_id, 0);
        }

        cartList[species.species_id] = cartList[species.species_id] + species.biomass;
        totalCost += species.cost;
        tutorialCreditsBeforePurchase -= species.cost;
      }

      public RequestShopAction RequestShopAction(short action, Dictionary<int, int> cartList) {
        RequestShopAction request = new RequestShopAction();
        request.Send(action, cartList);
        resetSpecies = false;
        
        return request;
      }
      
      public void ResponseShopAction(ExtendedEventArgs eventArgs) {
        if (resetSpecies) {
          gameObject.GetComponent<Tutorial>().resetSpecies();
          resetSpecies = false;
        }
        ResponseShopActionEventArgs args = eventArgs as ResponseShopActionEventArgs;
      }
    }
