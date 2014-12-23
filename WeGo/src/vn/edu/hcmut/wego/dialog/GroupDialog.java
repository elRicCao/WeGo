package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.GroupAdapter;
import vn.edu.hcmut.wego.dialog.ChatDialog.ChatDialogType;
import vn.edu.hcmut.wego.entity.Group;
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

public class GroupDialog extends DialogFragment {

	private Context context;
	private GroupAdapter adapter;
	private final int userId;
	private RelativeLayout headerView;
	private TextView numView;
	private View dividerView;
	private TextView noFriendView;
	private ImageButton sortView;
	private ImageButton createView;
	private ListView groupList;

	public GroupDialog(Context context, int userId) {
		this.context = context;
		this.userId = userId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		adapter = new GroupAdapter(context, new ArrayList<Group>());
		
		addFakeData();
		// sortRecent();
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
		View rootView = inflater.inflate(R.layout.dialog_group, container, false);

		headerView = (RelativeLayout) rootView.findViewById(R.id.dialog_group_header);
		numView = (TextView) rootView.findViewById(R.id.dialog_group_num);
		dividerView = (View) rootView.findViewById(R.id.dialog_group_divider);
		noFriendView = (TextView) rootView.findViewById(R.id.dialog_group_no_group);
		sortView = (ImageButton) rootView.findViewById(R.id.dialog_group_sort);
		createView = (ImageButton) rootView.findViewById(R.id.dialog_group_create);
		groupList = (ListView) rootView.findViewById(R.id.dialog_group_list);

		

		sortView.setOnClickListener(sortViewListener);

		groupList.setAdapter(adapter);
		groupList.setOnItemClickListener(groupListItemListener);
		
		createView.setOnClickListener(new CreateGroupDialog.CreateGroupDialogListener(context, getFragmentManager(), userId));

		return rootView;
	}

	private OnClickListener sortViewListener = new OnClickListener() {
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

	private OnItemClickListener groupListItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final Group group = adapter.getItem(position);

			FragmentManager fragmentManager = GroupDialog.this.getFragmentManager();
			ChatDialog chatDialog = new ChatDialog(context, ChatDialogType.GROUP_MESSAGE, userId, group.getId());
			chatDialog.show(fragmentManager, "chat_dialog");
		}
	};

	private void sortAlphabet() {
		adapter.sort(new Comparator<Group>() {
			@Override
			public int compare(Group lhs, Group rhs) {
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
		});
	}

	private void sortRecent() {
		adapter.sort(new Comparator<Group>() {
			@Override
			public int compare(Group lhs, Group rhs) {
				if (lhs.getAnnouncement() == null && rhs.getAnnouncement() != null) {
					return 1;
				}
				if (lhs.getAnnouncement() != null && rhs.getAnnouncement() == null) {
					return -1;
				}
				if (lhs.getAnnouncement() == null && rhs.getAnnouncement() == null) {
					return 0;
				}
				Message lhsAnnouncement = lhs.getAnnouncement();
				Message rhsAnnouncement = rhs.getAnnouncement();
				return lhsAnnouncement.getTime().compareTo(rhsAnnouncement.getTime());
			}
		});
	}
	
	public static class GroupDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private int userId;
		
		public GroupDialogListener(Context context, FragmentManager fragmentManager, int userId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.userId = userId;
		}
		
		@Override
		public void onClick(View view) {
			GroupDialog groupDialog = new GroupDialog(context, userId);
			groupDialog.show(fragmentManager, "group_dialog");
		}
		
	}

	private void addFakeData() {
		JSONObject params = new JSONObject();
		try {
			params.put("user", userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_GROUP_LIST, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				// respond friend request
				ArrayList<Group> groups = (ArrayList<Group>) results[0];
				for(int i = 0; i < groups.size(); i++)
				{
					adapter.add(groups.get(i));
				}	
				if (adapter.getCount() > 0) {
					numView.setText(String.valueOf(adapter.getCount()) + " groups");
				} else {
					headerView.setVisibility(View.GONE);
					dividerView.setVisibility(View.GONE);
					noFriendView.setVisibility(View.VISIBLE);
				}
			}

		}).executeAsync();
	}
}
