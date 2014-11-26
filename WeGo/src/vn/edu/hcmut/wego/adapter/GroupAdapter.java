package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.GroupInfoActivity;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.Message;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupAdapter extends ArrayAdapter<Group> {

	private Context context;
	private ArrayList<Group> groups;

	public GroupAdapter(Context context, ArrayList<Group> groups) {
		super(context, 0, groups);
		this.context = context;
		this.groups = groups;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_group, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.item_group_name);
			holder.messageGroup = (LinearLayout) convertView.findViewById(R.id.item_group_recent_message);
			holder.senderView = (TextView) convertView.findViewById(R.id.item_group_recent_message_sender);
			holder.messageView = (TextView) convertView.findViewById(R.id.item_group_recent_message_content);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_group_image);
			holder.infoButton = (ImageButton) convertView.findViewById(R.id.item_group_info);
			convertView.setTag(holder);
		}

		Group group = groups.get(position);
		ViewHolder holder = (ViewHolder) convertView.getTag();

		// Set name of group
		holder.nameView.setText(group.getName());

		// Set recent message
		if (!group.getMessages().isEmpty()) {
			Message recentMessage = group.getMessages().get(group.getMessages().size() - 1);
			holder.messageGroup.setVisibility(View.VISIBLE);
			holder.senderView.setText(recentMessage.getSender().getName());
			holder.messageView.setText(recentMessage.getContent());
		} else {
			holder.messageGroup.setVisibility(View.GONE);
		}

		// Set info button listener
		holder.infoButton.setFocusable(false);
		holder.infoButton.setOnClickListener(infoButtonClickListener);

		return convertView;
	}

	private OnClickListener infoButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, GroupInfoActivity.class);
			context.startActivity(intent);
		}
	};

	private static class ViewHolder {
		TextView nameView;
		LinearLayout messageGroup;
		TextView senderView;
		TextView messageView;
		ImageButton infoButton;
		ImageView imageView;
	}

}
