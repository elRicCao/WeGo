package vn.edu.hcmut.wego.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.InviteRequest;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.News.NewsType;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.Trip;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import android.util.Log;

public class ServerResult {

	/**
	 * Parse JSONObject result to Object
	 * 
	 * @param requestType
	 * @param result
	 * @return
	 */
	static ArrayList<Object> parse(RequestType requestType, JSONObject result) {

		ArrayList<Object> parseResult = new ArrayList<Object>();
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
					parseResult.add(user);
				}
				break;
				
			case SIGNUP:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case FETCH_NEWS_FEED:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray list = result.getJSONArray(Constant.RESULT);
					
					for (int i = 0; i < list.length(); i++) {
						JSONObject item = list.getJSONObject(i);
						
						News news = new News();
						news.setNumOfLikes(Integer.parseInt(item.getString("num_of_like")));
						news.setNumOfComments(Integer.parseInt(item.getString("num_of_comment")));
						
						User user = new User();
						user.setId(Integer.parseInt(item.getString("actor_id")));
						user.setName(item.getString("actor_name"));
						user.setImage(item.getString("actor_avatar"));
						news.setOwner(user);
						
						User actor = new User();
						actor.setId(Integer.parseInt(item.getString("friend_id")));
						actor.setName(item.getString("friend_name"));
						news.addActor(actor);
						
						switch (Integer.parseInt(item.getString("type"))) {
						case 0:
							news.setType(NewsType.POST);
							news.setContent(item.getString("content_post"));
							parseResult.add(news);
							break;
							
						case 1:
							news.setType(NewsType.PHOTO);
							news.setContent(item.getString("reference"));							
							parseResult.add(news);
							break;
							
						case 2:
							news.setType(NewsType.COMMENT_POST);
							news.setContent(item.getString("content_post"));
							parseResult.add(news);
							break;
							
						case 3:
							news.setType(NewsType.COMMENT_PHOTO);
							news.setContent(item.getString("content_photo"));
							parseResult.add(news);
							break;
							
						case 4:
							news.setType(NewsType.LIKE_POST);
							news.setContent(item.getString("content_post"));
							parseResult.add(news);
							break;
							
						case 5:
							news.setType(NewsType.LIKE_PHOTO);
							news.setContent(item.getString("content_photo"));
							parseResult.add(news);
							break;
							
						case 6:
							news.setType(NewsType.REVIEW);
							parseResult.add(news);
							break;
							
						case 7:
							news.setType(NewsType.LIKE_REVIEW);
							parseResult.add(news);
							break;
						default:
							break;
						}
					}
				}
				break;

			case FETCH_LAST_MESSAGE:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);
					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						Message message = new Message();
						message.setContent(tmp.getString("content"));
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
							message.setTime(formatter.parse(tmp.getString("time")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						User user = new User();
						user.setId(tmp.getInt("id"));
						user.setName(tmp.getString("name"));
						message.setSender(user);
						parseResult.add(message);
					}
				}
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
						if (repu.length() > 3)
							user.setNumOfVote(3);
						else
							user.setNumOfVote(repu.length());
						int totalRate = 0;
						for (int j = 0; j < user.getNumOfVote(); j++) {
							totalRate += Integer.parseInt(repu.getJSONObject(j).getString("rate"));
						}
						if (user.getNumOfVote() > 0)
							user.setAverageVote(totalRate / user.getNumOfVote());
						else
							user.setAverageVote(0);
						JSONArray activities = tmp.getJSONArray("review");
						for (int j = 0; j < activities.length(); j++) {
							user.addRecentActivity(activities.getJSONObject(j).getString("name"));
						}
						int friend = tmp.getInt("friend");
						parseResult.add(user);
						parseResult.add(friend);
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
						Message message = new Message();
						if (!tmp.getString("message").isEmpty()) {
							message.setContent(tmp.getString("message"));
							user.addRecentMessage(message);
						}
						parseResult.add(user);
					}
				}
				break;

			case FETCH_FRIEND_REQUEST:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i).getJSONArray("sender").getJSONObject(0);
						InviteRequest friendRequest = new InviteRequest();
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

						parseResult.add(friendRequest);
					}
				}
				break;

			case FETCH_GROUP_LIST:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);

						Group group = new Group();
						// group.setId(Integer.parseInt(tmp.getString("id")));
						group.setName(tmp.getString("name"));
						Message message = new Message();
						message.setContent(tmp.getString("annoucement"));
						// group.setOwner(Integer.parseInt(tmp.getString("owner")));
						group.setAnnouncement(message);

						parseResult.add(group);
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

					parseResult.add(group);

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

						parseResult.add(groupRequest);
					}
				}
				break;

			case ACTION_POST:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_COMMENT_POST:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_LIKE_POST:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_REVIEW:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_LIKE_REVIEW:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_PHOTO:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_COMMENT_PHOTO:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_LIKE_PHOTO:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_SEND_FRIEND_REQUEST:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_RESPOND_FRIEND_REQUEST:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_FOLLOW:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_CREATE_GROUP:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_SEND_GROUP_INVITE:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_CANCEL_GROUP_INVITE:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_SEND_GROUP_MESSAGE:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_UPDATE_GROUP_INFO:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;
				
			case PUSH_TO_SELECTED_USER:
				parseResult.add(result.getInt(Constant.SUCCESS));
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

						parseResult.add(place);
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

						parseResult.add(place);
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

						parseResult.add(place);
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

						parseResult.add(group);
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

						parseResult.add(user);
					}
				}
				break;
			case SEARCH_TRIP:

				break;
				
			case FETCH_TRIP_LIST:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);

						Trip trip = new Trip();
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
							trip.setStartDate(formatter.parse(tmp.getString("start_date")));
							trip.setEndDate(formatter.parse(tmp.getString("end_date")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						User leader = new User();
						leader.setName(tmp.getString("leader_name"));
						trip.setLeader(leader);
						Place startPlace = new Place();

						startPlace.setName(tmp.getString("start_name"));
						trip.setStartPlace(startPlace);
						Place endPlace = new Place();
						endPlace.setName(tmp.getString("end_name"));
						trip.setEndPlace(endPlace);
						parseResult.add(trip);
					}
				}
				break;
			case FETCH_PLACE_INFO:

				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						Place place = new Place();
						place.setId(Integer.parseInt(tmp.getString("id")));
						place.setName(tmp.getString("name"));
						double rate = Double.parseDouble(tmp.getString("total_rate")) / Double.parseDouble(tmp.getString("num_of_review"));
						place.setAverageRate(rate);
						place.setNumOfWishList(Integer.parseInt(tmp.getString("num_of_wishlist")));
						parseResult.add(place);
					}
				}
				break;

			case FETCH_TOP_PLACE:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						Place place = new Place();
						place.setId(Integer.parseInt(tmp.getString("id")));
						place.setName(tmp.getString("name"));
						place.setAvatar(tmp.getString("avatar"));
						double rate = Double.parseDouble(tmp.getString("total_rate")) / Double.parseDouble(tmp.getString("num_of_review"));
						place.setAverageRate(rate);
						place.setNumOfWishList(Integer.parseInt(tmp.getString("num_of_wishlist")));
						parseResult.add(place);
					}
				}
				break;
				
			case FETCH_TOP_PHOTO:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						
						News news = new News();
						news.setType(NewsType.PHOTO);
						news.setId(tmp.getInt("id"));
						news.setContent(tmp.getString("description"));
						news.setPhoto(tmp.getString("reference"));
						news.setNumOfLikes(tmp.getInt("like"));
						news.setNumOfComments(tmp.getInt("comment"));
						
						User user = new User();
						user.setId(tmp.getInt("user_id"));
						user.setName(tmp.getString("user_name"));
						user.setImage(tmp.getString("user_avatar"));
						
						news.setOwner(user);
						parseResult.add(news);
					}
				}
				break;
			default:
				break;

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return parseResult;
	}
}
