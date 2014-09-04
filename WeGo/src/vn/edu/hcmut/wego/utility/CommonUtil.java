package vn.edu.hcmut.wego.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommonUtil {
	public static JSONObject getJSONObject(JSONArray jArray, String name) throws JSONException {
		JSONObject result = new JSONObject();
		JSONArray tmp = new JSONArray();
		for(int i = 0; i < jArray.length(); i++) {
			if(jArray.getJSONObject(i).has(name)) {
				tmp.put(jArray.getJSONObject(i).get(name));
			}
		}
		result.put(name, tmp);
		return result;
	}
}
