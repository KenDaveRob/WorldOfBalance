using UnityEngine;

using System.Collections;
using System.Collections.Generic;

public class TutorialShopPanel : MonoBehaviour {

  private Vector2 scrollPosition = Vector2.zero;
  private TutorialShop shop;
  private List<SpeciesData> itemList;
  
  // Use this for initialization
  void Start () {
    shop = transform.root.gameObject.GetComponent<TutorialShop>();
  }
  
  // Update is called once per frame
  void Update () {

  }
  
  public void MakeWindow() {
    itemList = new List<SpeciesData>(shop.itemList.Values);
    itemList.Sort(ComparisonTypes.SortByTrophicLevels);

    int height = 20 + (itemList.Count + 6) / 6 * 130;

    scrollPosition = GUI.BeginScrollView(new Rect(5, 70, 570, 300), scrollPosition, new Rect(0, 0, 300, height));
    GUI.Box(new Rect(0, 0, 555, height), "");

    for (int i = 0; i < itemList.Count; i++) {
      SpeciesData species = itemList[i];

      GUI.BeginGroup(new Rect(10 + i % 6 * 90, 20 + i / 6 * 130, 160, 160));
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

          Texture texture = Resources.Load("white") as Texture;
          GUI.DrawTexture(new Rect(0, 0, 80, 105), texture);
          GUI.color = Color.white;

          if (GUI.Button(new Rect (0, 0, 80, 105), "")) {
            SelectSpecies(species);
          }
          GUI.backgroundColor = Color.white;

          GUI.DrawTexture(new Rect(10, 25, 60, 55), species.image);

          GUIStyle style = new GUIStyle(GUI.skin.button);
          style.alignment = TextAnchor.MiddleLeft;
          style.normal.textColor = Color.white;
          style.normal.background = null;

          GUI.Label(new Rect(5, -3, 70, 30), species.name, style);

          if(gameObject.GetComponent<Tutorial>().currentTutorialCredits - 
           gameObject.GetComponent<TutorialShopCartPanel>().totalCost -
           species.cost < 0 && gameObject.GetComponent<Tutorial>().currentlyOnChallenge) {
            GUI.Label(new Rect(5, 80, 70, 30), "<color=#ff0000ff>$" + species.cost.ToString() + "</color>", style);
            } else {
              GUI.Label(new Rect(5, 80, 70, 30), "$" + species.cost.ToString(), style);
            }

            GUI.EndGroup ();
          }
          GUI.EndScrollView();
        }

        public void SelectSpecies(SpeciesData species) {
          if (shop.selectedSpecies != null && species.species_id == shop.selectedSpecies.species_id &&
              (gameObject.GetComponent<Tutorial>().currentTutorialCredits - gameObject.GetComponent<TutorialShopCartPanel>().totalCost -species.cost >= 0) && 
              gameObject.GetComponent<Tutorial>().currentlyOnChallenge) {

            transform.root.gameObject.GetComponent<TutorialShopCartPanel>().Add(species);

          } else if (shop.selectedSpecies != null && species.species_id == shop.selectedSpecies.species_id &&
            !gameObject.GetComponent<Tutorial>().currentlyOnChallenge) {
            transform.root.gameObject.GetComponent<TutorialShopCartPanel>().Add(species);
          }

          shop.selectedSpecies = species;
        }
      }
