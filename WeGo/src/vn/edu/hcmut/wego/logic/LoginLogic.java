package vn.edu.hcmut.wego.logic;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.service.Server;
import vn.edu.hcmut.wego.utility.Security;

public class LoginLogic {

	/**
	 * Send login information to server and receive confirmation
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
			
			if(result.getString(Constant.RESULT).compareTo(Constant.EMPTY_RESULT) != 0)
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
		try{
			URL url = new URL("http://www.google.com");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.connect();
			if (con.getResponseCode() == 200)return true;
			else return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
