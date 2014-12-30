package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
			
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_search_user_image);
			holder.nameView = (TextView) convertView.findViewById(R.id.item_search_user_name);
			holder.locationView = (TextView) convertView.findViewById(R.id.item_search_user_location);
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		userItem = users.get(position);

		// Get view holder from view tag
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.nameView.setText(userItem.getName());
		holder.locationView.setText(userItem.getLocation().getName());
		Picasso.with(context).load(userItem.getImage()).into(holder.imageView);
		
		return convertView;
	}

	/**
	 * Hold view of news item
	 */
	private static class ViewHolder {

		ImageView imageView;
		TextView nameView;
		TextView locationView;

	}

}
