package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

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
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends ArrayAdapter<User> {

	private Context context;
	private ArrayList<User> friends;
	private int currentUserId;

	public FriendAdapter(Context context, ArrayList<User> friends, int currentUserId) {
		super(context, 0, friends);
		this.context = context;
		this.friends = friends;
		this.currentUserId = currentUserId;
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

//		if (friend.getStatus() == UserStatus.OFFLINE) {
//			holder.nameView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_status_offline, 0);
//		} else {
//			holder.nameView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_status_online, 0);
//		}

		if (!friend.getRecentMessages().isEmpty()) {
			holder.messageView.setVisibility(View.VISIBLE);
			holder.messageView.setText(friend.getRecentMessages().get(friend.getRecentMessages().size() - 1).getContent());
		}
		else {
			holder.messageView.setVisibility(View.GONE);
		}
		
		holder.infoButton.setFocusable(false);
		holder.infoButton.setOnClickListener(new infoButtonClickListener(friend.getId()));
		
		if (friend.getImage() == null || friend.getImage().isEmpty()) {
			Picasso.with(context).load(R.drawable.default_user).into(holder.imageView);
		}
		else {
			Picasso.with(context).load(friend.getImage()).error(R.drawable.default_user).into(holder.imageView);
		}
		

		return convertView;
	}
	private class infoButtonClickListener implements OnClickListener {
		private int userId;
		public infoButtonClickListener(int userId ) {
			this.userId = userId;
		}
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, UserInfoActivity.class);
			intent.putExtra("user_id", userId);
			intent.putExtra("current_user_id", currentUserId);
			context.startActivity(intent);
		}
	};
	private static class ViewHolder {
		TextView nameView;
		TextView messageView;
		ImageButton infoButton;
		ImageView imageView;
	}

}
