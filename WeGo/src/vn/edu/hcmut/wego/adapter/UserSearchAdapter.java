package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserSearchAdapter extends ArrayAdapter<User> {

	private Context context;
	private ArrayList<User> users;

	// private CommentListener listener;

	// private DatabaseOpenHelper database;
	private User userItem;

	public UserSearchAdapter(Context context, ArrayList<User> users) {
		super(context, 0, users);
		this.context = context;
		this.users = users;
		// database = new DatabaseOpenHelper(context);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_search_user, parent, false);

			ViewHolder holder = new ViewHolder();

			holder.ownerNameView = (TextView) convertView.findViewById(R.id.item_search_user_name);
	//		holder.ownerDescriptionView = (TextView) convertView.findViewById(R.id.item_search_group_description);
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		userItem = users.get(position);

		// Get view holder from view tag
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.ownerNameView.setText(userItem.getName());
	//	holder.ownerDescriptionView.setText(groupItem.getDescription());
		return convertView;
	}

	/**
	 * Hold view of news item
	 */
	private static class ViewHolder {

		// Owner info
		TextView ownerNameView;
	//	TextView ownerDescriptionView;

	}

}
