package vn.edu.hcmut.wego.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.User;

/**
 * Services collection used to authenticate user login and register
 */
public class AuthenticationService {

	/**
	 * PHP file name on server
	 */
	private static final String PHP_AUTHENTICATION = "AuthenticationLogic";

	/**
	 * Collection of possible result when signing up
	 */
	public enum SignUpResult {
		SUCCESS, EXIST_EMAIL, EXIST_PHONE
	}

	/**
	 * Send login information to server and receive confirmation
	 * 
	 * @param email
	 * @param encryptedPassword
	 * @return userId if login succeed, otherwise return -1
	 */
	public static User loginAuthentication(String email, String encryptedPassword) {
		JSONObject param = new JSONObject();

		try {
			param.put("email", email);
			param.put("password", encryptedPassword);

			JSONObject result = Server.execute(PHP_AUTHENTICATION, "selectUser", param);

			JSONObject userInfo = result.getJSONArray(Constant.RESULT).getJSONObject(0);

			if (result.getInt(Constant.SUCCESS) == 1) {
				User user = new User();
				user.setId(Integer.parseInt(userInfo.getString("id")));
				user.setName(userInfo.getString("name"));
				user.setEmail(userInfo.getString("email"));
				user.setPhone(userInfo.getString("phone"));
				return user;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Send register information to server and receive result
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @param phone
	 * @return SUCCESS if everything is fine, EXIST_EMAIL or EXIST_PHONE if email or phone has already been used
	 */
	public static SignUpResult submitSignupInformation(String username, String email, String password, String phone) {
		if (checkEmailExist(email))
			return SignUpResult.EXIST_EMAIL;

		if (checkPhoneExist(phone))
			return SignUpResult.EXIST_PHONE;

		JSONObject param = new JSONObject();

		try {
			param.put("user", username);
			param.put("email", email);
			param.put("password", password);
			param.put("phone", phone);

			Server.execute(PHP_AUTHENTICATION, "insertNewUser", param);
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

			JSONObject result = Server.execute(PHP_AUTHENTICATION, "checkEmailExist", param);

			if (result.getInt(Constant.SUCCESS) == 1)
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

			JSONObject result = Server.execute(PHP_AUTHENTICATION, "checkPhoneExist", param);

			if (result.getInt(Constant.SUCCESS) == 1)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

}
