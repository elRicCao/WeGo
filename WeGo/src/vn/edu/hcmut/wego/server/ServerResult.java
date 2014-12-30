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
import vn.edu.hcmut.wego.entity.InviteRequest.Type;
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
							news.setId(Integer.parseInt(item.getString("post_id")));

							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							try {
								news.setTime(dateFormat.parse(item.getString("time")));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (Integer.parseInt(item.getString("isLiked")) == 1)
								news.setLiked(true);
							else
								news.setLiked(false);
							parseResult.add(news);
							break;

						case 1:
							news.setType(NewsType.PHOTO);
							news.setContent(item.getString("description"));
							news.setPhoto(item.getString("reference"));
							news.setId(Integer.parseInt(item.getString("photo_id")));
							dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							try {
								news.setTime(dateFormat.parse(item.getString("time")));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							parseResult.add(news);
							break;

						case 2:
							news.setType(NewsType.COMMENT_POST);
							news.setContent(item.getString("content_post"));
							news.setId(Integer.parseInt(item.getString("post_id")));
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
							news.setId(Integer.parseInt(item.getString("post_id")));
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
						user.setImage(tmp.getString("avatar"));
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
						int friend = tmp.getInt("isFriend");
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
						user.setImage(tmp.getString("avatar"));
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
				Log.i("Debug group info", result.toString());
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);
					JSONObject tmp = array.getJSONObject(0);
					Group group = new Group();
					group.setName(tmp.getString("name"));
					group.setDescription(tmp.getString("description"));
					User owner = new User();
					owner.setName(tmp.getString("owner_name"));
					group.setAdmin(owner);
					Message annoucement = new Message();
					annoucement.setContent(tmp.getString("annoucement"));
					group.setAnnouncement(annoucement);

					group.setCount(Integer.parseInt(tmp.getString("num_of_member")));

					JSONArray arrayRequest = tmp.getJSONArray("requests");
					ArrayList<InviteRequest> inviteRequests = new ArrayList<InviteRequest>();
					for (int i = 0; i < arrayRequest.length(); i++) {
						JSONObject objRequest = arrayRequest.getJSONObject(i);
						InviteRequest inviteRequest = new InviteRequest();
						inviteRequest.setType(Type.GROUP_REQUEST);
						
						User sender = new User();
						sender.setId(Integer.parseInt(objRequest.getString("id")));
						sender.setImage(objRequest.getString("avatar"));
						sender.setName(objRequest.getString("name"));
						
						inviteRequest.setSender(sender);

						inviteRequests.add(inviteRequest);
					}
					group.setRequests(inviteRequests);

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
				parseResult.add(result.getInt("num_of_like"));
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
						place.setLongitude(Double.parseDouble(placeInfo.getString("longitude")));

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
						place.setNumOfWishList(Integer.parseInt(placeInfo.getString("num_of_wishlist")));
						place.setAvatar(placeInfo.getString("avatar"));

						int numOfReviews = Integer.parseInt(placeInfo.getString("num_of_review"));
						if (numOfReviews == 0) {
							place.setAverageRate(0.0);
						} else {
							place.setAverageRate((double) Integer.parseInt(placeInfo.getString("total_rate")) / numOfReviews);
						}

						Place province = new Place();
						province.setId(Integer.parseInt(placeInfo.getString("province_id")));
						place.setProvince(province);

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
						place.setAddress(placeInfo.getString("address"));
						place.setAvatar(placeInfo.getString("avatar"));
						double rate = 0;
						if (Double.parseDouble(placeInfo.getString("num_of_review")) != 0)
							rate = Double.parseDouble(placeInfo.getString("total_rate")) / Double.parseDouble(placeInfo.getString("num_of_review"));
						place.setAverageRate(rate);

						place.setNumOfWishList(Integer.parseInt(placeInfo.getString("num_of_wishlist")));

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
						user.setImage(userInfo.getString("avatar"));
						Place place = new Place();
						place.setName(userInfo.getString("location"));
						user.setLocation(place);
						parseResult.add(user);
					}
				}
				break;
			case SEARCH_TRIP:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);

						Trip trip = new Trip();
						trip.setId(Integer.parseInt(tmp.getString("id")));
						trip.setCost(Integer.parseInt(tmp.getString("cost")));
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

			case FETCH_TRIP_LIST:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);

						Trip trip = new Trip();
						trip.setId(Integer.parseInt(tmp.getString("id")));
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
						double rate = 0;
						if (Double.parseDouble(tmp.getString("num_of_review")) != 0)
							rate = Double.parseDouble(tmp.getString("total_rate")) / Double.parseDouble(tmp.getString("num_of_review"));
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
			case FETCH_TRIP_INFO:
				if (result.getInt(Constant.SUCCESS) == 1) {
					Trip trip = new Trip();
					JSONArray array = result.getJSONArray(Constant.RESULT);
					JSONObject tmp = array.getJSONObject(0);
					trip.setAnnouncement(tmp.getString("annoucement"));
					trip.setCost(Integer.parseInt(tmp.getString("cost")));
					trip.setDescription(tmp.getString("description"));
					Place startPlace = new Place();
					startPlace.setId(Integer.parseInt(tmp.getString("start_id")));
					startPlace.setName(tmp.getString("start_name"));
					startPlace.setLatitude(Double.parseDouble(tmp.getString("start_lat")));
					startPlace.setLongitude(Double.parseDouble(tmp.getString("start_long")));
					startPlace.setAvatar(tmp.getString("start_avatar"));
					trip.setStartPlace(startPlace);
					User leader = new User();
					leader.setName(tmp.getString("leader_name"));
					trip.setLeader(leader);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
					try {
						trip.setStartDate(formatter.parse(tmp.getString("start_date")));
						trip.setEndDate(formatter.parse(tmp.getString("end_date")));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					JSONArray arrayDestination = tmp.getJSONArray("destinations");
					ArrayList<Place> destinations = new ArrayList<Place>();
					for (int i = 0; i < arrayDestination.length(); i++) {
						JSONObject destination = arrayDestination.getJSONObject(i);
						Place place = new Place();
						place.setId(Integer.parseInt(destination.getString("id")));
						place.setName(destination.getString("name"));
						place.setLatitude(Double.parseDouble(destination.getString("latitude")));
						place.setLongitude(Double.parseDouble(destination.getString("longitude")));
						place.setAddress(destination.getString("avatar"));

						destinations.add(place);
					}
					trip.setMinorDestination(destinations);

					JSONArray arrayPlace = tmp.getJSONArray("places");
					ArrayList<Place> places = new ArrayList<Place>();
					for (int i = 0; i < arrayPlace.length(); i++) {
						JSONObject placeObject = arrayPlace.getJSONObject(i);
						Place place = new Place();
						place.setId(Integer.parseInt(placeObject.getString("id")));
						place.setName(placeObject.getString("name"));
						place.setLatitude(Double.parseDouble(placeObject.getString("latitude")));
						place.setLongitude(Double.parseDouble(placeObject.getString("longitude")));
						place.setAvatar(placeObject.getString("avatar"));
						places.add(place);
					}
					trip.setPlaces(places);

					JSONArray arrayUser = tmp.getJSONArray("users");
					ArrayList<User> users = new ArrayList<User>();
					for (int i = 0; i < arrayUser.length(); i++) {
						JSONObject user = arrayUser.getJSONObject(i);
						User member = new User();
						member.setId(Integer.parseInt(user.getString("id")));
						member.setName(user.getString("name"));
						member.setPhone(user.getString("phone"));
						member.setImage(user.getString("avatar"));
						users.add(member);
					}
					
					JSONArray arrayRequest = tmp.getJSONArray("requests");
					ArrayList<InviteRequest> requests = new ArrayList<InviteRequest>();
					for(int i = 0; i < arrayRequest.length(); i++)
					{
						JSONObject request = arrayRequest.getJSONObject(i);
						InviteRequest inviteRequest = new InviteRequest();
						inviteRequest.setType(Type.TRIP_REQUEST);
						
						User sender = new User();
						sender.setId(Integer.parseInt(request.getString("id")));
						sender.setName(request.getString("name"));
						sender.setImage(request.getString("avatar"));
						inviteRequest.setSender(sender);
						requests.add(inviteRequest);
					}
					trip.setRequests(requests);
					trip.setMembers(users);
					parseResult.add(trip);
				}
				break;
			case SEARCH_PLACE_AROUND:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						Place place = new Place();
						place.setId(Integer.parseInt(tmp.getString("id")));
						place.setName(tmp.getString("name"));
						place.setLatitude(Double.parseDouble(tmp.getString("latitude")));
						place.setLongitude(Double.parseDouble(tmp.getString("longitude")));
						parseResult.add(place);
					}
				}
				break;
			case FETCH_HISTORY_TRIP:
				break;
			case FETCH_FRIEND_MESSAGE:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						Message message = new Message();
						message.setContent(tmp.getString("content"));
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
						try {
							message.setTime(formatter.parse(tmp.getString("time")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						User user = new User();
						user.setId(Integer.parseInt(tmp.getString("sender_id")));
						user.setName(tmp.getString("sender_name"));
						user.setImage(tmp.getString("sender_avatar"));
						message.setSender(user);
						parseResult.add(message);
					}
				}
				break;
			case FETCH_COMMENT_POST:
				if (result.getInt(Constant.SUCCESS) == 1) {
					JSONArray array = result.getJSONArray(Constant.RESULT);

					for (int i = 0; i < array.length(); i++) {
						JSONObject tmp = array.getJSONObject(i);
						Message message = new Message();
						message.setContent(tmp.getString("content"));
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							message.setTime(formatter.parse(tmp.getString("time")));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						User user = new User();
						user.setId(Integer.parseInt(tmp.getString("user_id")));
						user.setName(tmp.getString("user_name"));
						message.setSender(user);
						parseResult.add(message);
					}
				}
				break;
			case ACTION_SEND_MESSAGE:
				parseResult.add(result.getInt(Constant.SUCCESS));
				break;

			case ACTION_CREATE_TRIP:
				Log.i("Debug create trip", result.toString());
				parseResult.add(result.getInt(Constant.SUCCESS));
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
