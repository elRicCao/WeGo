package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends ArrayAdapter<Message> {

	private Context context;
	private ArrayList<Message> messages;

	public CommentAdapter(Context context, ArrayList<Message> messages) {
		super(context, 0, messages);
		this.context = context;
		this.messages = messages;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_comment, parent, false);
			
			ViewHolder holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_comment_image);
			holder.name = (TextView) convertView.findViewById(R.id.item_comment_name);
			holder.content = (TextView) convertView.findViewById(R.id.item_comment_content);
			holder.time = (TextView) convertView.findViewById(R.id.item_comment_time);
			convertView.setTag(holder);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Message item = messages.get(position);
		
		//TODO: Load avatar
		
		holder.name.setText(item.getSender().getName());
		
		holder.content.setText(item.getContent());
		
		holder.time.setText(Utils.formatDate(item.getTime()));

		return convertView;
	}

	private static class ViewHolder {
		ImageView image;
		TextView name;
		TextView content;
		TextView time;
	}
}
