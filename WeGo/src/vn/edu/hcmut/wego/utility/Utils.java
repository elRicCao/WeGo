package vn.edu.hcmut.wego.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.News.NewsType;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	/**
	 * TODO: Description
	 * 
	 * @param jArray
	 * @param name
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getJSONObject(JSONArray jArray, String name) throws JSONException {
		JSONObject result = new JSONObject();
		JSONArray tmp = new JSONArray();
		for (int i = 0; i < jArray.length(); i++) {
			if (jArray.getJSONObject(i).has(name)) {
				tmp.put(jArray.getJSONObject(i).get(name));
			}
		}
		result.put(name, tmp);
		return result;
	}

	/**
	 * Check network status
	 * 
	 * @return true if Internet connected, otherwise return false
	 */
	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}

	/**
	 * Get value from Shared Preferences
	 * 
	 * @param context
	 * @param key
	 * @param cls
	 * @return value corresponding to key and class
	 */
	public static Object getValueFromSharedPreferences(Context context, String key, Class<?> cls) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
		if (cls == Integer.class)
			return sharedPreferences.getInt(key, 0);
		return sharedPreferences.getString(key, null);
	}

	/**
	 * Set value to Shared Preferences
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putValueToSharedPreferences(Context context, String key, Object value) {
		SharedPreferences preferences = context.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		if (value.getClass() == Integer.class)
			editor.putInt(key, (Integer) value);
		else
			editor.putString(key, (String) value);
		editor.commit();
	}

	/**
	 * Get user id saved in shared preferences
	 * 
	 * @param context
	 * @return
	 */
	public static int getUserId(Context context) {
		return (Integer) getValueFromSharedPreferences(context, Constant.PREFS_USER_ID, Integer.class);
	}

	public static String makeHtmlTextBold(String text) {
		return "<b>" + text + "</b>";
	}

	/**
	 * Get action text of owner from resources
	 * 
	 * @param type
	 * @return action text
	 */
	public static String getOwnerActionText(Context context, NewsType type) {
		String actionText = new String();
		switch (type) {
		case POST:
		case COMMENT_POST:
		case LIKE_POST:
			actionText = context.getResources().getString(R.string.item_news_user_action_write_status);
			break;

		case PHOTO:
		case COMMENT_PHOTO:
		case LIKE_PHOTO:
			actionText = context.getResources().getString(R.string.item_news_user_action_post_photo);
			break;

		case REVIEW:
		case LIKE_REVIEW:
			actionText = context.getResources().getString(R.string.item_news_user_action_write_review);
			break;
		default:
			break;
		}
		return actionText;
	}

	/**
	 * Get action text of actor from resources
	 * 
	 * @param type
	 * @return action text
	 */
	public static String getActionText(Context context, NewsType type) {
		String actionText = new String();
		switch (type) {
		case COMMENT_POST:
			actionText = context.getResources().getString(R.string.item_news_actor_action_comment_post);
			break;
		case LIKE_POST:
			actionText = context.getResources().getString(R.string.item_news_actor_action_like_post);
			break;
		case COMMENT_PHOTO:
			actionText = context.getResources().getString(R.string.item_news_actor_action_comment_photo);
			break;
		case LIKE_PHOTO:
			actionText = context.getResources().getString(R.string.item_news_actor_action_like_photo);
			break;
		case LIKE_REVIEW:
			actionText = context.getResources().getString(R.string.item_news_actor_action_like_review);
			break;
		default:
			break;
		}
		return actionText;
	}

	public static String getString(Context context, int id) {
		return context.getResources().getString(id);
	}

	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d 'at' h:ma", Locale.US);
		return dateFormat.format(date).replace("AM", "am").replace("PM", "pm");
	}

	public static boolean isDeviceSupportCamera(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// This device has a camera
			return true;
		} else {
			// No camera on this device
			return false;
		}
	}
}
