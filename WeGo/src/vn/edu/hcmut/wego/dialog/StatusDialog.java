package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.InviteRequest;
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
import android.widget.ImageButton;
import android.widget.TextView;

public class StatusDialog extends DialogFragment {

	private Context context;
	private final int userId;

	public StatusDialog(Context context, int userId) {
		this.context = context;
		this.userId = userId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		addFakeData();
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
		View rootView = inflater.inflate(R.layout.dialog_status, container, false);
		JSONObject params = new JSONObject();
		try {
			params.put(Constant.PARAM_USER_ID, userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.ACTION_CANCEL_GROUP_INVITE, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				if (results[0] != null) {
					
				}
			}

		}).executeAsync();
		return rootView;
	}

	public static class CreateStatusDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private int userId;

		public CreateStatusDialogListener(Context context, FragmentManager fragmentManager, int userId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.userId = userId;
		}

		@Override
		public void onClick(View view) {
			StatusDialog dialog = new StatusDialog(context, userId);
			dialog.show(fragmentManager, "status_dialog");
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

	}
}
