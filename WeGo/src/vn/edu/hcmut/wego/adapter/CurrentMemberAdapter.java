package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentMemberAdapter extends ArrayAdapter<User> {

	private Context context;
	private ArrayList<User> members;
	private MemberDialogCallback callback;

	public CurrentMemberAdapter(Context context, ArrayList<User> members, MemberDialogCallback callback) {
		super(context, 0, members);
		this.context = context;
		this.members = members;
		this.callback = callback;
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

		final User member = members.get(position);
		ViewHolder holder = (ViewHolder) convertView.getTag();

		holder.nameView.setText(member.getName());
		
		if (member.getImage() == null || member.getImage().isEmpty()) {
			Picasso.with(context).load(R.drawable.default_user).into(holder.imageView);
		}
		else {
			Picasso.with(context).load(member.getImage()).into(holder.imageView);
		}
		
		holder.callButton.setFocusable(false);
		holder.callButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "tel:" + member.getPhone();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
				context.startActivity(intent);
			}
		});

		holder.locateButton.setFocusable(false);
		holder.locateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onCallback(member);
				}
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
	
	public interface MemberDialogCallback {
		public void onCallback(User user);
	}

}
