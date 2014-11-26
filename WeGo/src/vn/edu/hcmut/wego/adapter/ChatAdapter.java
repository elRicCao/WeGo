package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;
import java.util.TreeSet;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<Message> {

	private static final int TYPE_CONTACT = 0;
	private static final int TYPE_CURRENT_USER = 1;
	private static final int TYPE_MAX_COUNT = 2;
	
	private int currentUserId;
	private LayoutInflater inflater;
	private ArrayList<Message> messages;
	private TreeSet<Integer> currentUserSet = new TreeSet<Integer>();

	public ChatAdapter(Context context, ArrayList<Message> messages, int currentUserId) {
		super(context, 0, messages);
		this.currentUserId = currentUserId;
		this.messages = messages;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public void add(Message message) {
		messages.add(message);
		if (message.getSender().getId() == currentUserId) {
			currentUserSet.add(messages.size() - 1);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		return (currentUserSet.contains(position)) ? TYPE_CURRENT_USER : TYPE_CONTACT;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		int type = getItemViewType(position);

		if (convertView == null) {
			holder = new ViewHolder();

			switch (type) {
			case TYPE_CONTACT:
				convertView = inflater.inflate(R.layout.item_chat_contact, parent, false);
				holder.image = (ImageView) convertView.findViewById(R.id.item_chat_contact_image);
				holder.content = (TextView) convertView.findViewById(R.id.item_chat_contact_content);
				holder.time = (TextView) convertView.findViewById(R.id.item_chat_contact_time);
				break;

			case TYPE_CURRENT_USER:
				convertView = inflater.inflate(R.layout.item_chat_current_user, parent, false);
				holder.image = (ImageView) convertView.findViewById(R.id.item_chat_current_user_image);
				holder.content = (TextView) convertView.findViewById(R.id.item_chat_current_user_content);
				holder.time = (TextView) convertView.findViewById(R.id.item_chat_current_user_time);
				break;
			}
			convertView.setTag(holder);
		}
		
		holder = (ViewHolder) convertView.getTag();
		
		Message message = getItem(position);
		
		holder.content.setText(message.getContent());
		
		holder.time.setText(Utils.formatDate(message.getTime()));

		return convertView;
	}

	private static class ViewHolder {
		ImageView image;
		TextView content;
		TextView time;
	}
}
