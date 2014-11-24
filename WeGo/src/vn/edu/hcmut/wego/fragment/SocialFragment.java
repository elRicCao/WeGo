package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity;
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
	
	private Context context;
	
	public SocialFragment(Context context) {
		super(title, iconRes);
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_social, container, false);
		
		LinearLayout friendRequest = (LinearLayout) rootView.findViewById(R.id.fragment_social_friend_request_list);
		for (int i = 0; i < 2; i++) {
			View requestView = inflater.inflate(R.layout.item_friend_request, container, false);
			friendRequest.addView(requestView);
		}
		
		LinearLayout groupInvite = (LinearLayout) rootView.findViewById(R.id.fragment_social_group_invite_list);
		for (int i = 0; i < 2; i++) {
			View inviteView = inflater.inflate(R.layout.item_group_invite, container, false);
			groupInvite.addView(inviteView);
		}
		
		LinearLayout friendMessage = (LinearLayout) rootView.findViewById(R.id.fragment_social_friend_message);
		for (int i = 0; i < 2; i++) {
			View messageView = inflater.inflate(R.layout.item_friend_message, container, false);
			friendMessage.addView(messageView);
		}
		
		LinearLayout groupMessage = (LinearLayout) rootView.findViewById(R.id.fragment_social_group_message);
		for (int i = 0; i < 2; i++) {
			View messageView = inflater.inflate(R.layout.item_group_message, container, false);
			groupMessage.addView(messageView);
		}
		
		LinearLayout buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_social_button_bar);
		
		ScrollView socialNews = (ScrollView) rootView.findViewById(R.id.fragment_social_list);
		socialNews.setOnTouchListener(new MainActivity.BottomBarListener(context, buttonBar));
		
		return rootView;
	}
}
