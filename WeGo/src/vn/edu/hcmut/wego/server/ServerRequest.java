package vn.edu.hcmut.wego.server;

import java.util.ArrayList;

import org.json.JSONObject;

import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.InviteRequest;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.User;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Create and hold an {@link AsyncTask} which is used for sending and receiving data from server. Each {@link AsyncTask} has a call back method which is executed by the <b>UI thread</b> after
 * finishing fetching data. This call back method can be <code><b>null</b></code>.
 */
public class ServerRequest {

	private AsyncTask<JSONObject, Void, JSONObject> task;
	private JSONObject params;

	public static final String PARAM_USER_ID = "user_id";
	
	/**
	 * Type of {@link ServerRequest}
	 */
	public enum RequestType {
		// Params: email, password. Return: success or fail
		LOGIN,

		// Params: email, name, password, phone. Return: success or fail (duplicate email or phone)
		SIGNUP,

		// Params: user_id. Return: ArrayList<News> all activities of friends. To each news, check whether user has like it yet
		FETCH_NEWS,

		// Params: user_id. Return: ArrayList<User> as friend list, ArrayList<InviteRequest> as friend request list
		FETCH_FRIEND_LIST, FETCH_FRIEND_REQUEST,

		// Params: user_id. Return: ArrayList<Group> as group list, ArrayList<InviteRequest> as group invite list
		FETCH_GROUP_LIST, FETCH_GROUP_INVITE,

		// Params: user_id, target user_id. Return: ArrayList<News> ONLY public news of target
		FETCH_FOLLOW_NEWS,

		// Params: user_id. Return: ArrayList<Trip>
		FETCH_TRIP_LIST,

		// Params: target user_id.
		// Return: User (name, location), ArrayList<News> as recent PUBLIC activities if not friend, ALL activites if friend (limit to 5), Current Relationship (Friend or not)
		FETCH_USER_INFO,

		// Params: user_id, target group_id.
		// Return: if requester is not a member (group name, description, number of members, leader); if member (all info above + group message); if leader (all info as normal member + group request +
		// group invite)
		FETCH_GROUP_INFO,

		// Params: user_id, place_id
		// Return: old review of user about this place (if have); Place info: name, description, num_of_review, average_rate; Top Reviews: 5 Reviews with top Like; Recent Review: 5 recent reviews
		FETCH_PLACE_INFO,

		// TODO: DONT IMPLEMENT THIS
		// Params: user_id, trip_id
		// Return: if not a member, return description, start date, end date, start - end locations (eg: HCM - Nha Trang - Da Lat), price
		FETCH_TRIP_INFO,

		// Params: user_id, content, isPublic. Return: success or fail
		ACTION_POST,

		// Params: user_id, post_id, owner_id, content. Return: update num of comments and likes
		ACTION_COMMENT_POST,

		// Params: user_id, post_id, owner_id. Return: update num of comments and likes
		ACTION_LIKE_POST,

		// Params: user_id, place_id, content, rate (1 - 5). Return: success or fail
		ACTION_REVIEW,

		// Params: user_id, owner_id, place_id. Return: update num of likes
		ACTION_LIKE_REVIEW,

		// Params: user_id, description, place_id, isPublic, bytes of photo. Return: success or fail
		ACTION_PHOTO,

		// Params: user_id, photo_id, owner_id, content. Return: update num of comments and likes
		ACTION_COMMENT_PHOTO,

		// Params: user_id, photo_id, owner_id. Return: update num of commets and likes
		ACTION_LIKE_PHOTO,

		// Params: user_id, target user_id. Return: success or fail
		ACTION_SEND_FRIEND_REQUEST,

		// Params: user_id, target user_id, true or false as respond. Return: success or fail
		ACTION_RESPOND_FRIEND_REQUEST,

		// Params: user_id, target user_id. Return: success or fail
		ACTION_FOLLOW,

		// Params: user_id, group_name, group_description, group_isPublic. Return: success or fail
		ACTION_CREATE_GROUP,

		// Params: user_id, group_id. Return: success or fail
		ACTION_REQUEST_JOIN_GROUP,

		// Params: group_id, user_id, true or false as respond. Return: success or fail
		ACTION_RESPOND_GROUP_REQUEST,

