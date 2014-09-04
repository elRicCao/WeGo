package vn.edu.hcmut.wego.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;

import android.os.AsyncTask;

public class Logic {
	private static JSONParser jParser = new JSONParser();

	public static JSONArray execute(String query) {
		String param = query;

		try {
			DoServiceASYNC async = new DoServiceASYNC();
			async.execute(param);
			return async.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Background Async Task to execute sql query by making HTTP Request
	 * */
	private static class DoServiceASYNC extends AsyncTask<String, Void, JSONArray> {
		protected JSONArray doInBackground(String... params) {
			JSONArray result = null;
			String query = params[0];

			// Building Input Parameters
			List<NameValuePair> input = new ArrayList<NameValuePair>();
			setParameters(input, Constant.QUERY, query);

			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(Constant.QUERY_URL, "POST", input);

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(Constant.SUCCESS);

				if (success == 1 && json.has(Constant.RESULT)) {
					result = json.getJSONArray(Constant.RESULT);
				} else {
					result = new JSONArray();
					JSONObject tmp = new JSONObject();
					tmp.put(Constant.MESSAGE, json.get(Constant.MESSAGE));
					result.put(tmp);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	private static void setParameters(List<NameValuePair> input, String name, String value) {
		if (value != null && !value.isEmpty())
			input.add(new BasicNameValuePair(name, value));
	}
}