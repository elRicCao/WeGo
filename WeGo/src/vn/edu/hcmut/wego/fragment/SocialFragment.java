package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.GroupInfoActivity;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.dialog.ChatDialog;
import vn.edu.hcmut.wego.dialog.ChatDialog.ChatDialogType;
import vn.edu.hcmut.wego.dialog.FriendDialog;
import vn.edu.hcmut.wego.dialog.GroupDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SocialFragment extends TabFragment {

	private static final String title = "Social";
	private static final int iconRes = R.drawable.ic_tab_social;

	private int userId;
	private Context context;

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

		// Populate friend request section
		for (int i = 0; i < 1; i++) {
			View requestView = inflater.inflate(R.layout.item_friend_request, container, false);
			friendRequest.addView(requestView);
			friendRequest.setOnClickListener(new UserInfoActivity.UserInfoListener(context, 0));
		}

		// Populate group invite section
		for (int i = 0; i < 1; i++) {
			View inviteView = inflater.inflate(R.layout.item_group_invite, container, false);
			groupInvite.addView(inviteView);
			groupInvite.setOnClickListener(new GroupInfoActivity.GroupInfoListener(context, 0));
		}

		// Populate friend message section
		for (int i = 0; i < 1; i++) {
			View messageView = inflater.inflate(R.layout.item_friend_message, container, false);
			friendMessage.addView(messageView);
			friendMessage.setOnClickListener(new ChatDialog.ChatDialogListener(context, getFragmentManager(), ChatDialogType.FRIEND_MESSAGE, 0, 1));
		}

		// Populate group message section
		for (int i = 0; i < 1; i++) {
			View messageView = inflater.inflate(R.layout.item_group_message, container, false);
			groupMessage.addView(messageView);
			groupMessage.setOnClickListener(new ChatDialog.ChatDialogListener(context, getFragmentManager(), ChatDialogType.GROUP_MESSAGE, 0, 1));
		}

		socialNews.setOnTouchListener(new MainActivity.BottomBarListener(context, buttonBar));

		buttonFriend.setOnClickListener(new FriendDialog.FriendDialogListener(context, getFragmentManager(), userId));

		buttonGroup.setOnClickListener(new GroupDialog.GroupDialogListener(context, getFragmentManager(), userId));

		return rootView;
	}
}
