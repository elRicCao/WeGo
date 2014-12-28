package vn.edu.hcmut.wego.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import android.util.Log;

class ServerUtils {

	private static final String PHP_AUTHENTICATION = "AuthenticationLogic";
	private static final String PHP_SOCIAL = "SocialLogic";

	private static final String LOGIN_FUNCTION = "selectUser";
	private static final String SIGNUP_FUNCTION = "insertNewUser";

	private static final String FETCH_NEWS_FEED_FUNCTION = "getNewsFeed";
	private static final String FETCH_USER_INFO_FUNCTION = "getUserInfo";
	private static final String FETCH_FRIEND_FUNCTION = "selectUserFriend";
	private static final String FETCH_FRIEND_REQUEST_FUNCTION = "getFriendRequest";
	private static final String FETCH_GROUP_LIST_FUNCTION = "selectUserGroup";
	private static final String FETCH_GROUP_INVITE_FUNCTION = "selectUserInviteGroup";
	private static final String FETCH_GROUP_INFO_FUNCTION = "getGroupInfo";
	private static final String FETCH_LAST_MESSAGE_FUNCTION = "getLastMessage";
	private static final String FETCH_TRIP_LIST_FUNCTION = "getTrip";
	private static final String FETCH_TOP_PLACE_FUNCTION = "getTopPlace";
	private static final String FETCH_TOP_PHOTO_FUNCTION = "getTopPhoto";
	private static final String FETCH_PLACE_INFO_FUNCTION = "getPlaceInfo";

	private static final String ACTION_POST_FUNCTION = "insertPost";
	private static final String ACTION_COMMENT_POST_FUNCTION = "commentPost";
	private static final String ACTION_LIKE_POST_FUNCTION = "likePost";
	private static final String ACTION_REVIEW_FUNCTION = "reviewPlace";
	private static final String ACTION_LIKE_REVIEW_FUNCTION = "likeReviewPlace";
	private static final String ACTION_PHOTO_FUNCTION = "insertPhoto";
	private static final String ACTION_COMMENT_PHOTO_FUNCTION = "commentPhoto";
	private static final String ACTION_LIKE_PHOTO_FUNCTION = "likePhoto";
	private static final String ACTION_SEND_FRIEND_REQUEST_FUNCTION = "sendFriendRequest";
	private static final String ACTION_RESPOND_FRIEND_REQUEST_FUNCTION = "respondFriendRequest";
	private static final String ACTION_FOLLOW_FUNCTION = "followUser";
	private static final String ACTION_CREATE_GROUP_FUNCTION = "createGroup";
	private static final String ACTION_SEND_GROUP_INVITE_FUNCTION = "sendGroupInvite";
	private static final String ACTION_CANCEL_GROUP_INVITE_FUNCTION = "cancelGroupInvite";
	private static final String ACTION_SEND_GROUP_MESSAGE_FUNCTION = "sendGroupMessage";
	private static final String ACTION_UPDATE_GROUP_INFO_FUNCTION = "updateGroup";

	private static final String PHP_SELECT = "Select";

	private static final String SELECT_FUNCTION = "selectPlace";

	private static final String SUGGEST_FUNCTION = "suggestPlace";

	private static final String PHP_SEARCH = "Search";

	private static final String SEARCH_PLACE_FUNCTION = "searchPlace";

	private static final String SEARCH_GROUP_FUNCTION = "searchGroup";

	private static final String SEARCH_USER_FUNCTION = "searchUser";

	private static final String SEARCH_TRIP_FUNCTION = "searchTrip";
	
	private static final String PUSH_TO_SELECTED_FUCTION = "pushNotification";

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
			httpPost.setHeader("Content-type", "application/json; charset=utf-8");

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
		case SIGNUP:
			return PHP_AUTHENTICATION;

		case FETCH_NEWS_FEED:
		case FETCH_LAST_MESSAGE:
		case FETCH_FRIEND_LIST:
		case FETCH_FRIEND_REQUEST:
		case FETCH_GROUP_LIST:
		case FETCH_GROUP_INFO:
		case FETCH_GROUP_INVITE:
			
		case ACTION_POST:
		case ACTION_COMMENT_POST:
		case ACTION_LIKE_POST:
			
		case ACTION_REVIEW:
		case ACTION_LIKE_REVIEW:
			
		case ACTION_PHOTO:
		case ACTION_COMMENT_PHOTO:
		case ACTION_LIKE_PHOTO:
			
		case ACTION_SEND_FRIEND_REQUEST:
		case ACTION_RESPOND_FRIEND_REQUEST:
			
		case ACTION_FOLLOW:
			
		case ACTION_CREATE_GROUP:
		case ACTION_SEND_GROUP_INVITE:
		case ACTION_CANCEL_GROUP_INVITE:
		case ACTION_SEND_GROUP_MESSAGE:
		case ACTION_UPDATE_GROUP_INFO:
			return PHP_SOCIAL;

