public class Strings 
{
	public static readonly string[] ecosystemTutorialStrings =
	{
		//BASICS
		//0
		"<color=#ffff00ff>Welcome to World of Balance!</color> In this unique game,<color=#ffff00ff> the goal is to create and maintain a balanced ecosystem.</color> " +
		"To do so you will<color=#ffff00ff> need to carefully pick animals for your ecosystem</color> , and <color=#ffff00ff> battle surrounding players for competing resources.</color>  " +
		"This tutorial will teach you the basics thatyou need to know. So lets get started!",

		//1
		"Your first lesson is camera and movement.  <color=#ffff00ff>To move the camera</color> around use the arrow keys.  " +
		"<color=#ffff00ff>[Up_Arrow]</color> and <color=#ffff00ff>[Down_Arraow] </color>move the camera up and down. " +
		"<color=#ffff00ff>[Left_Arrow] </color>and <color=#ffff00ff>[Right_Arrow] </color> will move the camera left and right. Give it a try now!",

		//2
		"At some point you might want to<color=#ffff00ff> zoom in</color> and view your ecosystem. Give it a try with the <color=#ffff00ff>[Mouse_Scroll_Wheel]</color>!",

		//3
		"<color=#ffff00ff>To select an object</color> you can use the <color=#ffff00ff>[Left_Mouse_Button]</color> at any time. Select an object on the map now!",

		//4
		//Not nextable until player selects a node on the edge
		"Now Lets use what we learned. <color=#ffff00ff>Move the camera to the highlighted node (hexagon)</color>. Then <color=#ffff00ff>select the node</color> with <color=#ffff00ff>[Left_Mouse_Click], and zoom in</color>.",

		//ECOSYSTEM
		//5
		//Not nextable
		"<color=#ffff00ff>The ecosystem</color> is <color=#ffff00ff>the fundamental part of WOB</color>. Your goal is to <color=#ffff00ff>maintain a healthy ecosystem.</color>  " +
		"Your Biomass represents how many points you have to spend on species to populate your ecosystem. " +
		"Your overall Environmental score represents how healthy your systems are. " +
		"Since 1% of your total Environmental score gets translated into Biomass Points, be sure to keep your Environment healthy!"+
		"\n\n"+
		"<color=#ffff00ff>Time progresses in 5 minute intervals</color>. Every 5 minutes whether you are online or off, your ecosystem will update. " +
		"Current points will be updated and your simulation time will pass by 3 months."+
		"\n\n"+
		"Now lets try to build our own ecosystem! <color=#ffff00ff>Lets start with 400 Environmental points and 50 biomass points...</color>",

		//SHOP
		//6
		//need more documentation to fill this out
		"You will <color=#ffff00ff>notice that the shop has 6 categories</color>.  Each category represents a vital component to your ecosystem. ",

		//7
		//Not nextable
		//Challenge 1
		"Obtain an environment <color=#ffff00ff>score of 3000</color>" + 
		" and a biomass of at least <color=#ffff00ff>8000</color>" + 
		" with atleast <color=#ffff00ff>2 species</color> within" + 
		" <color=#ffff00ff>6</color> days.",

		//8
		//Not nextable
		//Challenge 2
		"Obtain an environment <color=#ffff00ff>score of 13500</color>" + 
		" and a biomass of at least <color=#ffff00ff>8000</color>" + 
		" with atleast <color=#ffff00ff>3 species</color> within" + 
		" <color=#ffff00ff>10</color> days.",

		//9
		//Not nextable
		//Challenge 3
		"Obtain an environment <color=#ffff00ff>score of 20000</color>" + 
		" and a biomass of at least <color=#ffff00ff>9000</color>" + 
		" with atleast <color=#ffff00ff>5 species</color> within" + 
		" <color=#ffff00ff>20</color> days.",

		//10
			"Obtain an environment <color=#ffff00ff>score of 20000</color>" + 
		" and a biomass of at least <color=#ffff00ff>6000</color>" + 
		" with atleast <color=#ffff00ff>6 species</color> within" + 
		" <color=#ffff00ff>40</color> days.",

		//11
		"<size=32><color=#ffff00ff>Congratulations</color></size> you now know the basics of manipulation the game world and controlling an ecosystem!"+
		"\n\n"+
		"You can now move on to the next tutorial.  Click next to move on to the battle tutorial."
	};

	public static readonly string[] tutorialTitles =  {

		//0
		"<color=#ffff00ff><b>Welcome!</b></color>",

		//1
		"<color=#ffff00ff><b>Camera Manipulation</b></color>",

		//2
		"<color=#ffff00ff><b>Camera Manipulation</b></color>",

		//3
		"<color=#ffff00ff><b>Interface Manipulation</b></color>",

		//4
		"<color=#ffff00ff><b>Interface Manipulation</b></color>",

		//5
		"<color=#ffff00ff><b>Ecosystem Essentials</b></color>",

		//6
		"<color=#ffff00ff><b>Shop Essentials</b></color>",

		//7
		"<color=#ffff00ff><b>Challenge 1</b></color>",

		//8
		"<color=#ffff00ff><b>Challange 2</b></color>",

		//9
		"<color=#ffff00ff><b>Challange 3</b></color>",

		//10
		"<color=#ffff00ff><b>Challange 4</b></color>",

		//11
		"<color=#ffff00ff><b>Congratulations!</b></color>"

	};

	public static readonly string[] battleTutorialStrings = 
	{
		"This tutorial will guide you through conducting battles."
	};

	public static readonly bool[] ecosystemTutorialStringIsNextable =
	{
		true,
		true,
		true,
		true,
		false,
		true,
		false,
		false,
		false,
		false,
		false //Extra code will have to be implemented to transfer the tutorial to the battle tutorial.
	};

}
