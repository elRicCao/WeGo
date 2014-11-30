package vn.edu.hcmut.wego.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import vn.edu.hcmut.wego.entity.InviteRequest;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.User.UserStatus;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import android.util.Log;

class Utils {

	private static final String PHP_AUTHENTICATION = "AuthenticationLogic";
	private static final String PHP_SOCIAL = "SocialLogic";

	private static final String LOGIN_FUNCTION = "selectUser";
	private static final String SIGNUP_FUNCTION = "insertNewUser";

	private static final String FETCH_NEWS_FUNCTION = "insertNewUser";
	private static final String FETCH_FOLLOW_NEWS_FUNCTION = "getFollowNews";
	private static final String FETCH_USER_INFO_FUNCTION = "getUserNews";
	private static final String FETCH_FRIEND_FUNCTION = "selectUserFriend";
	private static final String FETCH_FRIEND_REQUEST_FUNCTION = "getFriendRequest";
	private static final String FETCH_GROUP_LIST_FUNCTION = "selectUserGroup";
	private static final String FETCH_GROUP_INVITE_FUNCTION = "selectUserInviteGroup";

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
		case SIGNUP:
			return PHP_AUTHENTICATION;
		case FETCH_NEWS:
			return PHP_SOCIAL;
		case FETCH_FOLLOW_NEWS:
			return PHP_SOCIAL;
		case FETCH_USER_INFO:
			return PHP_SOCIAL;
		case FETCH_FRIEND_LIST:
			return PHP_SOCIAL;
		case FETCH_FRIEND_REQUEST:
			return PHP_SOCIAL;
		case FETCH_GROUP_LIST:
			return PHP_SOCIAL;
		case FETCH_GROUP_INVITE:
			return PHP_SOCIAL;
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
		case FETCH_GROUP_INVITE:
			return FETCH_GROUP_INVITE_FUNCTION;
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

			case SIGNUP:

				objects.add(result.getInt(Constant.SUCCESS));
				break;

			case FETCH_NEWS:

				break;

			case FETCH_FOLLOW_NEWS:

				break;

			case FETCH_USER_INFO:

