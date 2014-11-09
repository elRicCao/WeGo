package vn.edu.hcmut.wego.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {

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
}
