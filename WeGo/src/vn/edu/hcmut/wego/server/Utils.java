package vn.edu.hcmut.wego.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.InviteRequest;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.entity.InviteRequest.Type;
import vn.edu.hcmut.wego.entity.User.UserStatus;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import android.util.Log;

class Utils {

	private static final String PHP_AUTHENTICATION = "AuthenticationLogic";
	private static final String PHP_SOCIAL = "SocialLogic";

	private static final String LOGIN_FUNCTION = "selectUser";
	private static final String SIGNUP_FUNCTION = "insertNewUser";

	private static final String FETCH_NEWS_FUNCTION = "insertNewUser";
	private static final String FETCH_NEWS_FEED_FUNCTION = "getNewFeeds";
	private static final String FETCH_FOLLOW_NEWS_FUNCTION = "getFollowNews";
	private static final String FETCH_USER_INFO_FUNCTION = "getUserInfo";
	private static final String FETCH_FRIEND_FUNCTION = "selectUserFriend";
	private static final String FETCH_FRIEND_REQUEST_FUNCTION = "getFriendRequest";
	private static final String FETCH_GROUP_LIST_FUNCTION = "selectUserGroup";
	private static final String FETCH_GROUP_INVITE_FUNCTION = "selectUserInviteGroup";
	private static final String FETCH_GROUP_INFO_FUNCTION = "getGroupInfo";
	private static final String FETCH_LAST_MESSAGE_FUNCTION = "getLastMessage";
	private static final String FETCH_TRIP_LIST_FUNCTION = "getTrip";

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
			return PHP_AUTHENTICATION;
		case SIGNUP:
			return PHP_AUTHENTICATION;
		case FETCH_NEWS:
			return PHP_SOCIAL;
		case FETCH_LAST_MESSAGE:
			return PHP_SOCIAL;
		case FETCH_FOLLOW_NEWS:
			return PHP_SOCIAL;
		case FETCH_USER_INFO:
			return PHP_SELECT;
		case FETCH_FRIEND_LIST:
			return PHP_SOCIAL;
		case FETCH_NEWS_FEED:
			return PHP_SOCIAL;
		case FETCH_FRIEND_REQUEST:
			return PHP_SOCIAL;
		case FETCH_GROUP_LIST:
			return PHP_SOCIAL;
		case FETCH_GROUP_INFO:
			return PHP_SOCIAL;
		case FETCH_GROUP_INVITE:
			return PHP_SOCIAL;
		case FETCH_TRIP_LIST:
			return PHP_SELECT;   
		case ACTION_POST:
			return PHP_SOCIAL;
		case ACTION_COMMENT_POST:
			return PHP_SOCIAL;
		case ACTION_LIKE_POST:
			return PHP_SOCIAL;
		case ACTION_REVIEW:
			return PHP_SOCIAL;
		case ACTION_LIKE_REVIEW:
			return PHP_SOCIAL;
		case ACTION_PHOTO:
			return PHP_SOCIAL;
		case ACTION_COMMENT_PHOTO:
			return PHP_SOCIAL;
		case ACTION_LIKE_PHOTO:
			return PHP_SOCIAL;
		case ACTION_SEND_FRIEND_REQUEST:
			return PHP_SOCIAL;
		case ACTION_RESPOND_FRIEND_REQUEST:
			return PHP_SOCIAL;
		case ACTION_FOLLOW:
			return PHP_SOCIAL;
		case ACTION_CREATE_GROUP:
			return PHP_SOCIAL;
		case ACTION_SEND_GROUP_INVITE:
			return PHP_SOCIAL;
		case ACTION_CANCEL_GROUP_INVITE:
			return PHP_SOCIAL;
		case ACTION_SEND_GROUP_MESSAGE:
			return PHP_SOCIAL;
		case ACTION_UPDATE_GROUP_INFO:
			return PHP_SOCIAL;
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
		case FETCH_NEWS:
			return FETCH_NEWS_FUNCTION;
		case FETCH_LAST_MESSAGE:
			return FETCH_LAST_MESSAGE_FUNCTION;
		case FETCH_FOLLOW_NEWS:
			return FETCH_FOLLOW_NEWS_FUNCTION;
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
		case FETCH_NEWS_FEED:
			return FETCH_NEWS_FEED_FUNCTION;
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

