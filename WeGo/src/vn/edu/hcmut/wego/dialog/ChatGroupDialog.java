package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.FriendAdapter;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatGroupDialog extends DialogFragment {

	private Context context;
	private FriendAdapter adapter;
	private int userId;
	
	public ChatGroupDialog(Context context, int userId) {
		this.context = context;
		this.userId = userId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		adapter = new FriendAdapter(context, new ArrayList<User>(), userId);
		addFakeData();
		sortRecent();
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_friend, container, false);

		RelativeLayout header = (RelativeLayout) rootView.findViewById(R.id.dialog_friend_header);
		TextView numView = (TextView) rootView.findViewById(R.id.dialog_friend_num);
		View dividerView = (View) rootView.findViewById(R.id.dialog_friend_divider);
		TextView noFriendView = (TextView) rootView.findViewById(R.id.dialog_friend_no_friend);
		ImageButton sortView = (ImageButton) rootView.findViewById(R.id.dialog_friend_sort);
		ListView friendList = (ListView) rootView.findViewById(R.id.dialog_friend_list);

		if (adapter.getCount() > 0) {
			numView.setText(String.valueOf(adapter.getCount()) + " friends");
		} else {
			header.setVisibility(View.GONE);
			dividerView.setVisibility(View.GONE);
			noFriendView.setVisibility(View.VISIBLE);
		}

		sortView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (view.isSelected()) {
					sortRecent();
				} else {
					sortAlphabet();
				}
				view.setSelected(!view.isSelected());
			}
		});

		friendList.setAdapter(adapter);
		friendList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Intent intent = new Intent(context, UserMessageActivity.class);
//				context.startActivity(intent);
				FragmentManager fragmentManager = ChatGroupDialog.this.getFragmentManager();
				CommentDialog commentDialog = new CommentDialog(context);
				commentDialog.show(fragmentManager, "comment_dialog");
			}

		});

		return rootView;
	}

	private void sortAlphabet() {
		adapter.sort(new Comparator<User>() {
			@Override
			public int compare(User lhs, User rhs) {
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
		});
	}

	private void sortRecent() {
		adapter.sort(new Comparator<User>() {
			@Override
			public int compare(User lhs, User rhs) {
				if (lhs.getRecentMessages().isEmpty() && !rhs.getRecentMessages().isEmpty()) {
					return 1;
				}
				if (!lhs.getRecentMessages().isEmpty() && rhs.getRecentMessages().isEmpty()) {
					return -1;
				}
				if (lhs.getRecentMessages().isEmpty() && rhs.getRecentMessages().isEmpty()) {
					return 0;
				}
				Message lastMessLhs = lhs.getRecentMessages().get(lhs.getRecentMessages().size() - 1);
				Message lastMessRhs = rhs.getRecentMessages().get(rhs.getRecentMessages().size() - 1);
				return lastMessRhs.getTime().compareTo(lastMessLhs.getTime());
			}

		});
	}

	private void addFakeData() {
		Message message = new Message();
		message.setContent("Let's go to Vung Tau");
		message.setTime(new Date());

		Message message2 = new Message();
		message2.setContent("I change my mind");
		message2.setTime(new Date(message.getTime().getTime() + 5000));

		User user1 = new User();
		user1.setName("elRic");
	//	user1.setStatus(UserStatus.ONLINE);

		User user2 = new User();
		user2.setName("Mai Huu Nhan");
	//	user2.setStatus(UserStatus.ONLINE);
		user2.addRecentMessage(message);

		User user3 = new User();
		user3.setName("Phan Tran Viet");
	//	user3.setStatus(UserStatus.ONLINE);
		user3.addRecentMessage(message2);
		
		Log.i("Debug compare message", message.getTime().toString() + " " + message2.getTime().toString() + " " + String.valueOf(message.getTime().compareTo(message2.getTime())));

		User user4 = new User();
		user4.setName("SuperBo");
	//	user4.setStatus(UserStatus.OFFLINE);

		adapter.add(user1);
		adapter.add(user2);
		adapter.add(user3);
		adapter.add(user4);
	}
}
