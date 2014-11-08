package vn.edu.hcmut.wego.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import android.util.Log;

class Utils {

	private static final String PHP_AUTHENTICATION = "AuthenticationLogic";

	private static final String LOGIN_FUNCTION = "selectUser";

	/**
	 * Make HTTP request to server
	 * 
	 * @param requestType
	 * @param params
	 * @return
	 */
	static JSONObject makeHttpRequest(RequestType requestType, JSONObject params) {
		
		// Request method is POST
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(Constant.QUERY_URL);
		JSONObject paramsPost = new JSONObject();

		InputStream inputStream = null;
		JSONObject jsonObject = null;
		String jsonString = "";

		// Indentify php file and function
		String file = requestFile(requestType);
		String function = requestFunction(requestType);

		// Making HTTP request
		try {
			paramsPost.put("file", file);
			paramsPost.put("function", function);
			paramsPost.put("param", params);

			JSONArray jsonPost = new JSONArray();
			jsonPost.put(paramsPost);

			httpPost.setHeader("json", paramsPost.toString());
			httpPost.getParams().setParameter("jsonpost", jsonPost);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Reading server's response
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			inputStream.close();
			jsonString = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// Parse JSON string to a JSON object
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jsonObject;
	}

	/**
	 * Identify php file
	 * 
	 * @param requestType
	 * @return
	 */
	private static String requestFile(RequestType requestType) {
		switch (requestType) {
		case LOGIN:
			return PHP_AUTHENTICATION;
		default:
			return null;
		}
	}

	/**
	 * Identify php function
	 * 
	 * @param requestType
	 * @return
	 */
	private static String requestFunction(RequestType requestType) {
		switch (requestType) {
		case LOGIN:
			return LOGIN_FUNCTION;
		default:
			return null;
		}
	}

	/**
	 * Parse JSONObject result to Object
	 * 
	 * @param requestType
	 * @param result
	 * @return
	 */
	static ArrayList<Object> parseResult(RequestType requestType, JSONObject result) {

		ArrayList<Object> objects = new ArrayList<Object>();
		Log.i("Debug", result.toString());
		try {
			switch (requestType) {

			case LOGIN:
				
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONObject userInfo = result.getJSONArray(Constant.RESULT).getJSONObject(0);
					User user = new User();
					user.setId(Integer.parseInt(userInfo.getString("id")));
					user.setName(userInfo.getString("name"));
					user.setEmail(userInfo.getString("email"));
					user.setPhone(userInfo.getString("phone"));
					objects.add(user);
				}
				break;

			default:
				break;
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("Debug", "Parse result size: " + String.valueOf(objects.size()));
		return objects;
	}
}
