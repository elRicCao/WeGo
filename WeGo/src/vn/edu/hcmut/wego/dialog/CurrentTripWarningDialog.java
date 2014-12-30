package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.GPSTracker;
import vn.edu.hcmut.wego.utility.Utils;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class CurrentTripWarningDialog extends DialogFragment {

	private Context context;
	private final int tripId;
	private final int userId;
	private final String userName;

	private LinearLayout detailGroup;

	private RadioGroup waitGroup;
	private RadioGroup policeGroup;
	private RadioGroup gatherGroup;
	private RadioGroup accidentGroup;
	
	private ArrayList<User> members;

	public CurrentTripWarningDialog(Context context, int tripId, ArrayList<User> members) {
		this.context = context;
		this.tripId = tripId;
		this.userId = Utils.getUserId(context);
		this.userName = Utils.getUserName(context);
		this.members = members;
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
		View rootView = inflater.inflate(R.layout.dialog_warning, container, false);

		ImageButton waitButton = (ImageButton) rootView.findViewById(R.id.dialog_warning_wait);
		ImageButton policeButton = (ImageButton) rootView.findViewById(R.id.dialog_warning_police);
		ImageButton gatherButton = (ImageButton) rootView.findViewById(R.id.dialog_warning_gather);
		ImageButton accidentButton = (ImageButton) rootView.findViewById(R.id.dialog_warning_accident);

		detailGroup = (LinearLayout) rootView.findViewById(R.id.dialog_warning_detail_group);
		waitGroup = (RadioGroup) rootView.findViewById(R.id.dialog_warning_wait_group);
		policeGroup = (RadioGroup) rootView.findViewById(R.id.dialog_warning_police_group);
		gatherGroup = (RadioGroup) rootView.findViewById(R.id.dialog_warning_gather_group);
		accidentGroup = (RadioGroup) rootView.findViewById(R.id.dialog_warning_accident_group);

		Button sendButton = (Button) rootView.findViewById(R.id.dialog_warning_send);

		waitButton.setOnClickListener(actionListener);
		policeButton.setOnClickListener(actionListener);
		gatherButton.setOnClickListener(actionListener);
		accidentButton.setOnClickListener(actionListener);

		sendButton.setOnClickListener(sendListener);

		return rootView;
	}

	private OnClickListener actionListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			detailGroup.setVisibility(View.VISIBLE);
			waitGroup.setVisibility(View.GONE);
			policeGroup.setVisibility(View.GONE);
			gatherGroup.setVisibility(View.GONE);
			accidentGroup.setVisibility(View.GONE);
			switch (view.getId()) {
			case R.id.dialog_warning_wait:
				waitGroup.setVisibility(View.VISIBLE);
				break;
			case R.id.dialog_warning_police:
				policeGroup.setVisibility(View.VISIBLE);
				break;
			case R.id.dialog_warning_gather:
				gatherGroup.setVisibility(View.VISIBLE);
				break;
			case R.id.dialog_warning_accident:
				accidentGroup.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	private OnClickListener sendListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			String header = new String();
			if (waitGroup.getVisibility() == View.VISIBLE) {
				switch (waitGroup.getCheckedRadioButtonId()) {
				case R.id.dialog_warning_wait_lost:
					header = Constant.WARNING_WAIT_LOST;
					break;
				case R.id.dialog_warning_wait_vehicle:
					header = Constant.WARNING_WAIT_VEHICLE;
					break;
				case R.id.dialog_warning_wait_gas:
					header = Constant.WARNING_WAIT_GAS;
					break;
				}
			} else if (policeGroup.getVisibility() == View.VISIBLE) {
				switch (policeGroup.getCheckedRadioButtonId()) {
				case R.id.dialog_warning_police_alert:
					header = Constant.WARNING_POLICE_ALERT;
					break;
				case R.id.dialog_warning_police_capture:
					header = Constant.WARNING_POLICE_CAPTURE;
					break;
				}
			} else if (gatherGroup.getVisibility() == View.VISIBLE) {
				switch (gatherGroup.getCheckedRadioButtonId()) {
				case R.id.dialog_warning_gather_regroup:
					header = Constant.WARNING_REGROUP;
					break;
				}
			} else {
				switch (accidentGroup.getCheckedRadioButtonId()) {
				case R.id.dialog_warning_accident_help:
					header = Constant.WARNING_ACCIDENT;
					break;
				}
			}
			if (!header.isEmpty()) {
				try {
					JSONObject params = new JSONObject();

					GPSTracker gpsTracker = new GPSTracker(context);

					String location = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();

					String message = header + "|" + String.valueOf(tripId) + "|" + String.valueOf(userId) + "|" + userName + "|" + location;
					
					String condition = " where user_id in ";
					String temp = new String();
					for (int i = 0; i < members.size(); i++) {
						temp += String.valueOf(members.get(i).getId());
						if (i < members.size() - 1) {
							temp += ",";
						}
					}
					condition += "(" + temp + ")";

					params.put("message", message);
					params.put("condition", condition);

					Log.i("Debug send GCM", params.toString());

					ServerRequest.newServerRequest(RequestType.PUSH_TO_SELECTED_USER, params, new ServerRequestCallback() {

						@Override
						public void onCompleted(Object... results) {
							CurrentTripWarningDialog.this.dismiss();
						}
					}).executeAsync();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public static void create(Context context, FragmentManager fragmentManager, int tripId, ArrayList<User> members) {
		CurrentTripWarningDialog currentWarningDialog = new CurrentTripWarningDialog(context, tripId, members);
		currentWarningDialog.show(fragmentManager, "warning_dialog");
	}

}
