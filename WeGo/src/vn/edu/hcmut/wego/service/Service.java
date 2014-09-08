package vn.edu.hcmut.wego.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.utility.CommonUtil;

public class Service {

	// TODO: Debug again. Always return true.
	/**
	 * Send login information to server and receive confirmation
	 * @param email
	 * @param password
	 * @return true if email and password are valid, otherwise return false
	 */
	public static boolean loginAuthentication(String email, String password) {
		// Viet test commit
		String query = "Select * from Users where Email='" + email + "' and Password='" + password + "'";
		JSONArray result = Logic.execute(query);
		try {
			JSONObject success = CommonUtil.getJSONObject(result, Constant.SUCCESS);
			if(success.get(Constant.SUCCESS).toString().contains("1"))
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//TODO: Check network status
	/**
	 * Check network status
	 * @return true if Internet connected, otherwise return false
	 */
	public static boolean checkInternetConnection() {
		return true;
	}
}
