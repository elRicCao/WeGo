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

public class MemberAdapter extends ArrayAdapter<User> {

	private Context context;
	private ArrayList<User> members;

	public MemberAdapter(Context context, ArrayList<User> members) {
		super(context, 0, members);
		this.context = context;
		this.members = members;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_member, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.item_member_name);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_member_image);
			holder.callButton = (ImageButton) convertView.findViewById(R.id.item_member_call);
			holder.locateButton = (ImageButton) convertView.findViewById(R.id.item_member_locate);
			convertView.setTag(holder);
		}

		User member = members.get(position);
		ViewHolder holder = (ViewHolder) convertView.getTag();

		holder.nameView.setText(member.getName());

//		if (!member.getRecentMessages().isEmpty()) {
//			holder.messageView.setVisibility(View.VISIBLE);
//			holder.messageView.setText(member.getRecentMessages().get(member.getRecentMessages().size() - 1).getContent());
//		} else {
//			holder.messageView.setVisibility(View.GONE);
//		}

		holder.callButton.setFocusable(false);
		holder.callButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		holder.locateButton.setFocusable(false);
		holder.locateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return convertView;
	}

	private static class ViewHolder {
		TextView nameView;
		ImageButton callButton;
		ImageButton locateButton;
		ImageView imageView;
	}

}
