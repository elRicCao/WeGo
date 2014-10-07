package vn.edu.hcmut.wego.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.User;

public class SocialService {
	private static final String PHP_SOCIAL = "SocialLogic";
	
	/**
	 * Get info of specific user based on id (after searching)
	 * @param targetId
	 * @return user info
	 */
	public static User getUserInfo(long targetId) {
		return null;
	}

	/**
	 * Check if user A and user B are friend or not
	 * @param userId
	 * @param targetId
	 * @return true if A and B are friend, false if A and B are not friend
	 */
	public static boolean checkFriend (long userId, long targetId) {
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
	 * Send friend request from current user to target user
	 * @param userId
	 * @param targetId
	 */
	public static void sendFriendRequest(long userId, long targetId) {
		
	}

	/**
	 * Respond friend request
	 * @param userId
	 * @param targetId
	 * @param type
	 */
	public static void respondFriendRequest(long userId, long targetId, boolean isApproved) {
		
	}

	/**
	 * Delete friend
	 * @param userId
	 * @param targetId
	 */
	public static void deleteFriend(long userId, long targetId) {
		
	}

	/**
	 * Follow specific user
	 * @param userId
	 * @param targetId
	 */
	public static void followUser(long userId, long targetId) {
		
	}

	/**
	 * Unfollow specific user
	 * @param userId
	 * @param targetId
	 */
	public static void unfollowUser(long userId, long targetId) {
		
	}
	
	/**
	 * Get info of specific group based on group id (after search)
	 * @param groupId
	 */
	public static void getGroupInfo(long groupId) {
		
	}

	/**
	 * Create group
	 * @param managerId
	 * @param name
	 * @param description
	 */
	public static void createGroup(long managerId, String name, String description) {
		
	}
	
	/**
	 * Update group info
	 * @param name
	 * @param description
	 */
	public static void updateGroupInfo(long groupId, String name, String description) {
		
	}
	
	/**
	 * Check user role in group
	 * @param userId
	 * @param groupId
	 */
	public static void checkUserRoleInGroup(long userId, long groupId) {
		
	}
	
	/**
	 * Send request to join group
	 * @param userId
	 * @param groupId
	 */
	public static void sendGroupRequest(long userId, long groupId) {
		
	}
	
	/**
	 * Respond to group request
	 * @param groupId
	 * @param requestUserId
	 * @param type
	 */
	public static void respondGroupRequest(long groupId, long requestUserId, boolean isApproved) {
		
	}
	
	/**
	 * Invite user to group
	 * @param groupId
	 * @param targetUserId
	 */
	public static void inviteUserToGroup(long groupId, long targetUserId) {
		
	}
	
	/**
	 * User respond to group invite
	 * @param userId
	 * @param groupId
	 * @param isApproved
	 */
	public static void respondGroupInvite(long userId, long groupId, boolean isApproved) {
		
	}
	
	/**
	 * Ban user in group
	 * @param groupId
	 * @param banUserId
	 */
	public static void banUserFromGroup(long groupId, long banUserId) {
		
	}
	
	/**
	 * User leave group
	 * @param userId
	 * @param groupId
	 */
	public static void leaveGroup(long userId, long groupId) {
		
	}
	
	/**
	 * Manager close group
	 * @param groupId
	 */
	public static void closeGroup(long groupId) {
		
	}
	
	/**
	 * Group member send message in group chat
	 * @param userId
	 * @param groupId
	 * @param content
	 */
	public static void sendGroupMessage(long userId, long groupId, String content) {
		
	}

	/**
	 * Send message from current user to target user
	 * @param userId
	 * @param targetId
	 * @param content
	 */
	public static void sendUserMessage(long userId, long targetId, long content) {
		
	}

	/**
	 * Load information of all friends in first use
	 * @param userId
	 * @return list of friends with information: name, phone, email, status, last update
	 */
	public static ArrayList<User> getAllFriendsInfo(long userId) {
		ArrayList<User> friends = new ArrayList<User>();
		JSONObject param = new JSONObject();
	
		try {
			param.put("user", userId);
	
			JSONObject result = Server.execute(PHP_SOCIAL, "selectUserFriend", param);
	
			if (result.getInt(Constant.SUCCESS) == 1) {
				JSONArray friend = result.getJSONArray(Constant.RESULT);
				for (int i = 0; i < friend.length(); i++) {
					JSONObject tmp = friend.getJSONObject(i);
					
					User user = new User();
					user.setName(tmp.getString("friendName"));
					user.setPhone(tmp.getString("friendPhone"));
					user.setEmail(tmp.getString("friendEmail"));
					//TODO: them vo load status voi last update
					
					friends.add(user);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return friends;
	}

	/**
	 * Load information of all groups in first use
	 * @param userId
	 * @return list of groups with information: name, description, status
	 */
	public static ArrayList<Group> getAllGroupsInfo(long userId) {
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
