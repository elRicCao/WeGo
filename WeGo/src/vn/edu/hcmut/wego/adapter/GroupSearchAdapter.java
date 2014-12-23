package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Group;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GroupSearchAdapter extends ArrayAdapter<Group> {

	private Context context;
	private ArrayList<Group> groups;

	// private CommentListener listener;

	// private DatabaseOpenHelper database;
	private Group groupItem;

	public GroupSearchAdapter(Context context, ArrayList<Group> groups) {
		super(context, 0, groups);
		this.context = context;
		this.groups = groups;
		// database = new DatabaseOpenHelper(context);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_search_group, parent, false);

			ViewHolder holder = new ViewHolder();

			holder.ownerNameView = (TextView) convertView.findViewById(R.id.item_search_group_name);
			holder.ownerDescriptionView = (TextView) convertView.findViewById(R.id.item_search_group_description);
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		groupItem = groups.get(position);

		// Get view holder from view tag
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.ownerNameView.setText(groupItem.getName());
		holder.ownerDescriptionView.setText(groupItem.getDescription());
		return convertView;
	}

	/**
	 * Hold view of news item
	 */
	private static class ViewHolder {

		// Owner info
		TextView ownerNameView;
		TextView ownerDescriptionView;

	}

}
