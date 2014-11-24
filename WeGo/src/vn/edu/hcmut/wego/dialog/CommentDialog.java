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
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_comment, container, false);

		ListView commentList = (ListView) rootView.findViewById(R.id.dialog_comment_list);
		commentList.setAdapter(adapter);

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
		message.setSender(user1);
		message.setContent("Hello World!");
		message.setTime(new Date());
		
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
		adapter.add(message);
	}
}