		// Params: group_id, target user_id. Return: success or fail
		ACTION_SEND_GROUP_INVITE,

		// Params: group_id, target user_id. Return: sucess or fail
		ACTION_CANCEL_GROUP_INVITE,

		// Params: user_id, group_id, content. Return: update messages which is sent before this
		ACTION_SEND_GROUP_MESSAGE,

		// Params: group_id, new name, new description. Return: success or fail
		ACTION_UPDATE_GROUP_INFO,

		// TODO: DONT IMPLEMENT THIS
		ACTION_CREATE_TRIP,

		SEARCH,

		SUGGEST
	}

	private ServerRequest(AsyncTask<JSONObject, Void, JSONObject> task, JSONObject params) {
		this.task = task;
		this.params = params;
	}

	/**
	 * Execute the async task held by this {@link ServerRequest} instance. Only call from <b>UI thread</b>.
	 */
	public void executeAsync() {
		if (task != null)
			task.execute(params);
	}

	/**
	 * Create a new {@link ServerRequest} to handle send and receive data from server
	 * 
	 * @param requestType
	 *            type of request
	 * @param params
	 *            parameters of request
	 * @param callback
	 *            actions after finishing receiving response from server
	 * @return {@link ServerRequest}
	 */
	public static ServerRequest newServerRequest(RequestType requestType, JSONObject params, ServerRequestCallback callback) {
		ServerRequestAsyncTask task = new ServerRequestAsyncTask(requestType, callback);
		return new ServerRequest(task, params);
	}

	/**
	 * Create an {@link AsyncTask} to to handle send and receive data from server
	 */
	private static class ServerRequestAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {

		private RequestType requestType;
		private ServerRequestCallback callback;

		public ServerRequestAsyncTask(RequestType requestType, ServerRequestCallback callBack) {
			this.requestType = requestType;
			this.callback = callBack;
		}

		@Override
		protected JSONObject doInBackground(JSONObject... params) {
			return Utils.makeHttpRequest(requestType, params[0]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			Log.i("Debug", "Server result " + result.toString());
			ArrayList<Object> parseResult;
			switch (requestType) {
			case LOGIN:
				parseResult = Utils.parseResult(requestType, result);
				User user = (parseResult.isEmpty()) ? null : (User) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(user);
				break;

			case SIGNUP:
				parseResult = Utils.parseResult(requestType, result);
				int status = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status);
				break;

			case FETCH_NEWS:
				parseResult = Utils.parseResult(requestType, result);
				ArrayList<News> news = new ArrayList<News>();
				if (!parseResult.isEmpty()) {
					for (int i = 0; i < parseResult.size(); i++) {
						news.add((News) parseResult.get(i));
					}
				} else {
					news = null;
				}
				if (callback != null)
					callback.onCompleted(news);
				break;

			case FETCH_FOLLOW_NEWS:
				parseResult = Utils.parseResult(requestType, result);
				ArrayList<News> follow_news = new ArrayList<News>();
				if (!parseResult.isEmpty()) {
					for (int i = 0; i < parseResult.size(); i++) {
						follow_news.add((News) parseResult.get(i));
					}
				} else {
					follow_news = null;
				}
				if (callback != null)
					callback.onCompleted(follow_news);
				break;

			case FETCH_USER_INFO:
				parseResult = Utils.parseResult(requestType, result);
				ArrayList<Object> objects = new ArrayList<Object>();
				ArrayList<News> user_news = new ArrayList<News>();
				if (!parseResult.isEmpty()) {
					int chkFriend = (Integer) parseResult.get(0);
					for (int i = 1; i < parseResult.size(); i++) {
						user_news.add((News) parseResult.get(i));
					}
					objects.add(chkFriend);
					objects.add(user_news);
				} else {
					objects = null;
				}
				if (callback != null)
					callback.onCompleted(objects);
				break;

			case FETCH_FRIEND_LIST:
				parseResult = Utils.parseResult(requestType, result);
				ArrayList<User> users = new ArrayList<User>();
				if (parseResult.isEmpty()) {
					users = null;
				} else {
					for (int i = 0; i < parseResult.size(); i++) {
						users.add((User) parseResult.get(i));
					}
				}
				if (callback != null)
					callback.onCompleted(users);
				break;

			case FETCH_FRIEND_REQUEST:
				parseResult = Utils.parseResult(requestType, result);
				ArrayList<InviteRequest> offer = new ArrayList<InviteRequest>();
				if (parseResult.isEmpty()) {
					offer = null;
				} else {
					for (int i = 0; i < parseResult.size(); i++) {
						offer.add((InviteRequest) parseResult.get(i));
					}
				}
				if (callback != null)
					callback.onCompleted(offer);
				break;

			case FETCH_GROUP_LIST:
				parseResult = Utils.parseResult(requestType, result);
				ArrayList<Group> group = new ArrayList<Group>();
				if (parseResult.isEmpty()) {
					group = null;
				} else {
					for (int i = 0; i < parseResult.size(); i++) {
						group.add((Group) parseResult.get(i));
					}
				}
				if (callback != null)
					callback.onCompleted(group);
				break;

			case FETCH_GROUP_INVITE:
				parseResult = Utils.parseResult(requestType, result);
				ArrayList<InviteRequest> groupoffer = new ArrayList<InviteRequest>();
				if (parseResult.isEmpty()) {
					groupoffer = null;
				} else {
					for (int i = 0; i < parseResult.size(); i++) {
						groupoffer.add((InviteRequest) parseResult.get(i));
					}
				}
				if (callback != null)
					callback.onCompleted(groupoffer);
				break;

			case ACTION_POST:
				parseResult = Utils.parseResult(requestType, result);
				int status1 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status1);
				break;

			case ACTION_COMMENT_POST:
				parseResult = Utils.parseResult(requestType, result);
				int status2 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status2);
				break;

