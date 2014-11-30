package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.GroupAdapter;
import vn.edu.hcmut.wego.dialog.ChatDialog.ChatDialogType;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.content.Intent;
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

public class CreateGroupDialog extends DialogFragment {

	private Context context;
	private GroupAdapter adapter;
	private final int userId;

	public CreateGroupDialog(Context context, int userId) {
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
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_create_group, container, false);

		return rootView;
	}

	public static class CreateGroupDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private int userId;

		public CreateGroupDialogListener(Context context, FragmentManager fragmentManager, int userId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.userId = userId;
		}

		@Override
		public void onClick(View view) {
			CreateGroupDialog dialog = new CreateGroupDialog(context, userId);
			dialog.show(fragmentManager, "group_dialog");
		}

	}

	private void addFakeData() {
		Group groupA = new Group();
		groupA.setName("Saigon Traveler");

		User userA = new User();
		userA.setName("elRic");

		Message messageA = new Message();
		messageA.setSender(userA);
		messageA.setContent("Hey, anyone want to have a trip to Da Lat?");
		messageA.setTime(new Date());

		groupA.setAnnouncement(messageA);

		Group groupB = new Group();
		groupB.setName("iTravel");

		adapter.add(groupA);
		adapter.add(groupB);
	}
}
