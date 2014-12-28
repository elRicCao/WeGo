package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Date;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.CommentAdapter;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class CommentDialog extends DialogFragment {

	private Context context;
	private CommentAdapter adapter;

	public CommentDialog(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		adapter = new CommentAdapter(context, new ArrayList<Message>());
		addFakeData();
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_comment, container, false);

		TextView numLikeView = (TextView) rootView.findViewById(R.id.dialog_comment_num_like);
		ListView commentList = (ListView) rootView.findViewById(R.id.dialog_comment_list);

		commentList.setAdapter(adapter);

		if (adapter.getCount() > 1) {
			numLikeView.setText(String.valueOf(adapter.getCount() + context.getResources().getString(R.string.dialog_comment_num_like_plural)));
		} else {
			numLikeView.setText(String.valueOf(adapter.getCount() + context.getResources().getString(R.string.dialog_comment_num_like_single)));
		}

		return rootView;
	}

	private void addFakeData() {
		User user1 = new User();
		user1.setName("elRic");

		User user2 = new User();
		user2.setName("Mai Huu Nhan");

		User user3 = new User();
		user3.setName("Phan Tran Viet");

		User user4 = new User();
		user4.setName("SuperBo");

		Message message = new Message();
		message.setSender(user2);
		message.setContent("Present >:D<");
		Date date = new Date();
		date.setDate(18);
		date.setHours(22);
		date.setMinutes(2);
		date.setSeconds(13);

		message.setTime(date);

		adapter.add(message);

		message = new Message();
		message.setSender(user3);
		message.setContent("I miss home :(");
		date = new Date();
		date.setDate(18);
		date.setHours(22);
		date.setMinutes(23);
		date.setSeconds(18);

		message.setTime(date);

		adapter.add(message);

		message = new Message();
		message.setSender(user4);
		message.setContent("When are you coming back?");
		date = new Date();
		date.setDate(18);
		date.setHours(22);
		date.setMinutes(44);
		date.setSeconds(53);

		message.setTime(date);

		adapter.add(message);
	}
}
