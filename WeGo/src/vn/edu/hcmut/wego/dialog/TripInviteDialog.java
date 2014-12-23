package vn.edu.hcmut.wego.dialog;

import vn.edu.hcmut.wego.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;

public class TripInviteDialog extends DialogFragment {

	private Context context;
	private int tripId;
	
	public TripInviteDialog(Context context, int tripId) {
		this.context = context;
		this.tripId = tripId;
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
		View rootView = inflater.inflate(R.layout.dialog_friend, container, false);

		

		return rootView;
	}
	public static class TripInviteDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private int tripId;
		
		public TripInviteDialogListener(Context context, FragmentManager fragmentManager, int tripId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.tripId = tripId;
		}
		
		@Override
		public void onClick(View view) {
			TripInviteDialog tripInviteDialog = new TripInviteDialog(context, tripId);
			tripInviteDialog.show(fragmentManager, "trip_invite_dialog");
		}
		
	}
	}
