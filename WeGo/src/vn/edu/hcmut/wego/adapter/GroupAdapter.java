package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.GroupInfoActivity;
import vn.edu.hcmut.wego.entity.Group;
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
			holder.announcementView = (TextView) convertView.findViewById(R.id.item_group_announcement);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_group_image);
			holder.infoButton = (ImageButton) convertView.findViewById(R.id.item_group_info);
			convertView.setTag(holder);
		}

		Group group = groups.get(position);
		ViewHolder holder = (ViewHolder) convertView.getTag();

		// Set name of group
		holder.nameView.setText(group.getName());

		// Set recent message
		if (group.getAnnouncement() == null) {
			holder.announcementView.setVisibility(View.GONE);
		}
		else {
			holder.announcementView.setVisibility(View.VISIBLE);
			holder.announcementView.setText(group.getAnnouncement().getContent());
		}

		// Set info button listener
		holder.infoButton.setFocusable(false);
		holder.infoButton.setOnClickListener(new infoButtonClickListener(group.getId()));

		return convertView;
	}

	private class infoButtonClickListener implements OnClickListener {
		private int id;
		public infoButtonClickListener(int id) {
			this.id = id;
		}
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, GroupInfoActivity.class);
			intent.putExtra("group_id", id);
			context.startActivity(intent);
		}
	};

	private static class ViewHolder {
		TextView nameView;
		TextView announcementView;
		ImageButton infoButton;
		ImageView imageView;
	}

}
