public class Constants {
	
	// Constants
	public static readonly string CLIENT_VERSION = "1.00";
	public static readonly string REMOTE_HOST = "localhost";
	public static readonly int REMOTE_PORT = 9252;
	public static readonly float HEARTBEAT_RATE = 1f;
	
	// Request (1xx) + Response (2xx)
	public static readonly short CMSG_AUTH = 101;
	public static readonly short SMSG_AUTH = 201;
	public static readonly short CMSG_REGISTER = 102;
	public static readonly short SMSG_REGISTER = 202;
	public static readonly short CMSG_WORLD_MENU_ACTION = 103;
	public static readonly short SMSG_WORLD_MENU_ACTION = 203;
	//	public static readonly short CMSG_WORLD_CREATE = 104;
	//	public static readonly short SMSG_WORLD_CREATE = 204;
	public static readonly short CMSG_AVATAR_CHANGE = 105;
	public static readonly short SMSG_AVATAR_CHANGE = 205;
	public static readonly short CMSG_START_GAME = 106;
	public static readonly short SMSG_START_GAME = 206;
	public static readonly short CMSG_CHAT = 107;
	public static readonly short SMSG_CHAT = 207;
	public static readonly short CMSG_HEARTBEAT = 108;
	public static readonly short SMSG_HEARTBEAT = 208;
	public static readonly short CMSG_SAVE_EXIT_GAME = 109;
	public static readonly short SMSG_SAVE_EXIT_GAME = 209;
	public static readonly short CMSG_READY = 110;
	public static readonly short SMSG_READY = 210;
	public static readonly short CMSG_GET_PLAYERS = 111;
	public static readonly short SMSG_GET_PLAYERS = 211;
	public static readonly short CMSG_SHOP = 112;
	public static readonly short SMSG_SHOP = 212;
	public static readonly short CMSG_SHOP_ACTION = 113;
	public static readonly short SMSG_SHOP_ACTION = 213;
	public static readonly short CMSG_WORLD_LIST = 114;
	public static readonly short SMSG_WORLD_LIST = 214;
	public static readonly short CMSG_CHANGE_PARAMETERS = 115;
	public static readonly short SMSG_CHANGE_PARAMETERS = 215;
	public static readonly short CMSG_LEAVE_WORLD = 116;
	public static readonly short SMSG_LEAVE_WORLD = 216;
	public static readonly short CMSG_STATISTICS = 117;
	public static readonly short SMSG_STATISTICS = 217;
	public static readonly short CMSG_PARAMS = 118;
	public static readonly short SMSG_PARAMS = 218;
	public static readonly short CMSG_RESTART = 119;
	public static readonly short SMSG_RESTART = 219;
	public static readonly short CMSG_CHANGE_FUNCTIONAL_PARAMETERS = 120;
	public static readonly short SMSG_CHANGE_FUNCTIONAL_PARAMETERS = 220;
	public static readonly short CMSG_HIGH_SCORE = 121;
	public static readonly short SMSG_HIGH_SCORE = 221;
	public static readonly short CMSG_GET_FUNCTIONAL_PARAMETERS = 122;
	public static readonly short SMSG_GET_FUNCTIONAL_PARAMETERS = 222;
	public static readonly short CMSG_CHART = 123;
	public static readonly short SMSG_CHART = 223;
	public static readonly short CMSG_ECOSYSTEM_TUTORIAL_COMPLETE = 131;
	public static readonly short SMSG_ECOSYSTEM_TUTORIAL_COMPLETE = 231;
	public static readonly short CMSG_BATTLE_TUTORIAL_COMPLETE = 132;
	public static readonly short SMSG_BATTLE_TUTORIAL_COMPLETE = 232;
	//    public static readonly short CMSG_DELETE_WORLD = 124;
	//    public static readonly short SMSG_DELETE_WORLD = 224;
	public static readonly short CMSG_ACTIVITY = 125;
	public static readonly short SMSG_ACTIVITY = 225;
	public static readonly short CMSG_SPECIES_LIST = 126;
	public static readonly short SMSG_SPECIES_LIST = 226;
	public static readonly short CMSG_SPECIES_ACTION = 127;
	public static readonly short SMSG_SPECIES_ACTION = 227;
	public static readonly short CMSG_AVATAR_LIST = 128;
	public static readonly short SMSG_AVATAR_LIST = 228;
	public static readonly short CMSG_BADGE_LIST = 129;
	public static readonly short SMSG_BADGE_LIST = 229;
	public static readonly short CMSG_ERROR_LOG = 130;
	public static readonly short SMSG_ERROR_LOG = 230;
	public static readonly short CMSG_PREDICTION = 131;
	public static readonly short SMSG_PREDICTION = 231;
	public static readonly short CMSG_TUTORIAL_CHALLENGE_SPECIES = 140;
	public static readonly short SMSG_TUTORIAL_CHALLENGE_SPECIES = 240;
	public static readonly short CMSG_TUTORIAL_CHALLENGE = 143;
	public static readonly short SMSG_TUTORIAL_CHALLENGE = 243;
	public static readonly short CMSG_TUTORIAL_REMOVE_SPECIES = 142;
	public static readonly short SMSG_TUTORIAL_REMOVE_SPECIES = 242;
	public static readonly short CMSG_TUTORIAL_DATA = 150;
	public static readonly short SMSG_TUTORIAL_DATA = 250;
	public static readonly short CMSG_TUTORIAL_UPDATE_CUR_TUT = 151;
	public static readonly short SMSG_TUTORIAL_UPDATE_CUR_TUT = 251;

	
	// Response Only (3xx)
	public static readonly short SMSG_BIRTH_ANIMAL = 301;
	public static readonly short SMSG_BIRTH_PLANT = 302;
	public static readonly short SMSG_UPDATE_RESOURCES = 303;
	public static readonly short SMSG_UPDATE_ANIMAL_BIOMASS = 304;
	public static readonly short SMSG_UPDATE_PLANT_BIOMASS = 305;
	public static readonly short SMSG_UPDATE_ANIMAL_NO_WATER_COUNT = 306;
	public static readonly short SMSG_UPDATE_PLANT_NO_WATER_COUNT = 307;
	public static readonly short SMSG_UPDATE_PLANT_NO_LIGHT_COUNT = 308;
	public static readonly short SMSG_SPECIES_KILL = 309;
	public static readonly short SMSG_UPDATE_WATER_SOURCE = 310;
	public static readonly short SMSG_UPDATE_TIME = 311;
	public static readonly short SMSG_WEATHER_PREDICTION = 312;
	public static readonly short SMSG_SPECIES_CREATE = 313;
	public static readonly short SMSG_OBJECTIVE_ACTION = 314;
	public static readonly short SMSG_UPDATE_ENV_SCORE = 315;
	public static readonly short SMSG_UPDATE_LEVEL = 316;
	public static readonly short SMSG_DRINK_WATER = 317;
	public static readonly short SMSG_CREATE_ENV = 318;
	public static readonly short SMSG_TARGET_REWARD = 319;
	public static readonly short SMSG_BADGE_UPDATE = 320;
	
	// Other
	public static readonly string IMAGE_RESOURCES_PATH = "Images/";
	public static readonly string PREFAB_RESOURCES_PATH = "Prefabs/";
	public static readonly string TEXTURE_RESOURCES_PATH = "Textures/";
	
	// GUI Window IDs
	public enum GUI_ID {
		Login,
		Register,
		World_Menu,
		Chat,
		Tutorial,
		Stats,
		Shop,
		Species_Viewer,
		Menu
	};
	
	public static int unique_id = 1000;
	
	public static int GetUniqueID() {
		return unique_id++;
	}
	
	public static int USER_ID = -1;
	
	public static int MONTH_DURATION = 180;
}