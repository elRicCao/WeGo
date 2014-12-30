package vn.edu.hcmut.wego.constant;

public class Constant {

	// Database parameters
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "WeGoDB.db";
	public static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static final String INTENT_USER_ID = "user_id";
	public static final String INTENT_GROUP_ID = "group_id";

	// Shared preferences
	public static final String PREFS_NAME = "WeGoFile";
	public static final String PREFS_USER_ID = "user_id";
	public static final String PREFS_USER_NAME = "user_name";
	public static final String PREFS_USER_EMAIL = "user_email";
	public static final String PREFS_IS_DATABASE_CREATED = "database_created";

	public static final String SUCCESS = "success";
	public static final String MESSAGE = "message";
	public static final String RESULT = "result";
	public static final String FILE = "file";
	public static final String FUNCTION = "function";
	public static final String PARAM = "param";
	public static final String EMPTY_RESULT = "[]";
	public static final String QUERY_URL = "http://wego.journeymanager.esy.es/db_server.php";

	// Server service params
	public static final String PARAM_USER_ID = "user";
	
	public static final String GCM_SENDER_ID = "803536658867";
	
	public static final String GET_USER_LOCATION = "GetLocation";
	public static final String RECEIVE_USER_LOCATION = "ReceiveLocation";
	public static final String WARNNING = "Warnning";
	public static final String FUEL = "Fuel";
	public static final String VISIT = "Visit";
	public static final String RELAX = "Relax";
	public static final String GROUP = "Group";
	public static final String TRIP = "Trip";
	public static final String USER = "User";
	
	public static final String WARNING_WAIT_LOST = "Lost";
	public static final String WARNING_WAIT_VEHICLE = "Vehicle";
	public static final String WARNING_WAIT_GAS = "Gas";
	public static final String WARNING_POLICE_ALERT = "Alert";
	public static final String WARNING_POLICE_CAPTURE = "Capture";
	public static final String WARNING_REGROUP = "Regroup";
	public static final String WARNING_ACCIDENT = "Accident";
	
	public static final String BROADCAST_LOCATION_ACTION = "vn.edu.hcmut.wego.RECEIVE_USER_LOCATION";
}