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

}
