package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserMessageActivity;
import vn.edu.hcmut.wego.adapter.FriendAdapter;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.SocialService;
import vn.edu.hcmut.wego.utility.CommonUtility;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FriendsFragment extends BaseFragment {

	private ArrayList<User> friends;
	private Context context;

	public FriendsFragment(Context context) {
		// Get context
		this.context = context;

		// Set title
		setTitle(this.context.getString(R.string.title_fragment_friends));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: Debug entry. Change to load friends info from database instead of direct load from server
		int userId = (Integer) CommonUtility.getValueFromSharedPreferences(context, Constant.PREFS_USER_ID, Integer.class);
//		friends = SocialService.getAllFriends(userId);
		friends = new ArrayList<User>();

		// Open database and load friends info from database
		// DatabaseOpenHelper database = new DatabaseOpenHelper(context);
		// friends = database.getAllFriends();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

		// Create friend adapter for list view
		FriendAdapter adapter = new FriendAdapter(context, friends);

		// Set up list view
		ListView list = (ListView) rootView.findViewById(R.id.fragment_friend_listview);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context, UserMessageActivity.class);
				context.startActivity(intent);
			}
		});
		
		Button messageButton = (Button) rootView.findViewById(R.id.fragment_friends_button_message);
		messageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		return rootView;
	}

}
