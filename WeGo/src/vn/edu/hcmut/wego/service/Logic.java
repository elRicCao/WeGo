package vn.edu.hcmut.wego.service;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.utility.JSONParser;
import android.os.AsyncTask;

public class Logic {
	private static JSONParser jParser = new JSONParser();

	public static JSONObject execute(String file, String method, JSONObject params) {
		JSONObject param = new JSONObject();
		
		try {		
			param.put("file", file);
			param.put("function", method);
			param.put("param", params);
			
			DoServiceASYNC async = new DoServiceASYNC();
			async.execute(param);
			return async.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Background Async Task to execute sql query by making HTTP Request
	 * */
	private static class DoServiceASYNC extends
			AsyncTask<JSONObject, Void, JSONObject> {
		protected JSONObject doInBackground(JSONObject... params) {
			String file = null;
			String method = null;
			JSONObject param = new JSONObject();
			
			try {
				file = params[0].getString("file");
				method = params[0].getString("function");
				param = params[0].getJSONObject("param");
			} catch (JSONException e) {
				e.printStackTrace();
			}
						
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(file,
					method, param);
			return json;
		}
	}
}