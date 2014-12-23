package vn.edu.hcmut.wego.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.GroupInfoActivity;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.dialog.ChatDialog;
import vn.edu.hcmut.wego.dialog.ChatDialog.ChatDialogType;
import vn.edu.hcmut.wego.dialog.FriendDialog;
import vn.edu.hcmut.wego.dialog.GroupDialog;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.InviteRequest;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SocialFragment extends TabFragment {

	private static final String title = "Social";
	private static final int iconRes = R.drawable.ic_tab_social;

	private static enum TypeInviteRequest {
		FRIEND_REQUEST, GROUP_REQUEST

	};

	private int userId;
	// private User sender;
	private Context context;

	private ArrayList<InviteRequest> inviteRequests;

	public SocialFragment(Context context, int userId) {
		super(title, iconRes);
		this.userId = userId;
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_social, container, false);

		ScrollView socialNews = (ScrollView) rootView.findViewById(R.id.fragment_social_list);

		LinearLayout friendRequest = (LinearLayout) rootView.findViewById(R.id.fragment_social_friend_request_list);
		LinearLayout groupInvite = (LinearLayout) rootView.findViewById(R.id.fragment_social_group_invite_list);
		LinearLayout friendMessage = (LinearLayout) rootView.findViewById(R.id.fragment_social_friend_message);
		LinearLayout groupMessage = (LinearLayout) rootView.findViewById(R.id.fragment_social_group_message);

		LinearLayout buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_social_button_bar);
		LinearLayout buttonFriend = (LinearLayout) rootView.findViewById(R.id.fragment_social_button_friend);
		LinearLayout buttonGroup = (LinearLayout) rootView.findViewById(R.id.fragment_social_button_group);

		populateFriendRequest(friendRequest, inflater);
		populateGroupInvite(groupInvite, inflater);
		populateFriendMessage(friendMessage, inflater);
		populateGroupMessage(groupMessage, inflater);

		socialNews.setOnTouchListener(new MainActivity.BottomBarListener(context, buttonBar));

		buttonFriend.setOnClickListener(new FriendDialog.FriendDialogListener(context, getFragmentManager(), userId));

		buttonGroup.setOnClickListener(new GroupDialog.GroupDialogListener(context, getFragmentManager(), userId));

		return rootView;
	}

	private void populateFriendRequest(final ViewGroup container, final LayoutInflater inflater) {
		// InviteRequest friendRequest = new InviteRequest();
		inviteRequests = new ArrayList<InviteRequest>();
		JSONObject params = new JSONObject();
		try {
			params.put(Constant.PARAM_USER_ID, userId);
			// for (int i = 0; i < 1; i++) {
			// View requestView = inflater.inflate(R.layout.item_friend_request, container, false);
			// container.addView(requestView);
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_FRIEND_REQUEST, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				if (results[0] != null) {
					inviteRequests = (ArrayList<InviteRequest>) results[0];

					for (int i = 0; i < inviteRequests.size(); i++) {
						View requestView = inflater.inflate(R.layout.item_friend_request, container, false);

						TextView nameRequest = (TextView) requestView.findViewById(R.id.item_friend_request_name);
						ImageButton btnAccept = (ImageButton) requestView.findViewById(R.id.item_friend_request_accept);
						ImageButton btnDecline = (ImageButton) requestView.findViewById(R.id.item_friend_request_decline);
						User sender = new User();
						sender = (User) inviteRequests.get(i).getSender();
						nameRequest.setText(sender.getName());
					//	container.setOnClickListener(new UserInfoActivity.UserInfoListener(context, sender.getId()));
						container.addView(requestView);
						requestView.setOnClickListener(new UserInfoActivity.UserInfoListener(context, sender.getId(), userId));
						btnAccept.setOnClickListener(new RespondListener(sender.getId(), container, requestView, true, TypeInviteRequest.FRIEND_REQUEST));
						btnDecline.setOnClickListener(new RespondListener(sender.getId(), container, requestView, false, TypeInviteRequest.FRIEND_REQUEST));
					}
				}
			}

		}).executeAsync();
	}

	private void populateGroupInvite(final ViewGroup container, final LayoutInflater inflater) {
		// try {
		// JSONObject params = new JSONObject().put(Constant.PARAM_USER_ID, userId);
		// for (int i = 0; i < 1; i++) {
		// View inviteView = inflater.inflate(R.layout.item_group_invite, container, false);
		// container.addView(inviteView);
		// container.setOnClickListener(new GroupInfoActivity.GroupInfoListener(context, 0));
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		inviteRequests = new ArrayList<InviteRequest>();
		JSONObject params = new JSONObject();
		try {
			params.put(Constant.PARAM_USER_ID, userId);
			// for (int i = 0; i < 1; i++) {
			// View requestView = inflater.inflate(R.layout.item_friend_request, container, false);
			// container.addView(requestView);
			
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_GROUP_INVITE, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				// TODO Auto-generated method stub
				inviteRequests = (ArrayList<InviteRequest>) results[0];
				for (int i = 0; i < inviteRequests.size(); i++) {
					View inviteView = inflater.inflate(R.layout.item_group_invite, container, false);

					TextView groupName = (TextView) inviteView.findViewById(R.id.item_group_invite_name);
					TextView groupDesc = (TextView) inviteView.findViewById(R.id.item_group_invite_description);
					ImageButton btnAccept = (ImageButton) inviteView.findViewById(R.id.item_group_invite_accept);
					ImageButton btnDecline = (ImageButton) inviteView.findViewById(R.id.item_group_invite_decline);
					Group groupInvite = new Group();
					groupInvite = (Group) inviteRequests.get(i).getSender();
					groupName.setText(groupInvite.getName());
					groupDesc.setText(groupInvite.getDescription());
					container.addView(inviteView);
					inviteView.setOnClickListener(new GroupInfoActivity.GroupInfoListener(context, groupInvite.getId()));
				}

			}
		}).executeAsync();

	}

	private void populateFriendMessage(final ViewGroup container, final LayoutInflater inflater) {
		// try {
		// JSONObject params = new JSONObject().put(Constant.PARAM_USER_ID, userId);
		// for (int i = 0; i < 1; i++) {
		// View messageView = inflater.inflate(R.layout.item_friend_message, container, false);
		// container.addView(messageView);
		// container.setOnClickListener(new ChatDialog.ChatDialogListener(context, getFragmentManager(), ChatDialogType.FRIEND_MESSAGE, 0, 1));
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		inviteRequests = new ArrayList<InviteRequest>();
		JSONObject params = new JSONObject();
		try {
			params.put("receiver_id", userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_LAST_MESSAGE, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				ArrayList<Message> message = (ArrayList<Message>) results[0];
				for (int i = 0; i < message.size(); i++) {
					for (int j = i + 1; j < message.size(); j++) {
						if (message.get(i).getSender().getId() == message.get(j).getSender().getId()) {
							message.remove(i);
							i--;
							break;
						}
					}
				}
				for (int i = 0; i < message.size(); i++) {
					View messageView = inflater.inflate(R.layout.item_friend_message, container, false);
					TextView messageName = (TextView) messageView.findViewById(R.id.item_friend_message_name);
					TextView messageContent = (TextView) messageView.findViewById(R.id.item_friend_message_content);
					TextView timeDate = (TextView) messageView.findViewById(R.id.item_friend_message_time_date);
					TextView timeHour = (TextView) messageView.findViewById(R.id.item_friend_message_time_hour);
					messageName.setText(message.get(i).getSender().getName());
					messageContent.setText(message.get(i).getContent());
					DateFormat df = new SimpleDateFormat("MMM dd");				
					timeDate.setText(df.format(message.get(i).getTime()));
					df = new SimpleDateFormat("h:mm a");				
					timeHour.setText(df.format(message.get(i).getTime()));
					container.addView(messageView);
					container.setOnClickListener(new ChatDialog.ChatDialogListener(context, getFragmentManager(), ChatDialogType.FRIEND_MESSAGE, 0, 1));
				}
			}
		}).executeAsync();
	}

	private void populateGroupMessage(ViewGroup container, LayoutInflater inflater) {
		try {
			JSONObject params = new JSONObject().put(Constant.PARAM_USER_ID, userId);
			for (int i = 0; i < 1; i++) {
				View messageView = inflater.inflate(R.layout.item_group_message, container, false);
				container.addView(messageView);
				container.setOnClickListener(new ChatDialog.ChatDialogListener(context, getFragmentManager(), ChatDialogType.GROUP_MESSAGE, 0, 1));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class RespondListener implements OnClickListener {
		private int senderId;
		private ViewGroup container;
		private View requestView;
		private boolean accept;
		private TypeInviteRequest typeRequest;

		public RespondListener(int senderId, ViewGroup container, View requestView, boolean accept, TypeInviteRequest typeRequest) {
			this.senderId = senderId;
			this.container = container;
			this.requestView = requestView;
			this.accept = accept;
			this.typeRequest = typeRequest;
		}

		@Override
		public void onClick(View v) {
			switch (typeRequest) {
			case FRIEND_REQUEST:

				JSONObject params = new JSONObject();
				try {
					if (accept)
						params.put("respond", true);
					else
						params.put("respond", false);
					params.put("user1", userId);
					params.put("user2", senderId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ServerRequest.newServerRequest(RequestType.ACTION_RESPOND_FRIEND_REQUEST, params, new ServerRequestCallback() {

					@Override
					public void onCompleted(Object... results) {
						// respond friend request
					}

				}).executeAsync();
				container.removeView(requestView);
				break;
			case GROUP_REQUEST:
				break;
			}
		}

	}
}