			case ACTION_LIKE_POST:
				parseResult = Utils.parseResult(requestType, result);
				int status3 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status3);
				break;

			case ACTION_REVIEW:
				parseResult = Utils.parseResult(requestType, result);
				int status4 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status4);
				break;

			case ACTION_LIKE_REVIEW:
				parseResult = Utils.parseResult(requestType, result);
				int status5 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status5);
				break;

			case ACTION_PHOTO:
				parseResult = Utils.parseResult(requestType, result);
				int status6 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status6);
				break;

			case ACTION_COMMENT_PHOTO:
				parseResult = Utils.parseResult(requestType, result);
				int status7 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status7);
				break;

			case ACTION_LIKE_PHOTO:
				parseResult = Utils.parseResult(requestType, result);
				int status8 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status8);
				break;

			case ACTION_SEND_FRIEND_REQUEST:
				parseResult = Utils.parseResult(requestType, result);
				int status9 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status9);
				break;

			case ACTION_RESPOND_FRIEND_REQUEST:
				parseResult = Utils.parseResult(requestType, result);
				int status16 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status16);
				break;

			case ACTION_FOLLOW:
				parseResult = Utils.parseResult(requestType, result);
				int status10 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status10);
				break;

			case ACTION_CREATE_GROUP:
				parseResult = Utils.parseResult(requestType, result);
				int status11 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status11);
				break;

			case ACTION_SEND_GROUP_INVITE:
				parseResult = Utils.parseResult(requestType, result);
				int status12 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status12);
				break;

			case ACTION_CANCEL_GROUP_INVITE:
				parseResult = Utils.parseResult(requestType, result);
				int status13 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status13);
				break;

			case ACTION_SEND_GROUP_MESSAGE:
				parseResult = Utils.parseResult(requestType, result);
				int status14 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status14);
				break;

			case ACTION_UPDATE_GROUP_INFO:
				parseResult = Utils.parseResult(requestType, result);
				int status15 = (parseResult.isEmpty()) ? 0 : (Integer) parseResult.get(0);
				if (callback != null)
					callback.onCompleted(status15);
				break;

			case SEARCH:
				parseResult = Utils.parseResult(requestType, result);
				if (callback != null)
					callback.onCompleted(parseResult);
				break;
			case SUGGEST:
				parseResult = Utils.parseResult(requestType, result);
				if (callback != null)
					callback.onCompleted(parseResult);
				break;

			default:
				break;
			}
		}
	}

	public interface ServerRequestCallback {
		public void onCompleted(Object... results);
	}
}