			case SIGNUP:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case FETCH_NEWS:

				break;
			case FETCH_LAST_MESSAGE:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);
					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						Message message = new Message();
						message.setContent(tmp.getString("content"));
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							message.setTime(formatter.parse(tmp.getString("time")));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						User user = new User();
						user.setId(tmp.getInt("id"));
						user.setName(tmp.getString("name"));
						message.setSender(user);
						objects.add(message);
					}
				}
				break;
      
			case FETCH_NEWS_FEED:
	
				break;

			case FETCH_FOLLOW_NEWS:

				break;

			case FETCH_USER_INFO:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);
					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						User user = new User();
						user.setId(Integer.parseInt(tmp.getString("id")));
						user.setName(tmp.getString("name"));
						user.setPhone(tmp.getString("phone"));
						user.setNumOfFollow(Integer.parseInt(tmp.getString("follower")));
						user.setNumOfFriend(Integer.parseInt(tmp.getString("friend")));
						user.setNumOfTrip(Integer.parseInt(tmp.getString("trip")));
						user.setEmail(tmp.getString("email"));
						Place place = new Place();
						place.setName(tmp.getString("location"));
						user.setLocation(place);
						JSONArray repu = tmp.getJSONArray("reputation");
						if(repu.length() > 3)user.setNumOfVote(3);
						else user.setNumOfVote(repu.length());
						int totalRate = 0;
						for(int j = 0; j < user.getNumOfVote(); j++)
						{
							totalRate += Integer.parseInt(repu.getJSONObject(j).getString("rate"));
						}
						if(user.getNumOfVote() > 0)user.setAverageVote(totalRate/user.getNumOfVote());
						else user.setAverageVote(0);
						JSONArray activities = tmp.getJSONArray("review");
						for(int j = 0; j < activities.length(); j++)
						{
							user.addRecentActivity(activities.getJSONObject(j).getString("name"));
						}
						int friend = tmp.getInt("friend");
						objects.add(user);
						objects.add(friend);
					}
				}
				break;

			case FETCH_FRIEND_LIST:

				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						User user = new User();
						user.setId(Integer.parseInt(tmp.getString("id")));
						user.setName(tmp.getString("name"));
					//	user.setStatus(UserStatus.ONLINE);
						Message message = new Message();
						if (!tmp.getString("message").isEmpty()) {
							message.setContent(tmp.getString("message"));
							user.addRecentMessage(message);
						}
						objects.add(user);
					}
				}
				break;

			case FETCH_FRIEND_REQUEST:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i).getJSONArray("sender").getJSONObject(0);
						InviteRequest friendRequest = new InviteRequest();
						// TODO: Sender is User
						// friendRequest.setSenderId(Integer.parseInt(tmp.getString("sender")));

						friendRequest.setType(InviteRequest.Type.FRIEND_REQUEST);
						User userRequest = new User();
						userRequest.setId(Integer.parseInt(tmp.getString("id")));
						userRequest.setName(tmp.getString("name"));
						Place placeRequest = new Place();
						JSONObject place = tmp.getJSONArray("location").getJSONObject(0);
						placeRequest.setId(Integer.parseInt(place.getString("id")));
						placeRequest.setName(place.getString("name"));
						userRequest.setLocation(placeRequest);
						friendRequest.setSender(userRequest);
						objects.add(friendRequest);
					}
				}
				break;

			case FETCH_GROUP_LIST:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);

						Group group = new Group();
				//		group.setId(Integer.parseInt(tmp.getString("id")));
						group.setName(tmp.getString("name"));
						Message message = new Message();
						message.setContent(tmp.getString("annoucement"));
						// group.setOwner(Integer.parseInt(tmp.getString("owner")));
						group.setAnnouncement(message);

						objects.add(group);
					}
				}
				break;
				
			case FETCH_GROUP_INFO:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);			
					JSONObject tmp = array.getJSONObject(0);
					Group group = new Group();
					Message message = new Message();
					message.setContent(tmp.getString("annoucement"));
					group.setAnnouncement(message);
					group.setName(tmp.getString("name"));
					User user = new User();
					user.setName(tmp.getString("owner"));
					group.setAdmin(user);
					group.setCount(Integer.parseInt(tmp.getString("count")));
					objects.add(group); 
						
					
				}
				break;
			case FETCH_GROUP_INVITE:

				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);
					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);

						InviteRequest groupRequest = new InviteRequest();
						// TODO: Sender is Group
						// groupRequest.setSenderId(Integer.parseInt(tmp.getString("sender")));
						groupRequest.setType(InviteRequest.Type.GROUP_INVITE);
						Group group = new Group();
						group.setId(Integer.parseInt(tmp.getString("id")));
						group.setName(tmp.getString("name"));
						group.setDescription(tmp.getString("description"));
						groupRequest.setSender(group);
						objects.add(groupRequest);
					}
				}
				break;

			case ACTION_POST:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_COMMENT_POST:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_LIKE_POST:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_REVIEW:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_LIKE_REVIEW:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_PHOTO:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_COMMENT_PHOTO:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_LIKE_PHOTO:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_SEND_FRIEND_REQUEST:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_RESPOND_FRIEND_REQUEST:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_FOLLOW:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_CREATE_GROUP:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_SEND_GROUP_INVITE:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_CANCEL_GROUP_INVITE:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_SEND_GROUP_MESSAGE:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_UPDATE_GROUP_INFO:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case SELECT:
				if (result.getInt(Constant.SUCCESS) == 1) {
					for (int i = 0; i < result.getJSONArray(Constant.RESULT).length(); i++) {
						JSONObject placeInfo = result.getJSONArray(Constant.RESULT).getJSONObject(i);
						Place place = new Place();
						place.setId(Integer.parseInt(placeInfo.getString("id")));
						place.setName(placeInfo.getString("name"));
						place.setLatitude(Double.parseDouble(placeInfo.getString("latitude")));
						place.setLongtitude(Double.parseDouble(placeInfo.getString("longitude")));
						objects.add(place);
					}
				}
				break;
				
			case SUGGEST:
				if (result.getInt(Constant.SUCCESS) == 1) {
					for (int i = 0; i < result.getJSONArray(Constant.RESULT).length(); i++) {
						JSONObject placeInfo = result.getJSONArray(Constant.RESULT).getJSONObject(i);
						Place place = new Place();
						place.setId(Integer.parseInt(placeInfo.getString("id")));
						place.setName(placeInfo.getString("name"));
						place.setLatitude(Double.parseDouble(placeInfo.getString("latitude")));
						place.setLongtitude(Double.parseDouble(placeInfo.getString("longitude")));
						place.setAddress(placeInfo.getString("address"));
						objects.add(place);
					}
				}
				break;
			case SEARCH_PLACE:
				if (result.getInt(Constant.SUCCESS) == 1) {
					for (int i = 0; i < result.getJSONArray(Constant.RESULT).length(); i++) {
						JSONObject placeInfo = result.getJSONArray(Constant.RESULT).getJSONObject(i);
						Place place = new Place();
						place.setId(Integer.parseInt(placeInfo.getString("id")));
						place.setName(placeInfo.getString("name"));
						place.setLatitude(Double.parseDouble(placeInfo.getString("latitude")));
						place.setLongtitude(Double.parseDouble(placeInfo.getString("longitude")));
						place.setAddress(placeInfo.getString("address"));
						objects.add(place);
					}
				}
				break;
			case SEARCH_GROUP:
				if (result.getInt(Constant.SUCCESS) == 1) {
					for (int i = 0; i < result.getJSONArray(Constant.RESULT).length(); i++) {
						JSONObject groupInfo = result.getJSONArray(Constant.RESULT).getJSONObject(i);
						Group group = new Group();
						group.setId(Integer.parseInt(groupInfo.getString("id")));
						group.setName(groupInfo.getString("name"));
						group.setDescription(groupInfo.getString("description"));

						objects.add(group);
					}
				}
				break;
			case SEARCH_USER:
				if (result.getInt(Constant.SUCCESS) == 1) {
					for (int i = 0; i < result.getJSONArray(Constant.RESULT).length(); i++) {
						JSONObject userInfo = result.getJSONArray(Constant.RESULT).getJSONObject(i);
						User user = new User();
						user.setId(Integer.parseInt(userInfo.getString("id")));
						user.setName(userInfo.getString("name"));
						
						objects.add(user);
					}
				}
				break;
			case SEARCH_TRIP:
				
				break;
			case FETCH_TRIP_LIST:
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
