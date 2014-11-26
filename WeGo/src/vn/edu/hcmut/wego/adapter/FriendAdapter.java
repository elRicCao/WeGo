package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.entity.User.UserStatus;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends ArrayAdapter<User> {

	private Context context;
	private ArrayList<User> friends;

	public FriendAdapter(Context context, ArrayList<User> friends) {
		super(context, 0, friends);
		this.context = context;
		this.friends = friends;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_friend, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.item_friend_name);
			holder.messageView = (TextView) convertView.findViewById(R.id.item_friend_message);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_friend_image);
			holder.infoButton = (ImageButton) convertView.findViewById(R.id.item_friend_info);
			convertView.setTag(holder);
		}

		User friend = friends.get(position);
		ViewHolder holder = (ViewHolder) convertView.getTag();

		holder.nameView.setText(friend.getName());

		if (friend.getStatus() == UserStatus.OFFLINE) {
			holder.nameView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_status_offline, 0);
		} else {
			holder.nameView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_status_online, 0);
		}

		if (!friend.getRecentMessages().isEmpty()) {
			holder.messageView.setVisibility(View.VISIBLE);
			holder.messageView.setText(friend.getRecentMessages().get(friend.getRecentMessages().size() - 1).getContent());
		}
		else {
			holder.messageView.setVisibility(View.GONE);
		}
		
		holder.infoButton.setFocusable(false);
		holder.infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserInfoActivity.class);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		TextView nameView;
		TextView messageView;
		ImageButton infoButton;
		ImageView imageView;
	}

}
