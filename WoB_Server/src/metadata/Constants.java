package metadata;

/**
 * The Constants class stores important variables as constants for later use.
 */
public class Constants {

    // Request (1xx) + Response (2xx)
    public final static short CMSG_AUTH = 101;
    public final static short SMSG_AUTH = 201;
    public final static short CMSG_REGISTER = 102;
    public final static short SMSG_REGISTER = 202;
    public final static short CMSG_WORLD_MENU_ACTION = 103;
    public final static short SMSG_WORLD_MENU_ACTION = 203;
//    public final static short CMSG_CREATE_NEW_WORLD = 104;
//    public final static short SMSG_CREATE_NEW_WORLD = 204;
    public final static short CMSG_CHANGE_AVATAR = 105;
    public final static short SMSG_CHANGE_AVATAR = 205;
    public final static short CMSG_START_GAME = 106;
    public final static short SMSG_START_GAME = 206;
    public final static short CMSG_CHAT = 107;
    public final static short SMSG_CHAT = 207;
    public final static short CMSG_HEARTBEAT = 108;
    public final static short SMSG_HEARTBEAT = 208;
    public final static short CMSG_SAVE_EXIT_GAME = 109;
    public final static short SMSG_SAVE_EXIT_GAME = 209;
    public final static short CMSG_READY = 110;
    public final static short SMSG_READY = 210;
    public final static short CMSG_GET_PLAYERS = 111;
    public final static short SMSG_GET_PLAYERS = 211;
    public final static short CSMG_SHOP = 112;
    public final static short SMSG_SHOP = 212;
    public final static short CSMG_SHOP_ACTION = 113;
    public final static short SMSG_SHOP_ACTION = 213;
    public final static short CMSG_WORLD_LIST = 114;
    public final static short SMSG_WORLD_LIST = 214;
    public final static short CMSG_CHANGE_PARAMETERS = 115;
    public final static short SMSG_CHANGE_PARAMETERS = 215;
    public final static short CMSG_LEAVE_WORLD = 116;
    public final static short SMSG_LEAVE_WORLD = 216;
    public final static short CMSG_STATISTICS = 117;
    public final static short SMSG_STATISTICS = 217;
    public final static short CMSG_PARAMS = 118;
    public final static short SMSG_PARAMS = 218;
    public final static short CMSG_RESTART = 119;
    public final static short SMSG_RESTART = 219;
    public final static short CMSG_CHANGE_FUNCTIONAL_PARAMETERS = 120;
    public final static short SMSG_CHANGE_FUNCTIONAL_PARAMETERS = 220;
    public final static short CMSG_HIGH_SCORE = 121;
    public final static short SMSG_HIGH_SCORE = 221;
    public final static short CMSG_GET_FUNCTIONAL_PARAMETERS = 122;
    public final static short SMSG_GET_FUNCTIONAL_PARAMETERS = 222;
    public final static short CMSG_CHART = 123;
    public final static short SMSG_CHART = 223;
//    public final static short CMSG_DELETE_WORLD = 124;
//    public final static short SMSG_DELETE_WORLD = 224;
    public final static short CMSG_ACTIVITY = 125;
    public final static short SMSG_ACTIVITY = 225;
    public final static short CMSG_SPECIES_LIST = 126;
    public final static short SMSG_SPECIES_LIST = 226;
    public final static short CMSG_SPECIES_ACTION = 127;
    public final static short SMSG_SPECIES_ACTION = 227;
    public final static short CMSG_AVATAR_LIST = 128;
    public final static short SMSG_AVATAR_LIST = 228;
    public final static short CMSG_BADGE_LIST = 129;
    public final static short SMSG_BADGE_LIST = 229;
    public final static short CMSG_ERROR_LOG = 130;
    public final static short SMSG_ERROR_LOG = 230;
    public final static short CMSG_PREDICTION = 131;
    public final static short SMSG_PREDICTION = 231;
    public final static short CMSG_TUTORIAL_CHALLENGE_SPECIES = 140;
    public final static short SMSG_TUTORIAL_CHALLENGE_SPECIES = 240;
    
    public final static short CMSG_TUTORIAL_REMOVE_SPECIES = 142;
    public final static short SMSG_TUTORIAL_REMOVE_SPECIES = 242;
    
    public final static short CMSG_TUTORIAL_CHALLENGE = 143;
    public final static short SMSG_TUTORIAL_CHALLENGE = 243;
    
    public final static short CMSG_TUTORIAL_DATA = 150;
    public final static short SMSG_TUTORIAL_DATA = 250;
    
    public final static short CMSG_TUTORIAL_UPDATE_CUR_TUT = 151;
    public final static short SMSG_TUTORIAL_UPDATE_CUR_TUT = 251;
    