		case FETCH_USER_INFO:
		case FETCH_TRIP_LIST:
		case FETCH_PLACE_INFO:
		case FETCH_TOP_PLACE:
		case FETCH_TOP_PHOTO:
			return PHP_SELECT;

		case SELECT:
			return PHP_SELECT;
		case SUGGEST:
			return PHP_SELECT;
		case SEARCH_PLACE:
			return PHP_SEARCH;
		case SEARCH_GROUP:
			return PHP_SEARCH;
		case SEARCH_USER:
			return PHP_SEARCH;
		case SEARCH_TRIP:
			return PHP_SEARCH;
			
		case PUSH_TO_SELECTED_USER:
			return PHP_SOCIAL;
			
		default:
			return null;
		}
	}

	/**
	 * Identify php function
	 * 
	 * @param requestType
	 * @return
	 * @throws JSONException
	 */
	private static String requestFunction(RequestType requestType) {
		switch (requestType) {
		case LOGIN:
			return LOGIN_FUNCTION;
		case SIGNUP:
			return SIGNUP_FUNCTION;
		case FETCH_NEWS_FEED:
			return FETCH_NEWS_FEED_FUNCTION;
		case FETCH_LAST_MESSAGE:
			return FETCH_LAST_MESSAGE_FUNCTION;
		case FETCH_USER_INFO:
			return FETCH_USER_INFO_FUNCTION;
		case FETCH_FRIEND_LIST:
			return FETCH_FRIEND_FUNCTION;
		case FETCH_FRIEND_REQUEST:
			return FETCH_FRIEND_REQUEST_FUNCTION;
		case FETCH_GROUP_LIST:
			return FETCH_GROUP_LIST_FUNCTION;
		case FETCH_GROUP_INFO:
			return FETCH_GROUP_INFO_FUNCTION;
		case FETCH_GROUP_INVITE:
			return FETCH_GROUP_INVITE_FUNCTION;
		case FETCH_TRIP_LIST:
			return FETCH_TRIP_LIST_FUNCTION;
		case ACTION_POST:
			return ACTION_POST_FUNCTION;
		case ACTION_COMMENT_POST:
			return ACTION_COMMENT_POST_FUNCTION;
		case ACTION_LIKE_POST:
			return ACTION_LIKE_POST_FUNCTION;
		case ACTION_REVIEW:
			return ACTION_REVIEW_FUNCTION;
		case ACTION_LIKE_REVIEW:
			return ACTION_LIKE_REVIEW_FUNCTION;
		case ACTION_PHOTO:
			return ACTION_PHOTO_FUNCTION;
		case ACTION_COMMENT_PHOTO:
			return ACTION_COMMENT_PHOTO_FUNCTION;
		case ACTION_LIKE_PHOTO:
			return ACTION_LIKE_PHOTO_FUNCTION;
		case ACTION_SEND_FRIEND_REQUEST:
			return ACTION_SEND_FRIEND_REQUEST_FUNCTION;
		case ACTION_RESPOND_FRIEND_REQUEST:
			return ACTION_RESPOND_FRIEND_REQUEST_FUNCTION;
		case ACTION_FOLLOW:
			return ACTION_FOLLOW_FUNCTION;
		case ACTION_CREATE_GROUP:
			return ACTION_CREATE_GROUP_FUNCTION;
		case ACTION_SEND_GROUP_INVITE:
			return ACTION_SEND_GROUP_INVITE_FUNCTION;
		case ACTION_CANCEL_GROUP_INVITE:
			return ACTION_CANCEL_GROUP_INVITE_FUNCTION;
		case ACTION_SEND_GROUP_MESSAGE:
			return ACTION_SEND_GROUP_MESSAGE_FUNCTION;
		case ACTION_UPDATE_GROUP_INFO:
			return ACTION_UPDATE_GROUP_INFO_FUNCTION;
		case SELECT:
			return SELECT_FUNCTION;
		case SUGGEST:
			return SUGGEST_FUNCTION;
		case SEARCH_PLACE:
			return SEARCH_PLACE_FUNCTION;
		case SEARCH_GROUP:
			return SEARCH_GROUP_FUNCTION;
		case SEARCH_USER:
			return SEARCH_USER_FUNCTION;
		case SEARCH_TRIP:
			return SEARCH_TRIP_FUNCTION;
		case FETCH_TOP_PLACE:
			return FETCH_TOP_PLACE_FUNCTION;
		case FETCH_TOP_PHOTO:
			return FETCH_TOP_PHOTO_FUNCTION;
		case FETCH_PLACE_INFO:
			return FETCH_PLACE_INFO_FUNCTION;
			
		case PUSH_TO_SELECTED_USER:
			return PUSH_TO_SELECTED_FUCTION;
		default:
			return null;
		}
	}

}
