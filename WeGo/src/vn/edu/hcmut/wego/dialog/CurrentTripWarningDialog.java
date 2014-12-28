package vn.edu.hcmut.wego.dialog;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.utility.Utils;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class CurrentTripWarningDialog extends DialogFragment {

	private Context context;
	private final int userId;

	private LinearLayout detailGroup;

	private RadioGroup waitGroup;
	private RadioGroup policeGroup;
	private RadioGroup gatherGroup;
	private RadioGroup accidentGroup;

	public CurrentTripWarningDialog(Context context) {
		this.context = context;
		this.userId = Utils.getUserId(context);
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

		waitButton.setOnClickListener(actionListener);
		policeButton.setOnClickListener(actionListener);
		gatherButton.setOnClickListener(actionListener);
		accidentButton.setOnClickListener(actionListener);

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

	public static void create(Context context, FragmentManager fragmentManager) {
		CurrentTripWarningDialog currentWarningDialog = new CurrentTripWarningDialog(context);
		currentWarningDialog.show(fragmentManager, "warning_dialog");
	}

}
