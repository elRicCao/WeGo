package vn.edu.hcmut.wego.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.LoginActivity;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.News.NewsType;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.RatingBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;

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
	
	public static String getUserName(Context context) {
		return (String) getValueFromSharedPreferences(context, Constant.PREFS_USER_NAME, String.class);
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

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it doesn't, display a dialog that allows users to download the APK from the Google Play Store or enable it in the device's
	 * system settings.
	 */
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	public static boolean checkPlayServices(Activity activity) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				activity.finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing registration ID.
	 */
	public static String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	public static SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(LoginActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	public static void setUpRatingBar(Context context, RatingBar ratingBar) {
		LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
		layerDrawable.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
		layerDrawable.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
	}
}