				if (result.getInt(Constant.SUCCESS) == 1) {
					int chkFriend = Integer.parseInt(result.getJSONArray("checkfriend").getJSONObject(0).getString("check_friend"));
					JSONArray newsInfo = result.getJSONArray("news");

					objects.add(chkFriend);
					for (int i = 0; i < newsInfo.length(); i++) {
						boolean chkNews = false;

						JSONObject news = result.getJSONArray("news").getJSONObject(i);
						JSONObject owner = result.getJSONArray("owner").getJSONObject(i);
						JSONObject place = result.getJSONArray("place").getJSONObject(i);
						JSONObject place_district = result.getJSONArray("place_district").getJSONObject(i);
						JSONObject place_province = result.getJSONArray("place_province").getJSONObject(i);
						JSONObject owner_place = result.getJSONArray("owner_place").getJSONObject(i);
						JSONObject owner_place_district = result.getJSONArray("owner_place_district").getJSONObject(i);
						JSONObject owner_place_province = result.getJSONArray("owner_place_province").getJSONObject(i);

						Place owner_province = new Place();
						owner_province.setId(Integer.parseInt(owner_place_province.getString("last_owner_province_id")));
						owner_province.setName(owner_place_province.getString("last_owner_province_name"));
						owner_province.setDescription(owner_place_province.getString("last_owner_province_description"));

						Place owner_district = new Place();
						owner_district.setId(Integer.parseInt(owner_place_district.getString("owner_place_district_id")));
						owner_district.setName(owner_place_district.getString("owner_place_district_name"));
						owner_district.setProvince(owner_province);
						owner_district.setDescription(owner_place_district.getString("owner_place_district_description"));

						Place owner_location = new Place();
						owner_location.setId(Integer.parseInt(owner_place.getString("owner_place_id")));
						owner_location.setName(owner_place.getString("owner_place_name"));
						owner_location.setAddress(owner_place.getString("owner_place_address"));
						owner_location.setDistrict(owner_district);
						owner_location.setLongtitude(Double.parseDouble(owner_place.getString("owner_place_longtitude")));
						owner_location.setLatitude(Double.parseDouble(owner_place.getString("owner_place_latitude")));

						User news_owner = new User();
						news_owner.setId(Integer.parseInt(owner.getString("owner")));
						news_owner.setEmail(owner.getString("owner_email"));
						news_owner.setImage(owner.getString("owner_avatar"));
						news_owner.setLocation(owner_location);
						news_owner.setName(owner.getString("owner_name"));
						news_owner.setPhone(owner.getString("owner_phone"));

						Place province = new Place();
						province.setId(Integer.parseInt(place_province.getString("last_province_id")));
						province.setName(place_province.getString("last_province_name"));
						province.setDescription(place_province.getString("last_province_description"));

						Place district = new Place();
						district.setId(Integer.parseInt(place_district.getString("place_district_id")));
						district.setName(place_district.getString("place_district_name"));
						district.setProvince(province);
						district.setDescription(place_district.getString("place_district_description"));

						Place news_place = new Place();
						news_place.setId(Integer.parseInt(place.getString("place_id")));
						news_place.setName(place.getString("place_name"));
						news_place.setAddress(place.getString("place_address"));
						news_place.setDistrict(district);
						news_place.setLongtitude(Double.parseDouble(place.getString("place_longtitude")));
						news_place.setLatitude(Double.parseDouble(place.getString("place_latitude")));

						News news_info = new News();
						news_info.setActors(null);
						news_info.setContent(news.getString("content"));
						news_info.setId(Integer.parseInt(news.getString("id")));
						news_info.setNumOfComments(Integer.parseInt(news.getString("num_of_comment")));
						news_info.setNumOfLikes(Integer.parseInt(news.getString("num_of_like")));
						news_info.setOwner(news_owner);
						news_info.setPhoto(news.getString("Photo"));
						news_info.setPlace(news_place);
						news_info.setRate(Integer.parseInt(news.getString("rate")));

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							news_info.setTime(formatter.parse(news.getString("time")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						// news_info.setType(Integer.parseInt(news.getString("type")));

						for (int j = 0; j < objects.size(); j++) {
							News nws = (News) objects.get(j);
							if (nws.getId() == news_info.getId() && nws.getType() == news_info.getType()) {
								if (nws.getTime().before(news_info.getTime())) {
									nws.setTime(news_info.getTime());
								}
								ArrayList<User> tmp = nws.getActors();
								tmp.addAll(news_info.getActors());
								nws.setActors(tmp);
								objects.set(j, nws);
								chkNews = true;
							}
						}
						if (!chkNews) {
							objects.add(news_info);
						}
					}
				}
				break;

			case FETCH_FRIEND_LIST:

				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);

						User friend = new User();
						friend.setId(Integer.parseInt(tmp.getString("friendId")));
						friend.setName(tmp.getString("friendName"));
						friend.setPhone(tmp.getString("friendPhone"));
						friend.setEmail(tmp.getString("friendEmail"));
						friend.setStatus(UserStatus.ONLINE);

						objects.add(friend);
					}
				}
				break;

			case FETCH_FRIEND_REQUEST:
				
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						
						JSONObject tmp = array.getJSONObject(i);

						InviteRequest friendRequest = new InviteRequest();
						friendRequest.setId(Integer.parseInt(tmp.getString("id")));
						friendRequest.setReceiverId(Integer.parseInt(tmp.getString("receiver")));
						friendRequest.setSenderId(Integer.parseInt(tmp.getString("sender")));
						friendRequest.setType(InviteRequest.Type.FRIEND_REQUEST);

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
						group.setId(Integer.parseInt(tmp.getString("id")));
						group.setName(tmp.getString("name"));
						// group.setOwner(Integer.parseInt(tmp.getString("owner")));
						group.setDescription(tmp.getString("description"));

						objects.add(group);
					}
				}
				break;

			case FETCH_GROUP_INVITE:

				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);

						InviteRequest groupRequest = new InviteRequest();
						groupRequest.setId(Integer.parseInt(tmp.getString("id")));
						groupRequest.setReceiverId(Integer.parseInt(tmp.getString("receiver")));
						groupRequest.setSenderId(Integer.parseInt(tmp.getString("sender")));
						groupRequest.setType(InviteRequest.Type.GROUP_INVITE);

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

			case SEARCH:
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
						objects.add(place);
					}
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
