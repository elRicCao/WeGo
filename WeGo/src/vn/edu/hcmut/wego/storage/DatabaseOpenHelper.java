package vn.edu.hcmut.wego.storage;

import java.util.ArrayList;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final String TABLE_USER = "user";
	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_USER_EMAIL = "user_email";
	private static final String KEY_USER_PHONE = "user_phone";
	private static final String KEY_USER_STATUS = "user_status";
	private static final int INDEX_KEY_USER_ID = 0;
	private static final int INDEX_KEY_USER_NAME = 1;
	private static final int INDEX_KEY_USER_EMAIL = 2;
	private static final int INDEX_KEY_USER_PHONE = 3;
	private static final int INDEX_KEY_USER_STATUS = 4;

	private static final String TABLE_FRIEND = "friends";
	private static final String KEY_FRIEND_ID = "friend_id";
	private static final String KEY_FRIEND_NAME = "friend_name";
	private static final String KEY_FRIEND_EMAIL = "friend_email";
	private static final String KEY_FRIEND_PHONE = "friend_phone";
	private static final String KEY_FRIEND_STATUS = "friend_status";
	private static final int INDEX_KEY_FRIEND_ID = 0;
	private static final int INDEX_KEY_FRIEND_NAME = 1;
	private static final int INDEX_KEY_FRIEND_EMAIL = 2;
	private static final int INDEX_KEY_FRIEND_PHONE = 3;
	private static final int INDEX_KEY_FRIEND_STATUS = 4;

	public DatabaseOpenHelper(Context context) {
		super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
	}

	/* @formatter:off */
	@Override
	public void onCreate(SQLiteDatabase database) {
		
		// Table User: store info of current logging user
		String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
												+ KEY_USER_ID + " LONG PRIMARY KEY, "
												+ KEY_USER_NAME + " TEXT, "
												+ KEY_USER_EMAIL + " TEXT, "
												+ KEY_USER_PHONE + " TEXT, "
												+ KEY_USER_STATUS + " TEXT"
												+ ");";
		
		// Table Friend: store contact info of current logging user's friends
		String CREATE_TABLE_FRIEND = "CREATE TABLE " + TABLE_FRIEND + " ("
													+ KEY_FRIEND_ID + " LONG PRIMARY KEY, "
													+ KEY_FRIEND_NAME + " TEXT, "
													+ KEY_FRIEND_EMAIL + " TEXT, "
													+ KEY_FRIEND_PHONE + " TEXT, "
													+ KEY_FRIEND_STATUS + " TEXT"
													+ ");";
		
		// Table Trip: store trip information of current user
		
		// Table Follow: store follow information of current user
		
		// Table Message: store messages of current user
		
		// Table Landmark: store landmark of trip which current user is planning to travel
		
		database.execSQL(CREATE_TABLE_USER);
		database.execSQL(CREATE_TABLE_FRIEND);
	}
	/* @formatter:on */

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);

		onCreate(database);
	}
	
	
	public ArrayList<User> getAllFriends() {
		ArrayList<User> friends = new ArrayList<User>();

		SQLiteDatabase database = getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_FRIEND, null);

		if (cursor.moveToFirst()) {
			do {
				User friend = new User();
				friend.setId(cursor.getInt(INDEX_KEY_FRIEND_ID));
				friend.setName(cursor.getString(INDEX_KEY_FRIEND_NAME));
				friend.setEmail(cursor.getString(INDEX_KEY_FRIEND_EMAIL));
				friend.setPhone(cursor.getString(INDEX_KEY_FRIEND_PHONE));
				friend.setStatus(cursor.getString(INDEX_KEY_FRIEND_STATUS));
				friends.add(friend);
			} while (cursor.moveToNext());
		}

		return friends;
	}

	/**
	 * Get user from database
	 * 
	 * @return User
	 */
	public User getCurrentUserInfo() {

		SQLiteDatabase database = getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_USER, null);

		if (cursor.moveToFirst()) {
			User user = new User();
			user.setId(cursor.getLong(INDEX_KEY_USER_ID));
			user.setName(cursor.getString(INDEX_KEY_USER_NAME));
			user.setEmail(cursor.getString(INDEX_KEY_USER_EMAIL));
			user.setPhone(cursor.getString(INDEX_KEY_USER_PHONE));
			user.setStatus(cursor.getString(INDEX_KEY_USER_STATUS));
			return user;
		}
		return null;
	}

	/**
	 * Get id of current logging user
	 * 
	 * @return user id
	 */
	public long getCurrentUserId() {
		SQLiteDatabase database = getReadableDatabase();

		Cursor cursor = database.query(TABLE_USER, new String[] { KEY_USER_ID }, null, null, null, null, null);

		if (cursor.moveToFirst())
			return cursor.getLong(0);

		return -1;
	}

	/**
	 * Insert user information to database when logging in
	 * 
	 * @param user
	 */
	public void insertUser(User user) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues info = new ContentValues();
		info.put(KEY_USER_ID, user.getId());
		info.put(KEY_USER_NAME, user.getName());
		info.put(KEY_USER_EMAIL, user.getEmail());
		info.put(KEY_USER_PHONE, user.getPhone());
		info.put(KEY_USER_STATUS, user.getStatus());

		database.insert(TABLE_USER, null, info);
		database.close();
	}

	/**
	 * Insert friends info of current user after loading from server database This info is used for offline uses
	 * 
	 * @param friend
	 */
	public void insertFriend(User friend) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues info = new ContentValues();
		info.put(KEY_FRIEND_NAME, friend.getName());
		info.put(KEY_FRIEND_EMAIL, friend.getEmail());
		info.put(KEY_FRIEND_PHONE, friend.getPhone());
		info.put(KEY_FRIEND_STATUS, friend.getStatus());

		database.insert(TABLE_FRIEND, null, info);
		database.close();
	}

	// TODO: use to debug
	/*private void addFakeData() {
		Friend friend = new Friend();
		friend.setName("elRic");
		friend.setEmail("duy@gmail.com");
		friend.setPhone("01677774447");
		friend.setStatus("Moving to Vinh Long");
		insertFriend(friend);

		friend.setName("nhan");
		friend.setEmail("nhan@gmail.com");
		friend.setPhone("01682404175");
		friend.setStatus("Online");
		insertFriend(friend);

		friend.setName("Viet");
		friend.setEmail("viet@gmail.com");
		friend.setPhone("0167123456");
		friend.setStatus("Offline");
		insertFriend(friend);

		friend.setName("Lam");
		friend.setEmail("lam@gmail.com");
		friend.setPhone("0164655667");
		friend.setStatus("Moving to Nha Trang");
		insertFriend(friend);
	}*/
}
