package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class FriendListAdapter extends ArrayAdapter<User> {

	private Context context;
	private ArrayList<User> friends;

	public FriendListAdapter(Context context, ArrayList<User> friends) {
		super(context, 0, friends);
		this.context = context;
		this.friends = friends;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_friend, parent, false);
		}

		User friend = friends.get(position);

		TextView nameView = (TextView) convertView.findViewById(R.id.item_friend_name);
		TextView statusView = (TextView) convertView.findViewById(R.id.item_friend_status);
		ImageButton infoButton = (ImageButton) convertView.findViewById(R.id.item_friend_info);
		nameView.setText(friend.getName());
		statusView.setText(friend.getStatus());
		infoButton.setFocusable(false);
		infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserInfoActivity.class);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

}
