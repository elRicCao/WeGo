package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.FriendAdapter;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper;
import vn.edu.hcmut.wego.utility.CommonUtility;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FriendsFragment extends BaseFragment {
	
	private Long userId;
	private ArrayList<User> friends;
	private Context context;
	public FriendsFragment(Context context) {
		setTitle(context.getString(R.string.title_fragment_friends));
		this.context = context;
		friends = new ArrayList<User>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
		
		// Open database
		DatabaseOpenHelper database = new DatabaseOpenHelper(context);
		
		// Get userId
		userId = (Long) CommonUtility.getValueFromSharedPreferences(context, Constant.PREFS_USER_ID, Long.class);
		
		// Load friends info
		if (CommonUtility.checkInternetConnection(context)) {
			//TODO: Load friends info from server and store to database
			
			friends = database.getAllFriends();
		}
		else {
			//TODO: Load friends info from database
			friends = database.getAllFriends();
		}
		
		// Create friend adapter for list view
		FriendAdapter adapter = new FriendAdapter(context, friends);
		
		// Set up list view
		ListView list = (ListView) rootView.findViewById(R.id.fragment_friend_listview);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Jump to friend detail
				
			}
		});
		
		return rootView;
	}

}
