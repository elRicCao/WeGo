package vn.edu.hcmut.wego.logic;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.service.Server;

public class SignUpLogic {
	
	// TODO: Add more result if needed
	/**
	 * Collection of possible result when signing up
	 * @author elRic
	 *
	 */
	public enum SignUpResult {SUCCESS, EXIST_EMAIL, EXIST_PHONE}

	// TODO: Send info to server and return result, remember to encode password before sent
	/**
	 * Send register information to server and receive result
	 * @param username
	 * @param email
	 * @param password
	 * @param phone
	 * @return SUCCESS if everything is fine, EXIST_EMAIL or EXIST_PHONE if email or phone has already been used
	 */
	public static SignUpResult submitSignupInformation(String username, String email, String password, String phone) {
		if(checkEmailExist(email))
			return SignUpResult.EXIST_EMAIL;
		
		if(checkPhoneExist(phone))
			return SignUpResult.EXIST_PHONE;
		
		JSONObject param = new JSONObject();

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
