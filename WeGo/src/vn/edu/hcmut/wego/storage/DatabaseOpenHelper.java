package vn.edu.hcmut.wego.storage;

import java.util.ArrayList;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.InviteRequest;
import vn.edu.hcmut.wego.entity.InviteRequest.Type;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public enum SelectType {
		BRIEF, DETAIL
	};

	private static final int TRUE = 1;
	private static final int FALSE = 0;

	private static final String TABLE_USER = "user";
	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_USER_EMAIL = "user_email";
	private static final String KEY_USER_PHONE = "user_phone";
	private static final String KEY_USER_STATUS = "user_status";
	private static final String KEY_USER_IS_FRIEND = "user_is_friend";
	private static final String KEY_USER_IS_FOLLOW = "user_is_follow";

	private static final String TABLE_OFFER = "offer";
	private static final String KEY_OFFER_ID = "offer_id";
	private static final String KEY_OFFER_SENDER_ID = "offer_sender_id";
	private static final String KEY_OFFER_RECEIVER_ID = "offer_receiver_id";
	private static final String KEY_OFFER_IS_FRIEND_REQUEST = "offer_is_friend_request";
	private static final String KEY_OFFER_IS_GROUP_REQUEST = "offer_is_group_request";
	private static final String KEY_OFFER_IS_GROUP_INVITE = "offer_is_group_invite";

	private static final String TABLE_NEWS = "news";
	private static final String KEY_NEWS_ID = "news_id";
	private static final String KEY_NEWS_OWNER_ID = "news_owner_id";
	private static final String KEY_NEWS_TIME = "news_time";
	private static final String KEY_NEWS_CONTENT = "news_content";
	private static final String KEY_NEWS_NUM_OF_LIKES = "news_num_of_likes";
	private static final String KEY_NEWS_NUM_OF_COMMENTS = "news_num_of_comment";

	private static final String TABLE_LOCATION = "location";
	private static final String KEY_LOCATION_ID = "location_id";
	private static final String KEY_LOCATION_NAME = "location_name";
	private static final String KEY_LOCATION_DESCRIPTION = "location_description";
	private static final String KEY_LOCATION_LONGTITUDE = "location_longtitude";
	private static final String KEY_LOCATION_LATITUDE = "location_latitude";

	private Context context;

	public DatabaseOpenHelper(Context context) {
		super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
		this.context = context;

		// String check = (String) Commons.getValueFromSharedPreferences(context, Constant.PREFS_IS_DATABASE_CREATED, String.class);
		// if (check == null)
		// addFakeData();
	}

	/* @formatter:off */
	@Override
	public void onCreate(SQLiteDatabase database) {
		
		// Table User: temporary store brief info of anonymous user
		String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
												+ KEY_USER_ID + " INTEGER PRIMARY KEY, "
												+ KEY_USER_NAME + " TEXT NOT NULL, "
												+ KEY_USER_EMAIL + " TEXT, "
												+ KEY_USER_PHONE + " TEXT, "
												+ KEY_USER_STATUS + " TEXT, "
												+ KEY_USER_IS_FRIEND + " INTEGER NOT NULL, "
												+ KEY_USER_IS_FOLLOW + " INTEGER NOT NULL"
												+ ");";
		
		// Table Offer: store offer from user, group
		String CREATE_TABLE_OFFER = "CREATE TABLE " + TABLE_OFFER + " ("
												+ KEY_OFFER_ID + " INTEGER PRIMARY KEY, "
												+ KEY_OFFER_SENDER_ID + " INTEGER NOT NULL, "
												+ KEY_OFFER_RECEIVER_ID + " INTEGER NOT NULL, "
												+ KEY_OFFER_IS_FRIEND_REQUEST + " INTEGER NOT NULL, "
												+ KEY_OFFER_IS_GROUP_REQUEST + " INTEGER NOT NULL, "
												+ KEY_OFFER_IS_GROUP_INVITE + " INTEGER NOT NULL"
												+ ");";
		
		// Table News: store news item feed
		String CREATE_TABLE_NEWS = "CREATE TABLE " + TABLE_NEWS + " ("
												+ KEY_NEWS_ID + " INTEGER PRIMARY KEY, "
												+ KEY_NEWS_OWNER_ID + " INTEGER NOT NULL, "
												+ KEY_NEWS_TIME + " TEXT NOT NULL, "
												+ KEY_NEWS_CONTENT + " TEXT NOT NULL, "
												+ KEY_NEWS_NUM_OF_LIKES + " INTEGER NOT NULL, "
												+ KEY_NEWS_NUM_OF_COMMENTS + " INTEGER NOT NULL"
												+ ");";
		
		// Table Location: store administrative location as city, town, province
		String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOCATION + " ("
												+ KEY_LOCATION_ID + " INTEGER PRIMARY KEY, "
												+ KEY_LOCATION_NAME + " TEXT NOT NULL, "
												+ KEY_LOCATION_DESCRIPTION + " TEXT NOT NULL, "
												+ KEY_LOCATION_LONGTITUDE + " REAL NOT NULL, "
												+ KEY_LOCATION_LATITUDE + " REAL NOT NULL"
												+ ");";
		
		// Table Trip: store trip information of current user
		
		// Table TripMember: store trip members info
		
		// Table Follow: store follow information of current user
		
		// Table Message: store messages of current user
		
		// Table Landmark: store landmark of trip which current user is planning to travel
		database.execSQL(CREATE_TABLE_USER);
		database.execSQL(CREATE_TABLE_OFFER);
		database.execSQL(CREATE_TABLE_NEWS);
		database.execSQL(CREATE_TABLE_LOCATION);
		
		Utils.putValueToSharedPreferences(context, Constant.PREFS_IS_DATABASE_CREATED, "true");
	}
	/* @formatter:on */

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
		onCreate(database);
	}

	/**
	 * Select user from database
	 * 
	 * @param userId
	 * @return User
	 */
	public User selectUser(int userId, SelectType type) {

		SQLiteDatabase database = getReadableDatabase();

		String[] columns = (type == SelectType.BRIEF) ? new String[] { KEY_USER_ID, KEY_USER_NAME, KEY_USER_STATUS } : null;

		Cursor cursor = database.query(TABLE_USER, columns, KEY_USER_ID + " = ?", new String[] { String.valueOf(userId) }, null, null, null);

		if (cursor.moveToFirst()) {
			User user = new User();
			if (type == SelectType.BRIEF) {
				user.setId(cursor.getInt(0));
				user.setName(cursor.getString(1));
				// user.setStatus(cursor.getString(2));
			} else {
				user.setId(cursor.getInt(0));
				user.setName(cursor.getString(1));
				user.setEmail(cursor.getString(2));
				user.setPhone(cursor.getString(3));
				// user.setStatus(cursor.getString(4));
			}
			return user;
		}
		return null;
	}

	/**
	 * Insert user info to database
	 * 
	 * @param user
	 */
	// public void insertUser(User user, UserType type) {
	// SQLiteDatabase database = this.getWritableDatabase();
	//
	// ContentValues info = new ContentValues();
	//
	// info.put(KEY_USER_ID, user.getId());
	// info.put(KEY_USER_NAME, user.getName());
	// info.put(KEY_USER_EMAIL, user.getEmail());
	// info.put(KEY_USER_PHONE, user.getPhone());
	// info.put(KEY_USER_STATUS, user.getStatus());
	//
	// if (type == UserType.FRIEND) {
	// info.put(KEY_USER_IS_FRIEND, TRUE);
	// info.put(KEY_USER_IS_FOLLOW, FALSE);
	// } else if (type == UserType.FOLLOW) {
	// info.put(KEY_USER_IS_FRIEND, FALSE);
	// info.put(KEY_USER_IS_FOLLOW, TRUE);
	// }
	// else {
	// info.put(KEY_USER_IS_FRIEND, FALSE);
	// info.put(KEY_USER_IS_FOLLOW, FALSE);
	// }
	//
	// database.insert(TABLE_USER, null, info);
	// database.close();
	// }

	/**
	 * Insert offer to database
	 * 
	 * @param offer
	 */
	public void insertOffer(InviteRequest offer) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues info = new ContentValues();

		info.put(KEY_OFFER_ID, offer.getId());
		info.put(KEY_OFFER_SENDER_ID, offer.getSenderId());
		info.put(KEY_OFFER_RECEIVER_ID, offer.getReceiverId());

		switch (offer.getType()) {
		case FRIEND_REQUEST:
			info.put(KEY_OFFER_IS_FRIEND_REQUEST, TRUE);
			info.put(KEY_OFFER_IS_GROUP_REQUEST, FALSE);
			info.put(KEY_OFFER_IS_GROUP_INVITE, FALSE);
			break;

		case GROUP_REQUEST:
			info.put(KEY_OFFER_IS_FRIEND_REQUEST, FALSE);
			info.put(KEY_OFFER_IS_GROUP_REQUEST, TRUE);
			info.put(KEY_OFFER_IS_GROUP_INVITE, FALSE);
			break;

		case GROUP_INVITE:
			info.put(KEY_OFFER_IS_FRIEND_REQUEST, FALSE);
			info.put(KEY_OFFER_IS_GROUP_REQUEST, FALSE);
			info.put(KEY_OFFER_IS_GROUP_INVITE, TRUE);
			break;
		}

		database.insert(TABLE_OFFER, null, info);
		database.close();
	}

	// public void insertNews(News news) {
	// SQLiteDatabase database = this.getWritableDatabase();
	//
	// ContentValues info = new ContentValues();
	//
	// SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATABASE_DATE_FORMAT, Locale.getDefault());
	//
	// info.put(KEY_NEWS_ID, news.getId());
	// info.put(KEY_NEWS_OWNER_ID, news.getOwnerId());
	// info.put(KEY_NEWS_TIME, dateFormat.format(news.getTime()));
	// info.put(KEY_NEWS_CONTENT, news.getContent());
	// info.put(KEY_NEWS_NUM_OF_LIKES, news.getNumOfLikes());
	// info.put(KEY_NEWS_NUM_OF_COMMENTS, news.getNumOfComments());
	//
	// database.insert(TABLE_NEWS, null, info);
	// database.close();
	// }

	public void deleteOffer(InviteRequest offer) {
		SQLiteDatabase database = this.getWritableDatabase();

		database.delete(TABLE_OFFER, KEY_OFFER_ID + " = ?", new String[] { String.valueOf(offer.getId()) });

		database.close();
	}

	/**
	 * Make this user as friend
	 * 
	 * @param user
	 */
	public void makeFriend(User user) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USER_IS_FRIEND, TRUE);

		database.update(TABLE_USER, values, KEY_USER_ID + " = ?", new String[] { String.valueOf(user.getId()) });

		database.close();
	}

	/**
	 * Get brief info of all friends from database
	 * 
	 * @return friends list
	 */
	public ArrayList<User> selectAllFriends() {
		ArrayList<User> friends = new ArrayList<User>();

		SQLiteDatabase database = getReadableDatabase();

		String[] columns = new String[] { KEY_USER_ID, KEY_USER_NAME, KEY_USER_STATUS };
		String selection = KEY_USER_IS_FRIEND + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(TRUE) };

		Cursor cursor = database.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				User friend = new User();
				friend.setId(cursor.getInt(0));
				friend.setName(cursor.getString(1));
				// friend.setStatus(cursor.getString(2));
				friends.add(friend);
			} while (cursor.moveToNext());
		}

		return friends;
	}

	/**
	 * Get all offer from specific sender
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public ArrayList<InviteRequest> selectAllOffers(int userId, InviteRequest.Type type, boolean isSent) {

		ArrayList<InviteRequest> offers = new ArrayList<InviteRequest>();

		SQLiteDatabase database = getReadableDatabase();

		// Identify type of offer
		String keyType = (type == Type.FRIEND_REQUEST) ? KEY_OFFER_IS_FRIEND_REQUEST : (type == Type.GROUP_REQUEST) ? KEY_OFFER_IS_GROUP_REQUEST : KEY_OFFER_IS_GROUP_INVITE;

		// Identify offer is sent or received
		String keySenderOrReceiver = (isSent) ? KEY_OFFER_SENDER_ID : KEY_OFFER_RECEIVER_ID;

		// Make selection
		String selection = keySenderOrReceiver + " = ? " + " AND " + keyType + " = ?";

		// Make arguments for selections
		String[] selectionArgs = new String[] { String.valueOf(userId), String.valueOf(TRUE) };

		// Query
		Cursor cursor = database.query(TABLE_OFFER, null, selection, selectionArgs, null, null, null);

		// Create objects to return
		if (cursor.moveToFirst()) {
			do {
				InviteRequest offer = new InviteRequest();
				offer.setId(cursor.getInt(0));
				offer.setSenderId(cursor.getInt(1));
				offer.setReceiverId(cursor.getInt(2));
				offer.setType(type);
				offers.add(offer);
			} while (cursor.moveToNext());
		}

		return offers;
	}

	/**
	 * Select all news from database
	 * 
	 * @return
	 */
	// public ArrayList<News> selectAllNews() {
	//
	// ArrayList<News> news = new ArrayList<News>();
	//
	// SQLiteDatabase database = getReadableDatabase();
	//
	// Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NEWS, null);
	//
	// SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATABASE_DATE_FORMAT, Locale.getDefault());
	//
	// try {
	// if (cursor.moveToFirst()) {
	// do {
	// News item = new News();
	// item.setId(cursor.getInt(0));
	// item.setOwnerId(cursor.getInt(1));
	// item.setTime(dateFormat.parse(cursor.getString(2)));
	// item.setContent(cursor.getString(3));
	// item.setNumOfLikes(cursor.getInt(4));
	// item.setNumOfComments(cursor.getInt(5));
	// news.add(item);
	// } while (cursor.moveToNext());
	// }
	// } catch (ParseException e) {
	// Log.i("Debug", e.toString());
	// }
	// return news;
	// }

	// TODO: use to debug
	// private void addFakeData() {
	//
	// User user = new User();
	//
	// user.setId(2);
	// user.setName("Mai Hữu Nhân");
	// user.setEmail("nhan@gmail.com");
	// user.setPhone("01682404175");
	// user.setStatus("Online");
	// insertUser(user, UserType.FRIEND);
	//
	// user.setId(3);
	// user.setName("Phan Trần Việt");
	// user.setEmail("viet@gmail.com");
	// user.setPhone("0167123456");
	// user.setStatus("Offline");
	// insertUser(user, UserType.FRIEND);
	//
	// user.setId(1);
	// user.setName("elRic");
	// user.setEmail("duy@gmail.com");
	// user.setPhone("01677774447");
	// user.setStatus("Something");
	// insertUser(user, UserType.NORMAL);
	//
	// user.setId(4);
	// user.setName("Nguyễn Hoài Lâm");
	// user.setEmail("lam@gmail.com");
	// user.setPhone("0164655667");
	// user.setStatus("Moving to Nha Trang");
	// insertUser(user, UserType.NORMAL);
	//
	// user.setId(5);
	// user.setName("Trần Đường Tú");
	// user.setEmail("tu@gmail.com");
	// user.setPhone("0123456789");
	// user.setStatus("Feeling sick");
	// insertUser(user, UserType.NORMAL);
	//
	// user.setId(6);
	// user.setName("Nguyễn Huỳnh Như Ý");
	// user.setEmail("superBo@gmail.com");
	// user.setPhone("0123456789");
	// user.setStatus("Feeling sick");
	// insertUser(user, UserType.NORMAL);
	//
	// Offer offer = new Offer();
	// offer.setId(1);
	// offer.setSenderId(1);
	// offer.setReceiverId(4);
	// offer.setType(Type.FRIEND_REQUEST);
	// insertOffer(offer);
	//
	// offer.setId(2);
	// offer.setSenderId(5);
	// offer.setReceiverId(1);
	// offer.setType(Type.FRIEND_REQUEST);
	// insertOffer(offer);
	//
	// offer.setId(3);
	// offer.setSenderId(6);
	// offer.setReceiverId(1);
	// offer.setType(Type.FRIEND_REQUEST);
	// insertOffer(offer);
	//
	// News news = new News();
	// news.setId(1);
	// news.setOwnerId(1);
	// news.setTime(new Date());
	// news.setContent("Hello World!");
	// news.setNumOfLikes(2);
	// news.setNumOfComments(3);
	// insertNews(news);
	//
	// news.setId(2);
	// news.setOwnerId(2);
	// news.setTime(new Date());
	// news.setContent("Hello World! 2");
	// news.setNumOfLikes(2);
	// news.setNumOfComments(3);
	// insertNews(news);
	//
	// news.setId(3);
	// news.setOwnerId(3);
	// news.setTime(new Date());
	// news.setContent("Hello World! 3");
	// news.setNumOfLikes(2);
	// news.setNumOfComments(3);
	// insertNews(news);
	//
	// news.setId(4);
	// news.setOwnerId(4);
	// news.setTime(new Date());
	// news.setContent("Hello World! 4");
	// news.setNumOfLikes(2);
	// news.setNumOfComments(3);
	// insertNews(news);
	//
	// news.setId(5);
	// news.setOwnerId(5);
	// news.setTime(new Date());
	// news.setContent("Hello World! 5");
	// news.setNumOfLikes(2);
	// news.setNumOfComments(3);
	// insertNews(news);
	// }

}
