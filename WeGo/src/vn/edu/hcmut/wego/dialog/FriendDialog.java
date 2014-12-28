package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Comparator;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.FriendAdapter;
import vn.edu.hcmut.wego.dialog.ChatDialog.ChatDialogType;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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

public class FriendDialog extends DialogFragment {

	private Context context;
	private FriendAdapter adapter;
	private final int userId;
	private RelativeLayout headerView;
	private TextView numView;
	private View dividerView;
	private TextView noFriendView;
	private ImageButton sortView;
	private ListView friendList;

	public FriendDialog(Context context, int userId) {
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
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_friend, container, false);

		headerView = (RelativeLayout) rootView.findViewById(R.id.dialog_friend_header);
		numView = (TextView) rootView.findViewById(R.id.dialog_friend_num);
		dividerView = (View) rootView.findViewById(R.id.dialog_friend_divider);
		noFriendView = (TextView) rootView.findViewById(R.id.dialog_friend_no_friend);
		sortView = (ImageButton) rootView.findViewById(R.id.dialog_friend_sort);
		friendList = (ListView) rootView.findViewById(R.id.dialog_friend_list);

		sortView.setOnClickListener(sortViewClickListener);

		friendList.setAdapter(adapter);
		friendList.setOnItemClickListener(friendListItemClickListener);

		return rootView;
	}

	private OnClickListener sortViewClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view.isSelected()) {
				sortRecent();
			} else {
				sortAlphabet();
			}
			view.setSelected(!view.isSelected());
		}
	};

	private OnItemClickListener friendListItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			User friend = adapter.getItem(position);
			FragmentManager fragmentManager = FriendDialog.this.getFragmentManager();
			ChatDialog chatDialog = new ChatDialog(context, ChatDialogType.FRIEND_MESSAGE, userId, friend.getId());
			chatDialog.show(fragmentManager, "chat_dialog");
		}
	};

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

	public static class FriendDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private int userId;

		public FriendDialogListener(Context context, FragmentManager fragmentManager, int userId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.userId = userId;
		}

		@Override
		public void onClick(View view) {
			FriendDialog friendDialog = new FriendDialog(context, userId);
			friendDialog.show(fragmentManager, "friend_dialog");
		}

	}

	private void addFakeData() {
		JSONObject params = new JSONObject();
		try {
			params.put("respond", false);
			params.put("user", userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_FRIEND_LIST, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				// respond friend request
				ArrayList<User> users = (ArrayList<User>) results[0];
				for (int i = 0; i < users.size(); i++)
					adapter.add(users.get(i));
				if (adapter.getCount() > 0) {
					numView.setText(String.valueOf(adapter.getCount()) + " friends");
				} else {
					headerView.setVisibility(View.GONE);
					dividerView.setVisibility(View.GONE);
					noFriendView.setVisibility(View.VISIBLE);
				}

			}

		}).executeAsync();

	}
}
