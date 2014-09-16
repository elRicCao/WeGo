package vn.edu.hcmut.wego.service;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.utility.Security;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Service {

	/**
	 * Collection of possible result when signing up
	 * 
	 * @author elRic
	 */
	public enum SignUpResult {
		SUCCESS, EXIST_EMAIL, EXIST_PHONE
	}

	/**
	 * Send login information to server and receive confirmation
	 * 
	 * @param email
	 * @param password
	 * @return true if email and password are valid, otherwise return false
	 */
	public static boolean loginAuthentication(String email, String password) {
		JSONObject param = new JSONObject();

		try {
			param.put("email", email);
			param.put("password", Security.encode(password));

			JSONObject result = Server.execute("UserLogic", "selectUser", param);

			if (result.getString(Constant.RESULT).compareTo(Constant.EMPTY_RESULT) != 0)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
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
	 * Send register information to server and receive result
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @param phone
	 * @return SUCCESS if everything is fine, EXIST_EMAIL or EXIST_PHONE if
	 *         email or phone has already been used
	 */
	public static SignUpResult submitSignupInformation(String username, String email, String password, String phone) {
		if (checkEmailExist(email))
			return SignUpResult.EXIST_EMAIL;

		if (checkPhoneExist(phone))
			return SignUpResult.EXIST_PHONE;

		JSONObject param = new JSONObject();

		// TODO: Check again. Cannot insert to database.
		try {
			param.put("user", username);
			param.put("email", email);
			param.put("password", password);
			param.put("phone", phone);

			Server.execute("UserLogic", "insertNewUser", param);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return SignUpResult.SUCCESS;
	}

	/**
	 * Check whether email is already registered or not
	 * 
	 * @param email
	 * @return true if email is exist, otherwise return false
	 */
	public static boolean checkEmailExist(String email) {
		JSONObject param = new JSONObject();

		try {
			param.put("email", email);

			JSONObject result = Server.execute("UserLogic", "checkEmailExist", param);

			if (result.getString(Constant.RESULT).compareTo(Constant.EMPTY_RESULT) != 0)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Check whether phone is already registered or not
	 * 
	 * @param phone
	 * @return true if email is exist, otherwise return false
	 */
	public static boolean checkPhoneExist(String phone) {
		JSONObject param = new JSONObject();

		try {
			param.put("phone", phone);

			JSONObject result = Server.execute("UserLogic", "checkPhoneExist", param);

			if (result.getString(Constant.RESULT).compareTo(Constant.EMPTY_RESULT) != 0)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
}