    // Response Only (3xx)
    public final static short SMSG_BIRTH_ANIMAL = 301;
    public final static short SMSG_BIRTH_PLANT = 302;
    public final static short SMSG_UPDATE_RESOURCES = 303;
    public final static short SMSG_UPDATE_ANIMAL_BIOMASS = 304;
    public final static short SMSG_UPDATE_PLANT_BIOMASS = 305;
    public final static short SMSG_UPDATE_ANIMAL_NO_WATER_COUNT = 306;
    public final static short SMSG_UPDATE_PLANT_NO_WATER_COUNT = 307;
    public final static short SMSG_UPDATE_PLANT_NO_LIGHT_COUNT = 308;
    public final static short SMSG_SPECIES_KILL = 309;
    public final static short SMSG_UPDATE_WATER_SOURCE = 310;
    public final static short SMSG_UPDATE_TIME = 311;
    public final static short SMSG_WEATHER_PREDICTION = 312;
    public final static short SMSG_SPECIES_CREATE = 313;
    public final static short SMSG_OBJECTIVE_ACTION = 314;
    public final static short SMSG_UPDATE_ENV_SCORE = 315;
    public final static short SMSG_UPDATE_LEVEL = 316;
    public final static short SMSG_DRINK_WATER = 317;
    public final static short SMSG_CREATE_ENV = 318;
    public final static short SMSG_TARGET_REWARD = 319;
    public final static short SMSG_BADGE_UPDATE = 320;
    

    // Game Types
    public static final short GAME_TYPE_PVE = 0;
    public static final short GAME_TYPE_PVP = 1;
    // Privacy Type
    public static final short PRIVACY_TYPE_PRIVATE = 0;
    public static final short PRIVACY_TYPE_PUBLIC = 1;
    // Diet Type
    public static final short DIET_TYPE_OMNIVORE = 0;
    public static final short DIET_TYPE_CARNIVORE = 1;
    public static final short DIET_TYPE_HERBIVORE = 2;
    // Avatar Type
    public static final short AVATAR_TYPE_PLANTER = 1;
    public static final short AVATAR_TYPE_BREEDER = 2;
    public static final short AVATAR_TYPE_WEATHER_MAN = 3;
    // Organism Type
    public static final short ORGANISM_TYPE_ANIMAL = 0;
    public static final short ORGANISM_TYPE_PLANT = 1;
    // Parameter Type
    public static final short PARAMETER_K = 0;	//Plants Carrying capacity >0
    public static final short PARAMETER_R = 1;	//Plants Growth rate 0-1
    public static final short PARAMETER_X = 2;	//Plants Metabolic rate 0-1
    public static final short PARAMETER_X_A = 3;	//Animals
    public static final short PARAMETER_E = 4; //Animals assimilationEfficiency
    public static final short PARAMETER_D = 5; //Animals predatorInterference
    public static final short PARAMETER_Q = 6; //Animals functionalResponseControl
    public static final short PARAMETER_A = 7; //Animals relativeHalfSaturationDensity
    // Create Organism Status
    public static final short CREATE_STATUS_DEFAULT = 0;
    public static final short CREATE_STATUS_BIRTH = 1;
    public static final short CREATE_STATUS_PURCHASE = 2;
    // Remove Organism Status
    public static final short REMOVE_STATUS_DEFAULT = 0;
    public static final short REMOVE_STATUS_DEATH = 1;
    // Activity Type
    public static final short ACTIVITY_MOUSE = 0;
    // Game Resource Type
    public static final short RESOURCE_XP = 0;
    public static final short RESOURCE_COINS = 1;
    public static final short RESOURCE_CREDITS = 2;
    public static final short RESOURCE_ENV_SCORE = 3;
    // Game Constants
    public static final int INITIAL_COINS = 100;
    public static final int MAX_COINS = 1000;
    public static final int INITIAL_CREDITS = 1500;
    public static final int MAX_CREDITS = 1000000;
    public static final int MAX_LEVEL = 10;
    public static final int STARTING_NEEDED_EXP = 1000;
    public static final float MULTIPLIER_EXP = 1f;    
    // Other
    public static final float TIME_MODIFIER = 1f;
    public static final int SAVE_INTERVAL = 60000;
    public static final int SHOP_PROCESS_DELAY = 0;//20000;
    public static final float BIOMASS_SCALE = 1000;
    public static final String CLIENT_VERSION = "1.00";
    public static final int TIMEOUT_SECONDS = 90;
    public static final int MONTH_DURATION = 180;
    public static final int MAX_SPECIES_SIZE = 10;
    public static final short CREATE_USER = 1;
    public static final short CREATE_SYSTEM = 2;
    public static final String CSV_SAVE_PATH = "src/log/";
}
