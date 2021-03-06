package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.CurrentTripActivity.FindLocationCallback;
import vn.edu.hcmut.wego.adapter.CurrentMemberAdapter;
import vn.edu.hcmut.wego.adapter.CurrentMemberAdapter.MemberDialogCallback;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CurrentTripMemberDialog extends DialogFragment {

	private Context context;
	private CurrentMemberAdapter adapter;
	private FindLocationCallback callback;
	private int userId;

	public CurrentTripMemberDialog(Context context, FindLocationCallback _callback, ArrayList<User> users) {
		this.context = context;
		this.userId = Utils.getUserId(context);
		this.callback = _callback;
		this.adapter = new CurrentMemberAdapter(context, users, new MemberDialogCallback() {

			@Override
			public void onCallback(User user) {
				if (callback != null) {
					callback.onFindLocation(user);
				}
				dismiss();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
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
		View rootView = inflater.inflate(R.layout.dialog_member, container, false);

		TextView numView = (TextView) rootView.findViewById(R.id.dialog_member_num);
		ListView memberList = (ListView) rootView.findViewById(R.id.dialog_member_list);

		if (adapter.getCount() > 0) {
			numView.setText(String.valueOf(adapter.getCount()) + " members");
		}

		memberList.setAdapter(adapter);
		memberList.setOnItemClickListener(memberItemListener);

		return rootView;
	}

	private OnItemClickListener memberItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			User friend = adapter.getItem(position);
			FragmentManager fragmentManager = CurrentTripMemberDialog.this.getFragmentManager();
			ChatDialog chatDialog = new ChatDialog(context, userId, friend.getId());
			chatDialog.show(fragmentManager, "chat_dialog");
		}
	};

	public static void create(Context context, FragmentManager fragmentManager, FindLocationCallback callback, ArrayList<User> users) {
		CurrentTripMemberDialog memberDialog = new CurrentTripMemberDialog(context, callback, users);
		memberDialog.show(fragmentManager, "member_dialog");
	}
}
