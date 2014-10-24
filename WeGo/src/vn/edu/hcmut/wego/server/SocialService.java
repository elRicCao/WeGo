package vn.edu.hcmut.wego.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.Offer;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.util.Log;

public class SocialService {
	private static final String PHP_SOCIAL = "SocialLogic";
	
	public enum ActionType {};

	/**
	 * Get all new data related to this user and save to database
	 * 
	 * @param userId
	 */
	public static void update(Context context) {

	}

	/**
	 * Send user action to server
	 */
	public static void action(Context context, ActionType type) {

	}

	/**
	 * Get info of specific user based on id (after searching)
	 * 
	 * @param targetId
	 * @return user info
	 */
	public static User getUserInfo(int targetId) {
		return null;
	}

	/**
	 * Check if user A and user B are friend or not
	 * 
	 * @param userId
	 * @param targetId
	 * @return true if A and B are friend, false if A and B are not friend
	 */
	public static boolean checkFriend(int userId, int targetId) {
		JSONObject param = new JSONObject();

		try {
			param.put("user1", userId);
			param.put("user2", targetId);

			JSONObject result = Server.execute(PHP_SOCIAL, "checkFriend", param);

			if (result.getInt(Constant.SUCCESS) == 1)
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Get friend request list from server
	 * 
	 * @param userId
	 */
	public static Offer getFriendRequest(int userId) {
		return null;
	}

	/**
	 * Send friend request from current user to target user
	 * 
	 * @param userId
	 * @param targetId
	 */
	public static void sendFriendRequest(int userId, int targetId) {

	}

	/**
	 * Respond friend request
	 * 
	 * @param userId
	 * @param targetId
	 * @param type
	 */
	public static void respondFriendRequest(int userId, int targetId, boolean isApproved) {

	}

	/**
	 * Delete friend
	 * 
	 * @param userId
	 * @param targetId
	 */
	public static void deleteFriend(int userId, int targetId) {

	}

	/**
	 * Follow specific user
	 * 
	 * @param userId
	 * @param targetId
	 */
	public static void followUser(int userId, int targetId) {

	}

	/**
	 * Unfollow specific user
	 * 
	 * @param userId
	 * @param targetId
	 */
	public static void unfollowUser(int userId, int targetId) {

	}

	/**
	 * Get info of specific group based on group id (after search)
	 * 
	 * @param groupId
	 */
	public static void getGroupInfo(int groupId) {

	}

	/**
	 * Create group
	 * 
	 * @param managerId
	 * @param name
	 * @param description
	 */
	public static void createGroup(int managerId, String name, String description) {

	}

	/**
	 * Update group info
	 * 
	 * @param name
	 * @param description
	 */
	public static void updateGroupInfo(int groupId, String name, String description) {

	}

	/**
	 * Check user role in group
	 * 
	 * @param userId
	 * @param groupId
	 */
	public static void checkUserRoleInGroup(int userId, int groupId) {

	}

	/**
	 * Get group request from server
	 * 
	 * @param groupId
	 */
	public static Offer getGroupRequest(int groupId) {
		return null;
	}

	/**
	 * Send request to join group
	 * 
	 * @param userId
	 * @param groupId
	 */
	public static void sendGroupRequest(int userId, int groupId) {

	}

	/**
	 * Respond to group request
	 * 
	 * @param groupId
	 * @param requestUserId
	 * @param type
	 */
	public static void respondGroupRequest(int groupId, int requestUserId, boolean isApproved) {

	}

	/**
	 * Get group invite from server
	 * 
	 * @param userId
	 */
	public static Offer getGroupInvite(int userId) {
		return null;
	}

	/**
	 * Invite user to group
	 * 
	 * @param groupId
	 * @param targetUserId
	 */
	public static void inviteUserToGroup(int groupId, int targetUserId) {

	}

	/**
	 * User respond to group invite
	 * 
	 * @param userId
	 * @param groupId
	 * @param isApproved
	 */
	public static void respondGroupInvite(int userId, int groupId, boolean isApproved) {

	}

	/**
	 * Ban user in group
	 * 
	 * @param groupId
	 * @param banUserId
	 */
	public static void banUserFromGroup(int groupId, int banUserId) {

	}

	/**
	 * User leave group
	 * 
	 * @param userId
	 * @param groupId
	 */
	public static void leaveGroup(int userId, int groupId) {

	}

	/**
	 * Manager close group
	 * 
	 * @param groupId
	 */
	public static void closeGroup(int groupId) {

	}

	/**
	 * Group member send message in group chat
	 * 
	 * @param userId
	 * @param groupId
	 * @param content
	 */
	public static void sendGroupMessage(int userId, int groupId, String content) {

	}

	/**
	 * Send message from current user to target user
	 * 
	 * @param userId
	 * @param targetId
	 * @param content
	 */
	public static void sendUserMessage(int userId, int targetId, String content) {

	}

	/**
	 * Load information of all friends in first use
	 * 
	 * @param userId
	 * @return list of friends with information: name, phone, email, status, last update
	 */
	public static ArrayList<User> getAllFriends(int userId) {
		ArrayList<User> friends = new ArrayList<User>();
		
		JSONObject param = new JSONObject();

		try {
			param.put("user", userId);

			JSONObject result = Server.execute(PHP_SOCIAL, "selectUserFriend", param);

			Log.i("Test", result.toString());
			
			if (result.getInt(Constant.SUCCESS) == 1) {
				JSONArray array = result.getJSONArray(Constant.RESULT);
				for (int i = 0; i < array.length(); i++) {
					JSONObject tmp = array.getJSONObject(i);

					User friend = new User();
					friend.setId(Integer.parseInt(tmp.getString("friendId")));
					friend.setName(tmp.getString("friendName"));
					friend.setPhone(tmp.getString("friendPhone"));
					friend.setEmail(tmp.getString("friendEmail"));
					// TODO: them vo load status voi last update
					friend.setStatus("Online");
					
					friends.add(friend);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return friends;
	}

	/**
	 * Load information of all groups in first use
	 * 
	 * @param userId
	 * @return list of groups with information: name, description, status
	 */
	public static ArrayList<Group> getAllGroups(int userId) {
		ArrayList<Group> groups = new ArrayList<Group>();
		JSONObject param = new JSONObject();

		try {
			param.put("user", userId);

			JSONObject result = Server.execute(PHP_SOCIAL, "selectUserGroup", param);

			if (result.getInt(Constant.SUCCESS) == 1) {
				JSONArray group = result.getJSONArray(Constant.RESULT);
				for (int i = 0; i < group.length(); i++) {
					JSONObject tmp = group.getJSONObject(i);

					Group gp = new Group();
					gp.setDescription("");
					gp.setName(tmp.getString("name"));
					gp.setStatus(tmp.getString("status"));

					groups.add(gp);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return groups;
	}
}
